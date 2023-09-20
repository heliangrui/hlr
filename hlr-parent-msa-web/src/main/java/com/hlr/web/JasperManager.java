package com.hlr.web;


import com.hlr.common.exception.CustomException;
import com.hlr.common.file.ImageUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shackle
 * @date 2021/2/3
 */
public class JasperManager {

    /**
     *
     * @param fileInputStream 模板文件流
     * @param map pamater参数
     * @param list details参数
     * @return 已填冲文件
     */
    public static JasperPrint createFillJasperPrint(FileInputStream fileInputStream, Map map, List list) throws CustomException {

        try {
        if(CollectionUtils.isEmpty(list)){
            //空数据源 details
            return JasperFillManager.fillReport(fileInputStream, map, new net.sf.jasperreports.engine.JREmptyDataSource());
        }else{
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(list);
            return JasperFillManager.fillReport(fileInputStream, map, ds);
        }

        } catch (JRException e) {
           throw new CustomException("模板填充错误！");
        }
    }

    /**
     *
     * @param fileInputStream 模板文件流
     * @param map pamater参数
     * @return 已填冲文件
     */
    public static JasperPrint createFillJasperPrint(FileInputStream fileInputStream, Map map) throws CustomException {
        return createFillJasperPrint(fileInputStream,map,null);
    }

    /**
     * @param jasperPrint 模板文件
     * @return pdf文件byte
     */
    public static byte[] jasperPrintExportPDF(JasperPrint jasperPrint) throws CustomException {
        try {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new CustomException("模板转换错误！");
        }
    }

    /**
     * @param jasperPrint 模板文件
     * @return pdf文件byte
     */
    public static void jasperPrintExportPDF(JasperPrint jasperPrint, OutputStream outputStream) throws CustomException {
        try {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (JRException e) {
            throw new CustomException("模板转换错误！");
        }
    }

    /**
     * @param jasperPrint 模板文件
     * @return world文件byte
     */
    public static byte[] jasperPrintExportWORD(JasperPrint jasperPrint) throws CustomException {
        ByteArrayOutputStream oStream = null;
        try {
            JRDocxExporter exporter = new JRDocxExporter();
            oStream = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
            exporter.exportReport();

            return oStream.toByteArray();
        } catch (JRException e) {
            throw new CustomException("模板转换错误！");
        } finally {
            try {
                if (!ObjectUtils.isEmpty(oStream)) {
                    oStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param jasperPrint 模板文件
     * @return excel文件byte
     */
    public static byte[] jasperPrintExportEXCEL(JasperPrint jasperPrint) throws CustomException {
        ByteArrayOutputStream oStream = null;
        try {
            /**
             * execl 导出类
             */
            JRXlsExporter exporter = new JRXlsExporter();
            oStream = new ByteArrayOutputStream();
            exporter
                    .setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
            exporter.setParameter(
                    JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                    Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
                    Boolean.FALSE);
            exporter.setParameter(
                    JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                    Boolean.FALSE);
            exporter.exportReport();
            return oStream.toByteArray();
        } catch (Exception e) {
            throw new CustomException("模板转换错误！");
        }finally{
            try {
                if(!ObjectUtils.isEmpty(oStream)){
                    oStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param jasperPrint 模板文件
     * @return img文件byte
     */
    public static byte[] jasperPrintExportIMG(JasperPrint jasperPrint) throws CustomException {
        ByteArrayOutputStream outputStream=null;
        try {
            List<BufferedImage> bufferedImageList = new ArrayList();
            outputStream = new ByteArrayOutputStream();
            for (int i = 0; i < jasperPrint.getPages().size(); i++) {
                bufferedImageList.add((BufferedImage) (JasperPrintManager.printPageToImage(jasperPrint, i, Float.valueOf(3))));
            }
            BufferedImage bufferedImage =null;
            try{
                bufferedImage = ImageUtils.mergeImage2(bufferedImageList);
            }catch (Exception e){
                e.printStackTrace();
            }
            ImageIO.write(bufferedImage, "png", outputStream);
            return  outputStream.toByteArray();
        } catch (Exception e) {
            throw new CustomException("模板转换错误！");
        }finally{
            try {
                if(!ObjectUtils.isEmpty(outputStream)){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param jasperPrint 模板文件
     * @param fileName    文件名称
     * @return multipartFile
     */
    public static MultipartFile jasperPrintExportPDFMultipartFile(JasperPrint jasperPrint, String fileName) throws CustomException {
        byte[] bytes = jasperPrintExportPDF(jasperPrint);
        return jasperPrintExportPDFMultipartFile(bytes, fileName);
    }

    /**
     * @param jasperPrint 模板文件
     * @param fileName    文件名称
     * @return multipartFile
     */
    public static MultipartFile jasperPrintExportWORDMultipartFile(JasperPrint jasperPrint, String fileName) throws CustomException {
        byte[] bytes = jasperPrintExportWORD(jasperPrint);
        return jasperPrintExportWORDMultipartFile(bytes, fileName);
    }

    /**
     * @param source   模板文件
     * @param fileName 文件名称
     * @return multipartFile
     */
    public static MultipartFile jasperPrintExportWORDMultipartFile(byte[] source, String fileName) throws CustomException {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem file = factory.createItem(fileName + ".docx", "application/msword", false, fileName + ".docx");
        return createMultipartFile(source, file);
    }

    /**
     * @param source   模板文件byte
     * @param fileName 文件名称
     * @return multipartFile
     */
    public static MultipartFile jasperPrintExportPDFMultipartFile(byte[] source, String fileName) throws CustomException {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem file = factory.createItem(fileName + ".pdf", "application/pdf", false, fileName + ".pdf");
        return createMultipartFile(source, file);
    }

    /**
     *
     * @param jasperPrint 模板文件
     * @param fileName 文件名称
     * @return multipartFile
     *
     */
    public static MultipartFile jasperPrintExportIMGMultipartFile(JasperPrint jasperPrint,String fileName) throws CustomException {
        byte[] bytes = jasperPrintExportIMG(jasperPrint);
       return jasperPrintExportIMGMultipartFile(bytes,fileName);
    }
    /**
     *
     * @param source 模板文件
     * @param fileName 文件名称
     * @return multipartFile
     *
     */
    public static MultipartFile jasperPrintExportIMGMultipartFile(byte[] source,String fileName) throws CustomException {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem file = factory.createItem(fileName+".png", "image/png", false, fileName+".png");
        return createMultipartFile(source,file);
    }
    /**
     *
     * @param jasperPrint 模板文件
     * @param fileName 文件名称
     * @return multipartFile
     *
     */
    public static MultipartFile jasperPrintExportEXCELMultipartFile(JasperPrint jasperPrint,String fileName) throws CustomException {
        byte[] bytes = jasperPrintExportEXCEL(jasperPrint);
        return jasperPrintExportEXCELMultipartFile(bytes,fileName);
    }
    /**
     *
     * @param source 模板文件
     * @param fileName 文件名称
     * @return multipartFile
     *
     */
    public static MultipartFile jasperPrintExportEXCELMultipartFile(byte[] source,String fileName) throws CustomException {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem  file = factory.createItem(fileName + ".xls", "application/vnd.ms-excel", false, fileName + ".xls");
        return createMultipartFile(source,file);
    }

    /**
     * 文件转换
     * @param bytes 数据
     * @param fileItem 文件格式
     * @return
     */
    private static MultipartFile createMultipartFile(byte[] bytes, FileItem fileItem) throws CustomException {
        ByteArrayInputStream inputStream=null;
        try {
            inputStream= new ByteArrayInputStream(bytes);
            IOUtils.copy(inputStream, fileItem.getOutputStream());
            return new CommonsMultipartFile(fileItem);
        } catch (IOException e) {
            throw new CustomException("文件转换出错！");
        }finally{
            try {
                if(ObjectUtils.isEmpty(inputStream)){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

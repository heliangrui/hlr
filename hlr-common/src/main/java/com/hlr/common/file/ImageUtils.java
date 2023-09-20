package com.hlr.common.file;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {
    /**
     * hlr
     * 合并图片
     */
    public static BufferedImage mergeImage(List<BufferedImage> list) throws IOException {
        int w1 = list.get(0).getWidth();
        int h1 = list.get(0).getHeight();

        int pageh=h1*list.size();
        BufferedImage pageImage = new BufferedImage(w1,pageh,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics =(Graphics2D) pageImage.getGraphics();
        for(int i=0;i<list.size();i++){
            graphics.drawImage(list.get(i),0,h1*i,null);
            list.get(i).getGraphics().dispose();
        }
        graphics.dispose();
        return pageImage;
    }

    public static BufferedImage mergeImage2(List<BufferedImage> piclist){
        if (piclist == null || piclist.size() <= 0) {
            System.out.println("图片数组为空!");
            return null;
        }
        BufferedImage imageResult =null;
        try {
            int height = 0, // 总高度
                    width = 0, // 总宽度
                    _height = 0, // 临时的高度 , 或保存偏移高度
                    __height = 0, // 临时的高度，主要保存每个高度
                    picNum = piclist.size();// 图片的数量
            int[] heightArray = new int[picNum]; // 保存每个文件的高度
            BufferedImage buffer = null; // 保存图片流
            List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
            int[] _imgRGB; // 保存一张图片中的RGB数据
            for (int i = 0; i < picNum; i++) {
                buffer = piclist.get(i);
                heightArray[i] = _height = buffer.getHeight();// 图片高度
                if (i == 0) {
                    width = buffer.getWidth();// 图片宽度
                }
                height += _height; // 获取总高度
                _imgRGB = new int[width * _height];// 从图片中读取RGB
                _imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }
            _height = 0; // 设置偏移高度为0
            // 生成新图片
            imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < picNum; i++) {
                __height = heightArray[i];
                if (i != 0) _height += __height; // 计算偏移高度
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageResult;
    }


}

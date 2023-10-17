package com.hlr.common.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * SerializeUtils
 * Description:
 * date: 2023/10/17 11:12
 *
 * @author hlr
 */
public class SerializeUtils {
    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        } else {
            ObjectOutputStream oos = null;

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            } catch (Exception var13) {
                var13.printStackTrace();
            } finally {
                try {
                    if (oos != null) {
                        oos.close();
                    }
                } catch (IOException var12) {
                    var12.printStackTrace();
                }

            }

            return null;
        }
    }

    public static Object unSerialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            ObjectInputStream ois = null;

            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception var13) {
                var13.printStackTrace();
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException var12) {
                    var12.printStackTrace();
                }

            }

            return null;
        }
    }
}

package com.hlr.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConvertUtil
 * Description:
 * date: 2023/11/6 16:42
 *
 * @author hlr
 */
public class ConvertUtil {

    static ObjectMapper om = new ObjectMapper();

    public ConvertUtil() {
    }

    public static byte[] object2byte(Object object) {
        return SerializationUtils.serialize((Serializable)object);
    }

    public static <T> T byte2object(byte[] bytes, Class<T> c) {
        return SerializationUtils.deserialize(bytes);
    }

    public static String object2json(Object obj) {
        try {
            return om.writeValueAsString(obj);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static <K, V> Map<K, V> json2map(String json, Class<K> kc, Class<V> vc) {
        JavaType javaType = om.getTypeFactory().constructParametricType(HashMap.class, new Class[]{kc, vc});

        try {
            return (Map)om.readValue(json, javaType);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> json2list(String json, Class<T> c) {
        JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, new Class[]{c});

        try {
            return (List)om.readValue(json, javaType);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static <T> T json2object(String json, Class<T> c) {
        try {
            return om.readValue(json, c);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    
    static {
        om.disable(new DeserializationConfig.Feature[]{Feature.FAIL_ON_UNKNOWN_PROPERTIES});
        om.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }
    
}

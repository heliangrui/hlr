package com.hlr.core.config;

/**
 * EnumConfig
 * Description:
 * date: 2023/10/20 11:18
 *
 * @author hlr
 */
public interface EnumConfig {

    public static enum R_CODE {
        OK(0),
        CLOUD_FAIL(2),
        FAIL(1);
        private int code;

        R_CODE(int code) {
            this.code = code;
        }

        public static R_CODE getEnum(int code) {
            for (R_CODE value : R_CODE.values()) {
                if (value.code == code) {
                    return value;
                }
            }
            return null;
        }

    }

    enum JmsObjectType{
        JMS_KAFKA("kafka");
        private final String type;
        JmsObjectType(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }
    enum JmsSourceType{
        JMS_MQTT("mqtt");
        private final String type;
        JmsSourceType(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

}

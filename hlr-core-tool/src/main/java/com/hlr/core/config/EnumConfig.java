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

    enum JmsType{
        JMS_KAFKA("kafka"),JMS_MQTT("mqtt"),JMS_ROCKETMQ("rocketMq"),JMS_NULL("null");
        private final String type;
        JmsType(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

}

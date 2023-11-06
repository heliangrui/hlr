package com.hlr.core.jms;

import java.io.Serializable;

/**
 * JmsObject
 * Description:
 * date: 2023/11/6 16:38
 *
 * @author hlr
 */
public class JmsObject implements Serializable {

    private static final long serialVersionUID = 4129316996370811011L;
    private int btime = 0;
    private long orderKey = 0L;

    public JmsObject() {
    }

    public int getBtime() {
        return this.btime;
    }

    public void setBtime(int btime) {
        this.btime = btime;
    }

    public long getOrderKey() {
        return this.orderKey;
    }

    public void setOrderKey(long orderKey) {
        this.orderKey = orderKey;
    }
}

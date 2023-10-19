package com.hlr.core.event.async;

/**
 * TaskItem
 * Description:
 * date: 2023/10/19 11:54
 *
 * @author hlr
 */
public class TaskItem {
    private IAsyncExecer execer;

    private Object obj;

    private int execCount;

    private long createTime;

    public TaskItem(IAsyncExecer execer, Object... obj) {
        this.execer = execer;
        this.obj = obj;
        this.createTime = System.currentTimeMillis();
    }


    public IAsyncExecer getExecer() {
        return this.execer;
    }

    public void setExecer(IAsyncExecer execer) {
        this.execer = execer;
    }

    public Object getObj() {
        return this.obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getExecCount() {
        return this.execCount;
    }

    public void setExecCount(int execCount) {
        this.execCount = execCount;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}

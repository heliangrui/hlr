package com.hlr.start.dubbo;

import com.hlr.start.utils.TraceUtil;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.filter.ClusterFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * TagFilter
 * Description: 标签设置
 * date: 2023/11/3 10:50
 *
 * @author hlr
 */
@Activate(group = {"consumer"})
public class TagFilter implements ClusterFilter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        invocation.setAttachment("dubbo.tag", TraceUtil.getTag(this.getServletRequest()));
        return invoker.invoke(invocation);
    }

    private HttpServletRequest getServletRequest() {
        try {
            return RequestContextHolder.getRequestAttributes() == null ? null : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception | NoClassDefFoundError  e) {
            return null;
        }
    }
}

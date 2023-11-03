package com.hlr.start.dubbo;

import com.hlr.start.utils.MsaContextHolder;
import com.hlr.start.utils.TpsUtil;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * TraceFilter
 * Description:
 * date: 2023/11/3 11:08
 *
 * @author hlr
 */
@Activate(group = {"provider","consumer"})
public class TraceFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        MsaContextHolder.setDubboAttachemnt(this.getServletRequest());
        if (RpcContext.getContext().isProviderSide()) {
            TpsUtil.inc();
        }

        return invoker.invoke(invocation);
    }

    private HttpServletRequest getServletRequest() {
        try {
            return RequestContextHolder.getRequestAttributes() == null ? null : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception | NoClassDefFoundError e) {
            return null;
        }
    }
}

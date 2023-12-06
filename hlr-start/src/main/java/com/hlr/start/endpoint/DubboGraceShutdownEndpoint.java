package com.hlr.start.endpoint;

import com.hlr.start.HlrPreStopApplicationListener;
import org.apache.dubbo.config.spring.util.DubboBeanUtils;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * DubboGraceShutdownEndpoint
 * Description:dubbo服务
 * date: 2023/12/6 14:02
 *
 * @author hlr
 */
@Endpoint(id = "graceshutdown")
public class DubboGraceShutdownEndpoint implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(DubboGraceShutdownEndpoint.class);
    private ApplicationContext applicationContext;

    private HlrPreStopApplicationListener hlrPreStopApplicationListener;

    public DubboGraceShutdownEndpoint(HlrPreStopApplicationListener hlrPreStopApplicationListener) {
        this.hlrPreStopApplicationListener = hlrPreStopApplicationListener;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @ReadOperation
    public String endpoint() {
        logger.info("DubboGraceShutdownEndpoint start ....");
        ApplicationModel applicationModel = DubboBeanUtils.getApplicationModel(applicationContext);
        applicationModel.getDeployer().preDestroy();
        hlrPreStopApplicationListener.endpoint();
        logger.info("DubboGraceShutdownEndpoint end");
        return "success";
    }

}

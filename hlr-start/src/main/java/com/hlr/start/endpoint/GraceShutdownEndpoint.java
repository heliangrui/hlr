package com.hlr.start.endpoint;

import com.hlr.start.HlrPreStopApplicationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

/**
 * GraceShutdownEndpoint
 * Description: 普通服务关闭
 * date: 2023/12/6 14:00
 *
 * @author hlr
 */
@Endpoint(id = "graceshutdown")
public class GraceShutdownEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(GraceShutdownEndpoint.class);

    private HlrPreStopApplicationListener hlrPreStopApplicationListener;

    public GraceShutdownEndpoint(HlrPreStopApplicationListener hlrPreStopApplicationListener) {
        this.hlrPreStopApplicationListener = hlrPreStopApplicationListener;
    }

    @ReadOperation
    public String endpoint() {
        logger.info("GraceShutdownEndpoint  start....");
        hlrPreStopApplicationListener.endpoint();
        logger.info("GraceShutdownEndpoint  end....");
        return "success";
    }


}

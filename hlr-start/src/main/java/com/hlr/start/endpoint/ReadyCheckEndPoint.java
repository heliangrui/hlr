package com.hlr.start.endpoint;

import com.hlr.start.HlrPreStartApplicationListener;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

/**
 * ReadyCheckEndPoint
 * Description:
 * date: 2023/12/7 17:31
 *
 * @author hlr
 */
@Endpoint(id = "readycheck")
public class ReadyCheckEndPoint {

    private HlrPreStartApplicationListener hlrPreStartApplicationListener;

    public ReadyCheckEndPoint(HlrPreStartApplicationListener hlrPreStartApplicationListener) {
        this.hlrPreStartApplicationListener = hlrPreStartApplicationListener;
    }

    @ReadOperation
    public boolean endpoint() {
        return hlrPreStartApplicationListener.isReady();
    }
}

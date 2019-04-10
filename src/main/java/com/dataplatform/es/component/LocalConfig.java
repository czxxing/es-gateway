package com.dataplatform.es.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by czx on 4/8/19.
 */
@Component("localConfig")
public class LocalConfig {


    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private Integer port;


    @Value("${is.first.gateway}")
    private Boolean isFirstGateway;

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public Boolean getFirstGateway() {
        return isFirstGateway;
    }


}

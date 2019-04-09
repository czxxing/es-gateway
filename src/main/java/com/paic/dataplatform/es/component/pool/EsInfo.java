package com.paic.dataplatform.es.component.pool;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by czx on 4/1/19.
 */
public class EsInfo implements Serializable {


    /*主机ip*/
    private String host;


    /*主机端口*/
    private int port;



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public String toString(){
        return JSONObject.toJSONString(this);
    }
}

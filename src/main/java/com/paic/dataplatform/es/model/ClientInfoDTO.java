package com.paic.dataplatform.es.model;

import com.paic.dataplatform.es.component.pool.EsInfo;

import java.io.Serializable;

/**
 * Created by czx on 4/2/19.
 */
public class ClientInfoDTO implements Serializable {

    private String key;

    private EsInfo info;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public EsInfo getInfo() {
        return info;
    }

    public void setInfo(EsInfo info) {
        this.info = info;
    }



}

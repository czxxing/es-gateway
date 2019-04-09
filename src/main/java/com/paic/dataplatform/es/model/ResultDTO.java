package com.paic.dataplatform.es.model;

import java.io.Serializable;

/**
 * Created by czx on 4/2/19.
 */
public class ResultDTO implements Serializable {

    private String resultCode;
    private String resultMessage;
    private Object data;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

package com.dataplatform.es.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by czx on 4/2/19.
 */
public class ClientInfosDTO implements Serializable {


    private ArrayList<ClientInfoDTO> clientInfos = new ArrayList<ClientInfoDTO>();

    public ArrayList<ClientInfoDTO> getClientInfos() {
        return clientInfos;
    }

    public void setClientInfos(ArrayList<ClientInfoDTO> clientInfos) {
        this.clientInfos = clientInfos;
    }
}

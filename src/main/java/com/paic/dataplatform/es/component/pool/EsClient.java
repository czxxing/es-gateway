package com.paic.dataplatform.es.component.pool;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Created by czx on 3/28/19.
 */

public class EsClient  implements Serializable {


    private RestClient restClient;

    public void setInfo(EsInfo info) {
        this.info = info;
    }

    public EsInfo getInfo() {
        return info;
    }

    private EsInfo info;


    public void init(){

        CreateEsClient();

    }



    public void CreateEsClient(){

        RestClientBuilder builder = RestClient.builder(new HttpHost(info.getHost(),info.getPort(),"http"));

        builder.setMaxRetryTimeoutMillis(1000);

        restClient = builder.build();

        SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();

        Sniffer sniffer = Sniffer.builder(restClient)
                .setSniffAfterFailureDelayMillis(30000)
                .build();

        sniffOnFailureListener.setSniffer(sniffer);



    }




    public Response handleEsRequest(String url,String body,String method) throws IOException {


        try {


            HttpEntity entity =null;
            if(body!=null){
                entity = new NStringEntity(body, ContentType.APPLICATION_JSON);
            }

            //Map<String,String> params =  Collections.emptyMap();
            Response response = restClient.performRequest(method, url, Collections.emptyMap(), entity);


            //responseBody = EntityUtils.toString(response.getEntity());

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void close() throws IOException {

        this.restClient.close();
    }


    public String toString(){
        return JSONObject.toJSONString(this);
    }




}

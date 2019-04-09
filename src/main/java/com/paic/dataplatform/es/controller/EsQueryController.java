package com.paic.dataplatform.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.paic.dataplatform.es.Util.ThisWebUtils;
import com.paic.dataplatform.es.component.LocalConfig;
import com.paic.dataplatform.es.component.pool.EsClient;
import com.paic.dataplatform.es.component.pool.EsClientPool;

import org.apache.http.*;

import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;

/**
 * Created by czx on 3/28/19.
 */
@RestController
public class EsQueryController {

    private static final Logger logger = LoggerFactory.getLogger(EsQueryController.class);

    @Resource
    private EsClientPool pool;


    @Resource
    private LocalConfig localConfig;


    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public void queryOfGet(HttpServletRequest request,HttpServletResponse response)  throws IOException {

        String contentType = request.getContentType();
        if(contentType!=null&&contentType.toLowerCase().contains("application/json")){

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();

                if(body!=null){
                     handleRequest("GET",request,response,body);
                }

            }catch (Exception e){
                e.printStackTrace();
                logger.error("es请求的请求内容有误",e);

            }

        }

         handleRequest("GET",request,response,null);

    }



    @RequestMapping(value = "/**", method = RequestMethod.PUT)
    public void queryOfPut(HttpServletRequest request,HttpServletResponse response,@RequestBody String body)  throws IOException {


         handleRequest("PUT",request,response,body);
    }


    @RequestMapping(value = "/**", method = RequestMethod.POST)
    public void queryOfPost(HttpServletRequest request,HttpServletResponse response,@RequestBody String body) throws IOException  {


         handleRequest("POST",request,response,body);
    }

    @RequestMapping(value = "/**", method = RequestMethod.DELETE)
    public void queryOfDelete(HttpServletRequest request,HttpServletResponse response,@RequestBody String body) throws IOException  {


         handleRequest("DELETE",request,response,body);
    }



    private void handleRequest(String method,HttpServletRequest request, HttpServletResponse response,String body) throws IOException {

        String key = null;

        JSONObject result = new JSONObject();
        try{

            key = ThisWebUtils.getUrlKey(request);

            if(key==null||key.length()==0||key.startsWith("/")){
                throw new Exception("没有指定es集群");
            }


            String rerouteUri = ThisWebUtils.getRerouteUri(request,localConfig.getFirstGateway());

            EsClient client = pool.getEsClient(key);
            if(client == null){
                throw new Exception("请求的集群不存在");
            }

            String  query = request.getQueryString();
            if(query!=null){
                rerouteUri =  rerouteUri+"?"+query;
            }

            Response esResponse= client.handleEsRequest(rerouteUri,body,method);

            copyResponseHeaders(esResponse,request,response);

            copyResponseEntity(esResponse,response);

            //return response;

        }catch (ConnectException e) {
            e.printStackTrace();
            logger.error("连接出错,不存在该集群： {}",key);

            result.put("error",e.getMessage());

        }catch (ResponseException e) {
            e.printStackTrace();
            logger.error("请求返回异常",e);

            String msg = e.getMessage();
            String exMsg = msg.substring(msg.indexOf("\n")+1,msg.length());

            result =  JSONObject.parseObject(exMsg);

        }catch (Exception e){
            e.printStackTrace();

            logger.error("请求es出错",e);

            if(e.getMessage() !=null&&e.getMessage().length()>0){
                result.put("error",e.getMessage());
            }else{
                result.put("error","请求es出错");
            }
        }

        try {
            if(!result.isEmpty()) {
                response.setContentType("application/json;charset=UTF-8");

                OutputStream stream = response.getOutputStream();

                stream.write(result.toJSONString().getBytes("UTF-8"));
            }
        }catch (Exception e){
            logger.error("服务器端出错",e);
            throw e;
        }

    }


    private void copyResponseHeaders(Response esResponse, HttpServletRequest servletRequest,
                                       HttpServletResponse servletResponse) {
        for (Header header : esResponse.getHeaders()) {
            copyResponseHeader(servletRequest, servletResponse, header);
        }
    }


    private void copyResponseHeader(HttpServletRequest servletRequest,
                                      HttpServletResponse servletResponse, Header header) {
        String headerName = header.getName();

        String headerValue = header.getValue();

        servletResponse.addHeader(headerName, headerValue);

    }

    /** Copy response body data (the entity) from the proxy to the servlet client. */
    protected void copyResponseEntity(Response esResponse, HttpServletResponse servletResponse)
            throws IOException {
        HttpEntity entity = esResponse.getEntity();
        if (entity != null) {
            OutputStream servletOutputStream = servletResponse.getOutputStream();
            entity.writeTo(servletOutputStream);
        }
    }



}

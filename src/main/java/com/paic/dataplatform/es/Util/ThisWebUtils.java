package com.paic.dataplatform.es.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by czx on 4/5/19.
 */
public class ThisWebUtils {


    public static  String getUrlKey(HttpServletRequest request) throws Exception {

        String uri = request.getRequestURI().substring(1);


        //jiqun
        if(uri==null||uri.length()==0||uri.startsWith("/")){
            return "";
        }

        int i = uri.indexOf("/");

        String key = uri.substring(0,i==-1?uri.length():i);

        return key;

    }

    /**
     *  zhi huo qu uri meiyou canshu
     * @param request
     * @param isFirstGateway
     * @return
     */
    public static String getRerouteUri(HttpServletRequest request,boolean isFirstGateway){

        String uri = request.getRequestURI().substring(1);

        String rerouteUri = null;

        if(isFirstGateway){
            rerouteUri = "/"+uri;
        }else{
            int i = uri.indexOf("/");
            rerouteUri = i==-1?"/":uri.substring(i);
        }
        return rerouteUri;
    }


    /**
     * 获取请求的内容
     * @param request
     * @return
     * @throws IOException
     */
    public  static   String getBody(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        if(contentType!=null&&contentType.toLowerCase().contains("application/json")){


                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();
                return  body;


        }
        return null;
    }



}

package com.paic.dataplatform.es.component.safegard;

import com.paic.dataplatform.es.Util.ThisWebUtils;
import com.paic.dataplatform.es.component.pool.EsClientPool;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * Created by czx on 4/9/19.
 */
public class InfoCompareSafeGard extends SafeGuard {

    private static final Logger logger
            = LoggerFactory.getLogger(EsClientPool.class);

    private HashMap<String,List<String>> infos = new  HashMap<String,List<String>>();


    private String key;

    @Override
    boolean check(HttpServletRequest request, HttpServletResponse response) {

        try {
            String key = ThisWebUtils.getUrlKey(request);

            String rerouteUri = ThisWebUtils.getRerouteUri(request,false);

            List<String> cp = infos.get(key);

            for(String s : cp){
               if( rerouteUri.contains(s)){
                  return false;
               }
            }


        }catch(Exception ex){
            logger.error("safe guard error:",ex);
        }

        return false;
    }

    @Override
    String checkMsg(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}

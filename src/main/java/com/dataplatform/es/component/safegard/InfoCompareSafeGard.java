package com.dataplatform.es.component.safegard;

import com.dataplatform.es.Util.ThisWebUtils;
import com.dataplatform.es.component.pool.EsClientPool;
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
    public boolean check(HttpServletRequest request, HttpServletResponse response) {

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
    public String checkMsg(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public Object clone() {
        return null;
    }
}

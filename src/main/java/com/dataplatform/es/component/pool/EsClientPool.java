package com.dataplatform.es.component.pool;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.io.IOException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by czx on 3/28/19.
 */
@Component
public class EsClientPool implements CommandLineRunner {

    // init the logger
    private static final Logger logger
                 = LoggerFactory.getLogger(EsClientPool.class);



    private static ConcurrentHashMap<String,EsClient> esClientPool = new ConcurrentHashMap<String,EsClient>();

    public static ConcurrentHashMap<String, EsClient> getEsClientPool() {
        return esClientPool;
    }



    public EsClient getEsClient(String key){

        return esClientPool.get(key);
    }


    @Override
    public void run(String... args) throws Exception {
        poolInitial(null,null);

    }


    public void poolInitial(String key,EsInfo info){

        EsClient client = new EsClient();
        info = new EsInfo();
        info.setHost("localhost");
        info.setPort(9292);

        logger.info("初始化一个es客户端： {}:{}",info.getHost(),info.getPort());

        client.setInfo(info);
        client.init();
        esClientPool.put("local",client);
    }



    /**
     * 增加一个es集群连接
     * @param key
     * @param info
     */
    public void AddEsClient(String key,EsInfo info){

        logger.info("增加一个es客户端： {}:{}",info.getHost(),info.getPort());

        EsClient client = new EsClient();
        client.setInfo(info);
        client.init();
        esClientPool.put("local",client);

    }

    public void AddEsClients(Map<String,EsInfo> infos){

        logger.info("增加一批客户端");

        for(String key : infos.keySet()){

            EsInfo info = infos.get(key);

            AddEsClient(key,info);
        }
    }

    /**
     * 移除一个es集群连接
     * @param key
     */
    public void removedEsClient(String key){

        try {
            EsClient client = esClientPool.get(key);

            logger.info("移除一个es客户端： {}:{}", client.getInfo().getHost(),client.getInfo().getPort());


            esClientPool.remove(key);

            client.close();

        }catch(NullPointerException e){
            logger.warn("移除es客户端失败，es-pool 中没找到对应的客户端。",e);
        }catch(IOException e){
            logger.warn("移除es客户端成功，client关闭失败。",e);
        }

    }

}

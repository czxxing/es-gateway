package com.paic.dataplatform.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.paic.dataplatform.es.Util.DateUtils;
import com.paic.dataplatform.es.Util.UuidUtils;
import com.paic.dataplatform.es.bus.ListenBus;
import com.paic.dataplatform.es.component.pool.EsClientPool;
import com.paic.dataplatform.es.component.pool.EsInfo;
import com.paic.dataplatform.es.event.CreateEsClientEsEvent;
import com.paic.dataplatform.es.event.CreateEsClientsEsEvent;
import com.paic.dataplatform.es.event.DeleteEsClientEsEvent;
import com.paic.dataplatform.es.model.ClientInfoDTO;
import com.paic.dataplatform.es.model.ClientInfosDTO;
import com.paic.dataplatform.es.model.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by czx on 4/2/19.
 */
@RestController
public class EsClientMaintenanceController {

    private static final Logger logger = LoggerFactory.getLogger(EsClientMaintenanceController.class);

    @Resource
    private ListenBus bus;

    @Resource
    private EsClientPool esPool;



    @RequestMapping(value = "/es/client", method = RequestMethod.PUT,produces="application/json;charset=UTF-8")
    public ResultDTO putEsClient(@RequestBody JSONObject body)  {

          ResultDTO result = new ResultDTO();

          try{

              logger.info("增加一个es的客户端");

              ClientInfoDTO  cInfo = JSONObject.toJavaObject(body,ClientInfoDTO.class);

              String uuid = UuidUtils.generateUUID();
              CreateEsClientEsEvent event =
                      new CreateEsClientEsEvent(uuid,cInfo.getKey(),cInfo.getInfo(), DateUtils.getCurrentDateTime());

              bus.post(event);

              result.setResultCode("0");
              result.setResultMessage("Suceess");

              return result;
          }catch(Exception e){
               logger.error("信息有误",e);

              result.setResultCode("1");
              result.setResultMessage(e.getMessage());

              return result;

          }

    }

    @RequestMapping(value = "/es/clients", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResultDTO getEsClients() {

        ResultDTO result = new ResultDTO();

        try{

            logger.info("查看es的客户端");

            result.setResultCode("0");
            result.setResultMessage("Suceess");

            result.setData(esPool.getEsClientPool());

            return result;
        }catch(Exception e){
            logger.error("信息有误",e);

            result.setResultCode("1");
            result.setResultMessage(e.getMessage());

            return result;

        }
    }

    @RequestMapping(value = "/es/clients", method = RequestMethod.PUT,produces="application/json;charset=UTF-8")
    public ResultDTO putEsClients(@RequestBody JSONObject body)  throws IOException {

        ResultDTO result = new ResultDTO();

        try{

            logger.info("增加一堆es的客户端");

            ClientInfosDTO  cInfos = JSONObject.toJavaObject(body, ClientInfosDTO.class);

            if(cInfos.getClientInfos().size()==0){
                throw new Exception("需要至少一个客户端");
            }

            String uuid = UuidUtils.generateUUID();

            HashMap<String,EsInfo> infos = new HashMap<String,EsInfo>();
            for(ClientInfoDTO info : cInfos.getClientInfos()){

                String  key = info.getKey();
                EsInfo c =  info.getInfo();
                infos.put(key,c);
            }

            CreateEsClientsEsEvent  event =
                    new CreateEsClientsEsEvent(uuid,infos, DateUtils.getCurrentDateTime());

            bus.post(event);

            result.setResultCode("0");
            result.setResultMessage("Suceess");

            return result;
        }catch(Exception e){
            logger.error("信息有误",e);

            result.setResultCode("1");
            result.setResultMessage(e.getMessage());

            return result;

        }
    }

    @RequestMapping(value = "/es/client", method = RequestMethod.DELETE,produces="application/json;charset=UTF-8")
    public ResultDTO delEsClient(@RequestBody JSONObject body)  {

        ResultDTO result = new ResultDTO();

        try{

            logger.info("删除一个es的客户端");

            String  key = body.getString("key");

            String uuid = UuidUtils.generateUUID();

            DeleteEsClientEsEvent event =
                    new DeleteEsClientEsEvent(uuid,key, DateUtils.getCurrentDateTime());

            bus.post(event);

            result.setResultCode("0");
            result.setResultMessage("Suceess");

            return result;
        }catch(Exception e){
            logger.error("信息有误",e);

            result.setResultCode("1");
            result.setResultMessage(e.getMessage());

            return result;

        }

    }


}

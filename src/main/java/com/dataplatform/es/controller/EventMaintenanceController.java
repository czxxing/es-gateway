package com.dataplatform.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.dataplatform.es.Util.DateUtils;
import com.dataplatform.es.Util.UuidUtils;
import com.dataplatform.es.bus.ListenBus;
import com.dataplatform.es.event.AllEsEvent;
import com.dataplatform.es.event.EventEsEvent;
import com.dataplatform.es.model.EventDTO;
import com.dataplatform.es.model.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by czx on 4/2/19.
 */
@RestController
public class EventMaintenanceController {

    private static final Logger logger = LoggerFactory.getLogger(EventMaintenanceController.class);

    @Resource
    private ListenBus bus;


    @RequestMapping(value = "/event", method = RequestMethod.PUT,produces="application/json;charset=UTF-8")
    public ResultDTO putEvent(@RequestBody JSONObject body)  {

        ResultDTO result = new ResultDTO();

        try{

            logger.info("修改一个事件内容");

            EventDTO eInfo = JSONObject.toJavaObject(body,EventDTO.class);

            String uuid = UuidUtils.generateUUID();
            EventEsEvent event =
                    new EventEsEvent(uuid,eInfo.getEventName(),eInfo.getOpen(), DateUtils.getCurrentDateTime());

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

    @RequestMapping(value = "/event", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ResultDTO getEvent()  {

        ResultDTO result = new ResultDTO();

        try{

            logger.info("查看事件内容");

            result.setResultCode("0");
            result.setResultMessage("Suceess");

            result.setData(AllEsEvent.eventMap());

            return result;
        }catch(Exception e){
            logger.error("信息有误",e);

            result.setResultCode("1");
            result.setResultMessage(e.getMessage());

            return result;

        }
    }



}

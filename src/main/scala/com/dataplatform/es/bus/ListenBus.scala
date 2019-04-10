package com.dataplatform.es.bus

import java.util.concurrent.LinkedBlockingQueue
import javax.annotation.Resource
import com.dataplatform.es.component.pool.EsClientPool
import com.dataplatform.es.serializer.LocalSerializer
import com.dataplatform.es.event._
import com.dataplatform.es.kafka.KafkaUtil
import org.json4s.native.Json
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component







/**
  * Created by czx on 4/1/19.
  */
@Component
class ListenBus extends Thread {

  private lazy val EVENT_QUEUE_CAPACITY = 10000;

  private lazy val eventQueue = new LinkedBlockingQueue[EsEvent](EVENT_QUEUE_CAPACITY)

  private lazy val logger = LoggerFactory.getLogger(classOf[ListenBus])

  @Resource
  private var esPool:EsClientPool = _

  override def run():Unit= {

    logger.info("{},listenbus 线程开始运行",Thread.currentThread().getId)

    while(true){
      try
        handle()
      catch {
        case e: InterruptedException =>
          logger.info("{},listenbus 中断运行", Thread.currentThread().getId)
      }
    }

  }

  def handle():Unit={

    val event: EsEvent = eventQueue.take();

    val eventName:String = event.getClass.getSimpleName;

    val isOpen = AllEsEvent.eventMap.get(eventName)

    isOpen match {
      case true =>
        eventHandle(event);
      case false =>
        // wu
    }


  }

  def eventHandle(event:EsEvent):Unit={

    implicit val formats = org.json4s.DefaultFormats + LocalSerializer ++ org.json4s.ext.JavaTypesSerializers.all

    event  match {

      case request :RequestEsEvent =>

        logger.debug(request.toString)

        KafkaUtil.sendMsg("RequestEsEvent",Json(formats).write(request))

      case respose:ResposeEsEvent =>

        logger.debug(respose.toString)
        KafkaUtil.sendMsg("ResposeEsEvent",Json(formats).write(respose))

      case periodEvent:  PeriodEvent =>

        logger.debug("上报节点的信息: {}",periodEvent.toString)
        KafkaUtil.sendMsg("PeriodEvent",Json(formats).write(periodEvent))


      case createOneEvent:CreateEsClientEsEvent =>

        logger.info("添加一个esClient: {}",createOneEvent.toString)
        esPool.AddEsClient(createOneEvent.key,createOneEvent.info)


      case  createEvent:CreateEsClientsEsEvent =>
        logger.info("添加一堆esClients: {}",createEvent.toString)
        esPool.AddEsClients(createEvent.infos)

      case delEvent : DeleteEsClientEsEvent =>
        logger.info("添加一堆esClients: {}",delEvent.toString)
        esPool.removedEsClient(delEvent.key)

      case eventEsEvent : EventEsEvent =>
        logger.info("事件上报开关: {}",eventEsEvent.toString)
        AllEsEvent.eventMap.put(eventEsEvent.key,eventEsEvent.open)


      case _ =>
        logger.warn("Some undefine event receive ")


    }
  }

  def post(event:EsEvent ):Unit = {

    eventQueue.put(event);

  }



}



package com.dataplatform.es.kafka

import org.springframework.beans.factory.annotation.Value
import java.util.Properties


import com.dataplatform.es.component.SpringContext
import com.dataplatform.es.event.AllEsEvent
import org.apache.kafka.clients.producer._
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component






/**
  * Created by czx on 4/2/19.
  */
@Configuration
@Component("KafkaUtil")
class KafkaUtil{

  @Value("${kafka.broker}")
  var brokers :String= _

  @Value("${kafka.topic}")
  var topic :String= _

}


object KafkaUtil {

  private lazy val logger = LoggerFactory.getLogger(classOf[KafkaUtil])


  private lazy val brokers :String= { SpringContext.getBean("KafkaUtil").asInstanceOf[KafkaUtil].brokers }

  private lazy val topic :String= { SpringContext.getBean("KafkaUtil").asInstanceOf[KafkaUtil].topic }

  /**
    * 有线程异常不管
    */
  private var errorNum = 5;

  private lazy val producer: KafkaProducer[String,String] = init()



  def init():KafkaProducer[String,String] = {




    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("acks", "all")
    props.put("retries", new Integer(0))
    props.put("batch.size", new Integer(16384))
    props.put("linger.ms", new Integer(1))
    props.put("buffer.memory", new Integer(33554432))
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


    new KafkaProducer(props)

  }


  def sendMsg(key:String,value:String): Unit = {

    logger.debug("kafka send msg : {},{}",key,value,"")

    val record = new ProducerRecord(topic,key,value)

    producer.send(record, new Callback() {
      override def onCompletion(metadata: RecordMetadata, ex: Exception) = {
        if (ex != null) {
          handleException(ex)
        } else {
          //println(s"Successfully sent message : $metadata")
        }
      }

      def handleException(ex: Exception): Unit = {

        import scala.collection.JavaConversions._
        errorNum = errorNum -1
        logger.error("发送kafka出错",ex)

        if(errorNum<0) {

          for (key <- AllEsEvent.eventMap.keySet() ){

            AllEsEvent.eventMap.put(key, false);

          }
        }


      }
    })



  }


}

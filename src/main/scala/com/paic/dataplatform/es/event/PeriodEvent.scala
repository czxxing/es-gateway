package com.paic.dataplatform.es.event


import java.sql.Timestamp

import com.paic.dataplatform.es.component.pool.EsClient

import scala.collection.JavaConversions._

/**
  * Created by czx on 4/2/19.
  */
case class PeriodEvent (override val uuid:String,
                        host: String,
                        port: Int,
                        eventInfos :java.util.concurrent.ConcurrentHashMap[String,Boolean],
                        esClients:java.util.concurrent.ConcurrentHashMap[String,EsClient],
                        override val creatTime:String,
                        creatTimestamp: Timestamp
                       ) extends EsEvent(uuid,creatTime) {

}

package com.dataplatform.es.event

import java.sql.Timestamp

import com.dataplatform.es.component.pool.EsClient

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

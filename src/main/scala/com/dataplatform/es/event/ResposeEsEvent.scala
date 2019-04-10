package com.dataplatform.es.event

import java.sql.Timestamp

/**
  * Created by czx on 4/1/19.
  */
case class ResposeEsEvent(override val uuid:String,
                          host: String,
                          port: Int,
                          key:String,
                          entity:String,
                          size:Double,
                          ttl:Double,
                          override val creatTime:String,
                          creatTimestamp: Timestamp,
                          hc:Double
                         ) extends  EsEvent(uuid,creatTime) {

}

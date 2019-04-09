package com.paic.dataplatform.es.event

import java.sql.Timestamp
import java.util.Date

/**
  * Created by czx on 4/1/19.
  */
case class RequestEsEvent( override val uuid:String,
                           host: String,
                           port: Int,
                           key:String,url:String,
                           entity:String,
                           method:String,
                           time:String,
                           override val creatTime:String,
                           creatTimestamp: Timestamp
                         ) extends  EsEvent(uuid,creatTime){




}

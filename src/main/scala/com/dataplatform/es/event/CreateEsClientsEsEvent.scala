package com.dataplatform.es.event

import com.dataplatform.es.component.pool.EsInfo

import scala.collection.JavaConversions._

/**
  * Created by czx on 4/1/19.
  */
case class CreateEsClientsEsEvent(override val uuid:String, infos: java.util.Map[String,EsInfo],override val creatTime:String) extends EsEvent(uuid,creatTime){

}

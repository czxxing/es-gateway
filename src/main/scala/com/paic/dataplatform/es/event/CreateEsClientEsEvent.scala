package com.paic.dataplatform.es.event

import com.paic.dataplatform.es.component.pool.EsInfo

/**
  * Created by czx on 4/1/19.
  */
case class CreateEsClientEsEvent(override val uuid:String, key:String, info:EsInfo,override val creatTime:String) extends EsEvent(uuid ,creatTime){

}

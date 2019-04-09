package com.paic.dataplatform.es.event

/**
  * Created by czx on 4/2/19.
  */
case class DeleteEsClientEsEvent(override val uuid:String, key:String,override val creatTime:String) extends EsEvent(uuid,creatTime ) {

}

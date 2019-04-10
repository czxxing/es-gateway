package com.dataplatform.es.event

/**
  * Created by czx on 4/2/19.
  */
case class EventEsEvent(override val uuid:String,
                        key:String,
                        open:Boolean,
                        override val creatTime:String) extends EsEvent(uuid,creatTime ) {

}

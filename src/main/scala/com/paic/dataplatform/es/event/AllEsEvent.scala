package com.paic.dataplatform.es.event

/**
  * Created by czx on 4/2/19.
  */
object AllEsEvent {





   val eventMap  = new java.util.concurrent.ConcurrentHashMap[String,Boolean](){
     {
       put("RequestEsEvent", true)
       put("ResposeEsEvent", true)
       put("CreateEsClientEsEvent", true)
       put("CreateEsClientsEsEvent", true)
       put("DeleteEsClientEsEvent", true)
       put("PeriodEvent", true)
       put("EventEsEvent", true)

     }
   }


}

package com.paic.dataplatform.es.serializer

import com.paic.dataplatform.es.component.pool.{EsClient, EsInfo}
import org.json4s.{CustomSerializer, DefaultFormats, Extraction, FieldSerializer}
import org.json4s.JsonAST.{JField, JObject}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by czx on 4/3/19.
  */
object LocalSerializer extends CustomSerializer[Any] (thisFormat =>
  (
    {
      case x =>
        throw new RuntimeException("不感兴趣")
    },
    {

      case e: EsClient => JObject("info" -> Extraction.decompose(e.getInfo)(thisFormat))
      case i: EsInfo => Extraction.decompose(i)(DefaultFormats + FieldSerializer[EsInfo]())
      case m:java.util.concurrent.ConcurrentHashMap[Any, Any] =>
        implicit val formats = org.json4s.DefaultFormats
        JObject({
          val con = ArrayBuffer[JField]()
          val it = m.keySet().iterator()
          while (it.hasNext) {
            val key = it.next()
            con.append(JField(key.toString, Extraction.decompose(m.get(key))(thisFormat)))
          }
          con.toList
        }
        )
    }
  )
)

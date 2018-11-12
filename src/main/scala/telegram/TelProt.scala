package telegram

import scodec.Codec
import scodec.codecs.{intL, long, longL}


case class ReqPQ(auth_key_id: Long, message_id: Long, message_length: Int, req_pq: Int, nonceB: Long, nonceM: Long)

object ReqPQ {
  val reqPQCodec: Codec[ReqPQ] = (longL(64) :: longL(64) :: intL(32) :: intL(32):: long(64):: long(64)).as[ReqPQ]
}

case class ResPQ(auth_key_id: Long, message_id: Long, message_length: Int, req_pq: Int, nonceB: Long, nonceM: Long)

object ResPQ {
  val resPQCodec: Codec[ResPQ] = (longL(64) :: longL(64) :: intL(32) :: intL(32):: long(64):: long(64)).as[ResPQ]
}

//req_DH_params
case class ReqDHParams(auth_key_id: Long, message_id: Long, message_length: Int, req_pq: Int, nonceB: Long, nonceM: Long)

object ReqDHParams {
  val reqDHParamsCodec: Codec[ReqDHParams] = (longL(64) :: longL(64) :: intL(32) :: intL(32):: long(64):: long(64)).as[ReqDHParams]
}

package telegram

import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.util.ByteString
import scodec.Attempt.{Failure, Successful}
import scodec.DecodeResult
import scodec.bits.BitVector


class TelStage extends GraphStage[FlowShape[ByteString, ByteString]] {

  val in = Inlet[ByteString]("TelStage.in")
  val out = Outlet[ByteString]("TelStage.out")

  def shape: FlowShape[ByteString, ByteString] = FlowShape(in, out)

  def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with OutHandler {

      def onPull(): Unit = if (!hasBeenPulled(in)) tryPull(in)
      setHandler(out, this)

      private var remainingBits = BitVector.empty

      val inReqPQHandler = new InHandler {

        def emitResPQ: Unit = {
          val v = ResPQ(0, 0x51e57ac42770964aL, 20, 0x60469778,  0x3E0549828CCA27E9L, 0x66B301A48FECE2FCL )
          ResPQ.resPQCodec.encode(v) match {
            case Successful(x) => emit(out, ByteString(x.toByteBuffer))
             setHandler(in, inReqDHParamsHandler)

            case Failure(err) => fail(out, new Exception(err.messageWithContext))
          }
        }

        def onPush(): Unit = {
          val bits = BitVector.view(grab(in).asByteBuffer)

          remainingBits = ReqPQ.reqPQCodec.decode(remainingBits++bits) match {
            case Successful(DecodeResult(_, BitVector.empty)) =>
              emitResPQ
              BitVector.empty
            case Successful(DecodeResult(_, remainder)) =>
              emitResPQ
              remainder
            case Failure(e) =>
              fail(out, new Exception(e.messageWithContext))
              BitVector.empty
          }

        }
      }

      val inReqDHParamsHandler = new InHandler {


        def onPush(): Unit = {
          val bits = BitVector.view(grab(in).asByteBuffer)

          remainingBits = ReqPQ.reqPQCodec.decode(remainingBits++bits) match {
            case Successful(DecodeResult(_, BitVector.empty)) =>
              completeStage()
              BitVector.empty
            case Successful(DecodeResult(_, remainder)) =>
              completeStage()
              remainder
            case Failure(e) =>
              fail(out, new Exception(e.messageWithContext))
              BitVector.empty
          }

        }
      }


      setHandler(in, inReqPQHandler)
    }

}

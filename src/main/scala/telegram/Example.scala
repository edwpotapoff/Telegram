package telegram

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Tcp}
import akka.stream.scaladsl.Tcp.ServerBinding
import akka.util.ByteString

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration




object Main extends App {


  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()


  val telFlow = Flow[ByteString]
    .via( new TelStage())

  val binding: Future[ServerBinding] =
    Tcp().bind("127.0.0.1", 8888).to(
      Sink.foreach{ connection =>
        connection.handleWith(telFlow)
      }
    ).run()



  println(s"Server online at http://localhost:8080/")

  Await.result(system.whenTerminated, Duration.Inf)


}


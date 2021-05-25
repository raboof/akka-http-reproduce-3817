
import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling._
import akka.http.scaladsl.server.Directives._

object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val ec = system.dispatcher

  for {
    binding <- Http()
      .newServerAt("localhost", 0)
      .bind(entity(as[String]) { e =>
        println(s"Got request $e")
        complete("Response")
      })
    response <- {
      val baseUrl = s"http://${binding.localAddress.getHostName}:${binding.localAddress.getPort}"
      println(baseUrl)
      Http().singleRequest(
        HttpRequest(
          method = HttpMethods.POST,
          uri = baseUrl,
          entity = HttpEntity(ContentTypes.`application/json`, """["value-1","value-2","value-3]""" )
        )
      )
    }
    body <- {
      println("Got response");
      Unmarshal( response.entity ).to[ String ]
    }
  } yield {
    println(body)
    //val res = read[ PollResponse ]( body )
    //UUID.fromString( res.jobId )
  }
}


import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling._

object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val ec = system.dispatcher

  val baseUrl = "https://akka.io";

  for {
            response <- Http().singleRequest(
                HttpRequest(
                    method = HttpMethods.POST,
                    uri = baseUrl + s"/submit",
                    entity = HttpEntity( ContentTypes.`application/json`, """["value-1","value-2","value-3]""" )
                )
            )
            body <- {
                Unmarshal( response.entity ).to[ String ]
            }
        } yield {
          println(body)
            //val res = read[ PollResponse ]( body )
            //UUID.fromString( res.jobId )
        }
}

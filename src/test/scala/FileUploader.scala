import cats.effect.IO
import cats.effect.kernel.Resource
import mx.cinvestav.DefaultConfig
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.{Method, Request, Uri}
import org.http4s.multipart.{Multipart, Part}
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import scala.concurrent.ExecutionContext.global
import java.io.File

class FileUploader extends munit .CatsEffectSuite {
  implicit val config:DefaultConfig = ConfigSource.default.loadOrThrow[DefaultConfig]
  implicit val unsafeLogger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]
  final val TARGET = "/home/nacho/Programming/Scala/file-api-http4s/target"
  final val SOURCE = s"$TARGET/source"
  val clientResource: Resource[IO, Client[IO]] = BlazeClientBuilder[IO](global).resource
  test("Upload a file") {
    val filename = "01.jpg"
    val file = new File(s"$SOURCE/$filename")
    val body = Multipart[IO](
      Vector(Part.fileData("image_bb",file))
    )
    val request = Request[IO](
      Method.POST,
      Uri.unsafeFromString(s"http://172.17.0.2:${config.port}/upload")
//        Uri.unsafeFromString(s"http://localhost:${config.port}/upload")
    )
      .withEntity(body)
      .withHeaders(body.headers.headers)

    clientResource.use{ client =>
      val res = client.expect[String](request)
      res.flatMap(IO.println)
    }

  }

}

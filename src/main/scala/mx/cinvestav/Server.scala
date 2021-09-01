package mx.cinvestav

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.implicits._
import org.http4s.dsl.io._
import org.typelevel.log4cats.Logger

import scala.concurrent.ExecutionContext.global

object Server {
  private def httpApp(sink:String)(implicit L:Logger[IO]): Kleisli[IO, Request[IO],
    Response[IO]] =
    Router[IO](
      "/" -> Routes(sink),
      "/freddy" -> HttpRoutes.of[IO]{
        case GET -> Root => Ok("FREDDY GAY")
      }
    ).orNotFound

  def run(host:String,port:Int,sink:String)(implicit L:Logger[IO]): IO[Unit] = for {
    _ <- BlazeServerBuilder[IO](executionContext = global)
      .bindHttp(port,host)
      .withHttpApp(httpApp = httpApp(sink))
      .serve
      .compile
      .drain
  } yield ()
}

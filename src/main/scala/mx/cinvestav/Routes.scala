package mx.cinvestav

import cats.implicits._
import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.multipart.Multipart
import fs2.Stream
import fs2.io.file.Files
import org.typelevel.log4cats.Logger

import java.io.File
import java.nio.file.Paths
import java.util.UUID

object Routes {

  def apply(sink:String)(implicit L:Logger[IO]) = HttpRoutes.of[IO]{
    case GET -> Root / "download"/ filename =>  for {
      _          <- L.debug(s"DOWNLOAD $filename")
      sinkPath   = Paths.get(sink)
      filePath   = sinkPath.resolve(filename)
      file       = sinkPath.toFile
      fileExists = file.exists()
      res <- if (!fileExists) NotFound()
      else for {
        _          <- IO.unit
        fileStream = Files[IO].readAll(filePath,8192)
        res        <- Ok(fileStream)
      } yield res
    } yield res
    case req@POST -> Root / "upload"  => for {
      _            <- L.debug("UPLOAD FILE")
      sinkPath     = Paths.get(sink)
      res      <- req.decode[Multipart[IO]]{ m=>
//      get parts from http request
        val parts = m.parts
//      Declaration of a stream of upload files.
        val streamOfUploads = Stream.emits(parts)
          .flatMap{part =>
            val filename = part.filename.getOrElse(UUID.randomUUID().toString)
            val fileSinkPath = sinkPath.resolve(filename)
            part.body.through(Files[IO].writeAll(fileSinkPath) )
          }
          .compile.drain
//      First upload all files and then Response with a message
        streamOfUploads *> Ok(s"UPLOAD ${parts.length} SUCCESSFULLY")
      }
    } yield res
  }

}

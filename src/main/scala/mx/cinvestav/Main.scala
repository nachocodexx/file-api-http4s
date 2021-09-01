package mx.cinvestav
import cats.implicits._
import cats.effect._
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig._
import pureconfig.generic.auto._

object Main extends IOApp{
  implicit val config:DefaultConfig = ConfigSource.default.loadOrThrow[DefaultConfig]
  implicit val unsafeLogger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]
  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- Logger[IO].debug(s"SERVER IS RUNNING AT http://localhost:${config.port}")
    _ <- Server.run(config.host,config.port,config.sink)
  } yield ExitCode.Success
}

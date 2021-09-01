import sbt._

object Dependencies {

  def apply(): Seq[ModuleID] = {
    lazy val PureConfig = "com.github.pureconfig" %% "pureconfig" % "0.15.0"
    lazy val Log4Cats =   "org.typelevel" %% "log4cats-slf4j"   % "2.1.1"
    val http4sVersion = "1.0.0-M23"
    lazy val Logback   = "ch.qos.logback" % "logback-classic" % "1.2.3"
    lazy val MUnitCats ="org.typelevel" %% "munit-cats-effect-3" % "1.0.3" % Test
    lazy val Http4s = Seq(
      "org.http4s" %% "http4s-dsl",
      "org.http4s" %% "http4s-blaze-server",
      "org.http4s" %% "http4s-blaze-client",
      "org.http4s" %% "http4s-circe"
    ).map(_ % http4sVersion)
    //
    val fs2Version = "3.0.6"
    lazy val Fs2 = Seq(
      "co.fs2" %% "fs2-core",
      "co.fs2" %% "fs2-io"
    ).map(_ % fs2Version)
    lazy val CatsEffect ="org.typelevel" %% "cats-effect" % "3.2.5"
    (Http4s++Fs2)++Seq(PureConfig,Log4Cats,CatsEffect,Logback,MUnitCats)
  }
}

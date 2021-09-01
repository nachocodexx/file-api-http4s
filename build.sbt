
lazy val app = (project in file(".")).settings(
  name := "file-api-http4s",
  version := "0.1",
  scalaVersion := "2.13.6",
  libraryDependencies ++= Dependencies(),
    assemblyJarName := "app.jar",
)

name := "scualador"
version := "0.1"
scalaVersion := "2.11.12"

libraryDependencies += "com.typesafe" % "config" % "1.2.0"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.11"


assemblyJarName in assembly := s"${sys.env.getOrElse("ALUMNO", "NO_ALUMNO")}-1.0.jar"

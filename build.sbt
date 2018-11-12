name := "TimothyKlimScala_test"

version := "0.1"

scalaVersion := "2.12.7"

resolvers += "Sonatype Public" at "https://oss.sonatype.org/content/groups/public/"

libraryDependencies += "org.scodec" %% "scodec-bits" % "1.1.6"

libraryDependencies += "org.scodec" %% "scodec-core" % "1.10.3"

libraryDependencies ++= {
  if (scalaBinaryVersion.value startsWith "2.10")
    Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
  else Nil
}

lazy val akkaVersion    = "2.5.11"


libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream"          % akkaVersion
  
)


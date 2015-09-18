name := "routematching"


lazy val commonSettings = Seq(
  organization := "com.zgagnon",
  version := "1.0",
  scalaVersion := "2.11.7",
  libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "org.specs2" %% "specs2-mock" % "3.6.4" % "test",
  "net.codingwell" %% "scala-guice" % "4.0.0"
  )
)

lazy val core = project.settings(commonSettings:_*)

lazy val verify1 = project.dependsOn(core).settings(commonSettings:_*).settings(
    mainClass in assembly := Some("com.zgagnon.routematching.MainVerify1"),
    assemblyJarName in assembly := "verify1.jar"
  )

lazy val verify2 = project.dependsOn(core).settings(commonSettings:_*).settings(
  mainClass in assembly := Some("com.zgagnon.routematching.MainVerify2"),
    assemblyJarName in assembly := "verify2.jar"
)

lazy val root = (project in file(".")).aggregate(core, verify1, verify2)
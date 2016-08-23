import sbt.Keys._

name := "AlbacrossPK"

version := "1.0"
scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.0",
  "io.argonaut" %% "argonaut" % "6.1",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",

  // to avoid warning about multiple dependencies with different versions
  "org.scala-lang" % "scala-compiler" % "2.11.8",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
)



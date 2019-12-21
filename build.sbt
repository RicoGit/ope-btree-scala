import SbtCommons._

lazy val global = project
  .in(file("."))
  .settings(commons)

// core entities for all b-tree modules
lazy val `b-tree-core` = project
  .in(file("b-tree/core"))
  .settings(commons, libraryDependencies ++= Seq(catsCore, scodecBits, monocle, scalatest))

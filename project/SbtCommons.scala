import sbt.Keys._
import sbt._

object SbtCommons {

  val scalaV = scalaVersion := "2.13.1"

  val commons = Seq(
    scalaV,
    version                   := "0.1",
    fork in Test              := true,
    parallelExecution in Test := false,
    organizationName          := "Constantine Solovev",
  )

  val Cats2V = "2.0.0"
  val MonixV = "3.1.0"
  val Scalatest = "3.1.0"
  val Scodec = "1.1.12"
  val Monocle = "1.3"

  val catsCore = "org.typelevel"   %% "cats-core"    % Cats2V
  val catsEffect = "org.typelevel" %% "cats-effect"  % Cats2V
  val monix3 = "io.monix"          %% "monix"        % MonixV
  val scodecBits = "org.scodec"    %% "scodec-bits"  % Scodec
  val monocle = "com.dragishak"    %% "monocle-cats" % Monocle

  val scalatest = "org.scalatest" %% "scalatest" % Scalatest
}

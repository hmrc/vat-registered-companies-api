import sbt._

object AppDependencies {

  private val playSuffix = "-play-30"

  val compile = Seq(
    "org.typelevel" %% "cats-core"                 % "2.9.0",
    "uk.gov.hmrc"   %% s"play-hmrc-api$playSuffix" % "8.0.0"
  )

  val test = Seq(
    "uk.gov.hmrc"            %% s"bootstrap-test$playSuffix" % "8.4.0",
    "org.scalatestplus.play" %% "scalatestplus-play"         % "7.0.0"
  ).map(_ % "test, it")
}

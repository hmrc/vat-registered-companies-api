import sbt._

object AppDependencies {

  private val playVersion = "play-30"

  val compile = Seq(
    "uk.gov.hmrc"   %% s"play-hmrc-api-$playVersion" % "8.3.0",
    "org.typelevel" %% "cats-core"                   % "2.13.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% s"bootstrap-test-$playVersion" % "10.6.0"
  ).map(_ % "test, it")
}

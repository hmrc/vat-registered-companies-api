import sbt._

object AppDependencies {

  private val playVersion = "play-30"

  val compile = Seq(
    "uk.gov.hmrc"   %% s"play-hmrc-api-$playVersion" % "8.0.0",
    "org.typelevel" %% "cats-core"                   % "2.12.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% s"bootstrap-test-$playVersion" % "8.6.0"
  ).map(_ % "test, it")
}

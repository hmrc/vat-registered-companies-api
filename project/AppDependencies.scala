import sbt._

object AppDependencies {

  private val testScope = "test,it"

  val compile = Seq(
    "uk.gov.hmrc" %% "customs-api-common" % "1.60.0",
    "uk.gov.hmrc" %% "play-hmrc-api"      % "7.2.0-play-28"
  )

  val test = Seq(
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2" % testScope,
    "uk.gov.hmrc"            %% "customs-api-common"     % "1.60.0" % testScope classifier "tests",
    "uk.gov.hmrc"            %% "bootstrap-test-play-28" % "8.4.0"  % testScope,
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.0"  % testScope
  )
}

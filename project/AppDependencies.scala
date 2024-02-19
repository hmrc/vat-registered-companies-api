import sbt._

object AppDependencies {

  private val playSuffix = "-play-30"
  private val testScope = "test,it"

  val compile = Seq(
    "uk.gov.hmrc" %% "customs-api-common"        % "1.61.0",
    "uk.gov.hmrc" %% s"play-hmrc-api$playSuffix" % "8.0.0"
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "customs-api-common"         % "1.60.0" % testScope classifier "tests",
    "uk.gov.hmrc"            %% s"bootstrap-test$playSuffix" % "8.4.0"  % testScope,
    "org.scalatestplus.play" %% "scalatestplus-play"         % "7.0.0"  % testScope
  )
}

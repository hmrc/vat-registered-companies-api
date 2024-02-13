import play.core.PlayVersion
import sbt._

object AppDependencies {

  private val testScope = "test,it"

  val compile = Seq(
    "uk.gov.hmrc"                                %% "customs-api-common" % "1.60.0",
    "uk.gov.hmrc"                                %% "play-hmrc-api"      % "7.2.0-play-28"
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"              % "3.2.16"            % testScope,
    "org.mockito"            %  "mockito-core"           % "5.4.0"             % testScope,
    "com.typesafe.play"      %% "play-test"              % "2.8.19" % testScope,
    "org.scalatestplus.play" %% "scalatestplus-play"     % "5.1.0"             % testScope,
    "com.github.tomakehurst" %  "wiremock-jre8"          % "2.35.0"            % testScope,
    "org.scalatestplus"      %% "mockito-3-4"            % "3.2.10.0"          % testScope,
    "com.vladsch.flexmark"   %  "flexmark-all"           % "0.62.2"            % testScope,
    "uk.gov.hmrc"            %% "customs-api-common"     % "1.60.0"            % testScope classifier "tests",
    "uk.gov.hmrc"            %% "bootstrap-test-play-28" % "7.16.0"            % testScope
  )
}

import play.core.PlayVersion
import sbt._

object AppDependencies {

  private val testScope = "test,it"

  val compile = Seq(
    "uk.gov.hmrc"                                %% "customs-api-common" % "1.57.0" withSources(),
    "uk.gov.hmrc"                                %% "play-hmrc-api"      % "7.0.0-play-28",
    compilerPlugin("com.github.ghik" %  "silencer-plugin"    % "1.7.11" cross CrossVersion.full),
    "com.github.ghik"                            %  "silencer-lib"       % "1.7.11" % Provided cross CrossVersion.full
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"          % "3.2.14"            % testScope,
    "org.mockito"            %  "mockito-core"       % "4.8.1"             % testScope,
    "com.typesafe.play"      %% "play-test"          % PlayVersion.current % testScope,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0"             % testScope,
    "com.github.tomakehurst" %  "wiremock-jre8"      % "2.34.0"            % testScope,
    "org.scalatestplus"      %% "mockito-3-4"        % "3.2.10.0"          % testScope,
    "com.vladsch.flexmark"   %  "flexmark-all"       % "0.62.2"            % testScope,
    "uk.gov.hmrc"            %% "customs-api-common" % "1.57.0"            % testScope classifier "tests"
    )
}

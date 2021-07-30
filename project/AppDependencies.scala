import play.core.PlayVersion
import sbt._

object AppDependencies {

  private val customsApiCommonVersion = "1.57.0"
  private val scalaTestVersion = "3.2.9"
  private val mockitoVersion = "3.11.2"
  private val scalaTestPlusPlayVersion = "5.0.0"
  private val wireMockVersion = "2.29.0"
  private val testScope = "test,it"

  val compile = Seq("uk.gov.hmrc" %% "customs-api-common" % customsApiCommonVersion withSources(),
    "uk.gov.hmrc" %% "play-hmrc-api" % "6.4.0-play-28",
    compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.5" cross CrossVersion.full),
    "com.github.ghik" % "silencer-lib" % "1.7.5" % Provided cross CrossVersion.full)

  val test = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % testScope,
    "org.mockito" % "mockito-core" % mockitoVersion % testScope,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % testScope,
    "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % testScope,
    "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion % testScope,
    "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % testScope,
    "com.vladsch.flexmark" %  "flexmark-all" % "0.36.8" % testScope,
    "uk.gov.hmrc" %% "customs-api-common" % customsApiCommonVersion % testScope classifier "tests"
    )
}

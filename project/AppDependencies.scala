import play.core.PlayVersion
import sbt._

object AppDependencies {

  private val customsApiCommonVersion = "1.56.0"
  private val scalaTestVersion = "3.0.9"
  private val mockitoVersion = "3.11.2"
  private val scalaTestPlusPlayVersion = "4.0.3"
  private val wireMockVersion = "2.28.0"
  private val testScope = "test,it"

  val compile = Seq("uk.gov.hmrc" %% "customs-api-common" % customsApiCommonVersion withSources(),
    "uk.gov.hmrc" %% "play-hmrc-api" % "6.4.0-play-27",
    compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.5" cross CrossVersion.full),
    "com.github.ghik" % "silencer-lib" % "1.7.5" % Provided cross CrossVersion.full)

  val test = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % testScope,
    "org.mockito" % "mockito-core" % mockitoVersion % testScope,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % testScope,
    "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % testScope,
    "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion % testScope,
    "uk.gov.hmrc" %% "customs-api-common" % customsApiCommonVersion % testScope classifier "tests")
}

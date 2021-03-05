import play.core.PlayVersion
import sbt._

object AppDependencies {

  private val customsApiCommonVersion = "1.53.0"
  private val hmrcTestVersion = "3.9.0-play-26"
  private val scalaTestVersion = "3.0.8"
  private val mockitoVersion = "3.0.0"
  private val scalaTestPlusPlayVersion = "3.1.2"
  private val wireMockVersion = "2.23.2"
  private val testScope = "test,it"

  val customsApiCommon = "uk.gov.hmrc" %% "customs-api-common" % customsApiCommonVersion withSources()
  val apiCommon = "uk.gov.hmrc" %% "play-hmrc-api" % "4.1.0-play-26"
  val hmrcTest = "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % testScope
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % testScope
  val mockito = "org.mockito" % "mockito-core" % mockitoVersion % testScope
  val playTest = "com.typesafe.play" %% "play-test" % PlayVersion.current % testScope
  val scalaTestPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % testScope
  val wireMock = "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion % testScope
  val customsApiCommonTests = "uk.gov.hmrc" %% "customs-api-common" % customsApiCommonVersion % testScope classifier "tests"
}

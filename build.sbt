/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import AppDependencies.{compile => compileDependencies, test => testDependencies}
import sbt.Keys._
import sbt.Tests.{Group, SubProcess}
import sbt.{Resolver, _}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, scalaSettings, targetJvm}
import uk.gov.hmrc.PublishingSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.gitstamp.GitStampPlugin._
import sbt.dsl.LinterLevel.Ignore

name := "vat-registered-companies-api"
PlayKeys.playDefaultPort := 8733

targetJvm := "jvm-1.8"
scalaVersion := "2.12.13"

Seq( gitStampSettings: _* )

lazy val ComponentTest = config("component") extend Test
lazy val CdsIntegrationTest = config("it") extend Test

val testConfig = Seq(ComponentTest, CdsIntegrationTest, Test)

def forkedJvmPerTestConfig(tests: Seq[TestDefinition], packages: String*): Seq[Group] =
  tests.groupBy(_.name.takeWhile(_ != '.')).filter(packageAndTests => packages contains packageAndTests._1) map {
    case (packg, theTests) =>
      Group(packg, theTests, SubProcess(ForkOptions()))
  } toSeq

lazy val testAll = TaskKey[Unit]("test-all")
lazy val allTest = Seq(testAll := (ComponentTest / test)
  .dependsOn((CdsIntegrationTest / test).dependsOn(Test / test)))


lazy val microservice = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
  .enablePlugins(SbtDistributablesPlugin)
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
  .configs(testConfig: _*)
  .settings(
    commonSettings,
    unitTestSettings,
    integrationTestSettings,
    componentTestSettings,
    playPublishingSettings,
    allTest,
    scoverageSettings
  )
  .settings(scalacOptions ++= Seq("-P:silencer:pathFilters=routes", "-P:silencer:globalFilters=Unused import"))
  .settings(majorVersion := 0)

def onPackageName(rootPackage: String): String => Boolean = {
  testName => testName startsWith rootPackage
}

lazy val unitTestSettings =
  inConfig(Test)(Defaults.testTasks) ++
    Seq(
      Test / unmanagedSourceDirectories := Seq((Test / baseDirectory).value / "test"),
      addTestReportOption(Test, "test-reports")
    )

lazy val integrationTestSettings =
  inConfig(CdsIntegrationTest)(Defaults.testTasks) ++
    Seq(
      CdsIntegrationTest / testOptions := Seq(Tests.Filters(Seq(onPackageName("integration"), onPackageName("component")))),
      CdsIntegrationTest / fork.withRank(KeyRanks.Invisible)  := false,
      CdsIntegrationTest / parallelExecution := false,
      addTestReportOption(CdsIntegrationTest, "int-test-reports"),
      CdsIntegrationTest / testGrouping := forkedJvmPerTestConfig((Test / definedTests).value, "integration", "component")
    )

lazy val componentTestSettings =
  inConfig(ComponentTest)(Defaults.testTasks) ++
    Seq(
      ComponentTest / testOptions := Seq(Tests.Filter(onPackageName("component"))),
      ComponentTest / fork := false,
      ComponentTest / parallelExecution := false,
      addTestReportOption(ComponentTest, "component-reports")
    )


lazy val commonSettings: Seq[Setting[_]] =
  scalaSettings ++
    publishingSettings ++
    defaultSettings() ++
    gitStampSettings

lazy val playPublishingSettings: Seq[sbt.Setting[_]] = Seq(credentials += SbtCredentials) ++
  Seq(credentials += SbtCredentials) ++
  publishAllArtefacts

lazy val scoverageSettings: Seq[Setting[_]] = Seq(
  coverageExcludedPackages := "<empty>;.*(Reverse|Routes).*;com.kenshoo.play.metrics.*;.*definition.*;prod.*;testOnlyDoNotUseInAppConf.*;app.*;uk.gov.hmrc.BuildInfo;.*connector.*;.*config.*",
  coverageMinimumStmtTotal := 96,
  coverageFailOnMinimum := true,
  coverageHighlighting := true,
  Test / parallelExecution := false
)

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

Compile / unmanagedResourceDirectories += baseDirectory.value / "public"

libraryDependencies ++= compileDependencies ++ testDependencies


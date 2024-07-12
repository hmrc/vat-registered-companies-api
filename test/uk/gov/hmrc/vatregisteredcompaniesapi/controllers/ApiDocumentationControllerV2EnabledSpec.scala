/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.vatregisteredcompaniesapi.controllers

import org.apache.pekko.stream.Materializer
import org.scalatest.concurrent.Eventually
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.vatregisteredcompaniesapi.util.UnitSpec

import java.io.FileNotFoundException
import scala.concurrent.Future

class ApiDocumentationControllerV2EnabledSpec extends UnitSpec
  with BeforeAndAfterEach with BeforeAndAfterAll with Eventually with MockitoSugar with GuiceOneAppPerSuite {

  private implicit lazy val materializer: Materializer = app.materializer

  private val definitionJsonContentV2Enabled = getResourceFileContent("/public/api/definitionV2Enabled.json")
  private val applicationRamlContent = getResourceFileContent("/public/api/conf/1.0/example.raml")

  override implicit lazy val app: Application = GuiceApplicationBuilder(
    modules = Seq()).
    configure(
      Map(
        "play.http.router" -> "dynamicDefinition.Routes",
        "application.logger.name" -> "vat-registered-companies-api",
        "appName" -> "vat-registered-companies-api",
        "appUrl" -> "https://vat-registered-companies-api.gov.uk",
        "auditing.enabled" -> false,
        "auditing.traceRequests" -> false,
        "api.v2.enabled" -> true
      )
    ).build()

  "DocumentationController" should {
    "serve definitionV2Enabled.json" in assertRoutedContent("/api/definition", definitionJsonContentV2Enabled)

    "serve example.raml" in assertRoutedContent("/api/conf/1.0/example.raml", applicationRamlContent)
  }

  private def assertRoutedContent(uri: String, expectedContent: String) = {

    val result: Option[Future[Result]] = route(app, FakeRequest("GET", uri))

    result shouldBe Symbol("defined")
    val resultFuture: Future[Result] = result.get

    status(resultFuture) shouldBe OK
    contentAsString(resultFuture) shouldBe expectedContent
  }

  private def getResourceFileContent(resourceFile: String): String = {
    val is = Option(getClass.getResourceAsStream(resourceFile)).getOrElse(
      throw new FileNotFoundException(s"Resource file not found: $resourceFile"))
    scala.io.Source.fromInputStream(is).mkString
  }
}

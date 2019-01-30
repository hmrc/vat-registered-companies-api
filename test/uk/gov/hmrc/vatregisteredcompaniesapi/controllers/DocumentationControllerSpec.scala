/*
 * Copyright 2019 HM Revenue & Customs
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

package unit.controllers

import java.io.FileNotFoundException

import akka.stream.Materializer
import org.scalatest.mockito.MockitoSugar
import play.api.http.Status._
import play.api.test.FakeRequest
import uk.gov.hmrc.apinotificationpull.controllers.DocumentationController
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

class DocumentationControllerSpec extends UnitSpec with MockitoSugar with WithFakeApplication {

  private implicit val materializer: Materializer = fakeApplication.materializer

  private lazy val controller = fakeApplication.injector.instanceOf[DocumentationController]
  private lazy val applicationRamlContent = getResourceFileContent("/public/api/conf/1.0/application.raml")


  "DocumentationController" should {
    lazy val result = getDocumentation(controller)

    "return OK status" in {
      status(result) shouldBe OK
    }

    "return application.raml in the body" in {
      bodyOf(result) shouldBe applicationRamlContent
    }
  }

  private def getDocumentation(controller: DocumentationController) = {
    await(controller.conf("1.0","application.raml").apply(FakeRequest("GET", "/api/conf/1.0/application.raml")))
  }

  private def getResourceFileContent(resourceFile: String): String = {
    val is = Option(getClass.getResourceAsStream(resourceFile)).getOrElse(
      throw new FileNotFoundException(s"Resource file not found: $resourceFile"))
    scala.io.Source.fromInputStream(is).mkString
  }
}

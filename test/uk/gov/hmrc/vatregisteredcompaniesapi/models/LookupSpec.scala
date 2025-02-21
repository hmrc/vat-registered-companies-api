/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.vatregisteredcompaniesapi.models

import org.scalatest.funsuite.AnyFunSuite
import play.api.libs.json.{JsSuccess, Json}

class LookupSpec extends AnyFunSuite {

  test("should serialize Lookup Class without requester to correct JSON ") {
    val error = Lookup("553557881", false, None)
    val json = Json.toJson(error)

    val expectedJson = Json.parse("""{"target":"553557881","withConsultationNumber":false}""")
    assert(json == expectedJson)
  }

  test("should deserialize Lookup Class without requester to correct JSON ") {
    val json = Json.parse("""{"target":"553557881","withConsultationNumber":false}""")
    val result = json.validate[Lookup]

    assert(result == JsSuccess(Lookup("553557881", false, None)))
  }

  test("should serialize Lookup Class with requester to correct JSON ") {
    val error = Lookup("553557881", false, Some("462794985"))
    val json = Json.toJson(error)

    val expectedJson = Json.parse("""{"target":"553557881","withConsultationNumber":false,"requester":"462794985"}""")
    assert(json == expectedJson)
  }

  test("should deserialize Lookup Class with requester to correct JSON ") {
    val json = Json.parse("""{"target":"553557881","withConsultationNumber":false,"requester":"462794985"}""")
    val result = json.validate[Lookup]

    assert(result == JsSuccess(Lookup("553557881", false, Some("462794985"))))
  }

}


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

class AddressSpec extends AnyFunSuite {

  private val expectedAddress = Address(
    line1 = "2 example road",
    line2 = None,
    line3 = None,
    line4 = None,
    line5 = None,
    postcode = None,
    countryCode = "GB"
  )

  private val expectedJson = Json.parse(
    """
      |{
      | "line1": "2 example road",
      | "countryCode": "GB"
      |}
      |
     """.stripMargin
  )

  test("should serialise Address to correct JSON"){
    val json = Json.toJson(expectedAddress)
    assert(json == expectedJson)
  }

  test("should deseralise Address Json to correct class"){
    val result = expectedJson.validate[Address]
    assert(result == JsSuccess(expectedAddress))
  }




}


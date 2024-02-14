/*
 * Copyright 2024 HM Revenue & Customs
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

import play.api.libs.json.{Json, OFormat}

case class LookupRequestError(code: String, message: String)

object LookupRequestError {

  val NOT_FOUND = "NOT_FOUND"
  val INVALID_REQUEST = "INVALID_REQUEST"
  val targetNotFoundMsg = "targetVrn does not match a registered company"
  val requesterNotFoundMsg = "requesterVrn does not match a registered company"
  val invalidTargetVrnMsg = "Invalid targetVrn - Vrn parameters should be 9 or 12 digits"
  val invalidRequesterVrnMsg = "Invalid requesterVrn - Vrn parameters should be 9 or 12 digits"
  val invalidTargetAndRequesterVrnMsg = "Invalid targetVrn and requesterVrn - Vrn parameters should be 9 or 12 digits"

  implicit val format: OFormat[LookupRequestError] = Json.format[LookupRequestError]
}



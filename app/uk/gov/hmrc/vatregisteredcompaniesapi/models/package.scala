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

package uk.gov.hmrc.vatregisteredcompaniesapi

import java.time._

import scala.util.Random

package object models {

  type CompanyName = String
  type VatNumber = String
  type ConsultationNumber = String
  type ProcessingDate = OffsetDateTime

  object ConsultationNumber {
    def generate: ConsultationNumber =
      new Random().alphanumeric.filter(x =>
        x.toLower >= 'a' && x.toLower <= 'z'
      ).take(9).toList.mkString
  }

  /* removes "GB" from the VatNumber, used by requests to the BE only */
  implicit class RichVatNumber(val self: VatNumber) {
    def clean: VatNumber = self.replace("GB", "")
  }

}

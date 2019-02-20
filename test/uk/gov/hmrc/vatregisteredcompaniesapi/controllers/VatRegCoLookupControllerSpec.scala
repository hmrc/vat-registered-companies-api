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

package uk.gov.hmrc.vatregisteredcompaniesapi.controllers

import java.time.LocalDateTime

import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, OptionValues, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, status, _}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.vatregisteredcompaniesapi.connectors.VatRegisteredCompaniesConnector
import uk.gov.hmrc.vatregisteredcompaniesapi.models._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class VatRegCoLookupControllerSpec extends WordSpec
  with Matchers
  with GuiceOneAppPerSuite
  with MockitoSugar
  with OptionValues
{

  val mockVatRegisteredCompaniesConnector: VatRegisteredCompaniesConnector = mock[VatRegisteredCompaniesConnector]
  val mockAuditConnector: AuditConnector = mock[AuditConnector]
  val controller = new VatRegCoLookupController(mockVatRegisteredCompaniesConnector, mockAuditConnector)
  val testVatNo: VatNumber = "123456789"
  val testConsultationNumber: ConsultationNumber = ConsultationNumber.generate
  val testProcessingDate: ProcessingDate = LocalDateTime.now
  val t = testProcessingDate.toString
  val fakeRequest = FakeRequest("GET", "/lookup/123456789")
  implicit val headerCarrier: HeaderCarrier = HeaderCarrier()

  val knownCo =
    VatRegisteredCompany(
      name = "ACME trading",
      vatNumber = testVatNo,
      address = Address("line 1", None, None, None, None, None, countryCode = "GB")
    )

  "GET of known VAT number " should {
    "return 200 " in {
      when(mockVatRegisteredCompaniesConnector.lookup(any())(any(),any()))
        .thenReturn(Future(Some(LookupResponse(Some(knownCo)))))
      val result = controller.lookup(testVatNo.clean).apply(fakeRequest)
      status(result) shouldBe Status.OK
      Json.fromJson[LookupResponse](contentAsJson(result)).map { lr =>
        lr.target shouldBe Some(knownCo)
      }
    }
  }

  "GET of known VAT number and requester " should {
    "return 200 " in {
      when(mockVatRegisteredCompaniesConnector.lookup(any())(any(),any()))
        .thenReturn(Future(Some(LookupResponse(Some(knownCo), Some(testVatNo), Some(testConsultationNumber)))))
      val result = controller.lookupVerified(testVatNo, testVatNo).apply(fakeRequest)
      status(result) shouldBe Status.OK
      Json.fromJson[LookupResponse](contentAsJson(result)).map { lr =>
        lr.target shouldBe Some(knownCo)
        lr.requester shouldBe Some(testVatNo)
        lr.consultationNumber shouldBe Some(testConsultationNumber)
      }
    }
  }

  "GET of unknown VAT number " should {
    "return 404 " in {
      when(mockVatRegisteredCompaniesConnector.lookup(any())(any(),any()))
        .thenReturn(Future(Some(LookupResponse(None))))
      val result = controller.lookup(testVatNo).apply(fakeRequest)
      status(result) shouldBe Status.NOT_FOUND
      Json.fromJson[LookupRequestError](contentAsJson(result)).map { lre =>
        lre.code shouldBe LookupRequestError.NOT_FOUND
        lre.message shouldBe LookupRequestError.targetNotFoundMsg
      }
    }
  }

  "GET of any VAT number with an unknown requester" should {
    "return 403 " in {
      when(mockVatRegisteredCompaniesConnector.lookup(any())(any(),any()))
        .thenReturn(Future(Some(LookupResponse(Some(knownCo), None, None))))
      val result = controller.lookupVerified(testVatNo, testVatNo).apply(fakeRequest)
      status(result) shouldBe Status.FORBIDDEN
      Json.fromJson[LookupRequestError](contentAsJson(result)).map { lre =>
        lre.code shouldBe LookupRequestError.NOT_FOUND
        lre.message shouldBe LookupRequestError.requesterNotFoundMsg
      }
    }
  }

  "GET of an invalid VAT number " should {
    "return 403 " in {
      val result = controller.lookup("foobar").apply(fakeRequest)
      status(result) shouldBe Status.FORBIDDEN
      Json.fromJson[LookupRequestError](contentAsJson(result)).map { lre =>
        lre.code shouldBe LookupRequestError.INVALID_REQUEST
        lre.message shouldBe LookupRequestError.invalidTargetVrnMsg
      }
    }
  }

  "GET of an invalid VAT number and invalid requester VAT number" should {
    "return 403 " in {
      val result = controller.lookupVerified("foobar", "barfoo").apply(fakeRequest)
      status(result) shouldBe Status.FORBIDDEN
      Json.fromJson[LookupRequestError](contentAsJson(result)).map { lre =>
        lre.code shouldBe LookupRequestError.INVALID_REQUEST
        lre.message shouldBe LookupRequestError.invalidTargetAndRequesterVrnMsg
      }
    }
  }

  "GET of an valid VAT number and invalid requester VAT number" should {
    "return 403 " in {
      val result = controller.lookupVerified(testVatNo, "barfoo").apply(fakeRequest)
      status(result) shouldBe Status.FORBIDDEN
      Json.fromJson[LookupRequestError](contentAsJson(result)).map { lre =>
        lre.code shouldBe LookupRequestError.INVALID_REQUEST
        lre.message shouldBe LookupRequestError.invalidRequesterVrnMsg
      }
    }
  }

  "GET of VAT number " should {
    "return a 500 if there is an underlying problem" in {
      when(mockVatRegisteredCompaniesConnector.lookup(any())(any(),any())).thenReturn(Future.failed(new Throwable))
      val result = controller.lookup(testVatNo).apply(fakeRequest)
      status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      Json.fromJson[LookupResponse](contentAsJson(result)).map { lr =>
        lr.target shouldBe Some(knownCo)
      }
    }
  }

}
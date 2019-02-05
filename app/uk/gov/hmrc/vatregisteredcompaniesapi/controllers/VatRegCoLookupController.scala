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

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import uk.gov.hmrc.vatregisteredcompaniesapi.connectors.VatRegisteredCompaniesConnector
import uk.gov.hmrc.vatregisteredcompaniesapi.models.{Lookup, LookupRequestError, LookupResponse, VatNumber}
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VatRegCoLookupController @Inject()(
  vatRegisteredCompaniesConnector: VatRegisteredCompaniesConnector,
  auditConnector: AuditConnector
)(implicit executionContext: ExecutionContext) extends BaseController {

  def lookupVerified(target: VatNumber, requester: VatNumber): Action[AnyContent] =
    Action.async { implicit request =>
      lookup(Lookup(target, true, requester.some))
    }

  def lookup(target: VatNumber): Action[AnyContent] =
    Action.async { implicit request =>
      lookup(Lookup(target))
    }

  private val vatNoRegex: String = "^[0-9]{9}|[0-9]{12}$"

  private def isValid(lookup: Lookup): Boolean =
    lookup.target.matches(vatNoRegex) && lookup.requester.forall(_.matches(vatNoRegex))

  private def handleInvalidRequest(lookup: Lookup): LookupRequestError = {
    (lookup.target.matches(vatNoRegex), lookup.requester.forall(_.matches(vatNoRegex))) match {
      case (false, false) =>
          LookupRequestError(
            "403",
            "Invalid targetVrn and requesterVrn - Vrn parameters should be 9 or 12 digits")
      case (_, false) =>
          LookupRequestError(
            "403",
            "Invalid requesterVrn - Vrn parameters should be 9 or 12 digits")
      case (false, _) =>
          LookupRequestError(
            "403",
            "Invalid targetVrn - Vrn parameters should be 9 or 12 digits")
    }
  }

  private def lookup(lookup: Lookup)(implicit headerCarrier: HeaderCarrier): Future[Result] = {

    if (!isValid(lookup)) {
      Future(Forbidden(Json.toJson(handleInvalidRequest(lookup))))
    } else {
      vatRegisteredCompaniesConnector.lookup(lookup).map {
        case Some(LookupResponse(Some(_), _, None, _)) if lookup.requester.nonEmpty =>
          Forbidden(Json.toJson(
            LookupRequestError("404", "requesterVrn does not match a registered company")
          ))
        case Some(LookupResponse(None, _, _, _)) =>
          NotFound(Json.toJson(
            LookupRequestError("404", "targetVrn does not match a registered company")
          ))
        case Some(company) =>
          Ok(Json.toJson(
            company
          ))
      }.recover {
        case _ => InternalServerError(Json.toJson(LookupRequestError("500", "Unknown error")))
      }
    }
  }

}

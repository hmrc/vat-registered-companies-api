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

import cats.implicits._
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.customs.api.common.logging.CdsLogger
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.controller.BackendController
import uk.gov.hmrc.vatregisteredcompaniesapi.connectors.VatRegisteredCompaniesConnector
import uk.gov.hmrc.vatregisteredcompaniesapi.models.{Lookup, LookupRequestError, LookupResponse, VatNumber}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VatRegCoLookupController @Inject()(
  vatRegisteredCompaniesConnector: VatRegisteredCompaniesConnector,
  auditConnector: AuditConnector,
  cc: ControllerComponents,
  logger: CdsLogger
)(implicit executionContext: ExecutionContext) extends BackendController(cc) {

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
            LookupRequestError.INVALID_REQUEST,
            LookupRequestError.invalidTargetAndRequesterVrnMsg)
      case (_, false) =>
          LookupRequestError(
            LookupRequestError.INVALID_REQUEST,
            LookupRequestError.invalidRequesterVrnMsg)
      case (false, _) =>
          LookupRequestError(
            LookupRequestError.INVALID_REQUEST,
            LookupRequestError.invalidTargetVrnMsg)
    }
  }

  private def lookup(lookup: Lookup)(implicit headerCarrier: HeaderCarrier): Future[Result] = {
    if (!isValid(lookup)) {
      Future(BadRequest(Json.toJson(handleInvalidRequest(lookup))))
    } else {
      vatRegisteredCompaniesConnector.lookup(lookup).map {
        case Some(LookupResponse(Some(_), _, None, _)) if lookup.requester.nonEmpty =>
          Forbidden(Json.toJson(
            LookupRequestError(LookupRequestError.INVALID_REQUEST, LookupRequestError.requesterNotFoundMsg)
          ))
        case Some(LookupResponse(None, _, _, _)) =>
          NotFound(Json.toJson(
            LookupRequestError(LookupRequestError.NOT_FOUND, LookupRequestError.targetNotFoundMsg)
          ))
        case Some(company) =>
          Ok(Json.toJson(company))
      }.recover {
        case e =>
          logger
            .error(
              e.getMessage,
              e.fillInStackTrace()
            )
          InternalServerError(Json.toJson(LookupRequestError("INTERNAL_SERVER_ERROR", "Unknown error")))
      }
    }
  }

}

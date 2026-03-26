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

package uk.gov.hmrc.vatregisteredcompaniesapi.controllers

import cats.implicits.*
import play.api.Logging

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request, Result}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.vatregisteredcompaniesapi.connectors.VatRegisteredCompaniesConnector
import uk.gov.hmrc.vatregisteredcompaniesapi.logging.VrcLogger
import uk.gov.hmrc.vatregisteredcompaniesapi.models.{Lookup, LookupRequestError, LookupResponse, VatNumber}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VatRegCoLookupController @Inject()(
  vatRegisteredCompaniesConnector: VatRegisteredCompaniesConnector,
  cc: ControllerComponents,
  logger: VrcLogger
)(using executionContext: ExecutionContext) extends BackendController(cc) with Logging {

  def lookupVerified(target: VatNumber, requester: VatNumber): Action[AnyContent] =
    Action.async { implicit request =>
      val startTime = System.currentTimeMillis()
      
      logger.info(s"VatRegCoLookup request target=$target requester=${hc.requestId.map(_.value).getOrElse("-")}")

      logVersionOfApiCalled
      handleRequest(Lookup(target, true, requester.some)).map {result =>
        logger.info(s"VatRegCoLookup request target=$target status=${result.header.status} duration=${System.currentTimeMillis() - startTime}")
        result
      }
    }

  def lookup(target: VatNumber): Action[AnyContent] =
    Action.async { implicit request =>
      val startTime = System.currentTimeMillis()

      logger.info(s"VatRegCoLookup request target=$target request=${hc.requestId.map(_.value).getOrElse("-")}")

      logVersionOfApiCalled
      handleRequest(Lookup(target)).map(result =>
        logger.info(s"VatRegCoLookup request target=$target status=${result.header.status} duration=${System.currentTimeMillis() - startTime}")
        result
      )
    }

  private val vatNoRegex: String = "^[0-9]{9}|[0-9]{12}$"


  private def handleRequest(lookup: Lookup)(implicit headerCarrier: HeaderCarrier): Future[Result] = {
    (lookup.target.matches(vatNoRegex), lookup.requester.forall(_.matches(vatNoRegex))) match {
      case (false, false) =>
        handleBadRequest(LookupRequestError.invalidTargetAndRequesterVrnMsg)
      case (_, false) =>
        handleBadRequest(LookupRequestError.invalidRequesterVrnMsg)
      case (false, _) =>
        handleBadRequest(LookupRequestError.invalidTargetVrnMsg)
      case (true, true) =>
        vatRegLookup(lookup)
    }
  }


  private def handleBadRequest(msg: String) = {
    Future(BadRequest(Json.toJson(
      LookupRequestError(LookupRequestError.INVALID_REQUEST, msg))
      ))
  }



  private def vatRegLookup(lookup: Lookup)(implicit headerCarrier: HeaderCarrier): Future[Result] = {
    vatRegisteredCompaniesConnector.lookup(lookup).map {

      case Some(LookupResponse(Some(_), _, None, _))
      if lookup.requester.nonEmpty
      =>
      Forbidden(Json.toJson(
        LookupRequestError(LookupRequestError.INVALID_REQUEST, LookupRequestError.requesterNotFoundMsg)
          ))

      case Some(LookupResponse(None, _, _, _)) | None =>
        NotFound(Json.toJson(
          LookupRequestError(LookupRequestError.NOT_FOUND, LookupRequestError.targetNotFoundMsg)))

      case Some(company) =>
        Ok(Json.toJson(company))

    }.recover {
      case e => 
        logger.error(e.getMessage, e.fillInStackTrace())
        InternalServerError(Json.toJson(LookupRequestError("INTERNAL_SERVER_ERROR", "Unknown error")))
    }
  }

  private def logVersionOfApiCalled(implicit request: Request[?]): Unit = {
    request.headers.get(ACCEPT) match {
      case Some(header) if header.endsWith("2.0+json") =>
        logger.info("Version 2.0 of the api has been called")
      case _ => logger.info("Version 1.0 of the api has been called")
    }
  }

}

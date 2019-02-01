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
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import uk.gov.hmrc.vatregisteredcompaniesapi.connectors.VatRegisteredCompaniesConnector
import uk.gov.hmrc.vatregisteredcompaniesapi.models.{Lookup, LookupResponse, VatNumber}
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VatRegCoLookupController @Inject()(
  vatRegisteredCompaniesConnector: VatRegisteredCompaniesConnector,
  auditConnector: AuditConnector
)(implicit executionContext: ExecutionContext) extends BaseController {

  private def lookup(lookup: Lookup)(implicit headerCarrier: HeaderCarrier) =
    vatRegisteredCompaniesConnector.lookup(lookup).map {x =>
      Ok(Json.toJson(x.getOrElse(LookupResponse(None))))
    } // TODO add failure outcomes

  def lookupVerified(target: VatNumber, requester: VatNumber): Action[AnyContent] =
    Action.async { implicit request =>
      lookup(Lookup(target, true, requester.some))
    }

  def lookup(target: VatNumber): Action[AnyContent] =
    Action.async { implicit request =>
      lookup(Lookup(target))
    }

}

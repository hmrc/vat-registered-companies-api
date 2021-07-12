/*
 * Copyright 2021 HM Revenue & Customs
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

package uk.gov.hmrc.vatregisteredcompaniesapi.connectors

import javax.inject.Inject
import play.api.{Configuration, Environment}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.vatregisteredcompaniesapi.models._
import uk.gov.hmrc.http.HttpReads.Implicits._
import scala.concurrent.{ExecutionContext, Future}

class VatRegisteredCompaniesConnector @Inject()(
  http: HttpClient,
  environment: Environment,
  configuration: Configuration,
  servicesConfig: ServicesConfig
) {

  lazy val url: String = s"${servicesConfig.baseUrl("vat-registered-companies")}/vat-registered-companies"

  def lookup(lookup: Lookup)
    (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[LookupResponse]] = lookup.requester match {
    case Some(requester) =>
      http.GET[LookupResponse](url = s"$url/lookup/${lookup.target.clean}/$requester").map(Some(_))
    case a =>
      http.GET[LookupResponse](url = s"$url/lookup/${lookup.target.clean}").map(Some(_))
  }

}

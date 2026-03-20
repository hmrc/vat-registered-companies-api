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

package uk.gov.hmrc.vatregisteredcompaniesapi.connectors


import javax.inject.Inject
import play.api.{Configuration, Environment, Logger, Logging}
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.vatregisteredcompaniesapi.models.*
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2

import java.net.{URL, URLEncoder}
import scala.util.control.NonFatal
import scala.concurrent.{ExecutionContext, Future}

class VatRegisteredCompaniesConnector @Inject()(
  http: HttpClientV2,
  servicesConfig: ServicesConfig
) extends Logging {

  private def requestId(hc: HeaderCarrier): String =
    hc.requestId.map(_.value).getOrElse("-")

  private def sessionId(hc: HeaderCarrier): String =
    hc.sessionId.map(_.value).getOrElse("-")

  private def outBoundHeaderCarrier(hc: HeaderCarrier): HeaderCarrier =
    HeaderCarrier(
      requestId = hc.requestId,
      sessionId = hc.sessionId
    )

  private def vatRegContext(path: URL, lookup: Lookup, hc: HeaderCarrier) = {
    Seq(
      Some(s"path=$path"),
      Some(s"lookup=${lookup.target.clean}"),
      Some(s"requestId=${requestId(hc)}"),
      Some(s"sessionId=${sessionId(hc)}")
    ).flatten.mkString(" ")
  }


  lazy val url: String = s"${servicesConfig.baseUrl("vat-registered-companies")}/vat-registered-companies"

  def lookup(lookup: Lookup)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[LookupResponse]] = {
    val startTime = System.currentTimeMillis()
    val vatHc = outBoundHeaderCarrier(hc)

    lookup.requester match {
      case Some(requester) =>
        val completeUrl = url"$url/lookup/${lookup.target.clean}/$requester"
        logger.info(s"VatRegisteredCompanies lookup request ${vatRegContext(completeUrl, lookup, hc)}")

        http.get(completeUrl)(using vatHc)
          .execute[LookupResponse]
          .map { response =>
            logger.info(s"VatRegisteredCompanies lookup response ${vatRegContext(completeUrl, lookup, hc)} status=200 durationMs=${System.currentTimeMillis() - startTime}")
            Some(response)
          }
          .recover {
            case NonFatal(e) =>
              logger.error(s"VatRegisteredCompanies lookup failure ${vatRegContext(completeUrl, lookup, hc)} durationMs=${System.currentTimeMillis() - startTime} error=${e.getMessage}", e)
              None
          }

      case _ =>
        val completeUrl = url"$url/lookup/${lookup.target.clean}"
        logger.info(s"VatRegisteredCompanies lookup request ${vatRegContext(completeUrl, lookup, hc)}")

        http.get(completeUrl)(using vatHc)
          .execute[LookupResponse]
          .map { response =>
            logger.info(s"VatRegisteredCompanies lookup response ${vatRegContext(completeUrl, lookup, hc)} status=200 durationMs=${System.currentTimeMillis() - startTime}")
            Some(response)
          }
          .recover {
            case NonFatal(e) =>
              logger.error(s"VatRegisteredCompanies lookup failure ${vatRegContext(completeUrl, lookup, hc)} durationMs=${System.currentTimeMillis() - startTime} error=${e.getMessage}", e)
              None
          }
    }
  }



}

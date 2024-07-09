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

import controllers.Assets

import javax.inject.{Inject, Singleton}
import play.api.http.{ContentTypes, MimeTypes}
import play.api.mvc.{Action, AnyContent, Codec, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.vatregisteredcompaniesapi.config.AppContext
import views.txt

import scala.concurrent.Future

@Singleton
class ApiDocumentationController @Inject()
(
  assets: Assets,
  cc: ControllerComponents,
  appContext: AppContext
) extends BackendController (cc) {

  def conf(version: String, file: String): Action[AnyContent] = {
    assets.at(s"/public/api/conf/$version", file)
  }
  def definition: Action[AnyContent] = Action.async {
    Future.successful(
      Ok(
        txt.definition(
          appContext.apiContext,
          appContext.whiteListedAppIds.getOrElse(Seq.empty[String]),
          appContext.v2Enabled
        )
      ).as(ContentTypes.withCharset(MimeTypes.JSON)(Codec.utf_8)))
  }
}

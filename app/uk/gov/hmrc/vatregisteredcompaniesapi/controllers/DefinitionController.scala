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
import play.api.http.{ContentTypes, MimeTypes}
import play.api.mvc.{Action, AnyContent, Codec}
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import uk.gov.hmrc.vatregisteredcompaniesapi.config.AppContext
import views.txt

import scala.collection.JavaConverters

@Singleton
class DefinitionController @Inject()(appContext: AppContext) extends BaseController {

  def get(): Action[AnyContent] = Action {
    Ok(txt.definition(appContext.apiContext, whitelist)).as(ContentTypes.withCharset(MimeTypes.JSON)(Codec.utf_8))
  }

  val whitelist: Seq[String] = convertListToSeq(appContext.whiteListedAppIds)

  private def convertListToSeq(inputList: java.util.List[String]): Seq[String] = {
    JavaConverters.asScalaIteratorConverter(inputList.iterator()).asScala.toSeq
  }
}

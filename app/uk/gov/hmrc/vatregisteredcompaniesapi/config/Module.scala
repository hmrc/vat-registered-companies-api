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

package uk.gov.hmrc.vatregisteredcompaniesapi.config

import com.google.inject.AbstractModule
import uk.gov.hmrc.api.connector.{ApiServiceLocatorConnector, ServiceLocatorConnector}
import uk.gov.hmrc.api.controllers.DocumentationController
import uk.gov.hmrc.http.CorePost
import uk.gov.hmrc.play.bootstrap.http.DefaultHttpClient

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[CorePost]).to(classOf[DefaultHttpClient])
    bind(classOf[ServiceLocatorConnector]).to(classOf[ApiServiceLocatorConnector])
    bind(classOf[DocumentationController]).toInstance(DocumentationController)
    bind(classOf[ApplicationRegistration]).asEagerSingleton()
  }

}

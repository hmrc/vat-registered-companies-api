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

import javax.inject.{Inject, Singleton}
import play.api.Mode.Mode
import play.api.{Application, Configuration, Environment, Logger}
import uk.gov.hmrc.api.config.ServiceLocatorConfig
import uk.gov.hmrc.api.connector.ServiceLocatorConnector
import uk.gov.hmrc.http.HeaderCarrier

@Singleton
class ApplicationRegistration @Inject()(environment: Environment, app: Application, serviceLocatorConnector: ServiceLocatorConnector)
  extends ServiceLocatorConfig {

  override val mode: Mode = environment.mode
  override val runModeConfiguration: Configuration = app.configuration
  val registrationEnabled: Boolean = getConfBool("service-locator.enabled", defBool = true)

  Logger.info(s"Registration enabled: $registrationEnabled")
  if (registrationEnabled) serviceLocatorConnector.register(HeaderCarrier())

}

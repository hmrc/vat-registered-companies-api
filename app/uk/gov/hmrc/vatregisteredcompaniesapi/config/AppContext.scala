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

package uk.gov.hmrc.vatregisteredcompaniesapi.config

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class AppContext @Inject()(configuration: Configuration) {
  private val apiScopeConfigKey = "api.definition.scope"
  private val apiContextConfigKey = "api.context"
  private val v2EnabledConfigKey = "api.v2.enabled"
  private val whitelistAppConfigKey = "api.access.white-list.applicationIds"
  val apiScopeKey: String = configuration.underlying.getString(apiScopeConfigKey)
  val apiContext: String = configuration.underlying.getString(apiContextConfigKey)
  val whiteListedAppIds: Option[Seq[String]] = configuration.getOptional[Seq[String]](whitelistAppConfigKey)
  val v2Enabled: Boolean = configuration.underlying.getBoolean(v2EnabledConfigKey)

}

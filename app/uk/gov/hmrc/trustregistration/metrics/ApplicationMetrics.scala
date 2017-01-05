/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.trustregistration.metrics

import com.codahale.metrics.Timer
import com.codahale.metrics.Timer.Context
import com.kenshoo.play.metrics.Metrics
import play.api.Play

trait ApplicationMetrics {

  val metrics = Play.current.injector.instanceOf[Metrics]

  def incrementUnauthorisedRequest(api: String): Unit
  def incrementAuthorisedRequest(api: String): Unit
  def incrementApiSuccessResponse(api: String): Unit
  def incrementBadRequestResponse(api: String): Unit
  def incrementNotFoundResponse(api: String) : Unit
  def incrementInternalServerErrorResponse(api: String) : Unit
  def startDesConnectorTimer(api: String): Timer.Context

}

object ApplicationMetrics extends ApplicationMetrics {

  override def incrementUnauthorisedRequest(api: String): Unit = metrics.defaultRegistry.counter(s"unauthorised-$api").inc()
  override def incrementAuthorisedRequest(api: String): Unit = metrics.defaultRegistry.counter(s"authorised-$api").inc()
  override def incrementApiSuccessResponse(api: String): Unit = metrics.defaultRegistry.counter(s"success-$api").inc()
  override def incrementBadRequestResponse(api: String): Unit = metrics.defaultRegistry.counter(s"bad-request-$api").inc()
  override def incrementNotFoundResponse(api: String): Unit = metrics.defaultRegistry.counter(s"not-found-$api").inc()
  override def incrementInternalServerErrorResponse(api: String): Unit = metrics.defaultRegistry.counter(s"internal-server-error-$api").inc()
  override def startDesConnectorTimer(api: String): Context = metrics.defaultRegistry.timer(s"des-$api-timer").time()

}

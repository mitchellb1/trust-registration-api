/*
 * Copyright 2016 HM Revenue & Customs
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

import java.util.concurrent.TimeUnit

import com.kenshoo.play.metrics.MetricsRegistry

trait Metrics {
  def desConnectorTimer(diff: Long, unit: TimeUnit, method: String): Unit
}

object Metrics extends Metrics {
  override def desConnectorTimer(diff: Long, unit: TimeUnit, method: String) =
    MetricsRegistry.defaultRegistry.timer(s"des-connector-${method}-timer").update(diff, unit)
}

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

package uk.gov.hmrc.trustregistration.connectors.DES

import org.scalatest.mock.MockitoSugar
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpGet, HttpPost, HttpPut}
import uk.gov.hmrc.trustregistration.TestMetrics
import uk.gov.hmrc.trustregistration.audit.TrustsAudit
import uk.gov.hmrc.trustregistration.connectors.DesConnector
import uk.gov.hmrc.trustregistration.metrics.Metrics

import scala.concurrent.ExecutionContext

trait DESConnectorMocks extends MockitoSugar {
  implicit val hc = HeaderCarrier()

  val mockHttpPost = mock[HttpPost]
  val mockHttpPut = mock[HttpPut]
  val mockHttpGet = mock[HttpGet]
  object DesConnector extends DesConnector {
    override val httpPost: HttpPost = mockHttpPost
    override val httpPut: HttpPut = mockHttpPut
    override val httpGet: HttpGet = mockHttpGet
    override val audit: TrustsAudit = new TrustsAudit {
      override def doAudit(eventTypelMessage: String, auditTag: String)(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Unit = ()
    }
    override val metrics: Metrics = TestMetrics
  }
  lazy val SUT = DesConnector
}

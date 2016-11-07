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

import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.audit.TrustsAudit
import uk.gov.hmrc.trustregistration.connectors.DesConnector
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics

import scala.concurrent.{ExecutionContext, Future}

trait DESConnectorMocks extends MockitoSugar {
  implicit val hc = HeaderCarrier()

  val mockHttpPost = mock[HttpPost]
  val mockHttpPut = mock[HttpPut]
  val mockHttpGet = mock[HttpGet]
  val mockTrustMetrics = mock[ApplicationMetrics]

  private val mockContext = new com.codahale.metrics.Timer().time()

  when (mockTrustMetrics.startDesConnectorTimer(any())).thenReturn(mockContext)


  object SUT extends DesConnector {
    override val httpPost: HttpPost = mockHttpPost
    override val httpPut: HttpPut = mockHttpPut
    override val httpGet: HttpGet = mockHttpGet

    override val metrics: ApplicationMetrics = mockTrustMetrics
    override val audit: TrustsAudit = new TrustsAudit {
      override def doAudit(eventTypelMessage: String, auditTag: String)(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Unit = ()
    }
  }

  def setHttpPutResponse(response: Future[HttpResponse]): Unit = {
    when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
      (Matchers.any(),Matchers.any(),Matchers.any())).
      thenReturn(response)
  }
}

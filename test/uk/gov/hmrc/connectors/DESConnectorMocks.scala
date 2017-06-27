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

package uk.gov.hmrc.connectors

import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import uk.gov.hmrc.audit.Auditor
import uk.gov.hmrc.metrics.ApplicationMetrics
import uk.gov.hmrc.play.http._

import scala.concurrent.Future

/**
  * Created by eulogiogutierrez on 26/06/2017.
  */
trait DESConnectorMocks extends MockitoSugar {
  implicit val hc = HeaderCarrier()

  val mockHttpPost = mock[HttpPost]
  val mockHttpPut = mock[HttpPut]
  val mockHttpGet = mock[HttpGet]
  val mockTrustMetrics = mock[ApplicationMetrics]
  val mockAudit = mock[Auditor]

  private val mockContext = new com.codahale.metrics.Timer().time()

  when (mockTrustMetrics.startDesConnectorTimer(any())).thenReturn(mockContext)

  object SUT extends DesConnector {
    override val httpPost: HttpPost = mockHttpPost
    override val httpPut: HttpPut = mockHttpPut
    override val httpGet: HttpGet = mockHttpGet

    override val metrics: ApplicationMetrics = mockTrustMetrics
    override val audit: Auditor = mockAudit
  }

  def setHttpPutResponse(response: Future[HttpResponse]): Unit = {
    when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
      (Matchers.any(),Matchers.any(),Matchers.any())).
      thenReturn(response)
  }
}

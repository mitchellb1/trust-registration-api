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
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Writes
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.TestMetrics
import uk.gov.hmrc.trustregistration.audit.TrustsAudit
import uk.gov.hmrc.trustregistration.connectors.DesConnector
import uk.gov.hmrc.trustregistration.metrics.Metrics
import uk.gov.hmrc.trustregistration.models._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}


class DesConnectorSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks {

  "DesConnector" must {
    "return an identifier" when {
      "given a RegisterDocument" in {
        when (mockHttpPost.POST[RegistrationDocument,HttpResponse](Matchers.any(),Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
        thenReturn(Future.successful(HttpResponse(201)))
        val doc = RegistrationDocument("1234")
        val result = Await.result(SUT.registerTrust(doc),Duration.Inf)
        result mustBe Right(TRN("TRN-1234"))
      }
    }
    "return a service unavailable response" when {
      "DES returns a service unavailable response " in {
        when (mockHttpPost.POST[RegistrationDocument,HttpResponse](any[String](),any[RegistrationDocument](),any[Seq[(String,String)]]())
          (any[Writes[RegistrationDocument]](),any[HttpReads[HttpResponse]](),any[HeaderCarrier]())).
          thenReturn(Future.successful(HttpResponse(503)))
        val doc = RegistrationDocument("1234")
        val result = Await.result(SUT.registerTrust(doc),Duration.Inf)
        result mustBe Left("503")
      }
    }
    "return an exception" when {
      "Call to DES fails" in {
        when (mockHttpPost.POST[RegistrationDocument,HttpResponse](any[String](),any[RegistrationDocument](),any[Seq[(String,String)]]())
          (any[Writes[RegistrationDocument]](),any[HttpReads[HttpResponse]](),any[HeaderCarrier]())).
          thenReturn(Future.failed(Upstream4xxResponse("Bad request",400,400)))
        val doc = RegistrationDocument("")
        val result = Await.result(SUT.registerTrust(doc),Duration.Inf)
        result mustBe Left("400")
      }
    }
  }
}

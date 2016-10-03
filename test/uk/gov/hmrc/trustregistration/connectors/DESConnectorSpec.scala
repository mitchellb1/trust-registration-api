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

package uk.gov.hmrc.trustregistration.connectors

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.models.{RegistrationDocument, TRN}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import org.mockito.Matchers
import org.mockito.Mockito._
import org.mockito.Matchers.any
import play.api.libs.json.Writes
class DesConnectorSpec extends PlaySpec
  with OneAppPerSuite
  with MockitoSugar {

  implicit val hc = HeaderCarrier()

  "DesConnector" must {
    "return an identifier" when {
      "given a RegisterDocument" in {
        when (mockHttpPost.POST[RegistrationDocument,HttpResponse](Matchers.any(),Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
        thenReturn(Future.successful(HttpResponse(200)))
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
  val mockHttpPost = mock[HttpPost]
  object DesConnector extends DesConnector {
    override val httpPost: HttpPost = mockHttpPost
  }
  val SUT = DesConnector
}

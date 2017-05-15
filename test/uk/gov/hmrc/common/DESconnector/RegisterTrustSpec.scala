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

package uk.gov.hmrc.trustregistration.connectors.DES

import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Writes
import uk.gov.hmrc.common.rest.resources.core.TRN
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustapi.rest.resources.core.Trust
import uk.gov.hmrc.utils.ScalaDataExamples

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class RegisterTrustSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks with ScalaDataExamples {

  "Register Trust endpoint" must {
    "return an identifier" when {
      "given a RegisterDocument" in {
        when (mockHttpPost.POST[Trust,HttpResponse](Matchers.any(),Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(201)))
        val result = Await.result(SUT.registerTrust(trustWithEmploymentTrust),Duration.Inf)
        result mustBe Right(TRN("TRN-1234"))
      }
    }
    "return a service unavailable response" when {
      "DES returns a service unavailable response " in {
        when (mockHttpPost.POST[Trust,HttpResponse](any[String](),any[Trust](),any[Seq[(String,String)]]())
          (any[Writes[Trust]](),any[HttpReads[HttpResponse]](),any[HeaderCarrier]())).
          thenReturn(Future.successful(HttpResponse(503)))
        val result = Await.result(SUT.registerTrust(trustWithEmploymentTrust),Duration.Inf)
        result mustBe Left("503")
      }
    }
    "return an exception" when {
      "Call to DES fails" in {
        when (mockHttpPost.POST[Trust,HttpResponse](any[String](),any[Trust](),any[Seq[(String,String)]]())
          (any[Writes[Trust]](),any[HttpReads[HttpResponse]](),any[HeaderCarrier]())).
          thenReturn(Future.failed(Upstream4xxResponse("Bad request",400,400)))
        val result = Await.result(SUT.registerTrust(trustWithEmploymentTrust),Duration.Inf)
        result mustBe Left("400")
      }
    }
  }
}

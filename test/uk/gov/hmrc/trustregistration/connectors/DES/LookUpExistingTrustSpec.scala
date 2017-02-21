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
import org.mockito.Mockito.when
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.play.http.{HttpResponse, Upstream4xxResponse}
import uk.gov.hmrc.trustregistration.ScalaDataExamples
import uk.gov.hmrc.trustregistration.models.{ReRegister, Trust}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class LookUpExistingTrustSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks with ScalaDataExamples{

  "Look Up existing trust endpoint" must {
    "return 'trust exists'" when {
      "when we submit a re-register object" in {
        when (mockHttpPost.POST[ReRegister,HttpResponse](Matchers.any(),Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(201)))
        val result = Await.result(SUT.lookUpExistingTrust(ReRegister("trustName", "123456", None)),Duration.Inf)
        result mustBe Right("trusts exists")
      }
    }

    "return 'no trust'" when {
      "we submit a re-register object" in {
        when (mockHttpPost.POST[ReRegister, HttpResponse](Matchers.any(),Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(404)))
        val result = Await.result(SUT.lookUpExistingTrust(ReRegister("trustName", "123456", None)), Duration.Inf)
        result mustBe Left("404")
      }
    }

    "return an exception" when {
      "Call to DES has failed" in {
        when (mockHttpPost.POST[ReRegister, HttpResponse](Matchers.any(),Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.failed(Upstream4xxResponse("Bad request",400,400)))
        val result = Await.result(SUT.lookUpExistingTrust(ReRegister("trustName", "123456", None)), Duration.Inf)
        result mustBe Left("400")
      }
    }

  }

  }

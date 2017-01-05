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
import org.mockito.Mockito._
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.models.{BadRequestResponse, InternalServerErrorResponse, NotFoundResponse, SuccessResponse}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class NoChangeSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks {

  "No Change endpoint" must {
    "Return a BadRequestResponse" when {
      "a bad requested is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(400)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "Return a NotFoundResponse" when {
      "a 404 is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(404)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe NotFoundResponse
      }
    }

    "Return a InternalServerError" when {
      "a 500 is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(500)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    "Return a InternalServerError" when {
      "a 418 is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(418)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    "Return a SuccessResponse" when {
      "a 204 is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(204)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe SuccessResponse
      }
    }

    "Return an InternalServerErrorResponse" when {
      "the call to DES fails on the call to no-change" in {
        when(mockHttpPut.PUT[String, HttpResponse](Matchers.any(), Matchers.any())
          (Matchers.any(), Matchers.any(), Matchers.any())).
          thenReturn(Future.failed(Upstream4xxResponse("Error", 418, 400)))
        val result = Await.result(SUT.noChange("1234"), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }
  }
}

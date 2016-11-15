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
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.play.http.HttpResponse
import uk.gov.hmrc.trustregistration.models.{BadRequestResponse, GetSuccessResponse, InternalServerErrorResponse, NotFoundResponse}
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source


class GetTrustSpec extends PlaySpec
  with OneAppPerSuite
  with DESConnectorMocks
  with BeforeAndAfter
  with JsonExamples
  with ScalaDataExamples {
  "Get Trust endpoint" must {
    "return a GetSuccessResponse with a populated Trust object" when {
      "DES returns a 200 response with a Trust JSON object that contains all required fields" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(validTrustJson)))))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(trust)
      }
    }

    "returns a BadRequestresponse" when {
      "DES returns a 400 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(400)))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "return a NotFoundResponse" when {
      "DES returns a 404 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(404)))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe NotFoundResponse
      }
    }

    "return an InternalServerErrorResponse" when {
      "DES returns a 404 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, None)))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns an invalid Json response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("""{"test":"test"}""")))))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a 500 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(500)))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns any unspecified error response (i.e. a 418)" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(418)))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a Trust without name" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(invalidTrustJson)))))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a Trust with an assets object that doesn't contain any assets" in {
        val invalidTrustJson = Source.fromFile(getClass.getResource("/ValidTrust.json").getPath).mkString
          .replace("\"{WILLINTESTACYTRUST}\"", invalidWillIntestacyTrustJson)
          .replace("\"{INDIVIDUAL}\"", validIndividualJson)
          .replace("\"{ADDRESS}\"", validAddressJson)
          .replace("\"{LEGALITY}\"", validLegalityJson)

        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(invalidTrustJson)))))

        val result = Await.result(SUT.getTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }
  }
}

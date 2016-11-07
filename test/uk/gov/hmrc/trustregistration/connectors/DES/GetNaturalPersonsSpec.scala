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

import org.joda.time.DateTime
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}
import uk.gov.hmrc.trustregistration.models._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source


class GetNaturalPersonsSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks with BeforeAndAfter with JsonExamples with ScalaDataExamples {

  "Get Natural Persons endpoint" must {
    "return a GetSuccessResponse with an empty Natural Persons list" when {
      "DES returns a 200 response with an empty array" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("[]")))))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(List())
      }
    }

    "return a GetSuccessResponse with a populated Natural Persons list" when {
      "DES returns a 200 response with a JSON array of Natural Persons" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(s"[$validIndividualJson,$validIndividualJson]")))))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(List(validIndividual, validIndividual))
      }
    }

    "return a BadRequestResponse" when {
      "DES returns a 400 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(400)))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "return a NotFoundResponse" when {
      "DES returns a 404 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(404)))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe NotFoundResponse
      }
    }

    "return a InternalServerErrorResponse" when {
      "DES' Json response is missing" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, None)))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "DES returns a Json response which is invalid" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("{}")))))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "DES returns a 500 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(500)))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "DES returns any unspecified error response (i.e. a 418)" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(418)))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "DES returns a Natural Person without a date of birth" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(s"[$invalidIndividualJson]")))))
        val result = Await.result(SUT.getNaturalPersons("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }
  }
}

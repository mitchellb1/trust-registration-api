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

import org.joda.time.DateTime
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.play.http.HttpResponse
import uk.gov.hmrc.trustregistration.JsonExamples
import uk.gov.hmrc.trustregistration.models._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source


class GetSettlorsSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks with BeforeAndAfter with JsonExamples {
  "Get Settlors endpoint" must {
    "return a GetSuccessResponse with a populated Settlors object" when {
      "DES returns a 200 response with a settlors JSON object that contains a list of individuals" in {

        val validSettlorsJson = ("""{"individuals" : [{INDIVIDUAL},{INDIVIDUAL}]}""").replace("{INDIVIDUAL}", validIndividualJson)
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(validSettlorsJson)))))

        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        val passport = Passport("IDENTIFIER",new DateTime("2020-01-01"),"ES")
        val address = Address("Line 1", Some("Line 2"), Some("Line 3"), Some("Line 4"), Some("NE1 2BR"), Some("ES"))
        val expectedIndividualSettlors = Settlors(Some(List(Individual("Dr","Leo","Spaceman",new DateTime("1800-01-01"),None,None,None,None,Some(passport),Some(address)),
          Individual("Dr","Leo","Spaceman",new DateTime("1800-01-01"),None,None,None,None,Some(passport),Some(address)))))

        result mustBe GetSuccessResponse(expectedIndividualSettlors)
      }

      "DES returns a 200 response with a settlors JSON object that contains a list of companies" in {
        val validSettlorsJson = ("""{"companies" : [{COMPANY},{COMPANY}]}""").replace("{COMPANY}", validCompanyJson)
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(validSettlorsJson)))))

        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        val address = Address("Line 1", Some("Line 2"), Some("Line 3"), Some("Line 4"), Some("NE1 2BR"), Some("ES"))
        val expectedCompanySettlors = Settlors(None,Some(List(Company("Company",address,Some("AAA5221")),Company("Company",address,Some("AAA5221")))))

        result mustBe GetSuccessResponse(expectedCompanySettlors)
      }
    }

    "return a BadRequestresponse" when {
      "DES returns a 400 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(400)))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "return a NotFoundResponse" when {
      "DES returns a 404 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(404)))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe NotFoundResponse
      }
    }

    "return an InternalServerErrorResponse" when {
      "DES Json response is missing" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, None)))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns an invalid Json response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("""{"test":"test"}""")))))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a 500 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(500)))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns any unspecified error response (i.e. a 418)" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(418)))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a Settlors object with a list of companies without all the required fields" in {
        val invalidSettlorsJson = ("""{"companies" : [{COMPANY}]}""").replace("{COMPANY}", invalidCompanyJson)
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(invalidSettlorsJson)))))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "DES returns a Settlors object with a list of individuals without all the required fields" in {
        val invalidSettlorsJson = ("""{"individuals" : [{INDIVIDUAL}]}""").replace("{INDIVIDUAL}", invalidIndividualJson)
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(invalidSettlorsJson)))))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }
  }


}

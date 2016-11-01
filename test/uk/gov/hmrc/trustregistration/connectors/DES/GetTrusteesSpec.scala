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
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.http.MediaRange.parse
import play.api.libs.json.Json
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.models._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class GetTrusteesSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks {

  "Get Trustees endpoint" must {
    "return a GetTrusteeSuccessResponse with an empty Trustee list" when {
      "DES returns a 200 response with an empty array" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("[]")))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetTrusteeSuccessResponse(List())
      }
    }

    "return a GetTrusteeSuccessResponse with a populated Trustee list" when {
      "DES returns a 200 response with a JSON array of Trustees" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("[{\"givenName\":\"Juan\"},{\"givenName\":\"Chris\"}]")))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetTrusteeSuccessResponse(List(Trustee("Juan",None),Trustee("Chris",None)))
      }

      "DES returns a 200 response with a JSON array of Trustees containing a Passport" in {
        val jsonReturn = "[{\"givenName\":\"Juan\",\"passport\":{\"identifier\" : \"AA12345\",\"expiryDate\" : \"2012-04-23T18:25:43.511Z\",\"countryOfIssue\" : \"ESP\"}}]"
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        val expectedPassport = Passport("AA12345", new DateTime("2012-04-23T18:25:43.511Z"), "ESP")
        result mustBe GetTrusteeSuccessResponse(List(Trustee("Juan", Some(expectedPassport))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing an Address with none of the optional parameters" in {
        val jsonReturn = "[{\"givenName\":\"Juan\", \"correspondenceAddress\" : {\"addressLine1\" : \"123 Any Street\",\"isNonUkAddress\" : false}}]"
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetTrusteeSuccessResponse(List(Trustee("Juan", None, Some(Address(false, "123 Any Street")))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing an Address with some of the optional parameters" in {
        val jsonReturn = "[{\"givenName\":\"Juan\", \"correspondenceAddress\" : {\"addressLine1\" : \"123 Any Street\",\"addressLine2\" : \"Mowr Town\",\"postcode\" : \"NE21 25A\",\"isNonUkAddress\" : false}}]"
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetTrusteeSuccessResponse(List(Trustee("Juan", None, Some(Address(false, "123 Any Street",Some("Mowr Town"),None,None,Some("NE21 25A"))))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing an Address with all the optional parameters" in {
        val jsonReturn = "[{\"givenName\":\"Juan\", \"correspondenceAddress\" : {\"addressLine1\" : \"123 Any Street\",\"addressLine2\" : \"Mowr Town\",\"addressLine3\" : \"Test\",\"addressLine4\" : \"Test Test Test 523125223\",\"postcode\" : \"NE21 25A\",\"country\" : \"ZAR\",\"isNonUkAddress\" : false}}]"
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetTrusteeSuccessResponse(List(Trustee("Juan", None, Some(Address(false, "123 Any Street",Some("Mowr Town"),Some("Test"),Some("Test Test Test 523125223"),Some("NE21 25A"),Some("ZAR"))))))
      }
    }

    "return a NotFoundResponse" when {
      "DES returns a 404 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(404)))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe NotFoundResponse
      }
    }

    "return a InternalServerErrorResponse" when {
      "DES' Json response is missing" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, None)))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a Json response which is invalid" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("{}")))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a 500 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(500)))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns any unspecified error response (i.e. a 418)" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(418)))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns an incorrectly formatted expiry date" in {
        val jsonReturn = "[{\"givenName\":\"Juan\",\"passport\":{\"identifier\" : \"AA12345\",\"expiryDate\" : \"12th Jan 2016\",\"countryOfIssue\" : \"ESP\"}}]"
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a trustee without all the required fields" in {
        val jsonReturn = "[{\"givenName\":\"Juan\",\"passport\":{\"identifier\" : \"AA12345\",\"expiryDate\" : \"12th Jan 2016\",\"countryOfIssue\" : \"ESP\", \"correspondenceAddress\" : {}}}]"
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }
  }
}

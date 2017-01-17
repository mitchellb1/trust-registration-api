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
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.models._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class GetTrusteesSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks with BeforeAndAfter {

  "Get Trustees endpoint" must {
    "return a GetSuccessResponse with an empty Trustee list" when {
      "DES returns a 200 response with an empty array" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("[]")))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(List())
      }
    }

    "return a GetSuccessResponse with a populated Trustee list" when {
      "DES returns a 200 response with a JSON array of Trustees" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse("""[{"givenName":"Juan","familyName":"Doe","dateOfBirth" : "2012-04-23T18:25:43.511Z"},""" +
                          """{"givenName":"Chris","familyName":"Doe","dateOfBirth" : "2012-04-23T18:25:43.511Z"}]""")))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(List(Individual("Juan","Doe",new DateTime("2012-04-23T18:25:43.511Z")),
                                                    Individual("Chris","Doe",new DateTime("2012-04-23T18:25:43.511Z"))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing a Passport" in {
        val jsonReturn = """[{"givenName":"Juan","familyName":"Doe","dateOfBirth" : "2012-04-23T18:25:43.511Z","passport":{"identifier" : "AA12345","expiryDate" : "2012-04-23T18:25:43.511Z","countryOfIssue" : "ESP"}}]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        val expectedPassport = Passport("AA12345", new DateTime("2012-04-23T18:25:43.511Z"), "ESP")
        result mustBe GetSuccessResponse(List(Individual("Juan","Doe",new DateTime("2012-04-23T18:25:43.511Z"),None,None,None,None,Some(expectedPassport))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing a Passport when given a 'null' address" in {
        val jsonReturn = """[{"givenName":"Juan","familyName":"Doe","dateOfBirth" : "2012-04-23T18:25:43.511Z","passport":{"identifier" : "AA12345","expiryDate" : "2012-04-23T18:25:43.511Z","countryOfIssue" : "ESP"}, "correspondenceAddress" : null}]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        val expectedPassport = Passport("AA12345", new DateTime("2012-04-23T18:25:43.511Z"), "ESP")
        result mustBe GetSuccessResponse(List(Individual("Juan","Doe",new DateTime("2012-04-23T18:25:43.511Z"),None,None,None,None,Some(expectedPassport))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing an Address with none of the optional parameters" in {
        val jsonReturn = """[{"givenName":"Juan","familyName":"Doe","dateOfBirth" : "2012-04-23T18:25:43.511Z", "correspondenceAddress" : {"line1" : "123 Any Street","countryCode":"ES"}}]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(List(Individual("Juan","Doe",new DateTime("2012-04-23T18:25:43.511Z"),None,None,None,None,None,Some(Address("123 Any Street",None,None,None,None,"ES")))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing an Address with some of the optional parameters" in {
        val jsonReturn = """[{"givenName":"Juan","familyName":"Doe","dateOfBirth" : "2012-04-23T18:25:43.511Z", "correspondenceAddress" : {"line1" : "123 Any Street","line2" : "Mowr Town","postalCode" : "NE21 25A","countryCode":"ES"}}]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(List(Individual("Juan","Doe",new DateTime("2012-04-23T18:25:43.511Z"),None,None,None,None,None,Some(Address("123 Any Street",Some("Mowr Town"),None,None,Some("NE21 25A"),"ES")))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing an Address with all the optional parameters" in {
        val jsonReturn = """[{"givenName":"Juan","familyName":"Doe","dateOfBirth" : "2012-04-23T18:25:43.511Z", "correspondenceAddress" : {"line1" : "123 Any Street","line2" : "Mowr Town","line3" : "Test","line4" : "Test Test Test 523125223","postalCode" : "NE21 25A","countryCode" : "ZAR"}}]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(List(Individual("Juan","Doe",new DateTime("2012-04-23T18:25:43.511Z"),None,None,None,None,None,Some(Address("123 Any Street",Some("Mowr Town"),Some("Test"),Some("Test Test Test 523125223"),Some("NE21 25A"),"ZAR")))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing an Address and Passport" in {
        val jsonReturn = """[{"givenName":"Juan","familyName":"Doe","dateOfBirth" : "2012-04-23T18:25:43.511Z","passport":{"identifier" : "AA12345","expiryDate" : "2012-04-23T18:25:43.511Z","countryOfIssue" : "ESP"}, "correspondenceAddress" : {"line1" : "123 Any Street","line2" : "Mowr Town","line3" : "Test","line4" : "Test Test Test 523125223","postalCode" : "NE21 25A","countryCode" : "ZAR"}}]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        val expectedPassport = Passport("AA12345", new DateTime("2012-04-23T18:25:43.511Z"), "ESP")
        result mustBe GetSuccessResponse(List(Individual("Juan","Doe",new DateTime("2012-04-23T18:25:43.511Z"),None,None,None,None,Some(expectedPassport),Some(Address("123 Any Street",Some("Mowr Town"),Some("Test"),Some("Test Test Test 523125223"),Some("NE21 25A"),"ZAR")))))
      }

      "DES returns a 200 response with a JSON array of Trustees containing all required fields" in {
        val jsonReturn =
          """[{
             "givenName": "John",
             "otherName": "B",
             "familyName": "Doe",
             "dateOfBirth": "1900-01-01T00:00:00.000Z",
             "nino": "1234567890",
             "dateOfDeath": "2016-01-01T00:00:00.000Z",
             "passport": {
                "identifier": "123456",
                "expiryDate": "2016-01-01T00:00:00.000Z",
                "countryOfIssue": "ES"
             },
             "correspondenceAddress": {
                "line1": "A House",
                "line2": "A Street",
                "line3": "An Area",
                "line4": "A Town",
                "postalCode": "AB1 1AB",
                "countryCode": "ES"
             },
             "telephoneNumber": "019112345678"
         }]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        val expectedPassport = Passport("123456", new DateTime("2016-01-01T00:00:00.000Z"), "ES")
        val expectedAddress = Address("A House", Some("A Street"), Some("An Area"), Some("A Town"), Some("AB1 1AB"), "ES")
        result mustBe GetSuccessResponse(List(Individual("John", "Doe", new DateTime("1900-01-01T00:00:00.000Z"),Some("B"), Some("1234567890"),Some(new DateTime("2016-01-01T00:00:00.000Z")), Some("019112345678"), Some(expectedPassport), Some(expectedAddress))))
      }
    }

    "return a BadRequestResponse" when {
      "DES returns a 400 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(400)))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "return a NotFoundResponse" when {
      "DES returns a 404 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(404)))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe NotFoundResponse
      }
    }

    "return an InternalServerErrorResponse" when {
      "DES Json response is missing" in {
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
        val jsonReturn = """[{"givenName":"Juan","passport":{"identifier" : "AA12345","expiryDate" : "12th Jan 2016","countryOfIssue" : "ESP"}}]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a trustee without all the required address fields" in {
        val jsonReturn = "[{\"givenName\":\"Juan\", \"correspondenceAddress\" : {}}]"
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
      "DES returns a trustee without all the required passport fields" in {
        val jsonReturn = """[{"givenName":"Juan", "passport" : {"identifier":"AA12345"}}]"""
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(jsonReturn)))))
        val result = Await.result(SUT.getTrustees("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }
  }
}

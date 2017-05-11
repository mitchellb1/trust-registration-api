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

package uk.gov.hmrc.trustregistration.controllers

import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{RequestHeader, Result}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.models.estates.Estate
import uk.gov.hmrc.trustregistration.services.RegisterEstateService
import uk.gov.hmrc.trustregistration.utils.{FailedValidation, JsonSchemaValidator, SuccessfulValidation, TrustsValidationError}
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

import scala.concurrent.Future

class RegisterEstateControllerSpec extends PlaySpec with OneAppPerSuite with JsonExamples with ScalaDataExamples with BeforeAndAfter with RegisterTrustServiceMocks  {

  before {
    when(mockRegisterEstateService.registerEstate(any[Estate])(any[HeaderCarrier]))
      .thenReturn(Future.successful(Right(TRN("TRN-1234"))))

    when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "AUTHORISED"))

    when(mockSchemaValidator.validateAgainstSchema(anyString())).thenReturn(SuccessfulValidation)

  }

  "Get estate endpoint" must {
    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterEstateService.getEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[Estate](validEstateWithPersonalRepresentative)))

        val result = SUT.getEstate("1234").apply(FakeRequest("GET",""))

        status(result) mustBe OK
        contentAsString(result) contains (validEstateWithPersonalRepresentative)
      }
    }

    "return 404 not found" when {
      "the endpoint is called with an identifier that can't be found" in {
        when(mockRegisterEstateService.getEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.getEstate("404NotFound").apply(FakeRequest("GET", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 400" when {
      "the  endpoint is called with an invalid identifier" in {
        when(mockRegisterEstateService.getEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.getEstate("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterEstateService.getEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.getEstate("sadfg").apply(FakeRequest("GET", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {
        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.getEstate("12345").apply(FakeRequest("GET", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }
  }

  "Register estate endpoint" must {

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {
        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        withCallToPOST(Json.parse("""{"message":"","code":""}""")) { result =>
          status(result) mustBe UNAUTHORIZED
        }
      }
    }

    "Return a Bad Request" when {
      "The json fails schema validation with a single error" in {
        when(mockSchemaValidator.validateAgainstSchema(anyString)).thenReturn(FailedValidation("Invalid Json", 0, Seq(TrustsValidationError("object has missing required properties ([\"location\"])", "/"))))
        withCallToPOST(Json.parse("""{"message":"","code":""}""")) { result =>

          status(result) mustBe BAD_REQUEST
          contentAsJson(result) must be (Json.parse("""{"message":"Invalid Json","code":0,"validationErrors":[{"message":"object has missing required properties ([\"location\"])","location":"/"}]}"""))
        }
      }

      "The json fails schema validation with two errors" in {
        when(mockSchemaValidator.validateAgainstSchema(anyString)).thenReturn(FailedValidation("Invalid Json", 0, Seq(TrustsValidationError("object has missing required properties ([\"code\",\"location\"])", "/"),TrustsValidationError("instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])", "/message"))))

        withCallToPOST(Json.parse("""{"message":1}""")) { result =>
          status(result) mustBe BAD_REQUEST
          val body = contentAsJson(result)

          contentAsJson(result) must be (Json.parse("""{"message":"Invalid Json","code":0,"validationErrors":[{"message":"object has missing required properties ([\"code\",\"location\"])","location":"/"},{"message":"instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])","location":"/message"}]}"""))
        }
      }

      "The json fails schema validation with multiple errors" in {
        when(mockSchemaValidator.validateAgainstSchema(anyString)).thenReturn(FailedValidation("Invalid Json", 0, Seq(TrustsValidationError("object has missing required properties ([\"code\",\"location\"])", "/"),TrustsValidationError("instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])", "/message"),TrustsValidationError("string \"1111111111\" is too long (length: 10, maximum allowed: 9)", "/numbers"),TrustsValidationError("ECMA 262 regex \"^[A-Za-z0-9]{3,4} [A-Za-z0-9]{3}$\" does not match input string \"1111\"", "/postalCode"))))

        withCallToPOST(Json.parse("""{"message":1,"numbers":"1111111111","postalCode":"1111"}""")) { result =>
          status(result) mustBe BAD_REQUEST
          contentAsJson(result) must be (Json.parse("""{"message":"Invalid Json","code":0,"validationErrors":[{"message":"object has missing required properties ([\"code\",\"location\"])","location":"/"},{"message":"instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])","location":"/message"},{"message":"string \"1111111111\" is too long (length: 10, maximum allowed: 9)","location":"/numbers"},{"message":"ECMA 262 regex \"^[A-Za-z0-9]{3,4} [A-Za-z0-9]{3}$\" does not match input string \"1111\"","location":"/postalCode"}]}"""))
        }
      }

      "The json trust document is invalid" in {
        withCallToPOST(badRegDocPayload) { result =>
          status(result) mustBe BAD_REQUEST
        }
      }
      "The json trust document is missing" in {
        withCallToPOST(Json.parse("{}")) { result =>
          status(result) mustBe BAD_REQUEST
        }
      }
    }
  }

  "Close estate endpoint" must {
    "return 200 ok" when {
      "the endpoint is valled with a valid identifier" in {
        when(mockRegisterEstateService.closeEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(SuccessResponse))

        val result = SUT.closeEstate("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe OK
      }
    }

    "return 400" when {
      "the endpoint is called with an invalid identifier" in {
        when(mockRegisterEstateService.closeEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.closeEstate("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {

        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.closeEstate("12345").apply(FakeRequest("PUT", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }

    "return 404" when {
      "the endpoint is called and we pass an identifier that does not return a trust" in {
        when(mockRegisterEstateService.closeEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.closeEstate("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterEstateService.closeEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.closeEstate("sadfg").apply(FakeRequest("PUT", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

  val mockSchemaValidator = mock[JsonSchemaValidator]


  object SUT extends RegisterEstateController {
    override implicit def hc(implicit rh: RequestHeader): HeaderCarrier = mockHC
    override val jsonSchemaValidator = mockSchemaValidator
    override val metrics: ApplicationMetrics = mockMetrics
    override val registerEstateService: RegisterEstateService = mockRegisterEstateService
  }

  private def withCallToPOST(payload: JsValue)(handler: Future[Result] => Any) = {
    handler(SUT.register.apply(registerRequestWithPayload(payload)))
  }

  private def withCallToPOST()(handler: Future[Result] => Any) = {
    val fr = FakeRequest(
      "PUT",
      "",
      FakeHeaders(),
      ""
    ).withHeaders(CONTENT_TYPE -> "application/json")
    SUT.register.apply(fr)
  }
}

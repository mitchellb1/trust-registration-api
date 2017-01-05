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
import play.api.libs.json.JsValue
import play.api.mvc.{RequestHeader, Result}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers._
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.services.RegisterTrustService

import scala.concurrent.Future

class RegisterEstateControllerSpec extends PlaySpec with OneAppPerSuite with JsonExamples with ScalaDataExamples with BeforeAndAfter with RegisterTrustServiceMocks  {

  before {
    when(mockRegisterTrustService.registerEstate(any[EstateRegistrationDocument])(any[HeaderCarrier]))
      .thenReturn(Future.successful(Right(TRN("TRN-1234"))))

    when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "AUTHORISED"))
  }

  "Get estate endpoint" must {
    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.getEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[Estate](validEstateWithDeceased)))

        val result = SUT.getEstate("1234").apply(FakeRequest("GET",""))

        status(result) mustBe OK
        contentAsString(result) contains (validEstateWithDeceased)
      }
    }

    "return 404 not found" when {
      "the endpoint is called with an identifier that can't be found" in {
        when(mockRegisterTrustService.getEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.getEstate("404NotFound").apply(FakeRequest("GET", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 400" when {
      "the  endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.getEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.getEstate("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.getEstate(any[String])(any[HeaderCarrier]))
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
    "return created with a TRN" when {
      "the register endpoint is called with a valid json payload" in {
        withCallToPOST(estateRegDocPayload) { result =>
          status(result) mustBe CREATED
          contentAsString(result) must include("TRN")
        }
      }
    }

    "Return a Bad Request" when {
      "The json trust document is invalid" in {
        withCallToPOST(badRegDocPayload) { result =>
          status(result) mustBe BAD_REQUEST
        }
      }
      "The json trust document is missing" in {
        withCallToPOST() { result =>
          status(result) mustBe BAD_REQUEST
        }
      }
    }
  }

  "Close estate endpoint" must {
    "return 200 ok" when {
      "the endpoint is valled with a valid identifier" in {
        when(mockRegisterTrustService.closeEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(SuccessResponse))

        val result = SUT.closeEstate("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe OK
      }
    }

    "return 400" when {
      "the endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.closeEstate(any[String])(any[HeaderCarrier]))
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
        when(mockRegisterTrustService.closeEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.closeEstate("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.closeEstate(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.closeEstate("sadfg").apply(FakeRequest("PUT", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

  object SUT extends RegisterEstateController {
    override implicit def hc(implicit rh: RequestHeader): HeaderCarrier = mockHC

    override val metrics: ApplicationMetrics = mockMetrics
    override val registerTrustService: RegisterTrustService = mockRegisterTrustService
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

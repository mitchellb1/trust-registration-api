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

package uk.gov.hmrc.trustregistration.controllers


import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.http.HeaderNames.{AUTHORIZATION => _, CONTENT_TYPE => _, _}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Request, RequestHeader, Result}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers._
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.services.RegisterTrustService

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class RegisterTrustControllerSpec extends PlaySpec
  with OneAppPerSuite
  with MockitoSugar
  with BeforeAndAfter {

  before {
    when(mockRegisterTrustService.registerTrust(any[RegistrationDocument])(any[HeaderCarrier]))
      .thenReturn(Future.successful(Right(TRN("TRN-1234"))))

    when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "AUTHORISED"))

  }

  "RegisterTrustController" must {
    "return created with a TRN" when {
      "the register endpoint is called with a valid json payload" in {
        withCallToPOST(regDocPayload) { result =>
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
    "return 200 ok" when {
      "the no change endpoint is called with a valid identifier" in {

        when(mockRegisterTrustService.noChange(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(SuccessResponse))

        val result = SUT.noChange("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe OK
      }
    }

    "return 400" when {
      "the no change endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.noChange(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.noChange("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 401" when {
      "authentication credentials are missing or incorrect" in {

        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.noChange("12345").apply(FakeRequest("PUT", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }

    "return 404" when {
      "we pass an identifier that does not return a trust" in {
        when(mockRegisterTrustService.noChange(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.noChange("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe NOT_FOUND
      }
    }


    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.noChange(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.noChange("sadfg").apply(FakeRequest("PUT", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

  private val regDocPayload = Json.obj(
    "value" -> "Trust Name"
  )
  private val badRegDocPayload = Json.obj(
    "wrongIdentifier" -> "Trust Name"
  )

  private val mockRegisterTrustService = mock[RegisterTrustService]
  private val mockHC = mock[HeaderCarrier]

  object TestRegisterTrustController extends RegisterTrustController {
    override implicit def hc(implicit rh: RequestHeader): HeaderCarrier = mockHC

    override val registerTrustService: RegisterTrustService = mockRegisterTrustService
  }

  private val SUT = TestRegisterTrustController


  private def withCallToPOST(payload: JsValue)(handler: Future[Result] => Any) = {
    handler(SUT.register.apply(registerRequestWithPayload(payload)))
  }

  private def registerRequestWithPayload(payload: JsValue): Request[JsValue] = FakeRequest(
    "POST",
    "",
    FakeHeaders(),
    payload
  ).withHeaders(CONTENT_TYPE -> "application/json")

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

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
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Request, Result}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers._
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.models.{RegistrationDocument, TRN}
import uk.gov.hmrc.trustregistration.services.RegisterTrustService

import scala.concurrent.Future


class RegisterTrustControllerSpec extends PlaySpec with OneAppPerSuite

  with MockitoSugar
  with BeforeAndAfter {

  before {
    when(MockRegisterTrustService.registerTrust(any[RegistrationDocument])(any[HeaderCarrier]))
      .thenReturn(Future.successful(Right(TRN("TRN-1234"))))
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
  }

  private val regDocPayload = Json.obj(
    "value" -> "Trust Name"
  )
  private val badRegDocPayload = Json.obj(
    "wrongIdentifier" -> "Trust Name"
  )

  private val MockRegisterTrustService = mock[RegisterTrustService]

  object TestRegisterTrustController extends RegisterTrustController {
    override val registerTrustService: RegisterTrustService = MockRegisterTrustService
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

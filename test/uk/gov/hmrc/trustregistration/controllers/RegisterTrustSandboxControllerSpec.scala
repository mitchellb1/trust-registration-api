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
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.http.HeaderNames.{AUTHORIZATION => _, CONTENT_TYPE => _}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{RequestHeader, Result}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.services.RegisterTrustService
import uk.gov.hmrc.trustregistration.utils.SchemaValidator
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

import scala.concurrent.Future

class RegisterTrustSandboxControllerSpec extends PlaySpec
  with OneAppPerSuite
  with BeforeAndAfter
  with JsonExamples
  with ScalaDataExamples
  with RegisterTrustServiceMocks {

  before {
    when(mockRegisterTrustService.registerTrust(any[Trust])(any[HeaderCarrier]))
      .thenReturn(Future.successful(Right(TRN("TRN-1234"))))

    when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "AUTHORISED"))
  }

  "RegisterTrustSandboxController" must {
    "return created with a TRN" when {
      "the register endpoint is called with a valid json payload" in {
        withCallToPOST(Json.parse(validTrustJson)) { result =>
          status(result) mustBe CREATED
          contentAsString(result) must include("TRN")
        }
      }
    }
  }

  "No change endpoint" must {
    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.noChange(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(SuccessResponse))

        val result = SUT.noChange("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe OK
      }
    }
  }

  "Close trust endpoint" must {
    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.closeTrust(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(SuccessResponse))

        val result = SUT.closeTrust("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe OK
      }
    }
  }

  object SUT extends RegisterTrustSandboxController {
    override implicit def hc(implicit rh: RequestHeader): HeaderCarrier = mockHC

    override val metrics: ApplicationMetrics = mockMetrics
    override val registerTrustService: RegisterTrustService = mockRegisterTrustService
    override val jsonSchemaValidator = SchemaValidator

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

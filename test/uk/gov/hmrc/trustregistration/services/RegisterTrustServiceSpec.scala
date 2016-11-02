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

package uk.gov.hmrc.trustregistration.services

import org.mockito.Matchers._
import org.mockito.Mockito.when
import org.mockito.Mockito.mock
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.connectors.DesConnector
import uk.gov.hmrc.trustregistration.models._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Right

class RegisterTrustServiceSpec extends PlaySpec
  with MockitoSugar
  with OneAppPerSuite {

  "RegisterTrustService" must {
    "Return a TRN" when {
      "Given a valid registration" in {

        when(mockDesConnector.registerTrust(any())(any())).thenReturn(Future.successful(Right(TRN(testTRN))))
        val registration = RegistrationDocument("this is the input")

        val result = Await.result(SUT.registerTrust(registration)(HeaderCarrier()), Duration.Inf)
        result mustBe Right(TRN(testTRN))
      }
    }

    "Return a SuccessResponse" when {
      "a successful response is returned from DES for the call to no-change" in {
        when(mockDesConnector.noChange(any())(any())).thenReturn(Future.successful(SuccessResponse))

        val result = Await.result(SUT.noChange("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe SuccessResponse
      }

      "a successful response is returned from DES for the call to closeTrust" in {
        when(mockDesConnector.closeTrust(any())(any())).thenReturn(Future.successful(SuccessResponse))

        val result = Await.result(SUT.closeTrust("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe SuccessResponse
      }

      "a successful response is returned from DES for the call to getTrustees" in {
        when(mockDesConnector.getTrustees(any())(any())).thenReturn(Future.successful(GetTrusteeSuccessResponse(Nil)))

        val result = Await.result(SUT.getTrustees("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetTrusteeSuccessResponse(Nil)
      }
    }

    "Return a BadRequestResponse" when {
      "a bad requested is returned from DES for the call to no-change" in {
        when(mockDesConnector.noChange(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.noChange("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a bad requested is returned from DES for the call to closeTrust" in {
        when(mockDesConnector.closeTrust(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.closeTrust("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "Return a InternalServerErrorResponse" when {
      "a InternalServerErrorResponse is returned from DES for the call to getTrustees" in {
        when(mockDesConnector.getTrustees(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getTrustees("500Error")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    "Return a NotFoundResponse" when {
      "a NotFoundResponse is returned from DES for the call to getTrustees" in {
        when(mockDesConnector.getTrustees(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getTrustees("404NotFound")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }
    }
  }


  val mockDesConnector = mock[DesConnector]
  object TestRegisterTrustService extends RegisterTrustService {
    override val desConnector: DesConnector = mockDesConnector
  }

  val testTRN: String = "TRN-1234"
  val SUT = TestRegisterTrustService
}





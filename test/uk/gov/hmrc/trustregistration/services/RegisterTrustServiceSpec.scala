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

package uk.gov.hmrc.trustregistration.services

import org.mockito.Matchers._
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.ScalaDataExamples
import uk.gov.hmrc.trustregistration.connectors.DesConnector
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.models.estates.EstateRegistrationDocument

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Right

class RegisterTrustServiceSpec extends PlaySpec
  with MockitoSugar
  with OneAppPerSuite
  with ScalaDataExamples {

  "RegisterTrustService" must {
    "Return a TRN" when {

      "Given a valid estate registration" in {
        when(mockDesConnector.registerEstate(any())(any())).thenReturn(Future.successful(Right(TRN(testTRN))))

        val result = Await.result(SUT.registerEstate(validEstateWithPersonalRepresentative)(HeaderCarrier()), Duration.Inf)
        result mustBe Right(TRN(testTRN))
      }
    }

    "Return a SuccessResponse" when {
      "a SuccessResponse is returned from DES for the call to noChange" in {
        when(mockDesConnector.noChange(any())(any())).thenReturn(Future.successful(SuccessResponse))

        val result = Await.result(SUT.noChange("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe SuccessResponse
      }

      "a SuccessResponse is returned from DES for the call to closeTrust" in {
        when(mockDesConnector.closeTrust(any())(any())).thenReturn(Future.successful(SuccessResponse))

        val result = Await.result(SUT.closeTrust("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe SuccessResponse
      }

      "a SuccessResponse is returned from DES for the call to getTrustees" in {
        when(mockDesConnector.getTrustees(any())(any())).thenReturn(Future.successful(GetSuccessResponse(Nil)))

        val result = Await.result(SUT.getTrustees("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(Nil)
      }

      "a SuccessResponse is returned from DES for the call to getSettlors" in {
        when(mockDesConnector.getSettlors(any())(any())).thenReturn(Future.successful(GetSuccessResponse(Nil)))

        val result = Await.result(SUT.getSettlors("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(Nil)
      }

      "a SuccessResponse is returned from DES for the call to getNaturalPersons" in {
        when(mockDesConnector.getNaturalPersons(any())(any())).thenReturn(Future.successful(GetSuccessResponse(Nil)))

        val result = Await.result(SUT.getNaturalPersons("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(Nil)
      }

      "a SuccessResponse is returned from DES for the call to getTrustContactDetails" in {
        when(mockDesConnector.getTrustContactDetails(any())(any())).thenReturn(Future.successful(GetSuccessResponse(Nil)))

        val result = Await.result(SUT.getTrustContactDetails("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(Nil)
      }

      "a SuccessResponse is returned from DES for the call to getLeadTrustee" in {
        when(mockDesConnector.getLeadTrustee(any())(any())).thenReturn(Future.successful(GetSuccessResponse(leadTrusteeCompany)))

        val result = Await.result(SUT.getLeadTrustee("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(leadTrusteeCompany)
      }

      "a SuccessResponse is returned from DES for the call to getBeneficiaries" in {
        when(mockDesConnector.getBeneficiaries(any())(any())).thenReturn(Future.successful(GetSuccessResponse(beneficiaries)))

        val result = Await.result(SUT.getBeneficiaries("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(beneficiaries)
      }

      "a SuccessResponse is returned from DES for the call to getProtectors" in {
        when(mockDesConnector.getProtectors(any())(any())).thenReturn(Future.successful(GetSuccessResponse(protectors)))

        val result = Await.result(SUT.getProtectors("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(protectors)
      }

      "a SuccessResponse is returned from DES for the call to getEstate" in {
        when(mockDesConnector.getEstate(any())(any())).thenReturn(Future.successful(GetSuccessResponse(validEstateWithPersonalRepresentative)))

        val result = Await.result(SUT.getEstate("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(validEstateWithPersonalRepresentative)
      }

      "a SuccessResponse is returned from DES for the call to closeEstate" in {
        when(mockDesConnector.closeEstate(any())(any())).thenReturn(Future.successful(SuccessResponse))

        val result = Await.result(SUT.closeEstate("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe SuccessResponse
      }

      "a SuccessResponse is returned from DES for the call to getTrust" in {
        when(mockDesConnector.getTrust(any())(any())).thenReturn(Future.successful(GetSuccessResponse(trust)))

        val result = Await.result(SUT.getTrust("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe GetSuccessResponse(trust)
      }
    }

    "Return a BadRequestResponse" when {
      "a BadRequestResponse is returned from DES for the call to noChange" in {
        when(mockDesConnector.noChange(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.noChange("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to closeTrust" in {
        when(mockDesConnector.closeTrust(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.closeTrust("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getTrustees" in {
        when(mockDesConnector.getTrustees(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getTrustees("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getSettlors" in {
        when(mockDesConnector.getSettlors(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getSettlors("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getNaturalPersons" in {
        when(mockDesConnector.getNaturalPersons(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getNaturalPersons("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getTrustContactDetails" in {
        when(mockDesConnector.getTrustContactDetails(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getTrustContactDetails("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getLeadTrustee" in {
        when(mockDesConnector.getLeadTrustee(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getLeadTrustee("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getBeneficiaries" in {
        when(mockDesConnector.getBeneficiaries(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getBeneficiaries("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getProtectors" in {
        when(mockDesConnector.getProtectors(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getProtectors("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getEstate" in {
        when(mockDesConnector.getEstate(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getEstate("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to closeEstate" in {
        when(mockDesConnector.closeEstate(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.closeEstate("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }

      "a BadRequestResponse is returned from DES for the call to getTrust" in {
        when(mockDesConnector.getTrust(any())(any())).thenReturn(Future.successful(BadRequestResponse))

        val result = Await.result(SUT.getTrust("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "Return a InternalServerErrorResponse" when {
      "a InternalServerErrorResponse is returned from DES for the call to noChange" in {
        when(mockDesConnector.noChange(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.noChange("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to closeTrust" in {
        when(mockDesConnector.closeTrust(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.closeTrust("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getTrustees" in {
        when(mockDesConnector.getTrustees(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getTrustees("500Error")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getSettlors" in {
        when(mockDesConnector.getSettlors(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getSettlors("500Error")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getNaturalPersons" in {
        when(mockDesConnector.getNaturalPersons(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getNaturalPersons("500Error")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getTrustContactDetails" in {
        when(mockDesConnector.getTrustContactDetails(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getTrustContactDetails("500Error")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getLeadTrustee" in {
        when(mockDesConnector.getLeadTrustee(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getLeadTrustee("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getBeneficiaries" in {
        when(mockDesConnector.getBeneficiaries(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getBeneficiaries("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getProtectors" in {
        when(mockDesConnector.getProtectors(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getProtectors("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getEstate" in {
        when(mockDesConnector.getEstate(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getEstate("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to closeEstate" in {
        when(mockDesConnector.closeEstate(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.closeEstate("400BadRequest")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }

      "a InternalServerErrorResponse is returned from DES for the call to getTrust" in {
        when(mockDesConnector.getTrust(any())(any())).thenReturn(Future.successful(InternalServerErrorResponse))

        val result = Await.result(SUT.getTrust("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    "Return a NotFoundResponse" when {
      "a NotFoundResponse is returned from DES for the call to noChange" in {
        when(mockDesConnector.noChange(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.noChange("404NotFound")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to closeTrust" in {
        when(mockDesConnector.closeTrust(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.closeTrust("404NotFound")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getTrustees" in {
        when(mockDesConnector.getTrustees(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getTrustees("404NotFound")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getSettlors" in {
        when(mockDesConnector.getSettlors(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getSettlors("404NotFound")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getNaturalPersons" in {
        when(mockDesConnector.getNaturalPersons(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getNaturalPersons("404NotFound")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getTrustContactDetails" in {
        when(mockDesConnector.getTrustContactDetails(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getTrustContactDetails("404NotFound")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getLeadTrustee" in {
        when(mockDesConnector.getLeadTrustee(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getLeadTrustee("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getBeneficiaries" in {
        when(mockDesConnector.getBeneficiaries(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getBeneficiaries("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getProtectors" in {
        when(mockDesConnector.getProtectors(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getProtectors("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getEstate" in {
        when(mockDesConnector.getEstate(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getEstate("1234567890")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to closeEstate" in {
        when(mockDesConnector.closeEstate(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.closeEstate("404NotFound")(HeaderCarrier()), Duration.Inf)
        result mustBe NotFoundResponse
      }

      "a NotFoundResponse is returned from DES for the call to getTrust" in {
        when(mockDesConnector.getTrust(any())(any())).thenReturn(Future.successful(NotFoundResponse))

        val result = Await.result(SUT.getTrust("1234567890")(HeaderCarrier()), Duration.Inf)
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

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

package uk.gov.hmrc.estateapi.rest.controllers.services

import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.connectors.DesConnector
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustapi.rest.services.TrustExistenceService
import uk.gov.hmrc.trustregistration.ScalaDataExamples

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class TrustExistenceServiceSpec extends PlaySpec
  with MockitoSugar
  with OneAppPerSuite
  with ScalaDataExamples {

  "ReRegisterTrustService" must {
    "Return 'trust exisits" when {
      "Given a valid existing trust" in {

        when(mockDesConnector.trustExistenceLookUp(any())(any())).thenReturn(Future.successful(Right(testExistingTrust)))
        val result = Await.result(SUT.trustExistence(trustExistenceExample)(HeaderCarrier()), Duration.Inf)
        result mustBe Right(testExistingTrust)
      }
    }

    "Return 'trust not found" when {
      "a trust is not found from DES for the call to trustExistenceLookUp  when name missing" in {
        when(mockDesConnector.trustExistenceLookUp(any())(any())).thenReturn((Future.successful(Left(testNotFound))))
        val result = Await.result(SUT.trustExistence(trustExistenceExample)(HeaderCarrier()), Duration.Inf)
          result mustBe Left(testNotFound)
      }
    }

    "Return an Internal server error" when {
      "500 error return from DES" in {
        when(mockDesConnector.trustExistenceLookUp(any())(any())).thenReturn((Future.successful(Left("503"))))
        val result = Await.result(SUT.trustExistence(trustExistenceExample)(HeaderCarrier()), Duration.Inf)
        result mustBe Left(testInternalServerError)
      }
    }

    }

  val mockDesConnector = mock[DesConnector]
  object TestTrustExistenceService extends TrustExistenceService {
    override val desConnector: DesConnector = mockDesConnector
  }

  val testExistingTrust: String = "trusts exists"
  val testNotFound: String = "trust not found"
  val testInternalServerError: String = "503"
  val testInternalFailedError: String = "400"
  val SUT = TestTrustExistenceService

}

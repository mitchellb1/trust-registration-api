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

import org.mockito.Matchers.any
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.trustregistration.ScalaDataExamples
import uk.gov.hmrc.trustregistration.connectors.DesConnector
import org.mockito.Mockito.when
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.models.NotFoundResponse

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by matthew on 22/02/17.
  */
class ReRegisterTrustServiceSpec extends PlaySpec
  with MockitoSugar
  with OneAppPerSuite
  with ScalaDataExamples {

  "ReRegisterTrustService" must {
    "Return 'trust exisits" when {
      "Given a valid existing trust" in {

        when(mockDesConnector.lookUpExistingTrust(any())(any())).thenReturn(Future.successful(Right(testReRegister)))

        val result = Await.result(SUT.reRegisterTrust(reRegisterExample)(HeaderCarrier()), Duration.Inf)

        result mustBe Right(testReRegister)
      }
    }

    "Return a NotFoundResponse" when {
      "a NotFoundResponse is returned from DES for the call to lookUpExistingTrust when name missing" in {
        when(mockDesConnector.lookUpExistingTrust(any())(any())).thenReturn((Future.successful(Left(testNotFound))))

        val result = Await.result(SUT.reRegisterTrust(reRegisterExample)(HeaderCarrier()), Duration.Inf)

          result mustBe Left(testNotFound)
      }
    }
  }

  val mockDesConnector = mock[DesConnector]
  object TestReRegisterTrustService extends ReRegisterTrustService {
    override val desConnector: DesConnector = mockDesConnector
  }

  val testReRegister: String = "trusts exists"
  val testNotFound: String = "404"
  val SUT = TestReRegisterTrustService

}

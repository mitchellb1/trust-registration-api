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

package unit.config

import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.OneAppPerSuite
import uk.gov.hmrc.common.config.ServiceLocatorRegistration
import uk.gov.hmrc.common.connectors.ServiceLocatorConnector
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec

class RegisterInServiceLocatorSpec extends UnitSpec with MockitoSugar with OneAppPerSuite {

  trait Setup extends ServiceLocatorRegistration {
    val mockConnector = mock[ServiceLocatorConnector]
    override val slConnector = mockConnector
    override implicit val hc: HeaderCarrier = HeaderCarrier()
  }

  "onStart" should {
    "not register the microservice in service locator when registration is disabled" in new Setup {
      override val registrationEnabled: Boolean = false
      verify(mockConnector,never()).register(any())
    }
  }
}

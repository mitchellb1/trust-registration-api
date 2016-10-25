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

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.test.FakeRequest
import play.api.test.Helpers._


class SandboxControllerSpec extends PlaySpec with OneAppPerSuite {

  "SandboxController" should {
    "not return a NotFound response" when {
      "/no-change is accessed through the routes" in {
        val result = route(FakeRequest(PUT, "/sandbox/trusts-estates/trusts/1234567890/no-change"))
        status(result.get) must not be (NOT_FOUND)
      }
      "/register is accessed through the routes" in {
        val result = route(FakeRequest(POST, "/sandbox/trusts-estates/trusts/"))
        status(result.get) must not be (NOT_FOUND)
      }
    }
  }

}

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

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers._
import com.kenshoo.play.metrics.Metrics
import play.api.inject.bind
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsJson}

class SandboxControllerSpec extends PlaySpec
  with MockitoSugar
  with OneAppPerSuite {

//  implicit val system = ActorSystem("test")
//  implicit def mat: Materializer = ActorMaterializer()

  //  "SandboxController" should {
  //    "not return a NotFound response" when {
  //      "/no-change is accessed through the routes" in {
  //        val result = RegisterTrustSandboxController.noChange ("1234")(FakeRequest(PUT, "/sandbox/trusts/1234567890/no-change"))
  //        status(result) must not be (NOT_FOUND)
  //      }
  //      "/register is accessed through the routes" in {
  //        val result = RegisterTrustSandboxController.register()(FakeRequest(POST, "/sandbox/trusts/").withJsonBody(Json.parse("{}"))).run
  //        status(result) must not be (NOT_FOUND)
  //      }
  //    }
  //  }
}

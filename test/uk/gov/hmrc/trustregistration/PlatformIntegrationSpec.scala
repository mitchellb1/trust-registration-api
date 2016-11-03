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

package uk.gov.hmrc.trustregistration

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.Play
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.trustregistration.controllers.DocumentationController
import uk.gov.hmrc.trustregistration.utils.WiremockServiceLocatorSugar

class PlatformIntegrationSpec extends PlaySpec
  with MockitoSugar
  with ScalaFutures
  with WiremockServiceLocatorSugar
  with BeforeAndAfter
  with OneAppPerTest {

  implicit val system = ActorSystem("test")

  implicit def mat: Materializer = ActorMaterializer()

  lazy val documentationController = Play.current.injector.instanceOf[DocumentationController]

  before {
    startMockServer()
    stubRegisterEndpoint(200)
  }

  after {
    stopMockServer()
  }

  "microservice" must {
    /*
    "register itself to service-locator" when {
      "the application starts" in {

        resetAllRequests()
        verify(
          1,
          postRequestedFor(urlMatching("/registration"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(equalTo(regPayloadStringFor("trust-registration-api", "http://trust-registration-api.service")))
        )
      }
    }
    */

    "provide definition endpoint and documentation endpoint for each api" in {

      def normalizeEndpointName(endpointName: String): String = endpointName.replaceAll(" ", "-")

      def verifyDocumentationPresent(version: String, endpointName: String) {
        withClue(s"Getting documentation version '$version' of endpoint '$endpointName'") {
          val documentationResult = documentationController.documentation(version, endpointName)(FakeRequest())
          status(documentationResult) mustBe 200
        }
      }

      val result = documentationController.definition()(FakeRequest())
      status(result) mustBe 200

      val jsonResponse = contentAsJson(result)

      val versions: Seq[String] = (jsonResponse \\ "version") map (_.as[String])
      val endpointNames: Seq[Seq[String]] = (jsonResponse \\ "endpoints").map(_ \\ "endpointName").map(_.map(_.as[String]))

      versions.zip(endpointNames).flatMap {
        case (version, endpoint) => {
          endpoint.map(endpointName => (version, endpointName))
        }
      }.foreach {
        case (version, endpointName) => {
          verifyDocumentationPresent(version, endpointName)
        }
      }
    }

    "provide raml documentation" in {

      val result = documentationController.raml("1.0", "application.raml")(FakeRequest())

      status(result) mustBe 200
      contentAsString(result) must startWith("#%RAML 1.0")
    }
  }
}

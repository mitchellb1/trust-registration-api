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

import com.github.tomakehurst.wiremock.client.WireMock._
import controllers.DocumentationController
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import play.api.test.FakeRequest
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.trustregistration.utils.{MicroserviceLocalRunSugar, WiremockServiceLocatorSugar}


class PlatformIntegrationSpec extends UnitSpec with MockitoSugar with ScalaFutures with WiremockServiceLocatorSugar with BeforeAndAfter {

  before {
    startMockServer()
    stubRegisterEndpoint(200)
  }

  after {
    stopMockServer()
  }

  trait Setup {
    val documentationController = new DocumentationController {}
    val request = FakeRequest()
  }

  "microservice" should {

    /*
    "register itself to service-locator" in new MicroserviceLocalRunSugar with Setup {
      override val additionalConfiguration: Map[String, Any] = Map(
        "appName" -> "trust-registration-api",
        "appUrl" -> "http://trust-registration-api.service",
        "Test.microservice.services.service-locator.host" -> stubHost,
        "Test.microservice.services.service-locator.port" -> stubPort
      )
      run {
        () => {
          verify(
            1,
            postRequestedFor(urlMatching("/registration"))
            //.withHeader("content-type", equalTo("application/json"))
            //.withRequestBody(equalTo(regPayloadStringFor("trust-registration-api", "http://trust-registration.service"))))
          )
        }
      }
    }
    */

    "provide definition endpoint and documentation endpoint for each api" in new MicroserviceLocalRunSugar with Setup {
      override val additionalConfiguration: Map[String, Any] = Map(
        "Test.microservice.services.service-locator.host" -> stubHost,
        "Test.microservice.services.service-locator.port" -> stubPort
      )
      run {
        () => {
          def normalizeEndpointName(endpointName: String): String = endpointName.replaceAll(" ", "-")

          def verifyDocumentationPresent(version: String, endpointName: String) {
            withClue(s"Getting documentation version '$version' of endpoint '$endpointName'") {
              val documentationResult = documentationController.documentation(version, endpointName)(request)
              status(documentationResult) shouldBe 200
            }
          }

          val result = documentationController.definition()(request)
          status(result) shouldBe 200

          val jsonResponse = jsonBodyOf(result).futureValue

          val versions: Seq[String] = (jsonResponse \\ "version") map (_.as[String])
          val endpointNames: Seq[Seq[String]] = (jsonResponse \\ "endpoints").map(_ \\ "endpointName").map(_.map(_.as[String]))

          versions.zip(endpointNames).flatMap {
            case (version, endpoint) => {
              endpoint.map(endpointName => (version, endpointName))
            }
          }.foreach { case (version, endpointName) => {
            verifyDocumentationPresent(version, endpointName)
          } }
        }
      }
    }


    "provide raml documentation" in new MicroserviceLocalRunSugar with Setup {
      override val additionalConfiguration: Map[String, Any] = Map(
        "Test.microservice.services.service-locator.host" -> stubHost,
        "Test.microservice.services.service-locator.port" -> stubPort
      )
      run {
        () => {

          val result = documentationController.raml("1.0", "trusts-template.raml")(request)

          status(result) shouldBe 200
          bodyOf(result).futureValue should startWith("#%RAML 1.0")
        }
      }
    }
  }


}

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

package uk.gov.hmrc.apiplatform

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Play
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.common.connectors.ServiceLocatorConnector
import uk.gov.hmrc.common.rest.controllers.EstateDocumentationController
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpPost}
import uk.gov.hmrc.trustapi.rest.controllers.TrustDocumentationController

import scala.concurrent.Future


class PlatformIntegrationSpec extends PlaySpec
  with MockitoSugar
  with OneAppPerSuite {

  lazy val estateDocumentationController = Play.current.injector.instanceOf[EstateDocumentationController]
  lazy val trustDocumentationController = Play.current.injector.instanceOf[TrustDocumentationController]
  val mockHttp: HttpPost = mock[HttpPost]


  implicit val system = ActorSystem("test")

  implicit def mat: Materializer = ActorMaterializer()

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private object SUT extends ServiceLocatorConnector {
    override val http = mockHttp
    val appName: String = "test"
    val appUrl: String = "test"
    val handlerError: Throwable => Unit = (x) => ()
    val handlerOK: () => Unit = () => ()
    val metadata: Option[Map[String, String]] = Some(Map())
    val serviceUrl: String = "test"
  }

  "microservice" must {

    "register itself to service-locator" when {
      "the application starts" in {

        when(mockHttp.POST[String, String](any(), any(), any())(any(), any(), any()))
          .thenReturn(Future.successful(""))

        val captor = ArgumentCaptor.forClass(classOf[String])

        SUT.register

        verify(mockHttp, times(1)).POST(captor.capture, any(), any())(any(), any(), any())

        val url = captor.getValue
        url must endWith("registration")

      }
    }

    "provide definition endpoint and documentation endpoint for each Estates api" in {

      val result = estateDocumentationController.definition()(FakeRequest())
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
          verifyEstateDocumentationPresent(version, endpointName)
        }
      }
    }

    "provide raml documentation for Estates" in {

      val result = estateDocumentationController.raml("1.0", "application.raml")(FakeRequest())

      status(result) mustBe 200
      contentAsString(result) must startWith("#%RAML 1.0")
    }

    "provide definition endpoint and documentation endpoint for each Trust api" in {

      val result = trustDocumentationController.definition()(FakeRequest())
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
          verifyTrustDocumentationPresent(version, endpointName)
        }
      }
    }

    "provide raml documentation for Trusts" in {

      val result = trustDocumentationController.raml("1.0", "application.raml")(FakeRequest())

      status(result) mustBe 200
      contentAsString(result) must startWith("#%RAML 1.0")
    }
  }


  def verifyTrustDocumentationPresent(version: String, endpointName: String) {
    withClue(s"Getting documentation version '$version' of endpoint '$endpointName'") {
      val documentationResult = trustDocumentationController.documentation(version, endpointName)(FakeRequest())
      status(documentationResult) mustBe 200
    }
  }

  def verifyEstateDocumentationPresent(version: String, endpointName: String) {
    withClue(s"Getting documentation version '$version' of endpoint '$endpointName'") {
      val documentationResult = estateDocumentationController.documentation(version, endpointName)(FakeRequest())
      status(documentationResult) mustBe 200
    }
  }
}

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

package uk.gov.hmrc.trustregistration.connectors

import com.codahale.metrics
import com.codahale.metrics.Timer
import com.codahale.metrics.Timer.Context
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.models._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import org.mockito.Matchers
import org.mockito.Mockito._
import org.mockito.Matchers.any
import play.api.libs.json.Writes
import uk.gov.hmrc.trustregistration.TestMetrics
import uk.gov.hmrc.trustregistration.audit.TrustsAudit
import uk.gov.hmrc.trustregistration.metrics.Metrics


class DesConnectorSpec extends PlaySpec
  with OneAppPerSuite
  with MockitoSugar {

  implicit val hc = HeaderCarrier()

  "DesConnector" must {
    "return an identifier" when {
      "given a RegisterDocument" in {
        when (mockHttpPost.POST[RegistrationDocument,HttpResponse](Matchers.any(),Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
        thenReturn(Future.successful(HttpResponse(201)))
        val doc = RegistrationDocument("1234")
        val result = Await.result(SUT.registerTrust(doc),Duration.Inf)
        result mustBe Right(TRN("TRN-1234"))
      }
    }
    "return a service unavailable response" when {
      "DES returns a service unavailable response " in {
        when (mockHttpPost.POST[RegistrationDocument,HttpResponse](any[String](),any[RegistrationDocument](),any[Seq[(String,String)]]())
          (any[Writes[RegistrationDocument]](),any[HttpReads[HttpResponse]](),any[HeaderCarrier]())).
          thenReturn(Future.successful(HttpResponse(503)))
        val doc = RegistrationDocument("1234")
        val result = Await.result(SUT.registerTrust(doc),Duration.Inf)
        result mustBe Left("503")
      }
    }
    "return an exception" when {
      "Call to DES fails" in {
        when (mockHttpPost.POST[RegistrationDocument,HttpResponse](any[String](),any[RegistrationDocument](),any[Seq[(String,String)]]())
          (any[Writes[RegistrationDocument]](),any[HttpReads[HttpResponse]](),any[HeaderCarrier]())).
          thenReturn(Future.failed(Upstream4xxResponse("Bad request",400,400)))
        val doc = RegistrationDocument("")
        val result = Await.result(SUT.registerTrust(doc),Duration.Inf)
        result mustBe Left("400")
      }
    }
//no change tests
    "Return a BadRequestResponse" when {
      "a bad requested is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(400)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "Return a NotFoundResponse" when {
      "a 404 is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(404)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe NotFoundResponse
      }
    }

    "Return a InternalServerError" when {
      "a 500 is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(500)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    "Return a InternalServerError" when {
      "a 418 is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(418)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    "Return a SuccessResponse" when {
      "a 204 is returned from DES for the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(204)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe SuccessResponse
      }
    }

    "Return an InternalServerErrorResponse" when {
      "the call to DES fails on the call to no-change" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.failed(Upstream4xxResponse("Error", 418, 400)))
        val result = Await.result(SUT.noChange("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    //close trust tests
    "Return a BadRequestResponse" when {
      "a bad requested is returned from DES for the call to close-trust" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(400)))
        val result = Await.result(SUT.closeTrust("1234"),Duration.Inf)
        result mustBe BadRequestResponse
      }
    }

    "Return a NotFoundResponse" when {
      "a 404 is returned from DES for the call to close-trust" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(404)))
        val result = Await.result(SUT.closeTrust("1234"),Duration.Inf)
        result mustBe NotFoundResponse
      }
    }

    "Return a InternalServerError" when {
      "a 500 is returned from DES for the call to close-trust" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(500)))
        val result = Await.result(SUT.closeTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    "Return a InternalServerError" when {
      "a 418 is returned from DES for the call to close-trust" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(418)))
        val result = Await.result(SUT.closeTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }

    "Return a SuccessResponse" when {
      "a 204 is returned from DES for the call to close-trust" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.successful(HttpResponse(204)))
        val result = Await.result(SUT.closeTrust("1234"),Duration.Inf)
        result mustBe SuccessResponse
      }
    }

    "Return an InternalServerErrorResponse" when {
      "the call to DES fails on the call to close-trust" in {
        when (mockHttpPut.PUT[String,HttpResponse](Matchers.any(),Matchers.any())
          (Matchers.any(),Matchers.any(),Matchers.any())).
          thenReturn(Future.failed(Upstream4xxResponse("Error", 418, 400)))
        val result = Await.result(SUT.closeTrust("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse
      }
    }
  }

  val mockHttpPost = mock[HttpPost]
  val mockHttpPut = mock[HttpPut]
  object DesConnector extends DesConnector {
    override val httpPost: HttpPost = mockHttpPost
    override val httpPut: HttpPut = mockHttpPut
    override val audit: TrustsAudit = new TrustsAudit {
      override def doAudit(eventTypelMessage: String, auditTag: String)(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Unit = ()
    }
    override val metrics: Metrics = TestMetrics
  }
  lazy val SUT = DesConnector


}

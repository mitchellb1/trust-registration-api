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

package uk.gov.hmrc.trustregistration.connectors.DES

import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.common.rest.resources.core.{BadRequestResponse, GetSuccessResponse, InternalServerErrorResponse, NotFoundResponse}
import uk.gov.hmrc.play.http.HttpResponse
import uk.gov.hmrc.trustapi.rest.resources.core.Protectors
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class GetProtectorsSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks with BeforeAndAfter with JsonExamples with ScalaDataExamples {

  val auditSuccessMessage = "Successful"
  val auditFailureMessage = "Failure"

  before {
    reset(mockAudit) // resets mock audit before each test to ensure the verify(mockAudit, times(1)) test is accurate
  }

  "Get Protectors endpoint" must {
    "return a GetSuccessResponse with a populated Protectors object" when {
      "DES returns a 200 with a valid Protectors json response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(validProtectorsJson)))))
        val result = Await.result(SUT.getProtectors("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(protectors)

        verify(mockAudit, times(1)).doAudit(auditSuccessMessage, SUT.AuditGetProtectorsIdentifier)
      }

      "DES returns a 200 with empty Json data" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("{}")))))
        val result = Await.result(SUT.getProtectors("12345"),Duration.Inf)
        result mustBe GetSuccessResponse(Protectors(None, None))

        verify(mockAudit, times(1)).doAudit(auditSuccessMessage, SUT.AuditGetProtectorsIdentifier)
      }
    }

    "return a BadRequestResponse" when {
      "DES returns a 400 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(400, None)))
        val result = Await.result(SUT.getProtectors("1234"),Duration.Inf)
        result mustBe BadRequestResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetProtectorsIdentifier)
      }
    }

    "return a NotFoundResponse" when {
      "DES returns a 404 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(404, None)))
        val result = Await.result(SUT.getProtectors("1234"),Duration.Inf)
        result mustBe NotFoundResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetProtectorsIdentifier)
      }
    }

    "return a InternalServerErrorResponse" when {
      "DES returns a 200 with no Json data" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, None)))
        val result = Await.result(SUT.getProtectors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetProtectorsIdentifier)
      }

      "DES returns a 200 with invalid Json data" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(invalidProtectorsJson)))))
        val result = Await.result(SUT.getProtectors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetProtectorsIdentifier)
      }

      "DES returns a 500" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(500, None)))
        val result = Await.result(SUT.getProtectors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetProtectorsIdentifier)
      }

      "DES returns any other response (i.e. 418)" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(418, None)))
        val result = Await.result(SUT.getProtectors("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetProtectorsIdentifier)
      }
    }
  }
}

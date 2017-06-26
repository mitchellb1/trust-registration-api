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

package uk.gov.hmrc.connectors

import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.models.{BadRequestResponse, GetSuccessResponse, InternalServerErrorResponse, NotFoundResponse}
import uk.gov.hmrc.play.http.HttpResponse
import uk.gov.hmrc.utils.{JsonExamples, ScalaDataExamples}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class GetLeadTrusteeSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks with BeforeAndAfter with JsonExamples with ScalaDataExamples {

  val auditSuccessMessage = "Successful"
  val auditFailureMessage = "Failure"

  before {
    reset(mockAudit) // resets mock audit before each test to ensure the verify(mockAudit, times(1)) test is accurate
  }

  "Get Lead Trustee endpoint" must {
    "return a GetSuccessResponse with a populated Lead Trustee object" when {
      "DES returns a 200 with a valid Lead Trustee Individual json response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(validLeadTrusteeIndividualJson)))))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(leadTrusteeIndividual)

        verify(mockAudit, times(1)).doAudit(auditSuccessMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }

      "DES returns a 200 with a valid Lead Trustee Company json response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(validLeadTrusteeCompanyJson)))))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(leadTrusteeCompany)

        verify(mockAudit, times(1)).doAudit(auditSuccessMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }
    }

    "return a BadRequestResponse" when {
      "DES returns a 400 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(400, None)))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe BadRequestResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }
    }

    "return a NotFoundResponse" when {
      "DES returns a 404 response" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(404, None)))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe NotFoundResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }
    }

    "return a InternalServerErrorResponse" when {
      "DES returns a 200 with no Json data" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, None)))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }

      "DES returns a 200 with empty Json data" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("{}")))))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }

      "DES returns a 200 with invalid Json data" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse(invalidLeadTrusteeJson)))))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }

      "DES returns a 500" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(500, None)))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }

      "DES returns any other response (i.e. 418)" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(418, None)))
        val result = Await.result(SUT.getLeadTrustee("1234"),Duration.Inf)
        result mustBe InternalServerErrorResponse

        verify(mockAudit, times(1)).doAudit(auditFailureMessage, SUT.AuditGetLeadTrusteeIdentifier)
      }
    }
  }
}

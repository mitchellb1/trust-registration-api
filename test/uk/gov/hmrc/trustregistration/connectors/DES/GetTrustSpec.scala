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

package uk.gov.hmrc.trustregistration.connectors.DES

import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.play.http.HttpResponse
import uk.gov.hmrc.trustregistration.models.GetSuccessResponse
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class GetTrustSpec extends PlaySpec
  with OneAppPerSuite
  with DESConnectorMocks
  with BeforeAndAfter
  with JsonExamples
  with ScalaDataExamples {
  "Get Trust endpoint" must {
    "return a GetSuccessResponse with a populated Trust object" when {
      "DES returns a 200 response with a Trust JSON object that contains all required fields" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(validTrustJson)))))


        val result = Await.result(SUT.getTrust("12314"),Duration.Inf)

        result mustBe GetSuccessResponse(trust)
      }
    }
  }
}

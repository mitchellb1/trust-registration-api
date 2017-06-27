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

package uk.gov.hmrc.services

import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Request
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}
import uk.gov.hmrc.metrics.ApplicationMetrics
import uk.gov.hmrc.play.http.HeaderCarrier


trait RegisterTrustServiceMocks extends MockitoSugar {

   val badRegDocPayload = Json.obj(
    "wrongIdentifier" -> "Trust Name"
  )

   val mockRegisterTrustService = mock[RegisterTrustService]
   val mockTrustExistenceService = mock[TrustExistenceService]
   val mockHC = mock[HeaderCarrier]
   val mockMetrics = mock[ApplicationMetrics]
   val mockContext = new com.codahale.metrics.Timer().time()

  when (mockMetrics.startDesConnectorTimer(any())).thenReturn(mockContext)

   def registerRequestWithPayload(payload: JsValue): Request[JsValue] = FakeRequest(
    "POST",
    "",
    FakeHeaders(),
    payload
  ).withHeaders(CONTENT_TYPE -> "application/json")
}

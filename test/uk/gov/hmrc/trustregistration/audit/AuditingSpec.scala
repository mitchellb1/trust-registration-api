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

package uk.gov.hmrc.trustregistration.audit

import org.mockito.Mockito._
import org.mockito.{ArgumentCaptor, Matchers}
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector, AuditResult}
import uk.gov.hmrc.play.audit.model.DataEvent
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.play.http.logging.Authorization

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuditingSpec extends PlaySpec
  with OneAppPerTest
  with MockitoSugar {

  val ValidNino = "AA111111A"
  val InvalidNino = "AA111111D"
  val ValidYear = "2014"

  "doAudit" must {
    "generate a data event" when {
      val mockAuditConnector = mock[AuditConnector]
      object TestAudit extends TrustsAudit {
        override val auditConnector = mockAuditConnector
      }

      "a noContent call is successfully made" in {
        val token = Token("tokenstring")
        val authorisationVal = Authorization("sdfgsdfghdsdhf")
        val taxYear = "2015-2016"
        implicit val headerCarrier = HeaderCarrier(userId = None, token = Some(token), deviceID = Some("aDeviceid*****"),
          authorization = Some(authorisationVal), trueClientIp = Some("192.168.2.2"))

        val dataEventArgumentCaptor = ArgumentCaptor.forClass(classOf[DataEvent])

        when(mockAuditConnector.sendEvent(Matchers.any[DataEvent])(Matchers.any(), Matchers.any())).thenReturn(Future.successful(AuditResult.Success))
        TestAudit.doAudit("noChangeTrustSuccessful", AuditNoChangeIdentifier)

        verify(mockAuditConnector).sendEvent(dataEventArgumentCaptor.capture())(Matchers.any(), Matchers.any())

        dataEventArgumentCaptor.getValue.auditSource mustBe "trust-registration-api"
        dataEventArgumentCaptor.getValue.auditType mustBe "noChangeTrustSuccessful"
        dataEventArgumentCaptor.getValue.tags mustBe Map("clientIP" -> "192.168.2.2",
          "path" -> "N/A",
          "X-Session-ID" -> "-",
          "Akamai-Reputation" -> "-",
          "X-Request-ID" -> "-",
          "clientPort" -> "-",
          "transactionName" -> "trustRegistration_noAnnualChangeTrust"
        )
        dataEventArgumentCaptor.getValue.detail mustBe Map(
          "deviceID" -> "aDeviceid*****",
          "ipAddress" -> "192.168.2.2",
          "token" -> "tokenstring",
          "Authorization" -> "sdfgsdfghdsdhf")
      }
    }
  }

  val AuditNoChangeIdentifier: String = "trustRegistration_noAnnualChangeTrust"
  val AuditCloseTrustIdentifier: String = "trustRegistration_CloseTrust"
}



/*
Example full content of EventDetail
[DataEvent( c2ni,
            liabilityPeriodUpdateSuccessful,
            81155667-4366-4e40-8271-3a1b768ad546,
            Map(  clientIP -> -,
                  path -> N/A,
                  X-Session-ID -> -,
                  X-Request-ID -> -,
                  clientPort -> -,
                  transactionName -> c2ni
            ),
            Map(  nino -> AA111111A,
                endsOn -> 2016-07-05,
                tax_Year -> 2014,
                startsOn -> 2015-04-06,
                deviceID -> -,
                ipAddress -> -,
                token -> -,
                Authorization -> -
            ),
            2016-03-14T12:59:47.395Z
          )
*/

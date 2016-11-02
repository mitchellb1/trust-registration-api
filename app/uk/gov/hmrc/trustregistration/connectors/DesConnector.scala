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

import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.trustregistration.audit.TrustsAudit
import uk.gov.hmrc.trustregistration.config.WSHttp
import uk.gov.hmrc.trustregistration.metrics.{TrustMetrics}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DesConnector extends ServicesConfig with RawResponseReads {
  val httpPost: HttpPost = WSHttp
  val httpPut: HttpPut = WSHttp

  val audit: TrustsAudit
  val metrics: TrustMetrics

  val AuditNoChangeIdentifier: String = "trustRegistration_noAnnualChangeTrust"
  val AuditCloseTrustIdentifier: String = "trustRegistration_CloseTrust"

  lazy val desUrl = baseUrl("des")
  lazy val serviceUrl = s"$desUrl/trust-registration-stub/trusts"

  def registerTrust(doc: RegistrationDocument)(implicit hc : HeaderCarrier) = {

    val uri: String = s"$serviceUrl/register"

    val result: Future[HttpResponse] = httpPost.POST[RegistrationDocument,HttpResponse](uri,doc)(implicitly, httpReads, implicitly)

    result.map(f=> {
      f.status match{
        case 201 => Right(TRN("TRN-1234"))
        case _ => Left("503")
      }
    }).recover({
      case _ => Left("400")
    })
  }

  def noChange(identifier: String)(implicit hc : HeaderCarrier): Future[TrustResponse] = {
    val uri: String = s"$serviceUrl/$identifier/no-change"

    val timerStart = metrics.startDesConnectorTimer("no-change")

    val result: Future[HttpResponse] = httpPut.PUT[String, HttpResponse](uri, identifier)(implicitly, httpReads, implicitly)

    result.map(f=> {
      timerStart.stop()
      f.status match {
        case 204 => {
          audit.doAudit("noChangeTrustSuccessful", AuditNoChangeIdentifier)
          SuccessResponse
        }
        case 400 => {
          audit.doAudit("noChangeTrustFailure", AuditNoChangeIdentifier)
          BadRequestResponse
        }
        case 404 => {
          audit.doAudit("noChangeTrustFailure", AuditNoChangeIdentifier)
          NotFoundResponse
        }
        case _ => {
          audit.doAudit("noChangeTrustFailure", AuditNoChangeIdentifier)
          InternalServerErrorResponse
        }
      }
    }).recover {
      case _ => {
        audit.doAudit("noChangeTrustFailure", AuditNoChangeIdentifier)
        InternalServerErrorResponse
      }
    }
  }

  def closeTrust(identifier: String)(implicit hc : HeaderCarrier): Future[TrustResponse] = {
    val uri: String = s"$serviceUrl/$identifier/closeTrust"

    val timerStart = metrics.startDesConnectorTimer("closeTrust")

    val result: Future[HttpResponse] = httpPut.PUT[String, HttpResponse](uri, identifier)(implicitly, httpReads, implicitly)

    result.map(f=> {
      timerStart.stop()
      f.status match {
        case 204 => {
          audit.doAudit("closeTrustSuccessful", AuditCloseTrustIdentifier)
          SuccessResponse
        }
        case 400 => {
          audit.doAudit("closeTrustFailure", AuditCloseTrustIdentifier)
          BadRequestResponse
        }
        case 404 => {
          audit.doAudit("closeTrustFailure", AuditCloseTrustIdentifier)
          NotFoundResponse
        }
        case _ => {
          audit.doAudit("closeTrustFailure", AuditCloseTrustIdentifier)
          InternalServerErrorResponse
        }
      }
    }).recover {
      case _ => {
        audit.doAudit("ncloseTrustFailure", AuditCloseTrustIdentifier)
        InternalServerErrorResponse
      }
    }
  }
}

object DesConnector extends DesConnector {
  override val audit: TrustsAudit = TrustsAudit
  override val metrics: TrustMetrics = TrustMetrics
}

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

import play.api.libs.json.JsValue
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.trustregistration.audit.TrustsAudit
import uk.gov.hmrc.trustregistration.config.WSHttp
import uk.gov.hmrc.trustregistration.metrics.Metrics

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DesConnector extends ServicesConfig with RawResponseReads {
  val httpPost: HttpPost = WSHttp
  val httpPut: HttpPut = WSHttp
  val httpGet: HttpGet = WSHttp

  val audit: TrustsAudit = TrustsAudit
  val AuditNoChangeIdentifier: String = "trustRegistration_noAnnualChangeTrust"
  val AuditCloseTrustIdentifier: String = "trustRegistration_closeTrust"
  val AuditGetTrusteesIdentifier: String = "trustRegistration_getTrustees"

  val metrics : Metrics

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
        audit.doAudit("closeTrustFailure", AuditCloseTrustIdentifier)
        InternalServerErrorResponse
      }
    }
  }

  def getTrustees(identifier: String)(implicit hc : HeaderCarrier): Future[TrustResponse] = {

    val uri: String = s"$serviceUrl/$identifier/trustees"

    val timerStart = metrics.startDesConnectorTimer("closeTrust")

    val result: Future[HttpResponse] = httpGet.GET[HttpResponse](uri)(httpReads, implicitly)

    result.map(f => {
      timerStart.stop()
      f.status match {
        case 200 => {
          val trustees = f.json.asOpt[List[Individual]]

          trustees match {
            case Some(value: List[Individual]) => {
              audit.doAudit("getTrusteesSuccessful", AuditGetTrusteesIdentifier)
              GetSuccessResponse(value)
            }
            case _ => {
              audit.doAudit("getTrusteesFailure", AuditGetTrusteesIdentifier)
              InternalServerErrorResponse
            }
          }
        }
        case 400 => {
          audit.doAudit("getTrusteesFailure", AuditGetTrusteesIdentifier)
          BadRequestResponse
        }
        case 404 => {
          audit.doAudit("getTrusteesFailure", AuditGetTrusteesIdentifier)
          NotFoundResponse
        }
        case _ => {
          audit.doAudit("getTrusteesFailure", AuditGetTrusteesIdentifier)
          InternalServerErrorResponse
        }
      }
    }).recover {
      case _ => {
        audit.doAudit("getTrusteesFailure", AuditGetTrusteesIdentifier)
        InternalServerErrorResponse
      }
    }
  }

}

object DesConnector extends DesConnector {
  override val audit: TrustsAudit = TrustsAudit
  override val metrics: Metrics = Metrics
}

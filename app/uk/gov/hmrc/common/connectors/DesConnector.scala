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

package uk.gov.hmrc.common.connectors

import com.codahale.metrics.Timer
import play.api.libs.json.Reads
import uk.gov.hmrc.common.audit.Auditor
import uk.gov.hmrc.common.config.WSHttp
import uk.gov.hmrc.common.metrics.ApplicationMetrics
import uk.gov.hmrc.common.rest.resources.core._
import uk.gov.hmrc.estateapi.rest.resources.core.Estate
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustapi.rest.resources.core._
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.Beneficiaries

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DesConnector extends ServicesConfig with RawResponseReads {
  val httpPost: HttpPost = WSHttp
  val httpPut: HttpPut = WSHttp
  val httpGet: HttpGet = WSHttp

  val audit: Auditor
  val metrics: ApplicationMetrics

  val AuditNoChangeIdentifier: String = "trustRegistration_noAnnualChangeTrust"
  val AuditCloseTrustIdentifier: String = "trustRegistration_closeTrust"
  val AuditCloseEstateIdentifier: String = "trustRegistration_closeEstate"
  val AuditGetTrusteesIdentifier: String = "trustRegistration_getTrustees"
  val AuditGetEstateIdentifier: String = "trustRegistration_getEstate"
  val AuditGetSettlorsIdentifier: String = "trustRegistration_getSettlors"
  val AuditGetNaturalPersonsIdentifier: String = "trustRegistration_getNaturalPersons"
  val AuditGetTrustContactDetailsIdentifier: String = "trustRegistration_getTrustContactDetails"
  val AuditGetLeadTrusteeIdentifier: String = "trustRegistration_getLeadTrustee"
  val AuditGetBeneficiariesIdentifier: String = "trustRegistration_getBeneficiaries"
  val AuditGetProtectorsIdentifier: String = "trustRegistration_getProtectors"
  val AuditGetTrustIdentifier: String = "trustRegistration_getTrust"

  lazy val desUrl = baseUrl("des")
  lazy val trustsServiceUrl = s"$desUrl/trust-registration-stub/trusts"
  lazy val estatesServiceUrl = s"$desUrl/trust-registration-stub/estates"


  def getTrust(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier"

    respond[Trust](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetTrustIdentifier,
      timer = metrics.startDesConnectorTimer("getTrust"))
  }


  def getLeadTrustee(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/leadTrustee"

    respond[LeadTrustee](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetLeadTrusteeIdentifier,
      timer = metrics.startDesConnectorTimer("getLeadTrustee"))
  }

  def getBeneficiaries(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/beneficiaries"

    respond[Beneficiaries](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetBeneficiariesIdentifier,
      timer = metrics.startDesConnectorTimer("getBeneficiaries"))
  }

  def getEstate(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$estatesServiceUrl/$identifier"

    respond[Estate](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetEstateIdentifier,
      timer = metrics.startDesConnectorTimer("getEstate"))
  }

  def getTrustees(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/trustees"

    respond[List[Individual]](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetTrusteesIdentifier,
      timer = metrics.startDesConnectorTimer("getTrustees"))
  }

  def getSettlors(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/settlors"

    respond[Settlors](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetSettlorsIdentifier,
      timer = metrics.startDesConnectorTimer("getSettlors"))
  }

  def getNaturalPersons(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/naturalPersons"

    respond[List[Individual]](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetNaturalPersonsIdentifier,
      timer = metrics.startDesConnectorTimer("getNaturalPersons"))
  }

  def getTrustContactDetails(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/contactDetails"

    respond[TrustContactDetails](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetTrustContactDetailsIdentifier,
      timer = metrics.startDesConnectorTimer("getTrustContactDetails"))
  }

  def getProtectors(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/protectors"

    respond[Protectors](
      desResponse = httpGet.GET[HttpResponse](uri)(httpReads, implicitly),
      auditIdentifier = AuditGetProtectorsIdentifier,
      timer = metrics.startDesConnectorTimer("getProtectors"))
  }


  def trustExistenceLookUp(trustExistence: TrustExistence)(implicit hc : HeaderCarrier) = {
    val uri: String = s"$trustsServiceUrl/trustExistence"
    val desRespone = httpPost.POST[TrustExistence,HttpResponse](uri,trustExistence)(implicitly, httpReads, implicitly)

    desRespone.map(f => {
      f.status match {
        case 204 => Right("204")
        case 404 => Left("404")
        case 409 => Left("409")
        case _ => Left("503")
      }
    }).recover({
      case _ => Left("400")
    })
  }

  def registerTrust(trust: Trust)(implicit hc : HeaderCarrier) = {
    val uri: String = s"$trustsServiceUrl/register"

    getRegisterResponse(httpPost.POST[Trust,HttpResponse](uri,trust)(implicitly, httpReads, implicitly))
  }

  def registerEstate(estate: Estate)(implicit hc : HeaderCarrier) = {
    val uri: String = s"$estatesServiceUrl/register"

    getRegisterResponse(httpPost.POST[Estate,HttpResponse](uri,estate)(implicitly, httpReads, implicitly))
  }

  def closeTrust(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/closeTrust"

    respondNoContent(
      desResponse = httpPut.PUT[String, HttpResponse](uri, identifier)(implicitly, httpReads, implicitly),
      auditIdentifier = AuditCloseTrustIdentifier,
      timer = metrics.startDesConnectorTimer("closeTrust"))
  }

  def closeEstate(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$estatesServiceUrl/$identifier/closeEstate"

    respondNoContent(
      desResponse = httpPut.PUT[String, HttpResponse](uri, identifier)(implicitly, httpReads, implicitly),
      auditIdentifier = AuditCloseEstateIdentifier,
      timer = metrics.startDesConnectorTimer("closeEstate"))
  }

  def noChange(identifier: String)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    val uri: String = s"$trustsServiceUrl/$identifier/no-change"

    respondNoContent(
      desResponse = httpPut.PUT[String, HttpResponse](uri, identifier)(implicitly, httpReads, implicitly),
      auditIdentifier = AuditGetTrustContactDetailsIdentifier,
      timer = metrics.startDesConnectorTimer("noChange"))
  }

  private def getRegisterResponse(result: Future[HttpResponse]): Future[Either[String, TRN] with Product with Serializable] = {
    result.map(f => {
      f.status match {
        case 201 => Right(TRN("TRN-1234"))
        case _ => Left("503")
      }
    }).recover({
      case _ => Left("400")
    })
  }

  private def respondNoContent(desResponse: Future[HttpResponse], auditIdentifier: String, timer:Timer.Context)(implicit hc : HeaderCarrier): Future[ApplicationResponse] = {
    desResponse.map(f => {
      timer.stop()
      f.status match {
        case 204 => {
          audit.doAudit("Successful", auditIdentifier)
          SuccessResponse
        }
        case 400 => {
          audit.doAudit("Failure", auditIdentifier)
          BadRequestResponse
        }
        case 404 => {
          audit.doAudit("Failure", auditIdentifier)
          NotFoundResponse
        }
      }
    }).recover {
      case _ => {
        audit.doAudit("Failure", auditIdentifier)
        InternalServerErrorResponse
      }
    }
  }

  private def respond[T](desResponse: Future[HttpResponse], auditIdentifier: String, timer:Timer.Context)(implicit hc : HeaderCarrier, tjs: Reads[T]): Future[ApplicationResponse] = {
    desResponse.map(f => {
      timer.stop()
      f.status match {
        case 200 => {
          val details = f.json.asOpt[T]
          details match {
            case Some(value: T) => {
              audit.doAudit("Successful", auditIdentifier)
              GetSuccessResponse(value)
            }
            case _ => {
              audit.doAudit("Failure", auditIdentifier)
              InternalServerErrorResponse
            }
          }
        }
        case 400 => {
          audit.doAudit("Failure", auditIdentifier)
          BadRequestResponse
        }
        case 404 => {
          audit.doAudit("Failure", auditIdentifier)
          NotFoundResponse
        }
      }
    }).recover {
      case _ => {
        audit.doAudit("Failure", auditIdentifier)
        InternalServerErrorResponse
      }
    }
  }

}

object DesConnector extends DesConnector {
  override val audit: Auditor = Auditor
  override val metrics: ApplicationMetrics = ApplicationMetrics
}

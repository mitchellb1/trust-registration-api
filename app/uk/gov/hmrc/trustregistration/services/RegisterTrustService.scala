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

package uk.gov.hmrc.trustregistration.services

import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.connectors.DesConnector
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.models.estates.{Estate, EstateRegistrationDocument}

import scala.concurrent.Future

trait RegisterTrustService {

  val desConnector: DesConnector

  def registerTrust(regDoc: Trust)(implicit hc : HeaderCarrier) : Future[Either[String,TRN]] = {
    desConnector.registerTrust(regDoc)(hc)
  }

  def registerEstate(estate: Estate)(implicit hc : HeaderCarrier) : Future[Either[String,TRN]] = {
    desConnector.registerEstate(estate)(hc)
  }

  def noChange(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
     desConnector.noChange(identifier)
  }

  def closeTrust(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.closeTrust(identifier)
  }

  def getTrustees(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getTrustees(identifier)
  }

  def getSettlors(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getSettlors(identifier)
  }

  def getNaturalPersons(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getNaturalPersons(identifier)
  }

  def getTrustContactDetails(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getTrustContactDetails(identifier)
  }

  def getLeadTrustee(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getLeadTrustee(identifier)
  }

  def getBeneficiaries(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getBeneficiaries(identifier)
  }

  def getProtectors(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getProtectors(identifier)
  }

  def getEstate(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getEstate(identifier)
  }

  def closeEstate(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.closeEstate(identifier)
  }

  def getTrust(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getTrust(identifier)
  }
}

object RegisterTrustService extends RegisterTrustService {
  override val desConnector: DesConnector = DesConnector
}

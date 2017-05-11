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
import uk.gov.hmrc.trustregistration.models.estates.Estate

import scala.concurrent.Future

trait RegisterEstateService {

  val desConnector: DesConnector

  def registerEstate(estate: Estate)(implicit hc : HeaderCarrier) : Future[Either[String,TRN]] = {
    desConnector.registerEstate(estate)(hc)
  }

  def noChange(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
     desConnector.noChange(identifier)
  }


  def getEstate(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.getEstate(identifier)
  }

  def closeEstate(identifier: String)(implicit hc: HeaderCarrier): Future[ApplicationResponse] = {
    desConnector.closeEstate(identifier)
  }

}

object RegisterEstateService extends RegisterEstateService {
  override val desConnector: DesConnector = DesConnector
}

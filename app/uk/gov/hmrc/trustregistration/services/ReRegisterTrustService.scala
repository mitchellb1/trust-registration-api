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
import uk.gov.hmrc.trustregistration.models.ReRegister

import scala.concurrent.Future

/**
  * Created by matthew on 22/02/17.
  */
trait ReRegisterTrustService {

  val desConnector: DesConnector

  def reRegisterTrust(regDoc: ReRegister)(implicit hc: HeaderCarrier): Future[Either[String, String]] = {
    desConnector.lookUpExistingTrust(regDoc)(hc)
  }

  object RegisterTrustService extends RegisterTrustService {
    override val desConnector: DesConnector = DesConnector
  }

}

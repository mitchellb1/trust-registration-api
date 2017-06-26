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

import uk.gov.hmrc.connectors.DesConnector
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.models.TrustExistence

import scala.concurrent.Future


trait TrustExistenceService {

  val desConnector: DesConnector

  def trustExistence(regDoc: TrustExistence)(implicit hc: HeaderCarrier): Future[Either[String, String]] = {
    desConnector.trustExistenceLookUp(regDoc)(hc)
  }

}

object TrustExistenceService extends TrustExistenceService {
  override val desConnector: DesConnector = DesConnector
}
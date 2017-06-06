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

package uk.gov.hmrc.trustapi.mapping.todes

import uk.gov.hmrc.common.des.{DesNonUkResidentialStatus, DesResidentialStatus, DesUkResidentialStatus}
import uk.gov.hmrc.trustapi.rest.resources.core.Trust


object DesResidentialStatusMapper {

  def toDes(domainTrust: Trust): Option[DesResidentialStatus] = {
    domainTrust.isTrustUkResident match {
      case true => {
        val ukres: DesUkResidentialStatus = DesUkResidentialStatus(domainTrust.legality.isEstablishedUnderScottishLaw,
          domainTrust.legality.previousOffshoreCountryCode)
        Some(DesResidentialStatus(Some(ukres), None))
      }
      case false => {//TODO SCHEMA CHANGE : Missing required elements for non uk residents Check Bussiness Rules
        val nonUKres: DesNonUkResidentialStatus = DesNonUkResidentialStatus(true, Some(true),Some(false),None)
        Some(DesResidentialStatus(None, Some(nonUKres)))
      }
    }
  }
}

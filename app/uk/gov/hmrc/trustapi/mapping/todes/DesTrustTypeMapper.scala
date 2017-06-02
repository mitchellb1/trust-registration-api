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

import uk.gov.hmrc.common.des.MissingPropertyException
import uk.gov.hmrc.trustapi.rest.resources.core.Trust

object DesTrustTypeMapper {
  def toDes(domainTrust: Trust): String = {

    domainTrust.trustType.employmentTrust.map(emp => "Employment Related")
      .orElse(domainTrust.trustType.flatManagementSinkingFundTrust.map(fmc => "Flat Management Company or Sinking Fund"))
      .orElse(domainTrust.trustType.heritageMaintenanceFundTrust.map(hm => "Heritage Maintenance Fund"))
      .orElse(domainTrust.trustType.interVivoTrust.map(iv => {
        iv.dovType.map(dovtypeExists => "Deed of Variation Trust or Family Arrangement")
          .orElse(Some("Inter vivos Settlement")).get
      }))
      .orElse(domainTrust.trustType.willIntestacyTrust.map(wi => {
        if (wi.isDovTypeAddition) "Deed of Variation Trust or Family Arrangement" else "Will Trust or Intestacy Trust"
      }))
      .getOrElse(throw new MissingPropertyException("Cannot find trust type"))
  }
}

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

package uk.gov.hmrc.estateapi.mapping

import uk.gov.hmrc.common.des.DesCorrespondence
import uk.gov.hmrc.common.mapping.AddressMapper
import uk.gov.hmrc.estateapi.rest.resources.core.Estate
import uk.gov.hmrc.trustapi.rest.resources.core.Trust


object DesCorrespondenceMapper {

  def toDes(estate: Estate): DesCorrespondence = {
    DesCorrespondence(
      abroadIndicator = !(estate.correspondenceAddress.countryCode.equals("GB")),
      name = estate.estateName,
      address = AddressMapper.toDes(estate.correspondenceAddress),
      phoneNumber = estate.telephoneNumber
    )
  }

  def toDes(trust: Trust): DesCorrespondence = {
    DesCorrespondence(
      abroadIndicator = !(trust.correspondenceAddress.countryCode.equals("GB")),
      name = trust.name,
      address = AddressMapper.toDes(trust.correspondenceAddress),
      phoneNumber = trust.telephoneNumber
    )
  }
}

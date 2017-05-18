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

package uk.gov.hmrc.common.mapping

import uk.gov.hmrc.common.des.DesCorrespondence
import uk.gov.hmrc.estateapi.rest.resources.core.Estate


trait DesCorrespondenceMap {

  def toDes(estate: Estate): DesCorrespondence = {
    if (estate.correspondenceAddress.countryCode.equals("GB")) {
      DesCorrespondence(
        abroadIndicator = true,
        name = estate.estateName,
        address = AddressMap.toDes(estate.correspondenceAddress),
        phoneNumber = estate.telephoneNumber
      )
    } else {
      DesCorrespondence(
        abroadIndicator = false,
        name = estate.estateName,
        address = AddressMap.toDes(estate.correspondenceAddress),
        phoneNumber = estate.telephoneNumber
      )
    }
  }
}

object DesCorrespondenceMap extends DesCorrespondenceMap

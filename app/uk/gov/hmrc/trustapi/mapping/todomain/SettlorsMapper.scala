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

package uk.gov.hmrc.trustapi.mapping.todomain

import uk.gov.hmrc.common.des.DesSettlorType
import uk.gov.hmrc.common.mapping.todomain.{CompanyMapper, IndividualMapper}
import uk.gov.hmrc.trustapi.rest.resources.core.Settlors


object SettlorsMapper {
  def toDomain(settlors: DesSettlorType) : Settlors = {
    Settlors(settlors.settlor.map(ls=>ls.map(s=>IndividualMapper.toDomain(s.name,s.dateOfBirth,None,Some(s.identification)))), //TODO: Missing phone number mapping
      settlors.settlorCompany.map(lsc=>lsc.map(c=>CompanyMapper.toDomain(c))))
  }
}

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

import uk.gov.hmrc.common.des.{DesLeadTrustee, DesLeadTrusteeInd, DesLeadTrusteeOrg}
import uk.gov.hmrc.common.mapping.todomain.{CompanyMapper, IndividualMapper}
import uk.gov.hmrc.trustapi.rest.resources.core.LeadTrustee


object LeadTrusteeMapper {
  def toDomain(leadTrustee: DesLeadTrustee, telephoneNumber: String, email: String) : LeadTrustee = {
    leadTrustee match {
      case individual: DesLeadTrusteeInd => {
        LeadTrustee(Some(IndividualMapper.toDomain(individual.name,
          individual.dateOfBirth,Some(individual.phoneNumber),
          Some(individual.identification))),
          telephoneNumber = telephoneNumber,
          email = email)
      }
      case company: DesLeadTrusteeOrg => {
        LeadTrustee(company =  Some(CompanyMapper.toDomain(company)),
          telephoneNumber = telephoneNumber,
          email = email)
      }
    }
  }
}

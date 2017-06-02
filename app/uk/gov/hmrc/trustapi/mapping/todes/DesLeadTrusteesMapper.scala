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

import uk.gov.hmrc.common.des._
import uk.gov.hmrc.common.mapping.todes.{DesIdentificationMapper, DesNameMapper, DesOrgIdentificationMapper}
import uk.gov.hmrc.common.rest.resources.core.{Company, Individual}
import uk.gov.hmrc.trustapi.rest.resources.core.LeadTrustee


object DesLeadTrusteesMapper {
  def toDes(leadTrustee: LeadTrustee) : DesLeadTrustee = {
    leadTrustee.company match {
      case Some(c:Company) => mapOrg(leadTrustee)
      case None             => {
        leadTrustee.individual match {
          case Some(i:Individual) => mapIndividual(leadTrustee)
          case None             => throw new MissingPropertyException("Missing a LeadTrustee")
        }
      }
    }
  }

  private def mapIndividual(leadTrustee: LeadTrustee): DesLeadTrusteeInd= {
    val identification: DesIdentification = DesIdentificationMapper.toDes(leadTrustee.individual.get)
    val name = DesNameMapper.toDes(leadTrustee.individual.get)
    DesLeadTrusteeInd(name,leadTrustee.individual.get.dateOfBirth,identification,leadTrustee.telephoneNumber,Some(leadTrustee.email))
  }

  private def mapOrg(leadTrustee: LeadTrustee): DesLeadTrusteeOrg= {
    val identification: DesOrgIdentification = DesOrgIdentificationMapper.toDes(leadTrustee.company.get)
    DesLeadTrusteeOrg(leadTrustee.company.get.name, leadTrustee.telephoneNumber, Some(leadTrustee.email), identification )
  }
}
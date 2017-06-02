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

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import sun.security.krb5.internal.crypto.Des
import uk.gov.hmrc.common.des.{DesLeadTrustee, DesLeadTrusteeInd, DesLeadTrusteeOrg}
import uk.gov.hmrc.trustapi.rest.resources.core.{LeadTrustee, Protectors}
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesLeadTrusteeMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples  {


  "Des LeadTrustee mapper" should {
    "map a rest domain company lead trustee to des lead trustee correctly" when {
      "we have a valid company" in {
        val companyLeadTrustee: LeadTrustee = leadTrusteeCompany.copy(individual = None)
        val output: DesLeadTrustee = DesLeadTrusteesMapper.toDes(companyLeadTrustee)

        output.asInstanceOf[DesLeadTrusteeOrg].name  mustBe leadTrusteeCompany.company.get.name
      }
    }
    "map a rest domain individual lead trustee to des lead trustee correctly" when {
      "we have a valid individual" in {
        val individualLeadTrustee: LeadTrustee = leadTrusteeIndividual.copy(company = None)
        val output: DesLeadTrustee = DesLeadTrusteesMapper.toDes(individualLeadTrustee)

        output.asInstanceOf[DesLeadTrusteeInd].dateOfBirth mustBe leadTrusteeIndividual.individual.get.dateOfBirth
      }
    }
  }
}


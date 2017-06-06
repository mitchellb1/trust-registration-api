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

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.{DesLeadTrusteeInd, DesLeadTrusteeOrg}
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class LeadTrusteeMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val individualLeadTrustee = DesLeadTrusteeInd(desName,date,desIdentification,phoneNumber,Some(email))
  val companyLeadTrustee = DesLeadTrusteeOrg("Test", "Test", Some(email), desOrgIdentification)


  "Lead trustee mapper" should {
    "map a desleadtrustee to a lead trustee correctly" when {
      "we have a valid DesLeadTrusteeInd" in {
        val output = LeadTrusteeMapper.toDomain(individualLeadTrustee,phoneNumber, email)
        
        output.individual.get.familyName mustBe individualLeadTrustee.name.lastName
      }

      "we have a valid DesLeadTrusteeOrg" in {
        val output = LeadTrusteeMapper.toDomain(companyLeadTrustee,phoneNumber, email)

        output.company.get.name mustBe companyLeadTrustee.name
      }

      "we have a telephone Number" in {
        val output = LeadTrusteeMapper.toDomain(companyLeadTrustee, phoneNumber, email)

        output.telephoneNumber mustBe phoneNumber
      }

      "we have an email" in {
        val output = LeadTrusteeMapper.toDomain(individualLeadTrustee, phoneNumber, email)

        output.email mustBe email
      }
    }
  }
}

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

package uk.gov.hmrc.common.mapping.todomain

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.{DesCompany, DesOrgIdentification, DesSettlorCompany, MissingPropertyException}
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class CompanyMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  "Company mapper" must {
    "Map a des company to a domain company correctly" when {
      val desCompany = DesCompany("Test",None,None,desOrgIdentification)
      val output = CompanyMapper.toDomain(Some(desCompany))

      "we have a correct company name" in {
        output.name mustBe desCompany.organisationName
      }

      "we have a correct des address" in {
        output.correspondenceAddress.line1 mustBe desCompany.identification.address.get.line1
      }

      "we have a correct utr" in {
        output.referenceNumber mustBe desCompany.identification.utr
      }

      "we dont have a utr" in {
        val companyNoUTR = desCompany.copy(identification = DesOrgIdentification(None,Some(desAddress)))
        val output = CompanyMapper.toDomain(Some(companyNoUTR))

        output.referenceNumber mustBe companyNoUTR.identification.utr
      }
    }

    "Map a des settlor company to a domain company correctly" when {
      val desSettlorCompany =  DesSettlorCompany("Test","Investment",false,desOrgIdentification)
      val output = CompanyMapper.toDomain(desSettlorCompany = Some(desSettlorCompany))

      "we have a name" in {
        output.name mustBe desSettlorCompany.name
      }

      "we have a correct des address" in {
        output.correspondenceAddress.line1 mustBe desSettlorCompany.identification.address.get.line1
      }

      "we have a correct utr" in {
        output.referenceNumber mustBe desSettlorCompany.identification.utr
      }

      "we dont have a utr" in {
        val companyNoUTR = desSettlorCompany.copy(identification = DesOrgIdentification(None,Some(desAddress)))
        val output = CompanyMapper.toDomain(desSettlorCompany= Some(companyNoUTR))

        output.referenceNumber mustBe companyNoUTR.identification.utr
      }
    }

    "throw an exception" when {
      "we don't have an address" in {
        val desCompanyNoId = DesCompany("Test",None,None,DesOrgIdentification())
        val ex = the[MissingPropertyException] thrownBy CompanyMapper.toDomain(Some(desCompanyNoId))

        ex.getMessage must include("Missing address")
      }

      "we don't specify any companies for the mapper" in {
        val ex = the[IllegalArgumentException] thrownBy CompanyMapper.toDomain(None,None)

        ex.getMessage must include("requirement failed")
      }
    }
  }
}

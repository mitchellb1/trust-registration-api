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

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.DesWill
import uk.gov.hmrc.common.rest.resources.core.Deceased
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class DesWillMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesWillMapper
  val domainDeceasedToMap: Deceased = deceased

  "Des Will Mapper" must {
    "map properties correctly" when {
      "we have a correct dateOfBirth in the des domain" in {
        val output: DesWill = SUT.toDes(deceased)
        output.dateOfBirth mustBe deceased.individual.dateOfBirth
      }
      "we have a correct dateOfDeath in the des domain" in {
        val output: DesWill = SUT.toDes(deceased)
        output.dateOfDeath mustBe deceased.dateOfDeath
      }
      "we have a correct address details in the des domain" in {
        val output: DesWill = SUT.toDes(deceased)
        output.identification.address.get.line1 mustBe deceased.individual.correspondenceAddress.get.line1
        Some(output.identification.address.get.line2) mustBe deceased.individual.correspondenceAddress.get.line2
        output.identification.address.get.line3 mustBe deceased.individual.correspondenceAddress.get.line3
        output.identification.address.get.line4 mustBe deceased.individual.correspondenceAddress.get.line4
        output.identification.address.get.postCode mustBe deceased.individual.correspondenceAddress.get.postalCode
        output.identification.address.get.country mustBe deceased.individual.correspondenceAddress.get.countryCode
      }
      "we have a correct nino for des domain if there is one" in {
        val output: DesWill = SUT.toDes(deceasedwithNino)
        output.identification.nino mustBe deceasedwithNino.individual.nino
      }
      "we have a correct name for des domain" in {
        val output: DesWill = SUT.toDes(deceased)
        output.name.firstName mustBe deceased.individual.givenName
        output.name.middleName mustBe deceased.individual.otherName
        output.name.lastName mustBe deceased.individual.familyName
      }
    }
  }
}




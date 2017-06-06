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


import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.common.des._
import uk.gov.hmrc.common.utils.{DesSchemaValidator, SuccessfulValidation}
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class EstateMappingSpec extends PlaySpec
  with OneAppPerSuite
  with DesScalaExamples
  with ScalaDataExamples {

  val SUT = EstateMapper
  val desWill = DesWill(desName,date,date,desWillId)
  val desCorrespondence = DesCorrespondence(true,"Test Estate",desAddress,phoneNumber)

  "EstateMapper" must {
    "accept a valid domain Estates case classes" when {
      "and return a valid DesEstates case classes" in {
        val convertedToDesCaseClasses = SUT.toDes(validEstateWithPersonalRepresentative)

        val result = DesSchemaValidator.validateAgainstSchema(Json.toJson(convertedToDesCaseClasses).toString())
        result mustBe SuccessfulValidation
      }
    }

    "map correctly to a estate domain" when {
      val output = SUT.toDomain(completeValidDesEstate)

      "we have a personal representative" in {
        estate.entities.personalRepresentative.email.get mustBe output.personalRepresentative.email
      }

      "we have a correct address" in {
        desAddress.line1 mustBe output.correspondenceAddress.line1
      }

      "we have an admin period finished date" in {
        estate.administrationEndDate.get mustBe output.adminPeriodFinishedDate.get
      }

      "we have a declaration" in {
        desDeclaration.name.firstName mustBe output.declaration.givenName
      }

      "we have a name" in {
        desCorrespondence.name mustBe output.estateName
      }

      "we have period tax dues" in {
        output.reasonEstateSetup mustBe "incomeTaxDueMoreThan10000"
      }

      "we have a phone number" in {
        desCorrespondence.phoneNumber mustBe output.telephoneNumber
      }

      "we have a deceased" in {
        output.deceased.dateOfDeath mustBe desWill.dateOfDeath
      }

      "we have a utr" in {
        output.utr mustBe Some(admin.utr)
      }
    }
  }
}
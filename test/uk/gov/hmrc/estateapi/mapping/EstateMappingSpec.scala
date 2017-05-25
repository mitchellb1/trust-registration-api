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


import org.joda.time.DateTime
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.common.des.{DesAddress, DesCorrespondence, DesDeclaration, DesEstate}
import uk.gov.hmrc.common.mapping.{AddressMapper, PersonalRepresentativeMapper}
import uk.gov.hmrc.common.utils.{DesSchemaValidator, SuccessfulValidation}
import uk.gov.hmrc.estateapi.rest.resources.core.Estate
import uk.gov.hmrc.trustapi.mapping.DeclarationMapper
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class EstateMappingSpec extends PlaySpec
  with OneAppPerSuite
  with DesScalaExamples
  with ScalaDataExamples {

//  val domainEstateFromCaseClasses = EstateRequest(validEstateWithPersonalRepresentative)
//  val domainEstateFromFileString: String = Json.prettyPrint(Json.toJson(validEstateWithPersonalRepresentative))

  val SUT = EstateMapper

  "EstateMapper" must {
    "accept a valid domain Estates case classes" when {
      "and return a valid DesEstates case classes" in {
        val convertedToDesCaseClasses = SUT.toDes(validEstateWithPersonalRepresentative)

        val result = DesSchemaValidator.validateAgainstSchema(Json.toJson(convertedToDesCaseClasses).toString())
        result mustBe SuccessfulValidation
      }
    }

    "map correctly to a estate domain" when {
      val correspondence = DesCorrespondence(true,"Test",desAddress,phoneNumber)

      val output = Mapper.toDomain(estate, desAddress, desDeclaration, correspondence)

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
        correspondence.name mustBe output.estateName
      }

      "we have period tax dues" in {
        output.reasonEstateSetup mustBe "incomeTaxDueMoreThan10000"
      }

      "we have a phone number" in {
        correspondence.phoneNumber mustBe output.telephoneNumber
      }

      "we have a deceased" in {

      }
    }
//    "accept a valid set of des Estates case class" when {
//      "and return a set of valid Domain Estates case class" in {
//
//        //println(s"From des case classes ---- ${completeValidDesEstate}")
//        val convertedToDomainCaseClasses = SUT.toDomain(completeValidDesEstate)
//        println(s"From domain case classes ---- ${convertedToDomainCaseClasses}")
//
//
//
//        val result = EstateSchemaValidator.validateAgainstSchema(Json.toJson(convertedToDomainCaseClasses).toString())
//        result mustBe SuccessfulValidation
//      }
//    }
  }
}

object Mapper extends ScalaDataExamples with DesScalaExamples{
  def toDomain(estate: DesEstate, address: DesAddress, declaration: DesDeclaration, correspondence: DesCorrespondence) : Estate = {
    Estate(correspondence.name,
      AddressMapper.toDomain(address),
      PersonalRepresentativeMapper.toDomain(estate.entities.personalRepresentative),
      estate.administrationEndDate,
      estate.periodTaxDues match {
        case "01" => "incomeTaxDueMoreThan10000"
        case "02" => "saleOfEstateAssetsMoreThan250000"
        case "03" => "saleOfEstateAssetsMoreThan500000"
        case "04" => "worthMoreThanTwoAndHalfMillionAtTimeOfDeath"
      },
      DeclarationMapper.toDomain(declaration,new DateTime("2016-03-31"),true),//TODO: For declaration, we have not got a field to map confirmation or date.
      deceased,
      correspondence.phoneNumber)
  }
}

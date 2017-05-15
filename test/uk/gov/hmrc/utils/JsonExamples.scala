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

package uk.gov.hmrc.utils

import scala.io.Source

trait JsonExamples {

  lazy val validReRegisterJson = Source.fromFile(getClass.getResource("/ValidTrustExistence.json").getPath).mkString
  lazy val validPassportJson = Source.fromFile(getClass.getResource("/ValidPassport.json").getPath).mkString
  lazy val validAddressJson = Source.fromFile(getClass.getResource("/ValidAddress.json").getPath).mkString
  lazy val validIndividualJson = Source
    .fromFile(getClass.getResource("/ValidIndividual.json").getPath)
    .mkString
    .replace("\"{PASSPORT}\"", validPassportJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
  lazy val invalidIndividualJson = Source.fromFile(getClass.getResource("/InvalidIndividual.json").getPath).mkString

  lazy val validDeclarationJson = Source.fromFile(getClass.getResource("/ValidDeclaration.json").getPath).mkString

  lazy val validDeceasedJson = s"""{"individual":${validIndividualJson},"dateOfDeath":"2000-01-01"}"""

  lazy val validCompanyJson = Source
    .fromFile(getClass.getResource("/ValidCompany.json").getPath)
    .mkString
    .replace("\"{ADDRESS}\"", validAddressJson)
  lazy val invalidCompanyJson = Source
    .fromFile(getClass.getResource("/InvalidCompany.json").getPath)
    .mkString
    .replace("\"{ADDRESS}\"", validAddressJson)

  lazy val invalidEstateJson = Source.fromFile(getClass.getResource("/InvalidEstate.json").getPath).mkString
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)

  lazy val validEstateWithPersonalRepresentativeJson = Source.fromFile(getClass.getResource("/ValidEstateWithPersonalRepresentative.json").getPath).mkString
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{DECLARATION}\"", validDeclarationJson)


  lazy val validLeadTrusteeIndividualJson = s"""{"individual":$validIndividualJson,"company":null,"telephoneNumber":"1234567890","email":"test@test.com"}"""
  lazy val validLeadTrusteeCompanyJson = s"""{"individual":null,"company":$validCompanyJson,"telephoneNumber":"1234567890","email":"test@test.com"}"""

  lazy val validIndividualBeneficiary = Source.fromFile(getClass.getResource("/ValidIndividualBeneficiary.json").getPath)
                                            .mkString
                                            .replace(""""{INDIVIDUAL}"""", validIndividualJson)

  lazy val validEmployeeBeneficiary = Source.fromFile(getClass.getResource("/ValidEmployeeBeneficiary.json").getPath)
    .mkString
    .replace(""""{INDIVIDUAL}"""", validIndividualJson)

  lazy val validCharityBeneficiary = Source.fromFile(getClass.getResource("/ValidCharityBeneficiary.json").getPath)
    .mkString
    .replace(""""{ADDRESS}"""", validAddressJson)

  lazy val invalidCharityBeneficiary = Source.fromFile(getClass.getResource("/InvalidCharityBeneficiary.json").getPath).mkString

  lazy val validOtherBeneficiary = Source.fromFile(getClass.getResource("/ValidOtherBeneficiary.json").getPath)
    .mkString
    .replace(""""{ADDRESS}"""", validAddressJson)

  lazy val matchedValidBeneficiariesJson =
    s"""{"individualBeneficiaries":[$validIndividualBeneficiary],
       |"employeeBeneficiaries":[$validEmployeeBeneficiary],
       |"charityBeneficiaries":[$validCharityBeneficiary],
       |"otherBeneficiaries":[$validOtherBeneficiary]
       |}""".stripMargin

  lazy val validBeneficiariesJson = s"""{"individualBeneficiaries":[$validIndividualBeneficiary],"charityBeneficiaries":[$validCharityBeneficiary],"otherBeneficiaries":[$validOtherBeneficiary],"employeeBeneficiaries":[$validEmployeeBeneficiary]}"""

  lazy val invalidBeneficiariesJson = s"""{"charityBeneficiaries": [$invalidCharityBeneficiary]}"""

  lazy val invalidLeadTrusteeJson = s"""{"individual":$validIndividualJson,"company":$validCompanyJson}"""

  lazy val validProtectorsJson = s"""{"individuals":[$validIndividualJson],"companies":[$validCompanyJson]}"""
  lazy val invalidProtectorsJson = s"""{"individuals":[$invalidIndividualJson],"companies":[$invalidCompanyJson]}"""

  lazy val validOtherAssetJson = Source.fromFile(getClass.getResource("/ValidOtherAsset.json").getPath).mkString
  lazy val validShareAssetJson = Source.fromFile(getClass.getResource("/ValidShareAsset.json").getPath).mkString
  lazy val validBusinessAssetJson = Source.fromFile(getClass.getResource("/ValidBusinessAsset.json").getPath).mkString
  lazy val validWillIntestacyTrustJson = Source.fromFile(getClass.getResource("/ValidWillIntestacyTrust.json").getPath).mkString
    .replace("\"{SHAREASSETS}\"", validShareAssetJson)
    .replace("\"{BUSINESSASSETS}\"", validBusinessAssetJson)
    .replace("\"{INDIVIDUALBENEFICIARY}\"", validIndividualBeneficiary)
    .replace("\"{DECEASED}\"", validDeceasedJson)


  lazy val validFlatManagementSinkingFundTrustJson = Source.fromFile(getClass.getResource("/ValidFlatManagementSinkingFundTrust.json").getPath).mkString
    .replace("\"{INDIVIDUAL}\"", validIndividualJson )
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{INDIVIDUALBENEFICIARY}\"", validIndividualBeneficiary)

  lazy val validEmploymentTrustJson = Source.fromFile(getClass.getResource("/ValidEmploymentTrust.json").getPath).mkString
    .replace("\"{SHAREASSETS}\"", validShareAssetJson)
    .replace("\"{BUSINESSASSETS}\"", validBusinessAssetJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson )
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{INDIVIDUALBENEFICIARY}\"", validIndividualBeneficiary)


  lazy val validHeritageMaintenanceFundTrustJson = Source.fromFile(getClass.getResource("/ValidHeritageMaintenanceFundTrust.json").getPath).mkString
    .replace("\"{SHAREASSETS}\"", validShareAssetJson)
    .replace("\"{OTHERASSETS}\"", validOtherAssetJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson )
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{OTHERBENEFICIARY}\"", validOtherBeneficiary)

  lazy val invalidWillIntestacyTrustJson = Source.fromFile(getClass.getResource("/InvalidWillInstestacyTrust.json").getPath).mkString
    .replace("\"{BUSINESSASSETS}\"", validBusinessAssetJson)
    .replace("\"{INDIVIDUALBENEFICIARY}\"", validIndividualBeneficiary)
    .replace("\"{DECEASED}\"", validDeceasedJson)

  lazy val validLegalityJson = Source.fromFile(getClass.getResource("/ValidLegality.json").getPath).mkString

  lazy val validTrustJson = Source.fromFile(getClass.getResource("/ValidTrust.json").getPath).mkString
    .replace("\"{WILLINTESTACYTRUST}\"", validWillIntestacyTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

  lazy val validCompleteTrustJson = Source.fromFile(getClass.getResource("/ValidCompleteTrust.json").getPath).mkString
    .replace("\"{WILLINTESTACYTRUST}\"", validWillIntestacyTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

  lazy val validCompleteTrustWithUTRJson = Source.fromFile(getClass.getResource("/ValidCompleteTrustWithUTR.json").getPath).mkString
    .replace("\"{WILLINTESTACYTRUST}\"", validWillIntestacyTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

  lazy val validTrustFlatManagementJson= Source.fromFile(getClass.getResource("/ValidTrustFlatManagementSinkingFund.json").getPath).mkString
    .replace("\"{FLATMANAGEMENTSINKINGTRUST}\"", validFlatManagementSinkingFundTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)
    .replace("\"{OTHERBENEFICIARY}\"", validOtherBeneficiary)

  lazy val validInterVivoTrustFundJson = Source.fromFile(getClass.getResource("/ValidInterVivoTrust.json").getPath).mkString
    .replace("\"{SHAREASSETS}\"", validShareAssetJson)
    .replace("\"{BUSINESSASSETS}\"", validBusinessAssetJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson )
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{INDIVIDUALBENEFICIARY}\"", validIndividualBeneficiary)

  lazy val validTrustWithInterVivoJson = Source.fromFile(getClass.getResource("/ValidTrustInterVivo.json").getPath).mkString
    .replace("\"{INTERVIVOTRUST}\"", validInterVivoTrustFundJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

  lazy val validTrustEmploymentJson = Source.fromFile(getClass.getResource("/ValidTrustEmployment.json").getPath).mkString
    .replace("\"{EMPLOYMENTTRUST}\"", validEmploymentTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

  lazy val validTrustHeritageMaintenanceJson = Source.fromFile(getClass.getResource("/ValidTrustHeritageMaintenance.json").getPath).mkString
    .replace("\"{HERITAGEMAINTENANCETRUST}\"", validHeritageMaintenanceFundTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

  lazy val invalidTrustJson = Source.fromFile(getClass.getResource("/InvalidTrust.json").getPath).mkString
    .replace("\"{WILLINTESTACYTRUST}\"", validWillIntestacyTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

  lazy val invalidTrustWithTwoTrustsJson = Source.fromFile(getClass.getResource("/InvalidTrustWithTwoTrusts.json").getPath).mkString
    .replace("\"{INTERVIVOTRUST}\"", validInterVivoTrustFundJson)
    .replace("\"{FLATMANAGEMENTSINKINGTRUST}\"", validFlatManagementSinkingFundTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)
}



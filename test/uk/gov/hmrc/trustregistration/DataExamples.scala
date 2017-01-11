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

package uk.gov.hmrc.trustregistration

import org.joda.time.DateTime
import uk.gov.hmrc.trustregistration.models._

import scala.io.Source

trait JsonExamples {
  lazy val validPassportJson = Source.fromFile(getClass.getResource("/ValidPassport.json").getPath).mkString
  lazy val validAddressJson = Source.fromFile(getClass.getResource("/ValidAddress.json").getPath).mkString
  lazy val validIndividualJson = Source
    .fromFile(getClass.getResource("/ValidIndividual.json").getPath)
    .mkString
    .replace("\"{PASSPORT}\"", validPassportJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
  lazy val invalidIndividualJson = Source.fromFile(getClass.getResource("/InvalidIndividual.json").getPath).mkString
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
  lazy val validEstateWithDeceasedJson = Source.fromFile(getClass.getResource("/ValidEstateWithDeceased.json").getPath).mkString
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
  lazy val validLeadTrusteeIndividualJson = s"""{"individual":$validIndividualJson,"company":null,"telephoneNumber":"1234567890","email":"test@test.com"}"""
  lazy val validLeadTrusteeCompanyJson = s"""{"individual":null,"company":$validCompanyJson,"telephoneNumber":"1234567890","email":"test@test.com"}"""

  lazy val validIndividualBeneficiary = Source.fromFile(getClass.getResource("/ValidIndividualBeneficiary.json").getPath)
                                            .mkString
                                            .replace(""""{INDIVIDUAL}"""", validIndividualJson)

  lazy val validCharityBeneficiary = Source.fromFile(getClass.getResource("/ValidCharityBeneficiary.json").getPath)
    .mkString
    .replace(""""{ADDRESS}"""", validAddressJson)

  lazy val invalidCharityBeneficiary = Source.fromFile(getClass.getResource("/InvalidCharityBeneficiary.json").getPath).mkString

  lazy val validOtherBeneficiary = Source.fromFile(getClass.getResource("/ValidOtherBeneficiary.json").getPath)
    .mkString
    .replace(""""{ADDRESS}"""", validAddressJson)



  lazy val validBeneficiariesJson = s"""{"individualBeneficiaries":[$validIndividualBeneficiary],"charityBeneficiaries":[$validCharityBeneficiary],"otherBeneficiaries":[$validOtherBeneficiary]}"""

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


  lazy val validFlatManagementSinkingFundTrustJson = Source.fromFile(getClass.getResource("/ValidFlatManagementSinkingFundTrust.json").getPath).mkString
    .replace("\"{SHAREASSETS}\"", validShareAssetJson)
    .replace("\"{BUSINESSASSETS}\"", validBusinessAssetJson)
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

  lazy val validLegalityJson = Source.fromFile(getClass.getResource("/ValidLegality.json").getPath).mkString

  lazy val validTrustJson = Source.fromFile(getClass.getResource("/ValidTrust.json").getPath).mkString
    .replace("\"{WILLINTESTACYTRUST}\"", validWillIntestacyTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

  lazy val validTrustFlatManagementJson= Source.fromFile(getClass.getResource("/ValidTrustFlatManagementSinkingFund.json").getPath).mkString
    .replace("\"{FLATMANAGEMENTSINKINGTRUST}\"", validFlatManagementSinkingFundTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)

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

trait ScalaDataExamples {

  val settlors = Settlors(Some(List(individual)))

  val address = Address(
    line1 = "Line 1",
    line2 = Some("Line 2"),
    line3 = Some("Line 3"),
    line4 = Some("Line 4"),
    postalCode = Some("NE1 2BR"),
    countryCode = Some("ES")
  )

  val passport = Passport(
    identifier = "IDENTIFIER",
    expiryDate = new DateTime("2020-01-01"),
    countryOfIssue = "ES"
  )

  val individual = Individual(
    title = "Dr",
    givenName = "Leo",
    otherName = None,
    familyName = "Spaceman",
    dateOfBirth = new DateTime("1800-01-01"),
    dateOfDeath = None,
    nino = None,
    passport = Some(passport),
    correspondenceAddress = Some(address),
    telephoneNumber = None
  )

  val company = Company(
    name = "Company",
    referenceNumber = Some("AAA5221"),
    correspondenceAddress = address
  )

  val leadTrusteeIndividual = LeadTrustee(
    individual = Some(individual),
    company = None,
    telephoneNumber = "1234567890",
    email = "test@test.com"
  )

  val leadTrusteeCompany = LeadTrustee(
    individual = None,
    company = Some(company),
    telephoneNumber = "1234567890",
    email = "test@test.com"
  )

  val personalRepresentative = PersonalRepresentative(individual,true)

  val validEstateWithPersonalRepresentative = Estate(true,true,true,false,Some(personalRepresentative))
  val validEstateWithDeceased = Estate(true,true,true,false,None,Some(individual),Some(false),Some(false),Some(false))

  val employeeBeneficiary = EmployeeBeneficiary(individual = individual,
    isVulnerable = false,
    isIncomeAtTrusteeDiscretion = false,
    shareOfIncome = Some(20))

  val individualBeneficiary = IndividualBeneficiary(
    individual = individual,
    isVulnerable = false,
    isIncomeAtTrusteeDiscretion = true,
    shareOfIncome = Some(30)
  )

  val charityBeneficiary = CharityBeneficiary(
    name = "Charity Name",
    number = "123456789087654",
    correspondenceAddress = address,
    isIncomeAtTrusteeDiscretion = false,
    shareOfIncome = Some(20)
  )

  val otherBeneficiary = OtherBeneficiary(
    description = "Beneficiary Description",
    correspondenceAddress = address,
    isIncomeAtTrusteeDiscretion = false,
    shareOfIncome = Some(50)
  )

  val beneficiaries = Beneficiaries(
    individualBeneficiaries = Some(List(individualBeneficiary)),
    charityBeneficiaries = Some(List(charityBeneficiary)),
    otherBeneficiaries = Some(List(otherBeneficiary))
  )

  val protectors = Protectors(
    individuals = Some(List(individual)),
    companies = Some(List(company))
  )

  val legality = Legality("ES","ES",true)

  val businessAsset = BusinessAsset("Test","Test","This is a description",address,1234, Some(new DateTime("1940-04-04")))

  val shareAsset = ShareAsset(1234,"Test","Test","1234",1234)

  val assets = Assets(None,None,Some(List(shareAsset,shareAsset)),None,Some(List(businessAsset,businessAsset)))

  val willIntestacyTrust = WillIntestacyTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false,true,Some(30))))),individual)

  val trust = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),legality,true,leadTrusteeIndividual,Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(List(individual,individual))),Some(willIntestacyTrust))

  val otherAsset = OtherAsset("This is a test description",1234,Some(new DateTime("1800-01-01")))
  val assetsWithOtherAsset = Assets(None,None,Some(List(shareAsset,shareAsset)),None,None,Some(List(otherAsset,otherAsset)))


  val heritageFund = Some(HeritageMaintenanceFundTrust(assetsWithOtherAsset,Beneficiaries(None,None,None,None,Some(List(otherBeneficiary))),true,Some(individual)))

  val trustWithHeritageMaintenance = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),legality,true,leadTrusteeIndividual, Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(List(individual,individual))),None,None,heritageFund)

  val employmentTrust = Some(EmploymentTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false,true,Some(30))))),true,Some(new DateTime("1940-01-01"))))
  val trustWithEmploymentTrust = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),legality,true,leadTrusteeIndividual, Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(List(individual,individual))),None,None,None,None,employmentTrust)

  val flatManagementFund = Some(FlatManagementSinkingFundTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false,true,Some(30)))))))
  val trustWithFlatManagementFund = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),legality,true,leadTrusteeIndividual, Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(List(individual,individual))),None,None,None,flatManagementFund)

  val interVivoTrust = Some(InterVivoTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false,true,Some(30))))),true,Some(individual)))
  val trustWithInterVivoTrust = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),legality,true,leadTrusteeIndividual, Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(List(individual,individual))),None,interVivoTrust)
}

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
import uk.gov.hmrc.common.rest.resources.core._
import uk.gov.hmrc.estateapi.rest.resources.core.{Estate, PersonalRepresentative}
import uk.gov.hmrc.trustapi.rest.resources.core._
import uk.gov.hmrc.trustapi.rest.resources.core.assets._
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries._
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes._

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
trait ScalaDataExamples {

 val settlors = Settlors(Some(List(individual)))

  val address = Address(
    line1 = "Line 1",
    line2 = Some("Line 2"),
    line3 = Some("Line 3"),
    line4 = Some("Line 4"),
    postalCode = None,
    countryCode = "ES"
  )

  val passport = Passport(
    identifier = "IDENTIFIER",
    expiryDate = new DateTime("2020-01-01"),
    countryOfIssue = "ES"
  )

  val individual = Individual(
    givenName = "Leo",
    otherName = None,
    familyName = "Spaceman",
    dateOfBirth = new DateTime("1800-01-01"),
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

  val personalRepresentative = PersonalRepresentative(individual,"01913651234","test@test.com")

  val declaration = Declaration(correspondenceAddress = address,
  confirmation = true,
  givenName = "george",
  familyName = "Spaceman",
  date = new DateTime(2000, 1, 1, 0, 0),
  otherName = Some("fred"))

  val validEstateWithPersonalRepresentative = Estate(estateName = "Test Estate",
                                                      correspondenceAddress = address,
                                                      personalRepresentative = personalRepresentative,
                                                      adminPeriodFinishedDate = Some(new DateTime("1800-01-01")),
                                                      reasonEstateSetup = "incomeTaxDueMoreThan10000",
                                                      declaration = declaration)

 val incomeDistribution = IncomeDistribution(
   isIncomeAtTrusteeDiscretion = false,
     shareOfIncome = Some(50)
 )
  val employeeBeneficiary = EmployeeBeneficiary(
    individual = individual,
    incomeDistribution = incomeDistribution
  )

  val individualBeneficiary = IndividualBeneficiary(
    individual = individual,
    isVulnerable = false,
    incomeDistribution = incomeDistribution
  )

  val largeNumbersCompanyBeneficiary = LargeNumbersCompanyBeneficiaries(
    description = "test",
    numberOfBeneficiaries = 123456,
    company = company,
    incomeDistribution = incomeDistribution
  )

  val trustBeneficiary = TrustBeneficiary(
    trustBeneficiaryName = "trust beneficiary Name",
    trustBeneficiaryUTR = Some("2134567"),
    correspondenceAddress = address,
    incomeDistribution = incomeDistribution
  )

  val companyBeneficiary = CompanyBeneficiary(
    company = company,
    incomeDistribution = incomeDistribution
  )

  val directorBeneficiary = DirectorBeneficiary(
    individual = individual,
    incomeDistribution = incomeDistribution
  )


  val charityBeneficiary = CharityBeneficiary(
    charityName = "Charity Name",
    charityNumber = "123456789087654",
    correspondenceAddress = address,
    incomeDistribution = incomeDistribution
  )

  val otherBeneficiary = OtherBeneficiary(
    beneficiaryDescription = "Beneficiary Description",
    correspondenceAddress = address,
    incomeDistribution = incomeDistribution
  )

  val beneficiaries = Beneficiaries(
    individualBeneficiaries = Some(List(individualBeneficiary)),
    employeeBeneficiaries = Some(List(employeeBeneficiary)),
    charityBeneficiaries = Some(List(charityBeneficiary)),
    otherBeneficiaries = Some(List(otherBeneficiary)))

  val otherBeneficiaries = Some(List(otherBeneficiary))
  val protectors = Protectors(
    individuals = Some(List(individual)),
    companies = Some(List(company))
  )

  val legality = Legality("ES",Some("ES"),true,None)

  val trustExistenceExample = TrustExistence("asdf", Some("12341234"), Some(""))

  val businessAsset = BusinessAsset("This is a description",1234L, company)

  val shareAsset = ShareAsset(1234L,"shareCompanyName","shareCompanyRegistrationNumber","shareClass","shareType",123400L)

  val assets = Assets(None,None,Some(List(shareAsset,shareAsset)),None,Some(List(businessAsset,businessAsset)))

  val monetaryAssets = Assets(monetaryAssets = Some(List(100L, 2L, 75L)))

  val deceased = Deceased(individual, new DateTime(2000, 1, 1, 0, 0))

  val willIntestacyTrust = WillIntestacyTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false, incomeDistribution)))), deceased, true)

  val trust = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),Some(List(2015,2016)),legality,true,leadTrusteeIndividual,Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(Some(List(individual,individual)))), TrustType(willIntestacyTrust = Some(willIntestacyTrust)))

  val otherAsset = OtherAsset("This is a test description", None , new DateTime("1800-01-01"))

  val partnershipAsset = PartnershipAsset("This is a test description", "123456UTR" , new DateTime("1800-01-01"))

  val assetsWithOtherAsset = Assets(None,None,Some(List(shareAsset,shareAsset)),None,None,Some(List(otherAsset,otherAsset)))

  val heritageFund = Some(HeritageMaintenanceFundTrust(assetsWithOtherAsset,Beneficiaries(None,None,None,None,Some(List(otherBeneficiary))),true,Some(individual)))

  val trustWithHeritageMaintenance = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),Some(List(2015,2016)),legality,true,leadTrusteeIndividual, Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(Some(List(individual,individual)))), TrustType(heritageMaintenanceFundTrust = heritageFund))

  val employmentTrust = Some(EmploymentTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false, incomeDistribution)))),Some(true),Some(new DateTime("1940-01-01"))))
  val trustWithEmploymentTrust = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),Some(List(2015,2016)),legality,true,leadTrusteeIndividual, Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(Some(List(individual,individual)))), TrustType(employmentTrust = employmentTrust))

  val flatManagementFund = Some(FlatManagementSinkingFundTrust(monetaryAssets , Beneficiaries(otherBeneficiaries = otherBeneficiaries)))
  val trustWithFlatManagementFund = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),Some(List(2015,2016)),legality,true,leadTrusteeIndividual, Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(Some(List(individual,individual)))), TrustType(flatManagementSinkingFundTrust = flatManagementFund))

  val interVivoTrust = Some(InterVivoTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false, incomeDistribution)))),true, Some("dovTypeAbsolute")))
  val trustWithInterVivoTrust = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),Some(List(2015,2016)),legality,true,leadTrusteeIndividual, Trustees(None, None),
    Protectors(Some(List(individual,individual))),Settlors(Some(List(individual,individual))),Some(NaturalPeople(Some(List(individual,individual)))), TrustType(interVivoTrust = interVivoTrust))
}
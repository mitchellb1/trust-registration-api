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

import org.joda.time.DateTime
import uk.gov.hmrc.common.rest.resources.core._
import uk.gov.hmrc.estateapi.rest.resources.core.{Estate, PersonalRepresentative}
import uk.gov.hmrc.trustapi.rest.resources.core._
import uk.gov.hmrc.trustapi.rest.resources.core.assets._
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries._
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes._


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
    referenceNumber = "IDENTIFIER",
    expiryDate = new DateTime("2020-01-01"),
    countryOfIssue = "ES"
  )

  val individual = Individual(
    givenName = "Leo",
    otherName = None,
    familyName = "Spaceman",
    dateOfBirth = new DateTime("1800-01-01"),
    nino = None,
    passportOrIdCard = Some(passport),
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

  val shareAsset = ShareAsset(1234L,"shareCompanyName","CompanyReg","Ordinary","Quoted",123400L)

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
/*
 * Copyright 2016 HM Revenue & Customs
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
  val validPassportJson = Source.fromFile(getClass.getResource("/ValidPassport.json").getPath).mkString
  val validAddressJson = Source.fromFile(getClass.getResource("/ValidAddress.json").getPath).mkString
  val validIndividualJson = Source
    .fromFile(getClass.getResource("/ValidIndividual.json").getPath)
    .mkString
    .replace("\"{PASSPORT}\"", validPassportJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
  val invalidIndividualJson = Source.fromFile(getClass.getResource("/InvalidIndividual.json").getPath).mkString
  val validCompanyJson = Source
    .fromFile(getClass.getResource("/ValidCompany.json").getPath)
    .mkString
    .replace("\"{ADDRESS}\"", validAddressJson)
  val invalidCompanyJson = Source
    .fromFile(getClass.getResource("/InvalidCompany.json").getPath)
    .mkString
    .replace("\"{ADDRESS}\"", validAddressJson)
  val invalidEstateJson = Source.fromFile(getClass.getResource("/InvalidEstate.json").getPath).mkString
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
  val validEstateWithPersonalRepresentativeJson = Source.fromFile(getClass.getResource("/ValidEstateWithPersonalRepresentative.json").getPath).mkString
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
  val validEstateWithDeceasedJson = Source.fromFile(getClass.getResource("/ValidEstateWithDeceased.json").getPath).mkString
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
  val validLeadTrusteeIndividualJson = s"""{"individual":$validIndividualJson,"company":null}"""
  val validLeadTrusteeCompanyJson = s"""{"individual":null,"company":$validCompanyJson}"""

  val validIndividualBeneficiary = Source.fromFile(getClass.getResource("/ValidIndividualBeneficiary.json").getPath)
                                            .mkString
                                            .replace(""""{INDIVIDUAL}"""", validIndividualJson)

  val validCharityBeneficiary = Source.fromFile(getClass.getResource("/ValidCharityBeneficiary.json").getPath)
    .mkString
    .replace(""""{ADDRESS}"""", validAddressJson)

  val invalidCharityBeneficiary = Source.fromFile(getClass.getResource("/InvalidCharityBeneficiary.json").getPath).mkString

  val validOtherBeneficiary = Source.fromFile(getClass.getResource("/ValidOtherBeneficiary.json").getPath)
    .mkString
    .replace(""""{ADDRESS}"""", validAddressJson)



  val validBeneficiariesJson = s"""{"individualBeneficiaries":[$validIndividualBeneficiary],"charityBeneficiaries":[$validCharityBeneficiary],"otherBeneficiaries":[$validOtherBeneficiary]}"""

  val invalidBeneficiariesJson = s"""{"charityBeneficiaries": [$invalidCharityBeneficiary]}"""

  val invalidLeadTrusteeJson = s"""{"individual":$validIndividualJson,"company":$validCompanyJson}"""

  val validProtectorsJson = s"""{"individuals":[$validIndividualJson],"companies":[$validCompanyJson]}"""
  val invalidProtectorsJson = s"""{"individuals":[$invalidIndividualJson],"companies":[$invalidCompanyJson]}"""

  val validShareAssetJson = Source.fromFile(getClass.getResource("/ValidShareAsset.json").getPath).mkString
  val validBusinessAssetJson = Source.fromFile(getClass.getResource("/ValidBusinessAsset.json").getPath).mkString
  val validWillIntestacyTrustJson = Source.fromFile(getClass.getResource("/ValidWillIntestacyTrust.json").getPath).mkString
    .replace("\"{SHAREASSETS}\"", validShareAssetJson)
    .replace("\"{BUSINESSASSETS}\"", validBusinessAssetJson)
    .replace("\"{INDIVIDUALBENEFICIARY}\"", validIndividualBeneficiary)
  val validLegalityJson = Source.fromFile(getClass.getResource("/ValidLegality.json").getPath).mkString
  val validTrustJson = Source.fromFile(getClass.getResource("/ValidTrust.json").getPath).mkString
    .replace("\"{WILLINTESTACYTRUST}\"", validWillIntestacyTrustJson)
    .replace("\"{INDIVIDUAL}\"", validIndividualJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
    .replace("\"{LEGALITY}\"", validLegalityJson)
}

trait ScalaDataExamples {

  val settlors = Settlors(Some(List(individual)))

  val address = Address(
    isNonUkAddress = false,
    addressLine1 = "Line 1",
    addressLine2 = Some("Line 2"),
    addressLine3 = Some("Line 3"),
    addressLine4 = Some("Line 4"),
    postcode = Some("NE1 2BR"),
    country = Some("UK")
  )

  val passport = Passport(
    identifier = "IDENTIFIER",
    expiryDate = new DateTime("2020-01-01"),
    countryOfIssue = "UK"
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
    correspondenceAddress = address,
    telephoneNumber = "12345"
  )

  val leadTrusteeIndividual = LeadTrustee(
    individual = Some(individual),
    company = None
  )

  val leadTrusteeCompany = LeadTrustee(
    individual = None,
    company = Some(company)
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

  val leadTrustee = LeadTrustee(Some(individual))

  val legality = Legality("Scotland","Scotland",true)

  val businessAsset = BusinessAsset("Test","Test","This is a description",address,1234)

  val shareAsset = ShareAsset(1234,"Test","Test","1234",1234)

  val assets = Assets(None,None,Some(List(shareAsset,shareAsset)),None,Some(List(businessAsset,businessAsset)))

  val willIntestacyTrust = WillIntestacyTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false,true,Some(30))))),individual)

  val trust = Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),legality,true,leadTrustee,List(individual,individual,individual,individual),
    Protectors(Some(List(individual,individual))),List(individual,individual,individual,individual),Some(willIntestacyTrust))
}

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

package uk.gov.hmrc.common.des

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class TrustEstate(admin: Option[Admin] = None, correspondence: Correspondence, yearsReturns: Option[YearsReturns] = None, assets: Option[Assets] = None, declaration: Declaration, details: Details)

case class Admin(utr: String)

case class Correspondence(abroadIndicator: Boolean, name: Name, addres: Address, phoneNumber: String)

case class Name(firstName: String, middleName: Option[String] = None, lastName: String)

case class Address(line1: String, line2: String, line3: Option[String] = None, line4: Option[String] = None, postCode: Option[String] = None, country: String)

case class Assets(monetary: List[Monetary], propertyOrLand: List[PropertyLand], shares: List[Share], business: List[BusinessAsset], partnerShip: List[Partnership], other: List[OtherAsset])

case class Monetary(assetMonetaryAmount: Double)

case class PropertyLand(buildingLandName: String, address: Option[Address] = None, valueFull: Option[Monetary] = None, valuePrevious: Option[Monetary] = None)

case class Share(numberOfShares: String, orgName: Option[String] = None, utr: Option[String] = None, shareClass: Option[String] = None, shareType: Option[String] = None, shareValue: Option[Monetary] = None)

object Share {
  implicit val reads: Reads[Share] = (
    (JsPath \ "numberOfShares").read[String] and
      (JsPath \ "orgName").readNullable[String] and
      (JsPath \ "utr").readNullable[String] and
      (JsPath \ "class").readNullable[String] and
      (JsPath \ "type").readNullable[String] and
      (JsPath \ "shareValue").readNullable[Monetary]
    ) (Share.apply _)
}

case class BusinessAsset(orgName: String, utr: Option[String] = None, businessDescription: Option[String] = None, address: Option[Address] = None, businessValue: Option[Monetary] = None)

case class Partnership(utr: Option[String] = None, partnershipType: String, partnershipStart: Option[String] = None)

object Partnership {
  implicit val reads: Reads[Partnership] = (
    (JsPath \ "utr").readNullable[String] and
      (JsPath \ "type").read[String] and
      (JsPath \ "partnershipStart").readNullable[String]
  )(Partnership.apply _)
}

case class OtherAsset(description: String, value: Option[Monetary] = None)

case class Declaration(name: Name, address: Address)

case class YearsReturns(taxReturnsNoDues: Option[Boolean] = None, returns: Option[List[YearReturn]] = None)

case class YearReturn(taxReturnYear: String, taxReturnYearC: String)

case class Details(estate: Option[EstateType] = None, trust: Option[TrustType] = None)

case class EstateType(estate: Estate)

case class Estate(entities: Entities, administrationEndDate: Option[String] = None, periodTaxDues: String)

case class Entities(personalRepresentative: PersonalRepresentative, deceased: Will)

case class Identification(nino: Option[Nino] = None, passportId: Option[PassportId] = None)

case class Nino(nino: String)

case class PassportId(passport: PassportType, address: Address)

case class PassportType(number: String, expirationDate: String, countryOfIssue: String)

case class PersonalRepresentative(name: Name, dateOfBirth: String, identification: Identification, phoneNumber: Option[String] = None, email: Option[String] = None)

case class Will(name: Name, dateOfBirth: String, dateOfDeath: String, identification: WillIdentification)

case class WillIdentification(nino: Option[Nino] = None, address: Option[Address] = None)

case class TrustType(trust: Trust)

case class Trust(details: TrustDetails, entities: TrustEntities)

case class TrustDetails(startDate: String, lawCountry: String, administrationCountry: Option[String] = None, residentialStatus: Option[ResidentialStatus] = None, typeOfTrust: String, deedOfVariation: Option[String] = None, interVivos: Option[Boolean] = None, efrbsStartDate: Option[String] = None)

case class TrustEntities(naturalPerson: Option[List[NaturalPerson]] = None, beneficiary: Beneficiary, deceased: Option[Will] = None, leadTrustees: LeadTrustee, trustees: Option[List[Trustee]] = None, protectors: Option[ProtectorType] = None, settlors: SettlorType)

case class NaturalPerson(name: Name, dateOfBirth: String, identification: Identification)

case class Beneficiary(individualDetails: List[IndividualDetails], company: List[Company], trust: List[BeneficiaryTrust], charity: List[Charity], unidentified: List[Unidentified], large: List[Large], other: List[Other])

case class IndividualDetails(name: Name, dateOfBirth: String, vulnerableBeneficiary: Option[Boolean] = None, beneficiaryType: Option[String] = None, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None, identification: Identification)

case class LeadTrustee(leadTrusteeOrg: Option[LeadTrusteeOrg] = None, leadTrusteeInd: Option[LeadTrusteeInd] = None)

case class LeadTrusteeOrg(name: String, phoneNumber: String, email: Option[String] = None, identification: OrgIdentification)

case class LeadTrusteeInd(name: Name, dateOfBirth: String, identification: OrgIdentification, phoneNumber: String, email: Option[String] = None)

case class OrgIdentification(utr: Option[String] = None, address: Option[Address] = None)

case class Trustee(name: Name, dateOfBirth: String, identification: Identification, phoneNumber: Option[String] = None)

case class ProtectorType(protector: Option[List[Protector]] = None, protectorCompany: Option[List[ProtectorCompany]] = None)

case class Protector(name: Name, dateOfBirth: String, identification: Identification)

case class ProtectorCompany(name: String, identification: OrgIdentification)

case class SettlorType(settlor: Option[List[Settlor]] = None, settlorCompany: Option[List[SettlorCompany]] = None)

case class Settlor(name: Name, dateOfBirth: String, identification: Identification)

case class SettlorCompany(name: Name, companyType: String, companyTime: Boolean, identification: OrgIdentification)

case class Company(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None, identification: OrgIdentification)

case class BeneficiaryTrust(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[Boolean] = None, identification: OrgIdentification)

case class Charity(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[Boolean] = None, identification: OrgIdentification)

case class Unidentified(description: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None)

case class Large(organisationName: String, description: Option[String] = None, numberOfBeneficiary: String, identification: OrgIdentification)

case class Other(description: String, address: Address, numberOfBeneficiary: Option[String] = None, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None)

case class ResidentialStatus(uk: Option[UkResidentialStatus] = None, nonUk: Option[NonUkResidentialStatus] = None)

case class UkResidentialStatus(scottishLaw: Boolean, preOffShore: Option[String] = None)

case class NonUkResidentialStatus(sch5atcgga92: Boolean, s218ihta84: Option[Boolean] = None, agentS218IHTA84: Option[Boolean] = None, trusteeStatus: Option[String] = None)

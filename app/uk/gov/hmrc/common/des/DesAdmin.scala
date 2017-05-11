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
import play.api.libs.json.{JsPath, Json, Reads}

case class DesTrustEstate(admin: Option[DesAdmin] = None, correspondence: DesCorrespondence, yearsReturns: Option[DesYearsReturns] = None, assets: Option[DesAssets] = None, declaration: DesDeclaration, details: DesDetails)

object DesTrustEstate {
  implicit val desTrustEstateFormats = Json.format[DesTrustEstate]
}


case class DesAdmin(utr: String)

object DesAdmin {
  implicit val formats = Json.format[DesAdmin]
}

case class DesCorrespondence(abroadIndicator: Boolean, name: DesName, addres: DesAddress, phoneNumber: String)

object DesCorrespondence {
  implicit val formats = Json.format[DesCorrespondence]
}

case class DesName(firstName: String, middleName: Option[String] = None, lastName: String)

object DesName {
  implicit val formats = Json.format[DesName]
}

case class DesAddress(line1: String, line2: String, line3: Option[String] = None, line4: Option[String] = None, postCode: Option[String] = None, country: String)

object DesAddress {
  implicit val formats = Json.format[DesAddress]
}

case class DesAssets(monetary: List[DesMonetary], propertyOrLand: List[DesPropertyLand], shares: List[DesShare], business: List[DesBusinessAsset], partnerShip: List[DesPartnership], other: List[DesOtherAsset])

object DesAssets {
  implicit val formats = Json.format[DesAssets]
}

case class DesMonetary(assetMonetaryAmount: Double)

object DesMonetary {
  implicit val formats = Json.format[DesMonetary]
}

case class DesPropertyLand(buildingLandName: String, address: Option[DesAddress] = None, valueFull: Option[DesMonetary] = None, valuePrevious: Option[DesMonetary] = None)

object DesPropertyLand {
  implicit val formats = Json.format[DesPropertyLand]
}

case class DesShare(numberOfShares: String, orgName: Option[String] = None, utr: Option[String] = None, shareClass: Option[String] = None, shareType: Option[String] = None, shareValue: Option[DesMonetary] = None)

object DesShare {
  implicit val reads: Reads[DesShare] = (
    (JsPath \ "numberOfShares").read[String] and
      (JsPath \ "orgName").readNullable[String] and
      (JsPath \ "utr").readNullable[String] and
      (JsPath \ "class").readNullable[String] and
      (JsPath \ "type").readNullable[String] and
      (JsPath \ "shareValue").readNullable[DesMonetary]
    ) (DesShare.apply _)
}

case class DesBusinessAsset(orgName: String, utr: Option[String] = None, businessDescription: Option[String] = None, address: Option[DesAddress] = None, businessValue: Option[DesMonetary] = None)

object DesBusinessAsset {
  implicit val formats = Json.format[DesBusinessAsset]
}

case class DesPartnership(utr: Option[String] = None, partnershipType: String, partnershipStart: Option[String] = None)

object DesPartnership {
  implicit val reads: Reads[DesPartnership] = (
    (JsPath \ "utr").readNullable[String] and
      (JsPath \ "type").read[String] and
      (JsPath \ "partnershipStart").readNullable[String]
    ) (DesPartnership.apply _)
}

case class DesOtherAsset(description: String, value: Option[DesMonetary] = None)

object DesOtherAsset {
  implicit val formats = Json.format[DesOtherAsset]
}

case class DesDeclaration(name: DesName, address: DesAddress)

object DesDeclaration {
  implicit val formats = Json.format[DesDeclaration]
}

case class DesYearsReturns(taxReturnsNoDues: Option[Boolean] = None, returns: Option[List[DesYearReturn]] = None)

object DesYearsReturns {
  implicit val formats = Json.format[DesYearsReturns]
}

case class DesYearReturn(taxReturnYear: String, taxReturnYearC: String)

object DesYearReturn {
  implicit val formats = Json.format[DesYearReturn]
}

case class DesDetails(estate: Option[DesEstateType] = None, trust: Option[DesTrustType] = None)

object DesDetails {
  implicit val formats = Json.format[DesDetails]
}

case class DesEstateType(estate: DesEstate)

object DesEstateType {
  implicit val formats = Json.format[DesEstateType]
}

case class DesEstate(entities: DesEntities, administrationEndDate: Option[String] = None, periodTaxDues: String)

object DesEstate {
  implicit val formats = Json.format[DesEstate]
}

case class DesEntities(personalRepresentative: DesPersonalRepresentative, deceased: DesWill)

object DesEntities {
  implicit val formats = Json.format[DesEntities]
}

case class DesIdentification(nino: Option[DesNino] = None, passportId: Option[DesPassportId] = None)

object DesIdentification {
  implicit val formats = Json.format[DesIdentification]
}

case class DesNino(nino: String)

object DesNino {
  implicit val formats = Json.format[DesNino]
}

case class DesPassportId(passport: DesPassportType, address: DesAddress)

object DesPassportId {
  implicit val formats = Json.format[DesPassportId]
}

case class DesPassportType(number: String, expirationDate: String, countryOfIssue: String)

object DesPassportType {
  implicit val formats = Json.format[DesPassportType]
}

case class DesPersonalRepresentative(name: DesName, dateOfBirth: String, identification: DesIdentification, phoneNumber: Option[String] = None, email: Option[String] = None)

object DesPersonalRepresentative {
  implicit val formats = Json.format[DesPersonalRepresentative]
}

case class DesWill(name: DesName, dateOfBirth: String, dateOfDeath: String, identification: DesWillIdentification)

object DesWill {
  implicit val formats = Json.format[DesWill]
}

case class DesWillIdentification(nino: Option[DesNino] = None, address: Option[DesAddress] = None)

object DesWillIdentification {
  implicit val formats = Json.format[DesWillIdentification]
}

case class DesTrustType(trust: DesTrust)

object DesTrustType {
  implicit val formats = Json.format[DesTrustType]
}

case class DesTrust(details: DesTrustDetails, entities: DesTrustEntities)

object DesTrust {
  implicit val formats = Json.format[DesTrust]
}

case class DesTrustDetails(startDate: String, lawCountry: String, administrationCountry: Option[String] = None, residentialStatus: Option[DesResidentialStatus] = None, typeOfTrust: String, deedOfVariation: Option[String] = None, interVivos: Option[Boolean] = None, efrbsStartDate: Option[String] = None)

object DesTrustDetails {
  implicit val formats = Json.format[DesTrustDetails]
}

case class DesTrustEntities(naturalPerson: Option[List[DesNaturalPerson]] = None, beneficiary: DesBeneficiary, deceased: Option[DesWill] = None, leadTrustees: DesLeadTrustee, trustees: Option[List[DesTrustee]] = None, protectors: Option[DesProtectorType] = None, settlors: DesSettlorType)

object DesTrustEntities {
  implicit val formats = Json.format[DesTrustEntities]
}

case class DesNaturalPerson(name: DesName, dateOfBirth: String, identification: DesIdentification)

object DesNaturalPerson {
  implicit val formats = Json.format[DesNaturalPerson]
}

case class DesBeneficiary(individualDetails: List[DesIndividualDetails], company: List[DesCompany], trust: List[DesBeneficiaryTrust], charity: List[DesCharity], unidentified: List[DesUnidentified], large: List[DesLarge], other: List[DesOther])

object DesBeneficiary {
  implicit val formats = Json.format[DesBeneficiary]
}

case class DesIndividualDetails(name: DesName, dateOfBirth: String, vulnerableBeneficiary: Option[Boolean] = None, beneficiaryType: Option[String] = None, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None, identification: DesIdentification)

object DesIndividualDetails {
  implicit val formats = Json.format[DesIndividualDetails]
}

case class DesLeadTrustee(leadTrusteeOrg: Option[DesLeadTrusteeOrg] = None, leadTrusteeInd: Option[DesLeadTrusteeInd] = None)

object DesLeadTrustee {
  implicit val formats = Json.format[DesLeadTrustee]
}

case class DesLeadTrusteeOrg(name: String, phoneNumber: String, email: Option[String] = None, identification: DesOrgIdentification)

object DesLeadTrusteeOrg {
  implicit val formats = Json.format[DesLeadTrusteeOrg]
}

case class DesLeadTrusteeInd(name: DesName, dateOfBirth: String, identification: DesOrgIdentification, phoneNumber: String, email: Option[String] = None)

object DesLeadTrusteeInd {
  implicit val formats = Json.format[DesLeadTrusteeInd]
}

case class DesOrgIdentification(utr: Option[String] = None, address: Option[DesAddress] = None)

object DesOrgIdentification {
  implicit val formats = Json.format[DesOrgIdentification]
}

case class DesTrustee(name: DesName, dateOfBirth: String, identification: DesIdentification, phoneNumber: Option[String] = None)

object DesTrustee {
  implicit val formats = Json.format[DesTrustee]
}

case class DesProtectorType(protector: Option[List[DesProtector]] = None, protectorCompany: Option[List[DesProtectorCompany]] = None)

object DesProtectorType {
  implicit val formats = Json.format[DesProtectorType]
}

case class DesProtector(name: DesName, dateOfBirth: String, identification: DesIdentification)

object DesProtector {
  implicit val formats = Json.format[DesProtector]
}

case class DesProtectorCompany(name: String, identification: DesOrgIdentification)

object DesProtectorCompany {
  implicit val formats = Json.format[DesProtectorCompany]
}

case class DesSettlorType(settlor: Option[List[DesSettlor]] = None, settlorCompany: Option[List[DesSettlorCompany]] = None)

object DesSettlorType {
  implicit val formats = Json.format[DesSettlorType]
}

case class DesSettlor(name: DesName, dateOfBirth: String, identification: DesIdentification)

object DesSettlor {
  implicit val formats = Json.format[DesSettlor]
}

case class DesSettlorCompany(name: DesName, companyType: String, companyTime: Boolean, identification: DesOrgIdentification)

object DesSettlorCompany {
  implicit val formats = Json.format[DesSettlorCompany]
}

case class DesCompany(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None, identification: DesOrgIdentification)

object DesCompany {
  implicit val formats = Json.format[DesCompany]
}

case class DesBeneficiaryTrust(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[Boolean] = None, identification: DesOrgIdentification)

object DesBeneficiaryTrust {
  implicit val formats = Json.format[DesBeneficiaryTrust]
}

case class DesCharity(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[Boolean] = None, identification: DesOrgIdentification)

object DesCharity {
  implicit val formats = Json.format[DesCharity]
}

case class DesUnidentified(description: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None)

object DesUnidentified {
  implicit val formats = Json.format[DesUnidentified]
}

case class DesLarge(organisationName: String, description: Option[String] = None, numberOfBeneficiary: String, identification: DesOrgIdentification)

object DesLarge {
  implicit val formats = Json.format[DesLarge]
}

case class DesOther(description: String, address: DesAddress, numberOfBeneficiary: Option[String] = None, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None)

object DesOther {
  implicit val formats = Json.format[DesOther]
}

case class DesResidentialStatus(uk: Option[DesUkResidentialStatus] = None, nonUk: Option[DesNonUkResidentialStatus] = None)

object DesResidentialStatus {
  implicit val formats = Json.format[DesResidentialStatus]
}

case class DesUkResidentialStatus(scottishLaw: Boolean, preOffShore: Option[String] = None)

object DesUkResidentialStatus {
    implicit val formats = Json.format[DesUkResidentialStatus]
}

case class DesNonUkResidentialStatus(sch5atcgga92: Boolean, s218ihta84: Option[Boolean] = None, agentS218IHTA84: Option[Boolean] = None, trusteeStatus: Option[String] = None)

object DesNonUkResidentialStatus {
  implicit val formats = Json.format[DesNonUkResidentialStatus]
}

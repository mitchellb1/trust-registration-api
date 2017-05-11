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

case class DesTrustEstate(admin: Option[DesAdmin] = None, correspondence: DesCorrespondence, yearsReturns: Option[DesYearsReturns] = None, assets: Option[DesAssets] = None, declaration: Declaration, details: DesDetails)

case class DesAdmin(utr: String)

case class DesCorrespondence(abroadIndicator: Boolean, name: DesName, addres: DesAddress, phoneNumber: String)

case class DesName(firstName: String, middleName: Option[String] = None, lastName: String)

case class DesAddress(line1: String, line2: String, line3: Option[String] = None, line4: Option[String] = None, postCode: Option[String] = None, country: String)

case class DesAssets(monetary: List[DesMonetary], propertyOrLand: List[DesPropertyLand], shares: List[DesShare], business: List[DesBusinessAsset], partnerShip: List[DesPartnership], other: List[DesOtherAsset])

case class DesMonetary(assetMonetaryAmount: Double)

object DesMonetary{
  implicit val formats = Json.format[DesMonetary]
}

case class DesPropertyLand(buildingLandName: String, address: Option[DesAddress] = None, valueFull: Option[DesMonetary] = None, valuePrevious: Option[DesMonetary] = None)

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

case class DesPartnership(utr: Option[String] = None, partnershipType: String, partnershipStart: Option[String] = None)

object DesPartnership {
  implicit val reads: Reads[DesPartnership] = (
    (JsPath \ "utr").readNullable[String] and
      (JsPath \ "type").read[String] and
      (JsPath \ "partnershipStart").readNullable[String]
  )(DesPartnership.apply _)
}

case class DesOtherAsset(description: String, value: Option[DesMonetary] = None)

case class Declaration(name: DesName, address: DesAddress)

case class DesYearsReturns(taxReturnsNoDues: Option[Boolean] = None, returns: Option[List[DesYearReturn]] = None)

case class DesYearReturn(taxReturnYear: String, taxReturnYearC: String)

case class DesDetails(estate: Option[DesEstateType] = None, trust: Option[DesTrustType] = None)

case class DesEstateType(estate: DesEstate)

case class DesEstate(entities: DesEntities, administrationEndDate: Option[String] = None, periodTaxDues: String)

case class DesEntities(personalRepresentative: DesPersonalRepresentative, deceased: DesWill)

case class DesIdentification(nino: Option[DesNino] = None, passportId: Option[DesPassportId] = None)

case class DesNino(nino: String)

case class DesPassportId(passport: DesPassportType, address: DesAddress)

case class DesPassportType(number: String, expirationDate: String, countryOfIssue: String)

case class DesPersonalRepresentative(name: DesName, dateOfBirth: String, identification: DesIdentification, phoneNumber: Option[String] = None, email: Option[String] = None)

case class DesWill(name: DesName, dateOfBirth: String, dateOfDeath: String, identification: DesWillIdentification)

case class DesWillIdentification(nino: Option[DesNino] = None, address: Option[DesAddress] = None)

case class DesTrustType(trust: DesTrust)

case class DesTrust(details: DesTrustDetails, entities: DesTrustEntities)

case class DesTrustDetails(startDate: String, lawCountry: String, administrationCountry: Option[String] = None, residentialStatus: Option[DesResidentialStatus] = None, typeOfTrust: String, deedOfVariation: Option[String] = None, interVivos: Option[Boolean] = None, efrbsStartDate: Option[String] = None)

case class DesTrustEntities(naturalPerson: Option[List[DesNaturalPerson]] = None, beneficiary: DesBeneficiary, deceased: Option[DesWill] = None, leadTrustees: DesLeadTrustee, trustees: Option[List[DesTrustee]] = None, protectors: Option[ProtectorType] = None, settlors: DesSettlorType)

case class DesNaturalPerson(name: DesName, dateOfBirth: String, identification: DesIdentification)

case class DesBeneficiary(individualDetails: List[DesIndividualDetails], company: List[DesCompany], trust: List[DesBeneficiaryTrust], charity: List[DesCharity], unidentified: List[DesUnidentified], large: List[DesLarge], other: List[DesOther])

case class DesIndividualDetails(name: DesName, dateOfBirth: String, vulnerableBeneficiary: Option[Boolean] = None, beneficiaryType: Option[String] = None, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None, identification: DesIdentification)

case class DesLeadTrustee(leadTrusteeOrg: Option[DesLeadTrusteeOrg] = None, leadTrusteeInd: Option[DesLeadTrusteeInd] = None)

case class DesLeadTrusteeOrg(name: String, phoneNumber: String, email: Option[String] = None, identification: DesOrgIdentification)

case class DesLeadTrusteeInd(name: DesName, dateOfBirth: String, identification: DesOrgIdentification, phoneNumber: String, email: Option[String] = None)

case class DesOrgIdentification(utr: Option[String] = None, address: Option[DesAddress] = None)

case class DesTrustee(name: DesName, dateOfBirth: String, identification: DesIdentification, phoneNumber: Option[String] = None)

case class ProtectorType(protector: Option[List[Protector]] = None, protectorCompany: Option[List[ProtectorCompany]] = None)

case class Protector(name: DesName, dateOfBirth: String, identification: DesIdentification)

case class ProtectorCompany(name: String, identification: DesOrgIdentification)

case class DesSettlorType(settlor: Option[List[DesSettlor]] = None, settlorCompany: Option[List[DesSettlorCompany]] = None)

case class DesSettlor(name: DesName, dateOfBirth: String, identification: DesIdentification)

case class DesSettlorCompany(name: DesName, companyType: String, companyTime: Boolean, identification: DesOrgIdentification)

case class DesCompany(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None, identification: DesOrgIdentification)

case class DesBeneficiaryTrust(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[Boolean] = None, identification: DesOrgIdentification)

case class DesCharity(organisationName: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[Boolean] = None, identification: DesOrgIdentification)

case class DesUnidentified(description: String, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None)

case class DesLarge(organisationName: String, description: Option[String] = None, numberOfBeneficiary: String, identification: DesOrgIdentification)

case class DesOther(description: String, address: DesAddress, numberOfBeneficiary: Option[String] = None, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None)

case class DesResidentialStatus(uk: Option[DesUkResidentialStatus] = None, nonUk: Option[DesNonUkResidentialStatus] = None)

case class DesUkResidentialStatus(scottishLaw: Boolean, preOffShore: Option[String] = None)

case class DesNonUkResidentialStatus(sch5atcgga92: Boolean, s218ihta84: Option[Boolean] = None, agentS218IHTA84: Option[Boolean] = None, trusteeStatus: Option[String] = None)

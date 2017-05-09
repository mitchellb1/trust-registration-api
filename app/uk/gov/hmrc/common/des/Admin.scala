package uk.gov.hmrc.common.des

case class TrustEstate(admin: Option[Admin]=None, correspondence: Correspondence, yearsReturns: Option[YearsReturns]=None, assets: Option[Assets] = None, declaration: Declaration, details: Details)
case class Admin(utr: String)
case class Correspondence(abroadIndicator: Boolean, name: Name, addres: Address, phoneNumber: String)
case class Name(firstName: String, middleName: Option[String] = None, lastName: String)
case class Address(line1: String, line2: String, line3: Option[String] = None, line4: Option[String] = None, postCode: Option[String] = None, country: String)
case class Assets(monetary: List[Monetary], propertyOrLand: List[PropertyLand], shares: List[Share], business: List[BusinessAsset], partnerShip: List[Partnership], other:  List[OtherAsset])
case class Monetary(assetMonetaryAmount: Double)
case class PropertyLand(buildingLandName: String, address: Option[Address] = None, valueFull: Option[Monetary] = None, valuePrevious: Option[Monetary] = None)
case class Share(numberOfShares: String, orgName: Option[String] = None, utr: Option[String] = None, shareClass: Option[String] = None, shareType: Option[String] = None, shareValue: Option[Monetary] = None)
case class BusinessAsset(orgName: String, utr: Option[String] = None, businessDescription: Option[String] = None, address: Option[Address] = None, businessValue: Option[Monetary] = None)
case class Partnership(utr: Option[String] = None, partnershipType: String, partnershipStart: Option[String] = None)
case class OtherAsset(description: String, value: Option[Monetary] = None)
case class Declaration(name: Name, address: Address)
case class YearsReturns(taxReturnsNoDues: Option[Boolean] = None, returns: Option[List[YearReturn]] = None)
case class YearReturn(taxReturnYear: String, taxReturnYearC: String)
case class Details(estate: Option[EstateType] = None, trust: Option[TrustType] = None)
case class EstateType(estate: Estate)
case class Estate(entities: Entities, administrationEndDate: Option[String] = None, periodTaxDues: String)
case class Entities(personalRepresentative: PersonalRepresentative, deceased: Will)
case class Identification(nino: Option[Nino] = None, passportId: Option[PassportId]=None)
case class Nino(nino: String)
case class PassportId(passport: PassportType, address: Address)
case class PassportType(number: String, expirationDate: String, countryOfIssue: String)
case class PersonalRepresentative(name: Name, dateOfBirth: String, identification: Identification, phoneNumber: Option[String]=None, email: Option[String]=None)
case class Will(name: Name, dateOfBirth: String, dateOfDeath: String,  identification: WillIdentification)
case class WillIdentification(nino: Option[Nino]=None, address: Option[Address] = None)
case class TrustType(trust: Trust)
case class Trust(details: TrustDetails, entities: TrustEntities)
case class TrustDetails()
case class TrustEntities(naturalPerson: List[NaturalPerson], beneficiary: Beneficiary, deceased: Will, leadTrustees: LeadTrustee)
case class NaturalPerson(name: Name, dateOfBirth: String, identification: Identification)
case class Beneficiary(individualDetails: IndividualDetails)
case class IndividualDetails(name: Name,dateOfBirth: String, vulnerableBeneficiary: Option[Boolean] = None, beneficiaryType: Option[String] = None, beneficiaryDiscretion: Option[Boolean] = None, beneficiaryShareOfIncome: Option[String] = None, identification: Identification)
case class LeadTrustee(leadTrusteeOrg: Option[LeadTrusteeOrg] = None, leadTrusteeInd: Option[LeadTrusteeInd] = None)
case class LeadTrusteeOrg()
case class LeadTrusteeInd()




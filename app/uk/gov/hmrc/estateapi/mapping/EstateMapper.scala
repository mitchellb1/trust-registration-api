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
import uk.gov.hmrc.common.des._
import uk.gov.hmrc.common.mapping.AddressMapper
import uk.gov.hmrc.common.rest.resources.core._
import uk.gov.hmrc.estateapi.rest.resources.core.{Estate, EstateRequest, PersonalRepresentative}


trait EstateMapper{

  def toDes(domainEstate: Estate): DesTrustEstate = {

    val date = new DateTime("2016-03-31")
    val nino = "WA123456A"
    val phoneNumber = "0191 000 0000"
    val email = "john.doe@somewhere.co.uk"
    val name = DesName("Joe", Some("John"), "Doe")
    val correspondenceAddress: DesAddress = AddressMapper.toDes(domainEstate.correspondenceAddress)
//    val declarationAddress = AddressMapper.toDes(domainEstate.declaration.correspondenceAddress)
//    val identificationAddress = AddressMapper.toDes(Some(domainEstate.personalRepresentative.individual.correspondenceAddress))
    val address = DesAddress(
      line1 = "address line 1",
      line2 = "address line 1",
      line3 = Some("address line 1"),
      line4 = Some("address line 1"),
      postCode = Some("NE45 23PQ"),
      country = "GB")

    val admin = DesAdmin("12345ABCDE")
    //if (domainEstate.correspondenceAddress.countryCode.equals("GB")

//    val correspondence = DesCorrespondence(abroadIndicator = true, "SomeName thats not a name", correspondenceAddress, phoneNumber)
    val desCorrespondence: DesCorrespondence = DesCorrespondenceMapper.toDes(true,
      domainEstate.estateName,
      domainEstate.correspondenceAddress,
      domainEstate.telephoneNumber)

    val yearsReturns = DesYearsReturns(Some(true), None)
    //val yearsReturns = DesYearsReturns(taxReturnsNoDues false, returns: Option[List[DesYearReturn]] = None)

    val declaration =  DesDeclaration(name, address)

    val desWillId = DesWillIdentification(Some(nino), None)

    val deceased = DesWill(name, date, date, identification = desWillId)

    val passport = DesPassportType("12134567", date, "GB")

    //val identification = DesIdentification(Some(nino), None, None)
    val identification = DesIdentification(None, Some(passport), Some(address))

    val personalRepresentative = DesPersonalRepresentative(name, date, identification, Some(phoneNumber), Some(email))

    val entities = DesEntities(personalRepresentative: DesPersonalRepresentative, deceased: DesWill)

    val administrationEndDate = Some(date)

    val periodTaxDues = "01"

    val estate = DesEstate(entities, administrationEndDate, periodTaxDues)

    val details = DesDetails(Some(estate), trust = None)

    DesTrustEstate(
      Some(admin),
      desCorrespondence,
      //  None,
      Some(yearsReturns),
      None,
      //    Some(assets),
      declaration: DesDeclaration,
      details)
  }

  def toDomain(desTrustEstate: DesTrustEstate): EstateRequest = {

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
      declaration = declaration,
      telephoneNumber = "0191 123 0000")

    EstateRequest(validEstateWithPersonalRepresentative)

  }
}

object EstateMapper extends EstateMapper

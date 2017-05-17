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

package uk.gov.hmrc.common.mapping

import uk.gov.hmrc.common.des._
import uk.gov.hmrc.estateapi.rest.resources.core.Estate

trait EstateMapper {

//  lazy val desEstate = Source.fromFile(getClass.getResource("/des/desCompleteEstate.json").getPath).mkString
//  lazy val domainEstate = Source.fromFile(getClass.getResource("/des/desCompleteEstate.json").getPath).mkString


  def toDes(domainEstate: Estate): DesTrustEstate = {
    //Using the domain

    val desPersonalRepresentativeAddress = AddressMap.toDes(domainEstate.personalRepresentative.individual.correspondenceAddress.get)

    val desPersonalRepresentativeName = DesName(
      domainEstate.personalRepresentative.individual.givenName,
      Some(domainEstate.personalRepresentative.individual.otherName.getOrElse("")),
      domainEstate.personalRepresentative.individual.familyName)

    //    val personalRepresentative: DesPersonalRepresentative = {
    //      DesPersonalRepresentative(
    //        name = desPersonalRepresentativeName,
    //        dateOfBirth = domainEstate.personalRepresentative.individual.dateOfBirth.formatted("yyyy-MM-dd"),
    //        identification = DesIdentification(None, Some(passport), Some(address)),
    //        phoneNumber = Some(domainEstate.personalRepresentative.telephoneNumber),
    //        email = Some(domainEstate.personalRepresentative.email))
    //    }

    val desDeclarationAddress = AddressMap.toDes(domainEstate.declaration.correspondenceAddress)
    val desDeclarationName = DesName(
      firstName = domainEstate.declaration.givenName,
      middleName = domainEstate.declaration.otherName,
      lastName = domainEstate.declaration.familyName)

    //val declaration =  DesDeclaration(name, address)
    val declaration = DesDeclaration(desDeclarationName, desDeclarationAddress)


    //val administrationEndDate = Some(date)
    val administrationEndDate = domainEstate.adminPeriodFinishedDate


    //------------------------------------------------------------------------------------------------------------------


    //Hard coded
    val date = "2016-03-31"
    val nino = "WA123456A"
    val phoneNumber = "0191 000 0000"
    val email = "john.doe@somewhere.co.uk"
    val name = DesName("Joe", Some("John"), "Doe")
    val address = DesAddress(
      line1 = "address line 1",
      line2 = "address line 1",
      line3 = Some("address line 1"),
      line4 = Some("address line 1"),
      postCode = Some("NE45 23PQ"),
      country = "GB")

    val admin = DesAdmin("12345ABCDE")


    val correspondence = DesCorrespondence(abroadIndicator = true, "SomeName thats not a name", address, phoneNumber)
    val yearsReturns = DesYearsReturns(Some(true), None)
    //val yearsReturns = DesYearsReturns(taxReturnsNoDues false, returns: Option[List[DesYearReturn]] = None)


    val desWillId = DesWillIdentification(Some(nino), None)

    val deceased = DesWill(name, date, date, identification = desWillId)

    val passport = DesPassportType("12134567", date, "GB")

    //val identification = DesIdentification(Some(nino), None, None)
    val identification = DesIdentification(None, Some(passport), Some(address))

    // val personalRepresentative = DesPersonalRepresentative(name, date, identification, Some(phoneNumber), Some(email))
    val personalRepresentative = DesPersonalRepresentative(desPersonalRepresentativeName, date, identification, Some(phoneNumber), Some(email))

    val entities = DesEntities(personalRepresentative: DesPersonalRepresentative, deceased: DesWill)


    val periodTaxDues = "01"

    val estate = DesEstate(entities, administrationEndDate, periodTaxDues)

    val details = DesDetails(Some(estate), trust = None)

    val completeEstate = DesTrustEstate(
      Some(admin),
      correspondence,
      //  None,
      Some(yearsReturns),
      None,
      //    Some(assets),
      declaration: DesDeclaration,
      details)




    completeEstate
  }



}

object EstateMapper extends EstateMapper

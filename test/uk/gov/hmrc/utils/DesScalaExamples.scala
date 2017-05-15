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

import uk.gov.hmrc.common.des._

trait DesScalaExamples {

  val date = "2016-03-31"
  val nino = DesNino("WA1234556A")
  val phoneNumber = "0191 000 0000"
  val email = "john.Doe@somewhere.co.uk"
  val name = DesName("Joe", Some("John"), "Doe")
  val address = DesAddress(
    line1 = "Line 1",
    line2 = "Line 1",
    line3 = Some("Line 1"),
    line4 = Some("Line 1"),
    postCode = None,
    country = "GB")

  val admin = DesAdmin("12345ABCDE")


  val correspondence = DesCorrespondence(abroadIndicator = true, "SomeName thats not a name", address, phoneNumber)
  val yearsReturns = DesYearsReturns(Some(true), None)
  //val yearsReturns = DesYearsReturns(taxReturnsNoDues false, returns: Option[List[DesYearReturn]] = None)

  //val assets = DesAssets
  val declaration =  DesDeclaration(name, address)




  val desWillId = DesWillIdentification(Some(nino), Some(address))

  val deceased = DesWill(name, date, date, identification = desWillId)

  val passport = DesPassportType("passportNum", date, "GB")

  val passportId = DesPassportId(passport: DesPassportType, address)

  //val identification = DesIdentification(Some(nino), None)
  val identification = DesIdentification(None, Some(passportId))

  val personalRepresentative = DesPersonalRepresentative(name, date, identification, Some(phoneNumber), Some(email))

  val entities = DesEntities(personalRepresentative: DesPersonalRepresentative, deceased: DesWill)

  val administrationEndDate = Some(date)

  val periodTaxDues = "01"

  val estate = DesEstate(entities, administrationEndDate, periodTaxDues)

 // val desEstateType = DesEstateType(estate)

  val details = DesDetails(Some(estate), trust = None)

  val completeValidDesEstate = DesTrustEstate(
    Some(admin),
  correspondence,
//  None,
    Some(yearsReturns),
  None,

//    Some(assets),
  declaration: DesDeclaration,
  details)
}

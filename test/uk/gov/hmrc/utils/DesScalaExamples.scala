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
import uk.gov.hmrc.common.des._

trait DesScalaExamples {

  val date = new DateTime("2016-03-31")
  val nino = "WA123456A"
  val phoneNumber = "0191 000 0000"
  val email = "john.doe@somewhere.co.uk"
  val desName = DesName("Joe", Some("John"), "Doe")
  val desAddress = DesAddress(
    line1 = "address line 1",
    line2 = "address line 1",
    line3 = Some("address line 1"),
    line4 = Some("address line 1"),
    postCode = Some("NE45 23PQ"),
    country = "GB")

  val admin = DesAdmin("12345ABCDE")


  val correspondence = DesCorrespondence(abroadIndicator = true, "Test Estate", desAddress, phoneNumber)

  val yearsReturns = DesYearsReturns(Some(true), None)

  val desDeclaration =  DesDeclaration(desName, desAddress)

  val desWillId = DesWillIdentification(Some(nino), None)

  val desDeceased = DesWill(desName, date, date, identification = desWillId)

  val desPassport = DesPassportType("12134567", date, "GB")

  val desIdentification = DesIdentification(None, Some(desPassport), Some(desAddress))

  val desPersonalRepresentative = DesPersonalRepresentative(desName, date, desIdentification, Some(phoneNumber), Some(email))

  val entities = DesEntities(desPersonalRepresentative: DesPersonalRepresentative, desDeceased: DesWill)

  val administrationEndDate = Some(date)

  val periodTaxDues = "01"

  val estate = DesEstate(entities, administrationEndDate, periodTaxDues)

  val details = DesDetails(Some(estate), trust = None)

  val completeValidDesEstate = DesTrustEstate(
    Some(admin),
    correspondence,
    Some(yearsReturns),
    desDeclaration: DesDeclaration,
    details)

  val desOrgIdentification = DesOrgIdentification(Some("19423480234"),Some(desAddress))
}

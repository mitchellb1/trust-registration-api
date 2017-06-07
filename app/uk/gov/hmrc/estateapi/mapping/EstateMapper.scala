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
import uk.gov.hmrc.common.mapping.todes._
import uk.gov.hmrc.common.mapping.todomain.{DeceasedMapper, DeclarationMapper, PersonalRepresentativeMapper}
import uk.gov.hmrc.common.rest.resources.core.{Deceased, YearsOfTaxConsequence}
import uk.gov.hmrc.estateapi.rest.resources.core.{Estate, PersonalRepresentative}


object EstateMapper {

  val reasonForSettingUpEstate = Map("01" -> "incomeTaxDueMoreThan10000",
    "02" -> "saleOfEstateAssetsMoreThan250000",
    "03" -> "saleOfEstateAssetsMoreThan500000",
    "04" -> "worthMoreThanTwoAndHalfMillionAtTimeOfDeath")

  def toDes(domainEstate: Estate): DesTrustEstate = {

    val deceased: DesWill = DesWillMapper.toDes(domainEstate.deceased)

    val personalRepresentative: DesPersonalRepresentative = DesPersonalRepresentativeMapper.toDes(domainEstate.personalRepresentative)

    val entities = DesEntities(personalRepresentative = personalRepresentative, deceased = deceased)

    val administrationEndDate: Option[DateTime] = domainEstate.adminPeriodFinishedDate

    val periodTaxDues = reasonForSettingUpEstate.find(_._2 == domainEstate.reasonEstateSetup).get._1

    val desEstate: DesEstate = DesEstate(
      entities = entities,
      administrationEndDate = administrationEndDate,
      periodTaxDues = periodTaxDues)

    val admin = domainEstate.utr.map(utr => DesAdmin(utr))

    val correspondence: DesCorrespondence = DesCorrespondenceMapper.toDes(domainEstate)

    val yearsReturns: Option[DesYearsReturns] = DesYearReturnsMapper.toDes(domainEstate.yearsOfTaxConsequence)

    val declaration = DesDeclarationMapper.toDes(domainEstate.declaration)

    val details = DesDetails(Some(desEstate), trust = None)

    DesTrustEstate(
      admin = admin,
      correspondence = correspondence,
      yearsReturns = yearsReturns,
      declaration = declaration,
      details = details
    )
  }

  def toDomain(desTrustEstate: DesTrustEstate) : Estate = {

    Estate(estateName = desTrustEstate.correspondence.name,
      correspondenceAddress = AddressMapper.toDomain(desTrustEstate.correspondence.address),
      personalRepresentative = PersonalRepresentativeMapper.toDomain(desTrustEstate.details.estate.get.entities.personalRepresentative),
      adminPeriodFinishedDate = desTrustEstate.details.estate.get.administrationEndDate,
      reasonEstateSetup = (reasonForSettingUpEstate get desTrustEstate.details.estate.get.periodTaxDues).get,
      declaration = DeclarationMapper.toDomain(desTrustEstate.declaration,new DateTime("2016-03-31"),true),//TODO: For declaration, we have not got a field to map confirmation or date.
      deceased = DeceasedMapper.toDomain(desTrustEstate.details.estate.get.entities.deceased),
      telephoneNumber = desTrustEstate.correspondence.phoneNumber,
      yearsOfTaxConsequence = None,
      utr = Some(desTrustEstate.admin.get.utr)
      )
  }
}

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
import uk.gov.hmrc.estateapi.rest.resources.core.Estate


object EstateMapper {

  val reasonForSettingUpEstate = Map("01" -> "incomeTaxDueMoreThan10000", "02" -> "saleOfEstateAssetsMoreThan250000", "03" -> "saleOfEstateAssetsMoreThan500000", "04" -> "worthMoreThanTwoAndHalfMillionAtTimeOfDeath")

  def toDes(domainEstate: Estate): DesTrustEstate = {

    val personalRepresentative: DesPersonalRepresentative = DesPersonalRepresentativeMapper.toDes(domainEstate.personalRepresentative)

    val deceased: DesWill = DesWillMapper.toDes(domainEstate.deceased)

    val administrationEndDate: Option[DateTime] = domainEstate.adminPeriodFinishedDate

    val correspondence: DesCorrespondence = DesCorrespondenceMapper.toDes(domainEstate)

    val yearReturns = DesYearReturnsMapper.toDes(domainEstate.yearsOfTaxConsequence)

    val periodTaxDues = reasonForSettingUpEstate.find(_._2 == domainEstate.reasonEstateSetup).get._1

    val estate: DesEstate = DesEstate(DesEntities(personalRepresentative, deceased),
      administrationEndDate,
      periodTaxDues)

    DesTrustEstate(
      None,
      correspondence,
      yearReturns,
      DesDeclarationMapper.toDes(domainEstate.declaration),
      DesDetails(Some(estate), trust = None)
    )
  }

  def toDomain(estate: DesEstate, address: DesAddress, declaration: DesDeclaration, correspondence: DesCorrespondence, deceased: DesWill) : Estate = {

    Estate(correspondence.name,
      AddressMapper.toDomain(address),
      PersonalRepresentativeMapper.toDomain(estate.entities.personalRepresentative),
      estate.administrationEndDate,
      (reasonForSettingUpEstate get estate.periodTaxDues).get,
      DeclarationMapper.toDomain(declaration,new DateTime("2016-03-31"),true),//TODO: For declaration, we have not got a field to map confirmation or date.
      DeceasedMapper.toDomain(deceased),
      correspondence.phoneNumber)
  }
}

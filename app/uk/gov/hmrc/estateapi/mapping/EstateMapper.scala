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
import uk.gov.hmrc.common.mapping._
import uk.gov.hmrc.estateapi.rest.resources.core.Estate


object EstateMapper {
  def toDes(domainEstate: Estate): DesTrustEstate = {

    val personalRepresentative: DesPersonalRepresentative = DesPersonalRepresentativeMapper.toDes(domainEstate.personalRepresentative)

    val deceased: DesWill = DesWillMapper.toDes(domainEstate.deceased)

    val administrationEndDate: Option[DateTime] = domainEstate.adminPeriodFinishedDate

    val correspondence: DesCorrespondence = DesCorrespondenceMapper.toDes(domainEstate)

    val yearReturns = Some(DesYearsReturns(Some(true), None))

    val periodTaxDues = domainEstate.reasonEstateSetup match {
      case "incomeTaxDueMoreThan10000" => "01"
      case "saleOfEstateAssetsMoreThan250000" => "02"
      case "saleOfEstateAssetsMoreThan500000" => "03"
      case "worthMoreThanTwoAndHalfMillionAtTimeOfDeath" => "04"
    }

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
}

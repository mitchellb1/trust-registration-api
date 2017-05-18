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

import org.joda.time.DateTime
import uk.gov.hmrc.common.des._
import uk.gov.hmrc.estateapi.rest.resources.core.Estate

trait EstateMapper {

  def toDes(domainEstate: Estate): DesTrustEstate = {

    val personalRepresentative: DesPersonalRepresentative = DesPersonalRepresentativeMap.toDes(domainEstate.personalRepresentative)

    val deceased: DesWill = DesWillMap.toDes(domainEstate.deceased)

    val administrationEndDate: Option[DateTime] = domainEstate.adminPeriodFinishedDate

    val correspondence: DesCorrespondence = DesCorrespondenceMap.toDes(domainEstate)

    val periodTaxDues = "01"

    val estate: DesEstate = DesEstate(DesEntities(personalRepresentative, deceased),
      administrationEndDate,
      periodTaxDues)

    DesTrustEstate(
      None,
      correspondence,
      //  None,
      Some(DesYearsReturns(Some(true), None)),
      None,
      //    Some(assets),
      DesDeclarationMap.toDes(domainEstate.declaration),
      DesDetails(Some(estate), trust = None)
    )
  }
}

object EstateMapper extends EstateMapper

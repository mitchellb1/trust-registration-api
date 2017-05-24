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

import uk.gov.hmrc.common.des.DesIndividualDetails
import uk.gov.hmrc.common.rest.resources.core.Individual

object DesIndividualDetailsMapper {

  def toDes(individual: Individual): DesIndividualDetails = {
    DesIndividualDetails(
      name = DesNameMapper.toDes(individual),
      dateOfBirth = individual.dateOfBirth,
      vulnerableBeneficiary = None,
      beneficiaryType = None,
      beneficiaryDiscretion = None,
      beneficiaryShareOfIncome = None,
      identification = DesIdentificationMapper.toDes(individual)
    )
  }
}

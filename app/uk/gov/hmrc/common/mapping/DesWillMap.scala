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

import uk.gov.hmrc.common.des.DesWill
import uk.gov.hmrc.common.rest.resources.core.Deceased


trait DesWillMap {

  def toDes(deceased: Deceased): DesWill = {
    new DesWill(
      name = DesNameMap.toDes(deceased.individual),
      dateOfBirth = deceased.individual.dateOfBirth,
      dateOfDeath = deceased.dateOfDeath,
      identification = DesWillIdentificationMap.toDes(deceased.individual)
    )
  }
}
object DesWillMap extends DesWillMap

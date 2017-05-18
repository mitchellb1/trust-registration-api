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
import uk.gov.hmrc.common.rest.resources.core.Individual


trait DesWillIdentificationMap {

  def toDes(individual: Individual): DesWillIdentification = {
    val ninoExists: String = individual.nino.getOrElse("")
    if (ninoExists.isEmpty) {
      new DesWillIdentification(
        nino = None,
        address = Some(AddressMap.toDes(individual.correspondenceAddress.get))
      )
    }
    else {
      new DesWillIdentification(
        nino = Some(individual.nino.getOrElse("")),
        address = None)
    }
  }

}

object DesWillIdentificationMap extends DesWillIdentificationMap

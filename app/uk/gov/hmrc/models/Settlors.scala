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

package uk.gov.hmrc.models

import play.api.libs.json.Json


case class Settlors(individuals: Option[List[Individual]] = None, settlorCompanies: Option[List[SettlorCompany]] = None) {

  val moreThanZeroSettlor = individuals.getOrElse(Nil).size + settlorCompanies.getOrElse(Nil).size > 0

  require(moreThanZeroSettlor,MoreThanZeroSettlorsException())
}

object Settlors {
  implicit val settlorsFormats = Json.format[Settlors]
}

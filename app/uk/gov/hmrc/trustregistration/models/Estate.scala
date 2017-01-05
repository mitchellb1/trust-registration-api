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

package uk.gov.hmrc.trustregistration.models

import play.api.libs.json.Json

case class Estate(val isCreatedByWill: Boolean,
                  val estateCriteriaMet: Boolean,
                  val adminPeriodFinishedDate: Boolean,
                  val incomeTaxDueMoreThan10000: Boolean,
                  val personalRepresentative: Option[PersonalRepresentative] = None,
                  val deceased: Option[Individual] = None,
                  val saleOfEstateAssetsMoreThan250000: Option[Boolean] = None,
                  val saleOfEstateAssetsMoreThan500000: Option[Boolean] = None,
                  val worthMoreThanTwoAndHalfMillionAtTimeOfDeath: Option[Boolean] = None){
  private val atleastAdeceasedOrPersonalRepresentative: Boolean = personalRepresentative.isDefined || deceased.isDefined

  require(atleastAdeceasedOrPersonalRepresentative, "Must have either a personal representative or a deceased")
}

object Estate{
  implicit val estateFormat = Json.format[Estate]
}

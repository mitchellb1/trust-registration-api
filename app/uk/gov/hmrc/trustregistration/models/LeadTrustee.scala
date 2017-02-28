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
import uk.gov.hmrc.trustregistration.models.exceptions.{AtLeastOneTypeOfTrusteeException, OnlyOneTypeOfTrusteeException}


case class LeadTrustee(individual: Option[Individual] = None, company: Option[Company] = None, telephoneNumber: String, email: String) {
  private val atleastOneTypeOfTrustee: Boolean = individual.isDefined || company.isDefined
  private val onlyOneTypeOfTrustee: Boolean = !(individual.isDefined && company.isDefined)

  require(atleastOneTypeOfTrustee, AtLeastOneTypeOfTrusteeException())
  require(onlyOneTypeOfTrustee, OnlyOneTypeOfTrusteeException())
}

object LeadTrustee {
  implicit val leadTrusteeFormats = Json.format[LeadTrustee]
}

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

import play.api.libs.json._

case class NaturalPeople(individuals: Option[List[Individual]]) {
  val noMoreThanTwoNaturalPeople = (individuals.getOrElse(Nil).size <= 2)

  require(noMoreThanTwoNaturalPeople,NoMoreThanTwoNaturalPeopleException())
}

object NaturalPeople {
  implicit val naturalPeopleFormats = Json.format[NaturalPeople]

  def addDesNaturalPeople(ppl: Option[NaturalPeople]): JsValue = {
    if (ppl.isEmpty) JsNull else JsObject(Map("naturalPerson" -> JsArray(
      ppl.get.individuals.get.map(c=>Json.toJson(c)(Individual.writesToDes))
    )))
  }
}

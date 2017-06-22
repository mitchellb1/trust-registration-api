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

package uk.gov.hmrc.trustapi.mapping

import org.joda.time.DateTime
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json._
import uk.gov.hmrc.trustapi.rest.resources.core.{NaturalPeople, Trust}
import uk.gov.hmrc.utils.ScalaDataExamples


class NaturalPersonMapperSpec extends PlaySpec with OneAppPerSuite with ScalaDataExamples {

  "Natural Persons" should {
    "Map to a valid DES Natural Person JSON Body" when {

      val domainTrust = trustWithEmploymentTrust
      val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)

      "we have entities natural person with firstname " in {
        val naturalPersonList = (json \ "details" \ "trust" \  "entities" \ "naturalPerson")(0)
        ( naturalPersonList \ "name" \ "firstName").get.as[String] mustBe domainTrust.naturalPeople.get.individuals.get.head.givenName
      }

      "we have entities natural person with lastname" in {
        val naturalPersonList = (json \ "details" \ "trust" \ "entities" \ "naturalPerson")(0)
        ( naturalPersonList \ "name" \ "lastName").get.as[String] mustBe domainTrust.naturalPeople.get.individuals.get.head.familyName
      }

      "we have entities natural person with no middlename/othername" in {

        val naturalPersonList = (json \ "details" \ "trust" \  "entities" \ "naturalPerson")(0)
        ( naturalPersonList \ "name" \ "middleName").validate[JsString].isError  mustBe true
      }

      "we have entities natural person with  middlename/othername" in {
        val naturalPeopleWithOtherName = Some(NaturalPeople(Some(List(individualWithOtherName))))
        val domainTrust = trustWithEmploymentTrust.copy(naturalPeople = naturalPeopleWithOtherName)
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        val naturalPersonList = (json \ "details" \ "trust" \  "entities" \ "naturalPerson")(0)
        ( naturalPersonList \ "name" \ "middleName").get.as[String] mustBe domainTrust.naturalPeople.get.individuals.get.head.otherName.get
      }

      "we have entities natural person with date of birth" in {
        val naturalPersonList = (json \ "details" \ "trust" \ "entities" \ "naturalPerson")(0)
        ( naturalPersonList \  "dateOfBirth").get.as[String] mustBe "1900-01-01"
        ( naturalPersonList \  "dateOfBirth").get.as[DateTime] mustBe domainTrust.naturalPeople.get.individuals.get.head.dateOfBirth
      }

      "we have entities natural person with nino as identification" in {
        val naturalPeopleWithNino = Some(NaturalPeople(Some(List(individualwithNino))))
        val domainTrust = trustWithEmploymentTrust.copy(naturalPeople = naturalPeopleWithNino)
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        val naturalPersonList = (json \ "details" \ "trust" \  "entities" \ "naturalPerson")(0)
        ( naturalPersonList \  "identification" \ "nino").get.as[String] mustBe domainTrust.naturalPeople.get.individuals.get.head.nino.get
      }

      "we have no natural people" in {
        val domainTrust = trustWithEmploymentTrust.copy(naturalPeople = None)
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \  "entities" \ "naturalPerson").validate[JsArray].isError mustBe true
      }
    }
  }
}

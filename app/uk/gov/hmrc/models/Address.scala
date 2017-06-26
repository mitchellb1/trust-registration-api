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

import play.api.libs.functional.syntax._
import play.api.libs.json._
import uk.gov.hmrc.models.{PostCodeMissingForGBAddressException, PostalCodeNotPresentForNonGbAddressException}

case class Address(
                    line1: String,
                    line2: Option[String] = None,
                    line3: Option[String] = None,
                    line4: Option[String] = None,
                    postalCode: Option[String] = None,
                    countryCode: String) {
  val postCodeMissingForGbAddress = countryCode == "GB" && postalCode.fold(true)(_.trim.isEmpty)
  val postalCodeNotPresentForNonGbAddresses = if (countryCode != "GB") !postalCode.isDefined else true


  require(!postCodeMissingForGbAddress,PostCodeMissingForGBAddressException())
  require(postalCodeNotPresentForNonGbAddresses,PostalCodeNotPresentForNonGbAddressException())
}

object Address {
  val readsFromDes : Reads[Address] = (
    (JsPath \\ "line1").read[String] and
      (JsPath \\ "line2").readNullable[String] and
      (JsPath \\ "line3").readNullable[String] and
      (JsPath \\ "line4").readNullable[String] and
      (JsPath \\ "postCode").readNullable[String] and
      (JsPath \\ "country").read[String]
    )(Address.apply _)

  val writesToDes: Writes[Address] = (
    (JsPath \ "line1").write[String] and
      (JsPath \ "line2").writeNullable[String] and
      (JsPath \ "line3").writeNullable[String] and
      (JsPath \ "line4").writeNullable[String] and
      (JsPath \ "postCode").writeNullable[String] and
      (JsPath \ "country").write[String]
    ) (unlift(Address.unapply))


  implicit val addressReads: Reads[Address] = (
    (JsPath \\ "line1").read[String] and
      (JsPath \\ "line2").readNullable[String] and
      (JsPath \\ "line3").readNullable[String] and
      (JsPath \\ "line4").readNullable[String] and
      (JsPath \\ "postalCode").readNullable[String] and
      (JsPath \\ "countryCode").read[String]
    ) (Address.apply _)

  implicit val addressWrites = Json.writes[Address]
}
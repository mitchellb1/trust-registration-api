/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.trustregistration.utils

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsValue, Json}

class CommonJsonTypesSpec extends PlaySpec  with  ValidatorBase{

  "JsonValidator" must {
    //Happy Path
    "read the schema and return a SuccessfulValidation" when {
      "given a valid correspondence Address" in {
        val parseResult = schemaValidator.createJsonNode(validCorrespondenceAddress)

        parseResult match {
          case Some(jsonNode) => {
            val result = schemaValidator.validateAgainstSchema(jsonNode,"/definitions/correspondenceAddressType")
            result mustBe SuccessfulValidation
          }
          case _ => fail("Could not parse Json to a JsonNode")
        }
      }
      "given a valid Company" in {
        val parseResult = schemaValidator.createJsonNode(validCompany)

        parseResult match {
          case Some(jsonNode) => {
            val result = schemaValidator.validateAgainstSchema(jsonNode,"/definitions/companyType")
            result mustBe SuccessfulValidation
          }
          case _ => fail("Could not parse Json to a JsonNode")
        }
      }
    }

    //Sad Path
  }

  val validCorrespondenceAddress = """
                                     |{
                                     |						"countryCode": {
                                     |							"$": "AD"
                                     |						},
                                     |						"line1": {
                                     |							"$": "Line 1"
                                     |						},
                                     |						"line2": {
                                     |							"$": "Line 2"
                                     |						},
                                     |						"line3": {
                                     |							"$": "Line 3"
                                     |						},
                                     |						"line4": {
                                     |							"$": "Line 4"
                                     |						},
                                     |						"postalCode": {
                                     |							"$": "DE6 23QH"
                                     |						}
                                     |}
                                   """.stripMargin

  val validCompany = """
                                     |{
                                     |					"companyName": {
                                     |						"$": "This is a company Name"
                                     |					},
                                     |					"correspondenceAddress": {
                                     |						"countryCode": {
                                     |							"$": "AD"
                                     |						},
                                     |						"line1": {
                                     |							"$": "Address Line 1"
                                     |						},
                                     |						"line2": {
                                     |							"$": "Address Line 2"
                                     |						},
                                     |						"line3": {
                                     |							"$": "Address Line 3"
                                     |						},
                                     |						"line4": {
                                     |							"$": "Address Line 4"
                                     |						},
                                     |						"postalCode": {
                                     |							"$": "NE21 6BR"
                                     |						}
                                     |					},
                                     |					"referenceNumber": {
                                     |						"$": "Ref Num123"
                                     |					},
                                     |					"telephoneNumber": {
                                     |						"$": "0151 256 1971"
                                     |					}
                                     |}
                                   """.stripMargin
}

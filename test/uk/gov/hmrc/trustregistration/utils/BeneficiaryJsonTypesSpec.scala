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


class BeneficiaryJsonTypesSpec extends PlaySpec with  ValidatorBase{

  "JsonValidator" must {
    //Happy Path
    "read the schema and return a SuccessfulValidation" when {
      "given a valid Other Beneficiary Type" in {
        val parseResult = schemaValidator.validateIsJson(validOtherBeneficiary)

        parseResult match {
          case Some(jsonNode) => {
            val result = schemaValidator.validateAgainstSchema(jsonNode,"/definitions/otherBeneficiariesType")
            result mustBe SuccessfulValidation
          }
          case _ => fail("Could not parse Json to a JsonNode")
        }
      }
      "given multiple valid Other Beneficiary" in {
        val parseResult = schemaValidator.validateIsJson(validMultipleOtherBeneficiary)

        parseResult match {
          case Some(jsonNode) => {
            val result = schemaValidator.validateAgainstSchema(jsonNode,"/definitions/otherBeneficiariesType")
            result mustBe SuccessfulValidation
          }
          case _ => fail("Could not parse Json to a JsonNode")
        }
      }
    }

    //Sad Path
  }

  val validOtherBeneficiary = """
                          |{
                          |							"otherBeneficiary": {
                          |								"beneficiaryDescription": {
                          |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |								},
                          |								"correspondenceAddress": {
                          |									"countryCode": {
                          |										"$": "AD"
                          |									},
                          |									"line1": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line2": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line3": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line4": {
                          |										"$": "aaaaaaaaaaaaaaaaa"
                          |									},
                          |									"postalCode": {
                          |										"$": "aaaaaaaaaa"
                          |									}
                          |								},
                          |								"income": {
                          |									"isIncomeAtTrusteeDiscretion": {
                          |										"$": true
                          |									},
                          |									"shareOfIncome": {
                          |										"$": 0
                          |									}
                          |								}
                          |        }
                          |}    """.stripMargin

  val validMultipleOtherBeneficiary = """
                          |{
                          |							"otherBeneficiary": {
                          |								"beneficiaryDescription": {
                          |									"$": "1st Beneficiary"
                          |								},
                          |								"correspondenceAddress": {
                          |									"countryCode": {
                          |										"$": "AD"
                          |									},
                          |									"line1": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line2": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line3": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line4": {
                          |										"$": "aaaaaaaaaaaaaaaaa"
                          |									},
                          |									"postalCode": {
                          |										"$": "aaaaaaaaaa"
                          |									}
                          |								},
                          |								"income": {
                          |									"isIncomeAtTrusteeDiscretion": {
                          |										"$": true
                          |									},
                          |									"shareOfIncome": {
                          |										"$": 0
                          |									}
                          |								}
                          |        },
                          |							"otherBeneficiary": {
                          |								"beneficiaryDescription": {
                          |									"$": "2nd Beneficiary"
                          |								},
                          |								"correspondenceAddress": {
                          |									"countryCode": {
                          |										"$": "AD"
                          |									},
                          |									"line1": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line2": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line3": {
                          |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                          |									},
                          |									"line4": {
                          |										"$": "aaaaaaaaaaaaaaaaa"
                          |									},
                          |									"postalCode": {
                          |										"$": "aaaaaaaaaa"
                          |									}
                          |								},
                          |								"income": {
                          |									"isIncomeAtTrusteeDiscretion": {
                          |										"$": true
                          |									},
                          |									"shareOfIncome": {
                          |										"$": 0
                          |									}
                          |								}
                          |        }
                          |}    """.stripMargin

}

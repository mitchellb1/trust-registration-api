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

class AssetsJsonTypesSpec extends PlaySpec with ValidatorBase {

//  "JsonValidator" must {
//    //Happy Path
//    "read the schema and return a SuccessfulValidation" when {
//      "given a valid monetary Assets Type" in {
//        val result = schemaValidator.validateAgainstSchema(validMonetaryAssets, "/definitions/monetaryAssetsType")
//        result mustBe SuccessfulValidation
//      }
//
//      "given multiple valid monetary Assets" in {
//
//        val result = schemaValidator.validateAgainstSchema(validMultipleMonetaryAssets, "/definitions/monetaryAssetsType")
//        result mustBe SuccessfulValidation
//
//      }
//    }
//
//    //Sad Path
//  }
//
//  val validMonetaryAssets =
//    """
//                          |{
//                          							"monetaryAsset": {
//                         |								"value": {
//                         |									"$": 1000
//                         |								}
//                         |							}
//                          |}    """.stripMargin
//
//  val validMultipleMonetaryAssets =
//    """
//                          |{
//                          							"monetaryAsset": [
//                                          {
//                           |								"value": {
//                           |									"$": 1000000
//                           |								}
//                           |							},
//                         |							  {
//                           |								"value": {
//                           |									"$": 200000
//                           |								}
//                         |							  }
//                         |         ]
//                          |}    """.stripMargin
}

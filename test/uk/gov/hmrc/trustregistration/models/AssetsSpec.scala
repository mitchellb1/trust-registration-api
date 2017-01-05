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

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

class AssetsSpec extends PlaySpec with ScalaDataExamples with JsonExamples{
 "Assets" must {
   "be parsed correctly from a valid JSON" in {
     val jsonInput ="""{
            "shareAssets" : ["{SHAREASSETS}","{SHAREASSETS}"],
            "businessAssets" : ["{BUSINESSASSETS}","{BUSINESSASSETS}"]
         }"""
       .mkString.replace("\"{SHAREASSETS}\"", validShareAssetJson)
       .replace("\"{BUSINESSASSETS}\"", validBusinessAssetJson)
       .replace("\"{ADDRESS}\"", validAddressJson)

     val result = Json.parse(jsonInput).as[Assets]
     result mustBe(assets)
   }
 }

  "ShareAssets" must {
    "be parsed correctly from a valid JSON" in {
      val result = Json.parse(validShareAssetJson).as[ShareAsset]
      result mustBe(shareAsset)
    }
  }

  "BusinessAssets" must {
    "be parsed correctly from a valid JSON" in {
      val jsonInput = validBusinessAssetJson.mkString.replace("\"{ADDRESS}\"", validAddressJson)
      val result = Json.parse(jsonInput).as[BusinessAsset]
      result mustBe(businessAsset)
    }
  }

}

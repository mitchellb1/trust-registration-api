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

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.utils.ScalaDataExamples

class TrustTypeMappingSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples {


  val SUT = TrustTypeMapper

  "TrustTypeMapper" must {
    "accept a valid set of domain will Intestacy Trust case classes" when {
      "and return a string containing >Will Trust or Intestacy Trust<" in {

        //Logger.info(s"From domain case classes ---- ${Json.toJson(trust).toString()}")
        val result = SUT.toDes(trustWithWillIntestacyTrust)

        result mustBe "Will Trust or Intestacy Trust"
      }
    }

    "accept a valid set of domain will Intestacy Trust Dov case classes" when {
      "and return a string containing >Deed of Variation Trust or Family Arrangement<" in {

        //Logger.info(s"From domain case classes ---- ${Json.toJson(trust).toString()}")
        val result = SUT.toDes(trustWithWillIntestacyTrustDOV)

        result mustBe "Deed of Variation Trust or Family Arrangement"
      }
    }

    "accept a valid set of domain Inter vivos Settlement Trust case classes" when {
      "and return a string containing >Inter vivos Settlement<" in {

        //Logger.info(s"From domain case classes ---- ${Json.toJson(trust).toString()}")
        val result = SUT.toDes(trustWithInterVivoTrust)

        result mustBe "Inter vivos Settlement"
      }
    }

    "accept a valid set of domain Inter vivos Settlement DOV case classes" when {
      "and return a string containing >Deed of Variation Trust or Family Arrangement<" in {

        //Logger.info(s"From domain case classes ---- ${Json.toJson(trust).toString()}")
        val result = SUT.toDes(trustWithInterVivoTrustDOV)

        result mustBe "Deed of Variation Trust or Family Arrangement"
      }
    }

    "accept a valid set of domain Employment Related Trust case classes" when {
      "and return a string containing >Employment Related<" in {

        //Logger.info(s"From domain case classes ---- ${Json.toJson(trust).toString()}")
        val result = SUT.toDes(trustWithEmploymentTrust)

        result mustBe "Employment Related"
      }
    }

    "accept a valid set of domain Heritage Maintenance Fund Trust case classes" when {
      "and return a string containing >Heritage Maintenance Fund<" in {

        //Logger.info(s"From domain case classes ---- ${Json.toJson(trust).toString()}")
        val result = SUT.toDes(trustWithHeritageMaintenance)

        result mustBe "Heritage Maintenance Fund"
      }
    }

    "accept a valid set of domain Flat management Trust case classes" when {
      "and return a string containing >Flat Management Company or Sinking Fund<" in {

        //Logger.info(s"From domain case classes ---- ${Json.toJson(trust).toString()}")
        val result = SUT.toDes(trustWithFlatManagementFund)

        result mustBe "Flat Management Company or Sinking Fund"
      }
    }
  }
}

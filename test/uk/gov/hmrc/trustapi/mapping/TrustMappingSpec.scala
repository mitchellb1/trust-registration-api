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
import play.api.libs.json.Json
import uk.gov.hmrc.common.utils.{DesSchemaValidator, SuccessfulValidation}
import uk.gov.hmrc.utils.ScalaDataExamples
import play.api.Logger
import uk.gov.hmrc.trustapi.mapping.todes.TrustMapper

class TrustMappingSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples {

  val SUT = TrustMapper

  "TrustMapper" must {
    "accept a valid set of domain Trust case classes" when {
      "and return a set of valid DesTrust case classes that validate with the schema" in {

        //Logger.info(s"From domain case classes ---- ${Json.toJson(trustWithWillIntestacyTrust).toString()}")
        val convertedToDesCaseClasses = SUT.toDes(trustWithWillIntestacyTrust)
        Logger.info(s"From des case classes ---- ${Json.toJson(convertedToDesCaseClasses).toString()}")

        val result = DesSchemaValidator.validateAgainstSchema(Json.toJson(convertedToDesCaseClasses).toString())
        result mustBe SuccessfulValidation
      }
    }
    "return Addition to the will trust" when {
      "given a will intestacy trust that is a dead of variation" in {
        val convertedToDesCaseClasses = SUT.toDes(trustWithWillIntestacyTrustDOV)
        val jsonOutput = Json.toJson(convertedToDesCaseClasses).toString()
        jsonOutput must include( "Addition to the will trust")
      }
    }
    "return Replaced the will trust" when {
      "given a will intestacy trust that is a dead of variation" in {
        val convertedToDesCaseClasses = SUT.toDes(trustWithInterVivoTrustDOV1)
        val jsonOutput = Json.toJson(convertedToDesCaseClasses).toString()
        jsonOutput must include( "Replaced the will trust")
      }
    }
    "return Previously there was only an absolute interest under the will" when {
      "given a will intestacy trust that is a dead of variation" in {
        val convertedToDesCaseClasses = SUT.toDes(trustWithInterVivoTrustDOV)
        val jsonOutput = Json.toJson(convertedToDesCaseClasses).toString()
        jsonOutput must include( "Previously there was only an absolute interest under the will")
      }
    }
  }
}
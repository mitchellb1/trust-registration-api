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

package uk.gov.hmrc.trustregistration.models

import org.joda.time.DateTime
import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.trustregistration.ScalaDataExamples


class TrustSpec extends PlaySpec with ScalaDataExamples {
  "Trust" must {
    "throw an exception" when {
      "there are no trusts" in {
        val ex = the [IllegalArgumentException] thrownBy (Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),legality,true,leadTrustee,List(individual,individual,individual,individual),
          Protectors(Some(List(individual,individual))),List(individual,individual,individual,individual),None,None,None,None,None))
        ex.getMessage() mustEqual  "requirement failed: Must have one type of Trust"
      }
      "there is more than one trust" in {
        val interVivoTrust = InterVivoTrust(assets,settlors,beneficiaries,true)
        val ex = the [IllegalArgumentException] thrownBy (Trust("Test Trust",address,"0044 1234 1234","1970",new DateTime("1940-01-01"),legality,true,leadTrustee,List(individual,individual,individual,individual),
          Protectors(Some(List(individual,individual))),List(individual,individual,individual,individual),Some(willIntestacyTrust),Some(interVivoTrust),None,None,None))
        ex.getMessage() mustEqual  "requirement failed: Must have one type of Trust"
      }
    }
  }
}

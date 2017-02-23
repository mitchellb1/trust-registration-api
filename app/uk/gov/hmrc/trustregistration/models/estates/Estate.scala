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

package uk.gov.hmrc.trustregistration.models.estates

import org.joda.time.DateTime
import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.models.{Address, Declaration}

case class Estate(estateName: String,
                  correspondenceAddress: Address,
                  personalRepresentative: PersonalRepresentative,
                  adminPeriodFinishedDate: Option[DateTime] = None,
                  reasonEstateSetup: String,
                  declaration: Declaration) {

//  private val atleastAdeceasedOrPersonalRepresentative: Boolean = personalRepresentative.isDefined || deceased.isDefined
//
//  require(atleastAdeceasedOrPersonalRepresentative,
//    s"""{\"message\": \"Invalid Json\",
//         \"code\": 0,
//         \"validationErrors\": [
//         {
//           \"message\": \"Must have either a personal representative or deceased\",
//           \"location\": \"/trustEstate/estate/\"
//         }
//         ]
//       }""".stripMargin
//  )

}

object Estate {
  implicit val estateFormat = Json.format[Estate]
}

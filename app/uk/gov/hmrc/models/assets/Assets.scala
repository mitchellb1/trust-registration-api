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

package uk.gov.hmrc.models.assets

import play.api.libs.json.Json

case class Assets(monetaryAssets: Option[List[Long]] = None,
                  propertyAssets: Option[List[PropertyAsset]] = None,
                  shareAssets: Option[List[ShareAsset]] = None,
                  partnershipAssets: Option[List[PartnershipAsset]] = None,
                  businessAssets: Option[List[BusinessAsset]] = None,
                  otherAssets: Option[List[OtherAsset]] = None)

object Assets {
  implicit val formats = Json.format[Assets]
}

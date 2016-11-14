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

import play.api.libs.json.Json


case class WillIntestacyTrust(monetaryAssets: Option[List[Float]] = None,
                  propertyAssets: Option[List[PropertyAssets]] = None,
                  shareAssets : Option[List[ShareAssets]] = None,
                  businessAssets: Option[List[BusinessAsset]] = None,
                  otherAssets: Option[List[OtherAsset]] = None) {

  private val atleastOneTypeOfAsset: Boolean = (monetaryAssets.isDefined ||
                  propertyAssets.isDefined ||
                  shareAssets.isDefined ||
                  businessAssets.isDefined ||
                  otherAssets.isDefined)

  require(atleastOneTypeOfAsset, "Must have at least one type of Asset")
}

object WillIntestacyTrust{
  implicit val assetsFormats = Json.format[WillIntestacyTrust]
}

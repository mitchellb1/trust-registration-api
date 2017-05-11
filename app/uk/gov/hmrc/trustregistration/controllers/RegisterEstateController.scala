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

package uk.gov.hmrc.trustregistration.controllers

import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.services.RegisterEstateService
import uk.gov.hmrc.trustregistration.utils.JsonSchemaValidator


trait RegisterEstateController extends EstateBaseController {

  val jsonSchemaValidator: JsonSchemaValidator

  def register(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    authorised("register", "") {
      validateEstate(request, jsonSchemaValidator, false)
    }
  }

  def closeEstate(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("closeEstate", identifier) {
      respond("closeEstate", registerEstateService.closeEstate(identifier))
    }
  }

  def getEstate(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getEstate", identifier) {
      respond("getEstate", registerEstateService.getEstate(identifier))
    }
  }
}

object RegisterEstateController extends RegisterEstateController {
  override val registerEstateService = RegisterEstateService
  override val metrics = ApplicationMetrics
  override lazy val jsonSchemaValidator = JsonSchemaValidator
}

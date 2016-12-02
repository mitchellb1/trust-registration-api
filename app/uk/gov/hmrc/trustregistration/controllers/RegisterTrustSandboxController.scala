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

package uk.gov.hmrc.trustregistration.controllers

import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models.TRN
import uk.gov.hmrc.trustregistration.services.RegisterTrustService
import uk.gov.hmrc.trustregistration.utils.JsonSchemaValidator

import scala.concurrent.Future

trait RegisterTrustSandboxController extends RegisterTrustController {
    override def register(): Action[JsValue] = Action.async(parse.json) { implicit request =>
      Future.successful(Created(Json.toJson(TRN("TRN-1234"))))
    }

    override def noChange(identifier: String): Action[AnyContent] = Action.async { implicit request =>
      Future.successful(Ok)
    }

    override def closeTrust(identifier: String): Action[AnyContent] = Action.async { implicit request =>
      Future.successful(Ok)
    }
}

object RegisterTrustSandboxController extends RegisterTrustSandboxController {
  override val registerTrustService = RegisterTrustService
  override val metrics = ApplicationMetrics
  override val jsonSchemaValidator = JsonSchemaValidator
}
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

import play.api.Logger
import play.api.libs.json.{JsError, JsResult, JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models.{TRN, EstateRegistrationDocument}
import uk.gov.hmrc.trustregistration.services.RegisterTrustService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait RegisterEstateController extends ApplicationBaseController {

  def register(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    Logger.info("Estate Register API invoked")

    val jsonBody: JsResult[EstateRegistrationDocument] = request.body.validate[EstateRegistrationDocument]
    jsonBody.map { regDoc: EstateRegistrationDocument => {
      val futureEither: Future[Either[String, TRN]] = registerTrustService.registerEstate(regDoc)
      futureEither.map {
        case Right(identifier) => Created(Json.toJson(identifier))
        case _ => BadRequest("Error:")
      }
    }
    }.recoverTotal {
      e => {
        Future.successful(BadRequest("Detected error:" + JsError.toFlatJson(e)))
      }
    }
  }

  def closeEstate(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("closeEstate", identifier) {
      respond("closeEstate", registerTrustService.closeEstate(identifier))
    }
  }

  def getEstate(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getEstate", identifier) {
      respond("getEstate", registerTrustService.getEstate(identifier))
    }
  }
}

object RegisterEstateController extends RegisterEstateController {
  override val registerTrustService = RegisterTrustService
  override val metrics = ApplicationMetrics
}

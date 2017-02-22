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

import play.api.Logger
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models.{TRN, TrustEstateRequest}
import uk.gov.hmrc.trustregistration.services.RegisterTrustService
import uk.gov.hmrc.trustregistration.utils.{FailedValidation, JsonSchemaValidator}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait RegisterEstateController extends ApplicationBaseController {

  val jsonSchemaValidator: JsonSchemaValidator

  def register(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    authorised("register", "") {
      Logger.info("Estate Register API invoked")

      val jsonString = request.body.toString()
      val validationResult = jsonSchemaValidator.validateAgainstSchema(jsonString)

      validationResult match {
        case fail: FailedValidation => {
          Future.successful(BadRequest(Json.toJson(fail)))
        }
        case _ => {
          try {
            request.body.validate[TrustEstateRequest].map {
              trustEstate: TrustEstateRequest => {
                val futureEither: Future[Either[String, TRN]] = registerTrustService.registerEstate(trustEstate.trustEstate.estate.get)
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
          catch {
            case e: Throwable => {
              val error = e.getMessage().substring(20)
              Future.successful(BadRequest(error))
            }
          }
        }
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
  override lazy val jsonSchemaValidator = JsonSchemaValidator
}

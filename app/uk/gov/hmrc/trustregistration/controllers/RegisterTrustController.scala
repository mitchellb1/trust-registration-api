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
import play.api.libs.json.json
import uk.gov.hmrc.play.microservice.controller.BaseController
import play.api.mvc.Action
import uk.gov.hmrc.trustregistration.models.{RegistrationDocument, TRN}
import uk.gov.hmrc.trustregistration.services.RegisterTrustService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait RegisterTrustController extends BaseController {

  val registerTrustService: RegisterTrustService

  def register(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    Logger.info("Register API invoked")

    val jsonBody: JsResult[RegistrationDocument] = request.body.validate[RegistrationDocument]
    jsonBody.map { regDoc: RegistrationDocument => {
        val futureEither: Future[Either[String, TRN]] = registerTrustService.registerTrust(regDoc)
        futureEither.flatMap {
          case Right(identifier) => Future.successful(Created(identifier.toString))
          case _ => Future.successful(BadRequest("Error:"))
        }
      }
    }.recoverTotal {
      e => {
        Future.successful(BadRequest("Detected error:" + JsError.toFlatJson(e)))
      }
    }
  }
}


object RegisterTrustController extends RegisterTrustController {
  override val registerTrustService = RegisterTrustService
}

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
import uk.gov.hmrc.trustregistration.metrics.TrustMetrics
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.services.RegisterTrustService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait RegisterTrustController extends TrustBaseController {

  def register(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    Logger.info("Register API invoked")

    val jsonBody: JsResult[RegistrationDocument] = request.body.validate[RegistrationDocument]
    jsonBody.map { regDoc: RegistrationDocument => {
        val futureEither: Future[Either[String, TRN]] = registerTrustService.registerTrust(regDoc)
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

  def noChange(identifier: String): Action[AnyContent] = Action.async{ implicit request =>

    Logger.info(s"$className:noChange API invoked")
    Logger.debug(s"$className:noChange($identifier) API invoked")

    val authorised: Option[(String, String)] = hc.headers.find((tup) => tup._1 == AUTHORIZATION)

    authorised match {
      case Some((key, "AUTHORISED")) => {
        Logger.info(s"$className:noChange API authorised")
        metrics.incrementAuthorisedRequest("noChange")
        respond("noChange", registerTrustService.noChange(identifier))
      }
      case _ => {
        Logger.info(s"$className:noChange API returned unauthorised")
        metrics.incrementUnauthorisedRequest("noChange")
        Future.successful(Unauthorized)
      }
    }
  }

  def closeTrust(identifier: String): Action[AnyContent] = Action.async{ implicit request =>

    Logger.info(s"$className:closeTrust API invoked")
    Logger.debug(s"$className:closeTrust($identifier) API invoked")

    val authorised: Option[(String, String)] = hc.headers.find((tup) => tup._1 == AUTHORIZATION)

    authorised match {
      case Some((key, "AUTHORISED")) => {
        Logger.info(s"$className:closeTrust API authorised")
        metrics.incrementAuthorisedRequest("closeTrust")
        respond("closeTrust", registerTrustService.closeTrust(identifier))
      }
      case _ => {
        Logger.info(s"$className:closeTrust API returned unauthorised")
        metrics.incrementUnauthorisedRequest("closeTrust")
        Future.successful(Unauthorized)
      }
    }
  }

  def getTrustees(identifier: String): Action[AnyContent] = Action.async{ implicit request =>

    Logger.info(s"$className:getTrustees API invoked")
    Logger.debug(s"$className:getTrustees($identifier) API invoked")

    val authorised: Option[(String, String)] = hc.headers.find((tup) => tup._1 == AUTHORIZATION)

    authorised match {
      case Some((key, "AUTHORISED")) => {
        Logger.info(s"$className:getTrustees API authorised")
        metrics.incrementAuthorisedRequest("getTrustees")
        respond("getTrustees", registerTrustService.getTrustees(identifier))
      }
      case _ => {
        Logger.info(s"$className:getTrustees API returned unauthorised")
        metrics.incrementUnauthorisedRequest("getTrustees")
        Future.successful(Unauthorized)
      }
    }
  }

  def getSettlors(identifier: String): Action[AnyContent] = Action.async{implicit request=>

    Logger.info(s"$className:getSettlors API invoked")
    Logger.debug(s"$className:getSettlors($identifier) API invoked")

    val authorised: Option[(String, String)] = hc.headers.find((tup) => tup._1 == AUTHORIZATION)

    authorised match {
      case Some((key, "AUTHORISED")) => {
        Logger.info(s"$className:getSettlors API authorised")
        metrics.incrementAuthorisedRequest("getSettlors")
        respond("getSettlors", registerTrustService.getSettlors(identifier))
      }
      case _ => {
        Logger.info(s"$className:getSettlors API returned unauthorised")
        metrics.incrementUnauthorisedRequest("getSettlors")
        Future.successful(Unauthorized)
      }
    }
  }

  def getNaturalPersons(identifier: String): Action[AnyContent] = Action.async{ implicit request =>

    Logger.info(s"$className:getNaturalPersons API invoked")
    Logger.debug(s"$className:getNaturalPersons($identifier) API invoked")

    val authorised: Option[(String, String)] = hc.headers.find((tup) => tup._1 == AUTHORIZATION)

    authorised match {
      case Some((key, "AUTHORISED")) => {
        Logger.info(s"$className:getNaturalPersons API authorised")
        metrics.incrementAuthorisedRequest("getNaturalPersons")
        respond("getTrustees", registerTrustService.getNaturalPersons(identifier))
      }
      case _ => {
        Logger.info(s"$className:getNaturalPersons API returned unauthorised")
        metrics.incrementUnauthorisedRequest("getNaturalPersons")
        Future.successful(Unauthorized)
      }
    }
  }

  def getTrustContactDetails(identifier: String): Action[AnyContent] = Action.async{ implicit request =>

    Logger.info(s"$className:getTrustContactDetails API invoked")
    Logger.debug(s"$className:getTrustContactDetails($identifier) API invoked")

    val authorised: Option[(String, String)] = hc.headers.find((tup) => tup._1 == AUTHORIZATION)

    authorised match {
      case Some((key, "AUTHORISED")) => {
        Logger.info(s"$className:getTrustContactDetails API authorised")
        metrics.incrementAuthorisedRequest("getTrustContactDetails")
        respond("getTrustees", registerTrustService.getTrustContactDetails(identifier))
      }
      case _ => {
        Logger.info(s"$className:getTrustContactDetails API returned unauthorised")
        metrics.incrementUnauthorisedRequest("getTrustContactDetails")
        Future.successful(Unauthorized)
      }
    }
  }
}

object RegisterTrustController extends RegisterTrustController {
  override val registerTrustService = RegisterTrustService
  override val metrics = TrustMetrics
}

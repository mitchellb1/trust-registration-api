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
import uk.gov.hmrc.trustregistration.services.{RegisterTrustService, TrustExistenceService}
import uk.gov.hmrc.trustregistration.utils.JsonSchemaValidator

trait RegisterTrustController extends ApplicationBaseController {

  val jsonSchemaValidator: JsonSchemaValidator

  def register(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    authorised("register", "") {
      registerTrustEstate(request, true, jsonSchemaValidator)
    }
  }

  def reRegister(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    authorised("register", "") {
      registerTrustEstate(request, true, jsonSchemaValidator, true)
    }
  }

  def noChange(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("noChange", identifier) {
      respond("noChange", registerTrustService.noChange(identifier))
    }
  }

  def closeTrust(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("closeTrust", identifier) {
      respond("closeTrust", registerTrustService.closeTrust(identifier))
    }
  }

  def getTrustees(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getTrustees", identifier) {
      respond("getTrustees", registerTrustService.getTrustees(identifier))
    }
  }

  def getSettlors(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getSettlors", identifier) {
      respond("getSettlors", registerTrustService.getSettlors(identifier))
    }
  }

  def getNaturalPersons(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getNaturalPersons", identifier) {
      respond("getNaturalPersons", registerTrustService.getNaturalPersons(identifier))
    }
  }

  def getTrustContactDetails(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getTrustContactDetails", identifier) {
      respond("getTrustContactDetails", registerTrustService.getTrustContactDetails(identifier))
    }
  }

  def getLeadTrustee(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getLeadTrustee", identifier) {
      respond("getLeadTrustee", registerTrustService.getLeadTrustee(identifier))
    }
  }

  def getBeneficiaries(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getBeneficiaries", identifier) {
      respond("getBeneficiaries", registerTrustService.getBeneficiaries(identifier))
    }
  }

  def getProtectors(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getProtectors", identifier) {
      respond("getProtectors", registerTrustService.getProtectors(identifier))
    }
  }

  def getTrust(identifier: String): Action[AnyContent] = Action.async { implicit request =>
    authorised("getTrust", identifier) {
      respond("getTrust", registerTrustService.getTrust(identifier))
    }
  }
}

object RegisterTrustController extends RegisterTrustController {
  override val registerTrustService = RegisterTrustService
  override val trustExistenceService = TrustExistenceService
  override lazy val jsonSchemaValidator = JsonSchemaValidator
  override val metrics = ApplicationMetrics
}

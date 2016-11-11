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
import play.api.libs.json.Json
import play.api.mvc.Result
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.microservice.controller.BaseController
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.services.RegisterTrustService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait ApplicationBaseController extends BaseController {
  val metrics: ApplicationMetrics
  val registerTrustService: RegisterTrustService
  
  val className: String = getClass.getSimpleName

  protected def authorised(apiName: String, identifier: String)(f: => Future[Result])(implicit hc : HeaderCarrier): Future[Result] = {
    Logger.info(s"$className:$apiName API invoked")
    Logger.debug(s"$className:$apiName($identifier) API invoked")

    val authorised: Option[(String, String)] = hc.headers.find((tup) => tup._1 == AUTHORIZATION)

    authorised match {
      case Some ((key, "AUTHORISED") ) => {
        Logger.info (s"$className:$apiName API authorised")
        metrics.incrementAuthorisedRequest (apiName)
        f
      }
      case _ => {
        Logger.info (s"$className:$apiName API returned unauthorised")
        metrics.incrementUnauthorisedRequest (apiName)
        Future.successful (Unauthorized)
      }
    }
  }

  def respond(methodName: String, result: Future[ApplicationResponse]): Future[Result] = {
    val okMessage = s"$className:$methodName API returned OK"

    result map {
      case GetSuccessResponse(payload:Estate) =>{
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload:Beneficiaries) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload:LeadTrustee) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload:Settlors) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload:List[Individual]) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload: TrustContactDetails) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case SuccessResponse => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok
      }
      case BadRequestResponse => {
        Logger.info(s"$className:$methodName API returned Bad Request")
        metrics.incrementBadRequestResponse(methodName)
        BadRequest
      }
      case NotFoundResponse => {
        Logger.info(s"$className:$methodName API returned Not Found")
        metrics.incrementNotFoundResponse(methodName)
        NotFound
      }
      case _ => {
        Logger.info(s"$className:$methodName API returned Internal Server Error")
        metrics.incrementInternalServerErrorResponse(methodName)
        InternalServerError
      }
    }
  }
}

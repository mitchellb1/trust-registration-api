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
import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import play.api.mvc.{Request, Result}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.microservice.controller.BaseController
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.models.beneficiaries.Beneficiaries
import uk.gov.hmrc.trustregistration.models.estates.Estate
import uk.gov.hmrc.trustregistration.services.{RegisterEstateService, RegisterTrustService, TrustExistenceService}
import uk.gov.hmrc.trustregistration.utils.{FailedValidation, JsonSchemaValidator}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait EstateBaseController extends BaseController {
  val metrics: ApplicationMetrics
  val registerEstateService: RegisterEstateService


  val className: String = getClass.getSimpleName


  def validateEstate(request: Request[JsValue], jsonSchemaValidator: JsonSchemaValidator, isTrust: Boolean = true)(implicit hc: HeaderCarrier) = {
    val jsonString = request.body.toString()
    val validationResult = jsonSchemaValidator.validateAgainstSchema(jsonString)

    validationResult match {
      case fail: FailedValidation => {
        Future.successful(BadRequest(Json.toJson(fail)))
      }
      case _ => {
        try {
          request.body.validate[EstateRequest].map {
            request: EstateRequest => {
                GetRegisterEstateResponse(request)
            }
          }.recoverTotal {
            e => {
              val error: JsValue = JsError.toJson(e)
              Future.successful(BadRequest(GenerateInvalidJsonResponse((error \\ "msg").head.toString().replace("\"", ""), s"/${error.as[JsObject].keys.head.replace("obj.", "").replace(".", "/")}")))
            }
          }
        }
        catch {
          case e: Throwable => {
            Future.successful(BadRequest(GenerateInvalidJsonResponse(e.getMessage().substring(20).split(':')(1).trim, "/")))
          }
        }
      }
    }
  }

  protected def authorised(apiName: String, identifier: String)(f: => Future[Result])(implicit hc: HeaderCarrier): Future[Result] = {
    Logger.info(s"$className:$apiName API invoked")
    Logger.debug(s"$className:$apiName($identifier) API invoked")

    val authorised: Option[(String, String)] = hc.headers.find((tup) => tup._1 == AUTHORIZATION)

    authorised match {
      case Some((key, "AUTHORISED")) => {
        Logger.info(s"$className:$apiName API authorised")
        metrics.incrementAuthorisedRequest(apiName)
        f
      }
      case _ => {
        Logger.info(s"$className:$apiName API returned unauthorised")
        metrics.incrementUnauthorisedRequest(apiName)
        Future.successful(Unauthorized)
      }
    }
  }

  def respond(methodName: String, result: Future[ApplicationResponse]): Future[Result] = {
    val okMessage = s"$className:$methodName API returned OK"

    result map {
      case GetSuccessResponse(payload: Trust) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload: Protectors) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload: Estate) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload: Beneficiaries) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload: LeadTrustee) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload: Settlors) => {
        Logger.info(okMessage)
        metrics.incrementApiSuccessResponse(methodName)
        Ok(Json.toJson(payload))
      }
      case GetSuccessResponse(payload: List[Individual]) => {
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

  private def isTrustReRegister(request: TrustEstateRequest, isTrust: Boolean) = {
    if (isTrust) request.trustEstate.trust.get.utr.exists(_.nonEmpty) else false
  }

  private def GetRegisterEstateResponse(estateRequest: EstateRequest)(implicit hc: HeaderCarrier) = {
    registerEstateService.registerEstate(estateRequest.estate).map {
      case Right(identifier) => Created(Json.toJson(identifier))
      case Left("503") => InternalServerError
      case _ => BadRequest("""{"message": "Failed serialization"}""")
    }
  }

  private def GenerateInvalidJsonResponse(errorMessage: String, location: String) = {
    Json.parse(s"""{
        "message": "Invalid Json",
        "code": 0,
        "validationErrors": [
        {
          "message": "${errorMessage}",
          "location":"${location}"
        }]
       }""")
  }
}

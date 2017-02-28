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
import uk.gov.hmrc.trustregistration.services.{RegisterTrustService, TrustExistenceService}
import uk.gov.hmrc.trustregistration.utils.{FailedValidation, JsonSchemaValidator}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait ApplicationBaseController extends BaseController {
  val metrics: ApplicationMetrics
  val registerTrustService: RegisterTrustService
  val trustExistenceService: TrustExistenceService


  val className: String = getClass.getSimpleName


  def validateTrustEstate(request: Request[JsValue], jsonSchemaValidator: JsonSchemaValidator, isTrust: Boolean = true)(implicit hc: HeaderCarrier) = {
    val jsonString = request.body.toString()
    val validationResult = jsonSchemaValidator.validateAgainstSchema(jsonString)

    validationResult match {
      case fail: FailedValidation => {
        Future.successful(BadRequest(Json.toJson(fail)))
      }
      case _ => {
        try {
          request.body.validate[TrustEstateRequest].map {
            request: TrustEstateRequest => {
              if (isTrustReRegister(request,isTrust)) {
                val trust = request.trustEstate.trust.get
                val response = trustExistenceService.trustExistence(TrustExistence(trust.name, trust.utr, trust.correspondenceAddress.postalCode))
                response.flatMap {
                  case Right("204") => {
                    GetRegisterTrustEstateResponse(isTrust, request)
                  }
                  case Left("404") => Future.successful(NotFound)
                  case Left("400") => Future.successful(BadRequest)
                  case Left("409") => Future.successful(Conflict)
                  case _ => Future.successful(InternalServerError)
                }
              }
              else {
                GetRegisterTrustEstateResponse(isTrust, request)
              }
            }
          }.recoverTotal {
            e => {
              val error: JsValue = JsError.toJson(e)
              val message = error \\ "msg"
              Future.successful(BadRequest(Json.parse(
                s"""
                                                  {
                                                    "message": "Invalid Json",
                                                    "code": 0,
                                                    "validationErrors": [
                                                    {
                                                      "message": "${message.head.toString().replace("\"", "")}",
                                                      "location":"/${error.as[JsObject].keys.head.replace("obj.", "").replace(".", "/")}"
                                                    }
                                                    ]
                                                  }
                                                  """)))
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

  private def isTrustReRegister(request: TrustEstateRequest, isTrust: Boolean) = {
    if (isTrust) request.trustEstate.trust.get.utr.exists(_.nonEmpty) else false
  }

  private def GetRegisterTrustEstateResponse(isTrust: Boolean, trustEstate: TrustEstateRequest)(implicit hc: HeaderCarrier) = {
    RegisterTrustOrEstate(isTrust, trustEstate).map {
      case Right(identifier) => Created(Json.toJson(identifier))
      case Left("503") => InternalServerError
      case _ => BadRequest("""{"message": "Failed serialization"}""")
    }
  }

  private def RegisterTrustOrEstate(isTrust: Boolean, trustEstateRequest: TrustEstateRequest)(implicit hc: HeaderCarrier) = {
    if (isTrust) registerTrustService.registerTrust(trustEstateRequest.trustEstate.trust.get) else registerTrustService.registerEstate(trustEstateRequest.trustEstate.estate.get)
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
}

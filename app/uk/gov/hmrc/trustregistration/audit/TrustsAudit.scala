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

package uk.gov.hmrc.trustregistration.audit

import play.api.Logger
import uk.gov.hmrc.play.audit.AuditExtensions._
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector, AuditResult}
import uk.gov.hmrc.play.audit.model.DataEvent
import uk.gov.hmrc.play.config.AppName
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.ApiGlobal

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait TrustsAudit {

  val auditConnector: AuditConnector = ApiGlobal.auditConnector
  val appName: String = AppName.appName

  def doAudit(eventTypeMessage: String,
              auditTag: String)(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Unit = {

    val auditDetails: Map[String, String] = Map(
      "Authorization" -> headerCarrier.authorization.map(_.value).getOrElse(""),
      "deviceID" -> headerCarrier.deviceID.getOrElse(""),
      "ipAddress" -> headerCarrier.trueClientIp.getOrElse(""),
      "token" -> headerCarrier.token.map(_.value).getOrElse("")
    )

    val auditResult: Future[AuditResult] = auditConnector.sendEvent(
      DataEvent(
        appName,
        eventTypeMessage,
        tags = headerCarrier.toAuditTags(auditTag, "N/A"),
        detail = headerCarrier.toAuditDetails() ++ auditDetails)
    )

    auditResult.onComplete({
      case Success(listInt) => {
      }
      case Failure(exception) => {
        exception: Throwable => Logger.warn("[DesConnector][doAudit] : auditResult: Error", exception)
      }
    })
  }
}

object TrustsAudit extends TrustsAudit

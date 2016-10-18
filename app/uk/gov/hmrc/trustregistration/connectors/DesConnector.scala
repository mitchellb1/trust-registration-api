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

package uk.gov.hmrc.trustregistration.connectors

import uk.gov.hmrc.play.http._
import uk.gov.hmrc.trustregistration.WSHttp
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.play.config.ServicesConfig

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DesConnector extends ServicesConfig with RawResponseReads {
  val httpPost: HttpPost = WSHttp
  val httpPut: HttpPut = WSHttp

  lazy val desUrl = baseUrl("des")
  lazy val serviceUrl = s"$desUrl/trust-registration-stub/trusts"

  def registerTrust(doc: RegistrationDocument)(implicit hc : HeaderCarrier) = {

    val uri: String = s"$serviceUrl/hello-world"

    val result: Future[HttpResponse] = httpPost.POST[RegistrationDocument,HttpResponse](uri,doc)(implicitly, httpReads, implicitly)

    result.map(f=> {
      f.status match{
        case 201 => Right(TRN("TRN-1234"))
        case _ => Left("503")
      }
    }).recover({
      case _ => Left("400")
    })
  }

  def noChange(identifier: String)(implicit hc : HeaderCarrier): Future[TrustResponse] = {
    val uri: String = s"$serviceUrl/$identifier/no-change"

    val result: Future[HttpResponse] = httpPut.PUT[String, HttpResponse](uri, identifier)(implicitly, httpReads, implicitly)

    result.map(f=> {
      f.status match {
        case 204 => SuccessResponse
        case 400 => BadRequestResponse
        case 404 => NotFoundResponse
        case _ => InternalServerErrorResponse
      }
    }).recover {
      case _ => InternalServerErrorResponse
    }
  }
}

object DesConnector extends DesConnector
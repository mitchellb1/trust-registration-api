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

import uk.gov.hmrc.play.http.{HeaderCarrier, HttpPost, HttpResponse, Upstream4xxResponse}
import uk.gov.hmrc.trustregistration.WSHttp
import uk.gov.hmrc.trustregistration.models.{RegistrationDocument, TRN}
import uk.gov.hmrc.play.config.ServicesConfig

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DesConnector extends ServicesConfig {
  val httpPost: HttpPost = WSHttp

  lazy val desUrl = baseUrl("des")
  lazy val serviceUrl = s"$desUrl/trust-registration-stub"

  def registerTrust(doc: RegistrationDocument)(implicit hc : HeaderCarrier) = {

    val uri: String = s"$serviceUrl/hello-world"

    val result: Future[HttpResponse] = httpPost.POST[RegistrationDocument,HttpResponse](uri,doc)

    result.map(f=> {
      f.status match{
        case 200 => Right(TRN("TRN-1234"))
        case _ => Left("503")
      }
    }).recover({
      case _ => Left("400")
    })
  }
}

object DesConnector extends DesConnector
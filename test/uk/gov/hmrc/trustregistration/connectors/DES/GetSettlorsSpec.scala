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

package uk.gov.hmrc.trustregistration.connectors.DES

import org.joda.time.DateTime
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.play.http.HttpResponse
import uk.gov.hmrc.trustregistration.models._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source


class GetSettlorsSpec extends PlaySpec with OneAppPerSuite with DESConnectorMocks with BeforeAndAfter {

  val validPassportJson = Source.fromFile(getClass.getResource("/ValidPassport.json").getPath).mkString
  val validAddressJson = Source.fromFile(getClass.getResource("/ValidAddress.json").getPath).mkString
  val validIndividualJson = Source
    .fromFile(getClass.getResource("/ValidIndividual.json").getPath)
    .mkString
    .replace("\"{PASSPORT}\"", validPassportJson)
    .replace("\"{ADDRESS}\"", validAddressJson)
  val validCompanyJson = Source
    .fromFile(getClass.getResource("/ValidCompany.json").getPath)
    .mkString
    .replace("\"{ADDRESS}\"", validPassportJson)


  "Get Settlors endpoint" must {
    "return a GetSuccessResponse with an empty Settlors" when{
      "DES returns a 200 response with an empty settlors" in {
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200, Some(Json.parse("[]")))))
        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        result mustBe GetSuccessResponse(Settlors())
      }
    }

    "return a GetSuccessResponse with a populated Settlors object" when {
      "DES returns a 200 response with a settlors JSON object that contains a list of individuals" in {

        val validSettlorsJson = ("""{"individuals" : [{INDIVIDUAL},{INDIVIDUAL}]}""").replace("{INDIVIDUAL}", validIndividualJson)
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(validSettlorsJson)))))

        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        val passport = Passport("IDENTIFIER",new DateTime("2020-01-01"),"UK")
        val address = Address(false, "Fake Street 123, Testland")
        val expectedIndividualSettlors = Settlors(Some(List(Individual("Dr","Leo","Spaceman",new DateTime("1800-01-01"),None,None,None,None,Some(passport),Some(address)),
          Individual("Dr","Leo","Spaceman",new DateTime("1800-01-01"),None,None,None,None,Some(passport),Some(address)))))

        result mustBe GetSuccessResponse(expectedIndividualSettlors)
      }

      "DES returns a 200 response with a settlors JSON object that contains a list of companies" in {
        val validSettlorsJson = ("""{"companies" : [{COMPANY},{COMPANY}]}""").replace("{COMPANY}", validCompanyJson)
        when (mockHttpGet.GET[HttpResponse](Matchers.any())(Matchers.any(),Matchers.any())).thenReturn(Future.successful(HttpResponse(200,
          Some(Json.parse(validSettlorsJson)))))

        val result = Await.result(SUT.getSettlors("1234"),Duration.Inf)
        val address = Address(false, "Fake Street 123, Testland")
        val expectedCompanySettlors = Settlors(None,Some(List(Company("Company",address,"12345",Some("AAA5221")),Company("Company",address,"12345",Some("AAA5221")))))

        result mustBe GetSuccessResponse(expectedCompanySettlors)
      }
    }
  }
}

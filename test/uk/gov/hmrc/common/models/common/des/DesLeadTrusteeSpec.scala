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

package uk.gov.hmrc.common.models.common.des
import org.joda.time.DateTime
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.{JsError, JsSuccess, Json}
import uk.gov.hmrc.common.des._
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesLeadTrusteeSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val orgId = DesOrgIdentification(Some("1245"),Some(DesAddress("1","2",None,None,Some("DE45 2HG"),"GB")))
  val orgLeadtrustee = DesLeadTrusteeOrg("company name", "0191 222 0000", Some("asdf@sdgfdsg.com"), orgId)
  val indId = DesIdentification(Some("1234567"), None, Some(DesAddress("1","2",None,None,Some("DE45 2HG"),"GB")))
  val indLeadtrustee =  DesLeadTrusteeInd(DesName("Joe", None, "Bloogs"), new DateTime("1900-01-01"), indId, "0191 222 0000", Some("asdf@sdgfdsg.com"))

"Des Lead Trustee" must {
  "write the correct json" when {
    "given a valid individual lead trustee" in {
      val jsonRepresentation = Json.toJson(indLeadtrustee).toString()
      jsonRepresentation mustBe   indJson  }
  }
  "write the correct json" when {
    "given a valid org lead trustee" in {
      val jsonRepresentation = Json.toJson(orgLeadtrustee).toString()
      jsonRepresentation mustBe   orgJson  }
  }

  "parse to the correct classes" when {
    "given a valid individual lead trustee the correct json" in {
      val jsonRepresentation = Json.parse(indJson).validate[DesLeadTrustee]
      val res = jsonRepresentation match {
        case s: JsSuccess[DesLeadTrustee] => s.get
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }
      res.toString mustBe   indClasses  }
  }
  "parse to the correct classes" when {
    "given a valid org lead trustee the correct json" in {
      val jsonRepresentation = Json.parse(orgJson).validate[DesLeadTrustee]
      val res = jsonRepresentation match {
        case s: JsSuccess[DesLeadTrustee] => s.get
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }
      res.toString mustBe   orgClasses  }
  }
}
  val indClasses = """DesLeadTrusteeInd(DesName(Joe,None,Bloogs),1900-01-01T00:00:00.000Z,DesIdentification(Some(1234567),None,Some(DesAddress(1,2,None,None,Some(DE45 2HG),GB))),0191 222 0000,Some(asdf@sdgfdsg.com))"""

  val orgClasses = """DesLeadTrusteeOrg(company name,0191 222 0000,Some(asdf@sdgfdsg.com),DesOrgIdentification(Some(1245),Some(DesAddress(1,2,None,None,Some(DE45 2HG),GB))))"""


  val indJson = """{"name":{"firstName":"Joe","lastName":"Bloogs"},"dateOfBirth":"1900-01-01","identification":{"nino":"1234567","address":{"line1":"1","line2":"2","postCode":"DE45 2HG","country":"GB"}},"phoneNumber":"0191 222 0000","email":"asdf@sdgfdsg.com"}"""

  val orgJson = """{"name":"company name","phoneNumber":"0191 222 0000","email":"asdf@sdgfdsg.com","identification":{"utr":"1245","address":{"line1":"1","line2":"2","postCode":"DE45 2HG","country":"GB"}}}"""

}

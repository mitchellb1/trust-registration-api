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


import akka.stream.{ActorMaterializer, Materializer}
import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import com.google.i18n.phonenumbers.PhoneNumberUtil.ValidationResult
import org.joda.time.DateTime
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.http.HeaderNames.{AUTHORIZATION => _, CONTENT_TYPE => _}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Request, RequestHeader, Result}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers._
import play.mvc.BodyParser
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}
import uk.gov.hmrc.trustregistration.metrics.ApplicationMetrics
import uk.gov.hmrc.trustregistration.models._
import uk.gov.hmrc.trustregistration.services.RegisterTrustService
import uk.gov.hmrc.trustregistration.utils.{TrustsValidationError, _}

import scala.concurrent.Future
import play.api.libs.json.Json


class RegisterTrustControllerSpec extends PlaySpec
  with OneAppPerSuite
  with BeforeAndAfter
  with JsonExamples
  with ScalaDataExamples
  with RegisterTrustServiceMocks {

  before {
    when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "AUTHORISED"))

    when(mockSchemaValidator.validateAgainstSchema(anyString())).thenReturn(SuccessfulValidation)
  }

  "RegisterTrustController" must {
    "return created with a TRN" when {
      "the register endpoint is called with a valid json payload" ignore {
        when(mockRegisterTrustService.registerTrust(any[Trust])(any[HeaderCarrier]))
          .thenReturn(Future.successful(Right(TRN("TRN-1234"))))

        withCallToPOST(Json.parse(validTrustJson)) { result =>
          status(result) mustBe CREATED
          contentAsString(result) must include("TRN")
        }
      }
    }
    "Return a Bad Request" when {
      "The json trust document is missing" in {
        withCallToPOST(Json.parse("{}")) { result =>
          status(result) mustBe BAD_REQUEST
        }
      }

      "The json fails schema validation with a single error" in {
        when(mockSchemaValidator.validateAgainstSchema(anyString)).thenReturn(FailedValidation("Invalid Json", 0, Seq(TrustsValidationError("object has missing required properties ([\"location\"])", "/"))))
        withCallToPOST(Json.parse("""{"message":"","code":""}""")) { result =>

          status(result) mustBe BAD_REQUEST
          contentAsJson(result) must be (Json.parse("""{"message":"Invalid Json","code":0,"validationErrors":[{"message":"object has missing required properties ([\"location\"])","location":"/"}]}"""))
        }
      }

      "The json fails schema validation with two errors" in {
        when(mockSchemaValidator.validateAgainstSchema(anyString)).thenReturn(FailedValidation("Invalid Json", 0, Seq(TrustsValidationError("object has missing required properties ([\"code\",\"location\"])", "/"),TrustsValidationError("instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])", "/message"))))

        withCallToPOST(Json.parse("""{"message":1}""")) { result =>
          status(result) mustBe BAD_REQUEST
          val body = contentAsJson(result)

          contentAsJson(result) must be (Json.parse("""{"message":"Invalid Json","code":0,"validationErrors":[{"message":"object has missing required properties ([\"code\",\"location\"])","location":"/"},{"message":"instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])","location":"/message"}]}"""))
        }
      }

      "The json fails schema validation with multiple errors" in {
        when(mockSchemaValidator.validateAgainstSchema(anyString)).thenReturn(FailedValidation("Invalid Json", 0, Seq(TrustsValidationError("object has missing required properties ([\"code\",\"location\"])", "/"),TrustsValidationError("instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])", "/message"),TrustsValidationError("string \"1111111111\" is too long (length: 10, maximum allowed: 9)", "/numbers"),TrustsValidationError("ECMA 262 regex \"^[A-Za-z0-9]{3,4} [A-Za-z0-9]{3}$\" does not match input string \"1111\"", "/postalCode"))))

        withCallToPOST(Json.parse("""{"message":1,"numbers":"1111111111","postalCode":"1111"}""")) { result =>
          status(result) mustBe BAD_REQUEST
          contentAsJson(result) must be (Json.parse("""{"message":"Invalid Json","code":0,"validationErrors":[{"message":"object has missing required properties ([\"code\",\"location\"])","location":"/"},{"message":"instance type (integer) does not match any allowed primitive type (allowed: [\"string\"])","location":"/message"},{"message":"string \"1111111111\" is too long (length: 10, maximum allowed: 9)","location":"/numbers"},{"message":"ECMA 262 regex \"^[A-Za-z0-9]{3,4} [A-Za-z0-9]{3}$\" does not match input string \"1111\"","location":"/postalCode"}]}"""))
        }
      }

    }
  }

  "No change endpoint" must {
    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.noChange(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(SuccessResponse))

        val result = SUT.noChange("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe OK
      }
    }

    "return 400" when {
      "the endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.noChange(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.noChange("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 401" when {
      "authentication credentials are missing or incorrect" in {

        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.noChange("12345").apply(FakeRequest("PUT", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }

    "return 404" when {
      "we pass an identifier that does not return a trust" in {
        when(mockRegisterTrustService.noChange(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.noChange("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe NOT_FOUND
      }
    }
  }

  "Close trusts endpoint" must {
    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.closeTrust(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(SuccessResponse))

        val result = SUT.closeTrust("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe OK
      }
    }

    "return 400" when {
      "the endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.closeTrust(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.closeTrust("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {

        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.closeTrust("12345").apply(FakeRequest("PUT", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }

    "return 404" when {
      "the endpoint is called and we pass an identifier that does not return a trust" in {
        when(mockRegisterTrustService.closeTrust(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.closeTrust("sadfg").apply(FakeRequest("PUT", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.closeTrust(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.closeTrust("sadfg").apply(FakeRequest("PUT", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

  "Get Trustees endpoint" must {

    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.getTrustees(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[List[Individual]](Nil)))

        val result = SUT.getTrustees("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe OK
      }
    }

    "return 200 ok with valid json" when {
      "the endpoint is called with a valid identifier" in {
        val individual = Individual(
          title = "Mr",
          givenName = "John",
          familyName = "Doe",
          dateOfBirth = new DateTime("1800-01-01"),
          passport = Some(Passport(
            identifier = "IDENTIFIER",
            expiryDate = new DateTime("2000-01-01"),
            countryOfIssue = "ES"
          )),
          correspondenceAddress = Some(Address(
            line1 = "Address Line 1"
          ))
        )
        when(mockRegisterTrustService.getTrustees(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[List[Individual]](List(individual))))

        val result = SUT.getTrustees("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe OK
        contentAsString(result) mustBe (
          """[{"title":"Mr","givenName":"John","familyName":"Doe","dateOfBirth":"1800-01-01",""" +
            """"passport":{"identifier":"IDENTIFIER","expiryDate":"2000-01-01","countryOfIssue":"ES"},""" +
            """"correspondenceAddress":{"line1":"Address Line 1"}}]""")
      }
    }

    "return 404 not found" when {
      "the endpoint is called with an identifier that can't be found" in {
        when(mockRegisterTrustService.getTrustees(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.getTrustees("404NotFound").apply(FakeRequest("GET", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 400" when {
      "the  endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.getTrustees(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.getTrustees("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.getTrustees(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.getTrustees("sadfg").apply(FakeRequest("GET", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {
        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.getTrustees("12345").apply(FakeRequest("GET", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }
  }

  "Get Settlors endpoint" must {
    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.getSettlors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[List[Settlors]](Nil)))

        val result = SUT.getSettlors("1234").apply(FakeRequest("GET",""))

        status(result) mustBe OK
      }
    }

    "return 200 ok with valid json" when {
      "the endpoint is called with a valid identifier" in {
        val validAddress = Address("Fake Street 123, Testland")
        val validCompanySettlors = Settlors(None,Some(List(Company("Company",validAddress,"12345",Some("AAA5221")),Company("Company",validAddress,"12345",Some("AAA5221")))))

        val expectedSettlorsJson = ("""{"companies" : [{COMPANY},{COMPANY}]}""").replace("{COMPANY}", validCompanyJson)

        when(mockRegisterTrustService.getSettlors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[Settlors](validCompanySettlors)))

        val result = SUT.getSettlors("1234").apply(FakeRequest("GET",""))
        status(result) mustBe OK
        contentAsString(result) contains (expectedSettlorsJson)
      }
    }

    "return 404 not found" when {
      "the endpoint is called with an identifier that can't be found" in {
        when(mockRegisterTrustService.getSettlors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.getSettlors("404NotFound").apply(FakeRequest("GET", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 400" when {
      "the  endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.getSettlors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.getSettlors("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.getSettlors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.getSettlors("sadfg").apply(FakeRequest("GET", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {
        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.getSettlors("12345").apply(FakeRequest("GET", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }
  }

  "Get Natural Persons endpoint" must {

      "return 200 ok" when {
        "the endpoint is called with a valid identifier" in {
          when(mockRegisterTrustService.getNaturalPersons(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(new GetSuccessResponse[List[Individual]](Nil)))

          val result = SUT.getNaturalPersons("sadfg").apply(FakeRequest("GET", ""))

          status(result) mustBe OK
        }
      }

      "return 200 ok with valid json" when {
        "the endpoint is called with a valid identifier" in {
          val individual = Individual(
            title = "Mr",
            givenName = "John",
            familyName = "Doe",
            dateOfBirth = new DateTime("1800-01-01"),
            passport = Some(Passport(
              identifier = "IDENTIFIER",
              expiryDate = new DateTime("2000-01-01"),
              countryOfIssue = "ES"
            )),
            correspondenceAddress = Some(Address(
              line1 = "Address Line 1"
            ))
          )
          when(mockRegisterTrustService.getNaturalPersons(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(new GetSuccessResponse[List[Individual]](List(individual))))

          val result = SUT.getNaturalPersons("sadfg").apply(FakeRequest("GET", ""))

          status(result) mustBe OK
          contentAsString(result) mustBe (
            """[{"title":"Mr","givenName":"John","familyName":"Doe","dateOfBirth":"1800-01-01",""" +
              """"passport":{"identifier":"IDENTIFIER","expiryDate":"2000-01-01","countryOfIssue":"ES"},""" +
              """"correspondenceAddress":{"line1":"Address Line 1"}}]""")
        }
      }

      "return 404 not found" when {
        "the endpoint is called with an identifier that can't be found" in {
          when(mockRegisterTrustService.getNaturalPersons(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(NotFoundResponse))

          val result = SUT.getNaturalPersons("404NotFound").apply(FakeRequest("GET", ""))

          status(result) mustBe NOT_FOUND
        }
      }

      "return 400" when {
        "the  endpoint is called with an invalid identifier" in {
          when(mockRegisterTrustService.getNaturalPersons(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(BadRequestResponse))

          val result = SUT.getNaturalPersons("sadfg").apply(FakeRequest("GET", ""))

          status(result) mustBe BAD_REQUEST
        }
      }

      "return 500" when {
        "something is broken" in {
          when(mockRegisterTrustService.getNaturalPersons(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(InternalServerErrorResponse))

          val result = SUT.getNaturalPersons("sadfg").apply(FakeRequest("GET", ""))
          status(result) mustBe INTERNAL_SERVER_ERROR
        }
      }

      "return 401" when {
        "the endpoint is called and authentication credentials are missing or incorrect" in {
          when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
          val result = SUT.getNaturalPersons("12345").apply(FakeRequest("GET", ""))

          status(result) mustBe UNAUTHORIZED
        }
      }

  }

  "Get Trust Contact Details endpoint" must {

    "return 200 ok" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.getTrustContactDetails(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[TrustContactDetails](TrustContactDetails(
            correspondenceAddress = address,
            telephoneNumber = "0191 234 5678"
          ))))

        val result = SUT.getTrustContactDetails("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe OK
      }
    }

    "return 200 ok with valid json" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.getTrustContactDetails(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[TrustContactDetails](TrustContactDetails(
            correspondenceAddress = address,
            telephoneNumber = "0191 234 5678"
          ))))

        val result = SUT.getTrustContactDetails("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe OK
        contentAsString(result) mustBe (
          """{"correspondenceAddress":""" +
            """{"line1":"Line 1","line2":"Line 2","line3":"Line 3",""" +
            """"line4":"Line 4","postalCode":"NE1 2BR","countryCode":"ES"},""" +
          """"telephoneNumber":"0191 234 5678"}""")
      }
    }

    "return 404 not found" when {
      "the endpoint is called with an identifier that can't be found" in {
        when(mockRegisterTrustService.getTrustContactDetails(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.getTrustContactDetails("404NotFound").apply(FakeRequest("GET", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 400" when {
      "the  endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.getTrustContactDetails(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.getTrustContactDetails("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.getTrustContactDetails(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.getTrustContactDetails("sadfg").apply(FakeRequest("GET", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {
        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.getTrustContactDetails("12345").apply(FakeRequest("GET", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }
  }

  "Get Lead Trustee endpoint" must {

    "return 200 ok with valid json" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.getLeadTrustee(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[LeadTrustee](leadTrusteeIndividual)))

        val result = SUT.getLeadTrustee("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe OK
        contentAsString(result) mustBe(
          """{"individual":{"title":"Dr","givenName":"Leo","familyName":"Spaceman","dateOfBirth":"1800-01-01",""" +
          """"passport":{"identifier":"IDENTIFIER","expiryDate":"2020-01-01","countryOfIssue":"ES"},""" +
          """"correspondenceAddress":{"line1":"Line 1","line2":"Line 2","line3":"Line 3","line4":"Line 4",""" +
          """"postalCode":"NE1 2BR","countryCode":"ES"}}}"""
        )
      }
    }

    "return 404 not found" when {
      "the endpoint is called with an identifier that can't be found" in {
        when(mockRegisterTrustService.getLeadTrustee(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.getLeadTrustee("404NotFound").apply(FakeRequest("GET", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 400" when {
      "the  endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.getLeadTrustee(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.getLeadTrustee("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.getLeadTrustee(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.getLeadTrustee("sadfg").apply(FakeRequest("GET", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {
        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.getLeadTrustee("12345").apply(FakeRequest("GET", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }

  }

  "Get Beneficiaries endpoint" must {

    "return 200 ok with valid json" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.getBeneficiaries(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[Beneficiaries](beneficiaries)))

        val result = SUT.getBeneficiaries("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe OK
        contentAsString(result) mustBe(
          """{"individualBeneficiaries":""" +
          """[{"individual":{"title":"Dr","givenName":"Leo","familyName":"Spaceman","dateOfBirth":"1800-01-01",""" +
          """"passport":{"identifier":"IDENTIFIER","expiryDate":"2020-01-01","countryOfIssue":"ES"},""" +
          """"correspondenceAddress":{"line1":"Line 1","line2":"Line 2","line3":"Line 3",""" +
          """"line4":"Line 4","postalCode":"NE1 2BR","countryCode":"ES"}},"isVulnerable":false,"isIncomeAtTrusteeDiscretion":true,"shareOfIncome":30}],""" +
          """"charityBeneficiaries":[{"name":"Charity Name","number":"123456789087654",""" +
          """"correspondenceAddress":{"line1":"Line 1","line2":"Line 2","line3":"Line 3",""" +
          """"line4":"Line 4","postalCode":"NE1 2BR","countryCode":"ES"},"isIncomeAtTrusteeDiscretion":false,"shareOfIncome":20}],""" +
          """"otherBeneficiaries":[{"description":"Beneficiary Description","correspondenceAddress":""" +
          """{"line1":"Line 1","line2":"Line 2","line3":"Line 3","line4":"Line 4",""" +
          """"postalCode":"NE1 2BR","countryCode":"ES"},"isIncomeAtTrusteeDiscretion":false,"shareOfIncome":50}]}"""
        )
      }
    }

    "return 404 not found" when {
      "the endpoint is called with an identifier that can't be found" in {
        when(mockRegisterTrustService.getBeneficiaries(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.getBeneficiaries("404NotFound").apply(FakeRequest("GET", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 400" when {
      "the  endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.getBeneficiaries(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.getBeneficiaries("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.getBeneficiaries(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.getBeneficiaries("sadfg").apply(FakeRequest("GET", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {
        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))
        val result = SUT.getBeneficiaries("12345").apply(FakeRequest("GET", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }

  }

  "Get Protectors endpoint" must {

    "return 200 ok with valid json" when {
      "the endpoint is called with a valid identifier" in {
        when(mockRegisterTrustService.getProtectors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(new GetSuccessResponse[Protectors](protectors)))

        val result = SUT.getProtectors("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe OK
        contentAsString(result) mustBe(
          """{"individuals":[{"title":"Dr","givenName":"Leo","familyName":"Spaceman","dateOfBirth":"1800-01-01",""" +
          """"passport":{"identifier":"IDENTIFIER","expiryDate":"2020-01-01","countryOfIssue":"ES"},""" +
          """"correspondenceAddress":{"line1":"Line 1","line2":"Line 2",""" +
          """"line3":"Line 3","line4":"Line 4","postalCode":"NE1 2BR","countryCode":"ES"}}],"companies":[""" +
          """{"name":"Company","correspondenceAddress":{"line1":"Line 1",""" +
          """"line2":"Line 2","line3":"Line 3","line4":"Line 4","postalCode":"NE1 2BR","countryCode":"ES"}""" +
          ""","telephoneNumber":"12345","referenceNumber":"AAA5221"}]}"""
        )
      }
    }

    "return 404 not found" when {
      "the endpoint is called with an identifier that can't be found" in {
        when(mockRegisterTrustService.getProtectors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(NotFoundResponse))

        val result = SUT.getProtectors("404NotFound").apply(FakeRequest("GET", ""))

        status(result) mustBe NOT_FOUND
      }
    }

    "return 400" when {
      "the  endpoint is called with an invalid identifier" in {
        when(mockRegisterTrustService.getProtectors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(BadRequestResponse))

        val result = SUT.getProtectors("sadfg").apply(FakeRequest("GET", ""))

        status(result) mustBe BAD_REQUEST
      }
    }

    "return 500" when {
      "something is broken" in {
        when(mockRegisterTrustService.getProtectors(any[String])(any[HeaderCarrier]))
          .thenReturn(Future.successful(InternalServerErrorResponse))

        val result = SUT.getProtectors("sadfg").apply(FakeRequest("GET", ""))
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return 401" when {
      "the endpoint is called and authentication credentials are missing or incorrect" in {
        when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))

        val result = SUT.getProtectors("12345").apply(FakeRequest("GET", ""))
        status(result) mustBe UNAUTHORIZED
      }
    }

    "Get Trust endpoint" must {
      "return 200 ok with valid json" when {
        "the endpoint is called with a valid identifier" in {
          when(mockRegisterTrustService.getTrust(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(new GetSuccessResponse[Trust](trust)))

          val result = SUT.getTrust("sadfg").apply(FakeRequest("GET", ""))
          val jsonResult = Json.parse(contentAsString(result))

          status(result) mustBe OK
          jsonResult.as[Trust] mustBe trust
        }
      }

      "return 404 not found" when {
        "the endpoint is called with an identifier that can't be found" in {
          when(mockRegisterTrustService.getTrust(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(NotFoundResponse))

          val result = SUT.getTrust("404NotFound").apply(FakeRequest("GET", ""))
          status(result) mustBe NOT_FOUND
        }
      }

      "return 400" when {
        "the  endpoint is called with an invalid identifier" in {
          when(mockRegisterTrustService.getTrust(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(BadRequestResponse))

          val result = SUT.getTrust("sadfg").apply(FakeRequest("GET", ""))
          status(result) mustBe BAD_REQUEST
        }
      }

      "return 500" when {
        "something is broken" in {
          when(mockRegisterTrustService.getTrust(any[String])(any[HeaderCarrier]))
            .thenReturn(Future.successful(InternalServerErrorResponse))

          val result = SUT.getTrust("sadfg").apply(FakeRequest("GET", ""))
          status(result) mustBe INTERNAL_SERVER_ERROR
        }
      }

      "return 401" when {
        "the endpoint is called and authentication credentials are missing or incorrect" in {
          when(mockHC.headers).thenReturn(List(AUTHORIZATION -> "NOT_AUTHORISED"))

          val result = SUT.getTrust("12345").apply(FakeRequest("GET", ""))
          status(result) mustBe UNAUTHORIZED
        }
      }
    }

  }

  val mockSchemaValidator = mock[JsonSchemaValidator]

  object SUT extends RegisterTrustController {
    override implicit def hc(implicit rh: RequestHeader): HeaderCarrier = mockHC

    override val metrics: ApplicationMetrics = mockMetrics
    override val registerTrustService: RegisterTrustService = mockRegisterTrustService
    override val jsonSchemaValidator = mockSchemaValidator
  }

  private def withCalltoPOSTInvalidPayload(payload: String)(handler: Future[Result] => Any) = {
    handler(SUT.register.apply(registerRequestWithPayload(Json.parse(payload))))
  }

  private def withCallToPOST(payload: JsValue)(handler: Future[Result] => Any) = {
    handler(SUT.register.apply(registerRequestWithPayload(payload)))
  }
}

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

package uk.gov.hmrc.trustregistration.utils

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsValue, Json}

class PeopleJsonTypesSpec extends PlaySpec with  ValidatorBase{

  "JsonValidator" must {
    //Happy Path
    "read the schema and return a SuccessfulValidation" when {
      "given a valid individual" in {
        val result = schemaValidator.validate(validIndividual, "/definitions/individualType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"validIndividual =>${messages.map(_.as[String])}")
            messages
          }
        }
        result mustBe SuccessfulValidation
      }

      "given a valid individual leadtrustee" in {
        val result = schemaValidator.validate(validIndividualLeadtrustee, "/definitions/leadTrusteeType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"validIndividualLeadtrustee =>${messages.map(_.as[String])}")
            messages
          }
        }
        res mustBe SuccessfulValidation
      }
      "given a valid company leadtrustee" in {
        val result = schemaValidator.validate(validCompanyLeadtrustee, "/definitions/leadTrusteeType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"validCompanyLeadtrustee =>${messages.map(_.as[String])}")
            messages
          }
        }
        res mustBe SuccessfulValidation
      }
    }

    //Sad Path
    "read the schema and return a FailedValidation" when {
      "given an invalid individual missing a given name" in {
        val result = schemaValidator.validate(invalidIndividualNoGivenName, "/definitions/individualType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"invalidIndividualNoGivenName =>${messages.map(_.as[String])}")
            messages.map(_.as[String]) must contain("object has missing required properties ([\"givenName\"])")
          }
        }
      }
      "given an invalid leadtrustee" in {
        val result = schemaValidator.validate(invalidLeadtrustee, "/definitions/leadTrusteeType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"invalidLeadtrustee =>${messages.map(_.as[String])}")
            messages.map(_.as[String]) must contain("string \"Invalid Extra Long Family Name\" is too long (length: 30, maximum allowed: 25)")
          }
        }
      }
      "given a valid leadtrustee and a valid company" in {
        val result = schemaValidator.validate(mixedLeadtrustees, "/definitions/leadTrusteeType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"mixedLeadtrustees =>${messages.map(_.as[String])}")
            messages.map(_.as[String]) must contain("instance failed to match exactly one schema (matched 2 out of 2)")
          }
        }
      }
      "given multiple valid individual lead trustees" in {
        val result = schemaValidator.validate(twoIndividualLeadtrustees, "/definitions/leadTrusteeType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"twoIndividualLeadtrustees =>${messages.map(_.as[String])}")
            messages.map(_.as[String]) must contain("instance failed to match exactly one schema (matched 0 out of 1)")
          }
        }
      }
      "given multiple valid company lead trustees" in {
        val result = schemaValidator.validate(twoCompanyLeadtrustees, "/definitions/leadTrusteeType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"twoCompanyLeadtrustees =>${messages.map(_.as[String])}")
            messages.map(_.as[String]) must contain("instance failed to match exactly one schema (matched 0 out of 1)")
          }
        }
      }
    }
  }

  //Happy Json
  val validIndividual = """
                          |{
                          |					"correspondenceAddress": {
                          |						"countryCode": {
                          |							"$": "AD"
                          |						},
                          |						"line1": {
                          |							"$": "Line 1"
                          |						},
                          |						"line2": {
                          |							"$": "Line 2"
                          |						},
                          |						"line3": {
                          |							"$": "Line 3"
                          |						},
                          |						"line4": {
                          |							"$": "Line 4"
                          |						},
                          |						"postalCode": {
                          |							"$": "NE24 1BR"
                          |						}
                          |					},
                          |					"dateOfBirth": {
                          |						"$": "2016-12-14"
                          |					},
                          |					"familyName": {
                          |						"$": "Bloggs"
                          |					},
                          |					"givenName": {
                          |						"$": "Joe"
                          |					},
                          |					"isUkNationalOrNonUkWithANino": {
                          |						"$": true
                          |					},
                          |					"nino": {
                          |						"$": "WA123456A"
                          |					},
                          |					"otherName": {
                          |						"$": "Fred"
                          |					},
                          |					"passportOrIdCard": {
                          |						"countryOfIssue": {
                          |							"$": "AD"
                          |						},
                          |						"expiryDate": {
                          |							"$": "2018-12-14"
                          |						},
                          |						"referenceNumber": {
                          |							"$": "123456789dsfg"
                          |						}
                          |					},
                          |					"telephoneNumber": {
                          |						"$": "0191 265 1234"
                          |					},
                          |					"title": {
                          |						"$": "Mr"
                          |					}
                          |}    """.stripMargin

  val validIndividualLeadtrustee = """
                                     |{
                                     |				"individual": {
                                     |					"correspondenceAddress": {
                                     |						"countryCode": {
                                     |							"$": "AD"
                                     |						},
                                     |						"line1": {
                                     |							"$": "Line 1"
                                     |						},
                                     |						"line2": {
                                     |							"$": "Line 2"
                                     |						},
                                     |						"line3": {
                                     |							"$": "Line 3"
                                     |						},
                                     |						"line4": {
                                     |							"$": "Line 4"
                                     |						},
                                     |						"postalCode": {
                                     |							"$": "NE24 1BR"
                                     |						}
                                     |					},
                                     |					"dateOfBirth": {
                                     |						"$": "2016-12-14"
                                     |					},
                                     |					"familyName": {
                                     |						"$": "Bloggs"
                                     |					},
                                     |					"givenName": {
                                     |						"$": "Joe"
                                     |					},
                                     |					"isUkNationalOrNonUkWithANino": {
                                     |						"$": true
                                     |					},
                                     |					"nino": {
                                     |						"$": "WA123456A"
                                     |					},
                                     |					"otherName": {
                                     |						"$": "Fred"
                                     |					},
                                     |					"passportOrIdCard": {
                                     |						"countryOfIssue": {
                                     |							"$": "AD"
                                     |						},
                                     |						"expiryDate": {
                                     |							"$": "2018-12-14"
                                     |						},
                                     |						"referenceNumber": {
                                     |							"$": "123456789dsfg"
                                     |						}
                                     |					},
                                     |					"telephoneNumber": {
                                     |						"$": "0191 265 1234"
                                     |					},
                                     |					"title": {
                                     |						"$": "Mr"
                                     |					}
                                     |				}
                                     |       }""".stripMargin

  val validCompanyLeadtrustee = """
                                  |{
                                  |				"company": {
                                  |					"companyName": {
                                  |						"$": "New Company Ltd"
                                  |					},
                                  |					"correspondenceAddress": {
                                  |						"countryCode": {
                                  |							"$": "AD"
                                  |						},
                                  |						"line1": {
                                  |							"$": "Line 1"
                                  |						},
                                  |						"line2": {
                                  |							"$": "Line 2"
                                  |						},
                                  |						"line3": {
                                  |							"$": "Line 3"
                                  |						},
                                  |						"postalCode": {
                                  |							"$": "DE6 23QH"
                                  |						}
                                  |					},
                                  |					"referenceNumber": {
                                  |						"$": "REF1234"
                                  |					},
                                  |					"telephoneNumber": {
                                  |						"$": "0121 569 1478"
                                  |					}
                                  |				}
                                  |       }""".stripMargin

  //Sad Json
  val invalidIndividualNoGivenName = """
                          |{
                          |					"correspondenceAddress": {
                          |						"countryCode": {
                          |							"$": "AD"
                          |						},
                          |						"line1": {
                          |							"$": "Line 1"
                          |						},
                          |						"line2": {
                          |							"$": "Line 2"
                          |						},
                          |						"line3": {
                          |							"$": "Line 3"
                          |						},
                          |						"line4": {
                          |							"$": "Line 4"
                          |						},
                          |						"postalCode": {
                          |							"$": "NE24 1BR"
                          |						}
                          |					},
                          |					"dateOfBirth": {
                          |						"$": "2016-12-14"
                          |					},
                          |					"familyName": {
                          |						"$": "Bloggs"
                          |					},
                          |					"isUkNationalOrNonUkWithANino": {
                          |						"$": true
                          |					},
                          |					"nino": {
                          |						"$": "WA123456A"
                          |					},
                          |					"otherName": {
                          |						"$": "Fred"
                          |					},
                          |					"passportOrIdCard": {
                          |						"countryOfIssue": {
                          |							"$": "AD"
                          |						},
                          |						"expiryDate": {
                          |							"$": "2018-12-14"
                          |						},
                          |						"referenceNumber": {
                          |							"$": "123456789dsfg"
                          |						}
                          |					},
                          |					"telephoneNumber": {
                          |						"$": "0191 265 1234"
                          |					},
                          |					"title": {
                          |						"$": "Mr"
                          |					}
                          |}    """.stripMargin

  val twoCompanyLeadtrustees = """
                                 |{
                                 |				"company": {
                                 |					"companyName": {
                                 |						"$": "New Company Ltd"
                                 |					},
                                 |					"correspondenceAddress": {
                                 |						"countryCode": {
                                 |							"$": "AD"
                                 |						},
                                 |						"line1": {
                                 |							"$": "Line 1"
                                 |						},
                                 |						"line2": {
                                 |							"$": "Line 2"
                                 |						},
                                 |						"line3": {
                                 |							"$": "Line 3"
                                 |						},
                                 |						"postalCode": {
                                 |							"$": "DE6 23QH"
                                 |						}
                                 |					},
                                 |					"referenceNumber": {
                                 |						"$": "REF1234"
                                 |					},
                                 |					"telephoneNumber": {
                                 |						"$": "0121 569 1478"
                                 |					}
                                 |				},
                                 |				"company": {
                                 |					"companyName": {
                                 |						"$": "New Company Ltd"
                                 |					},
                                 |					"correspondenceAddress": {
                                 |						"countryCode": {
                                 |							"$": "AD"
                                 |						},
                                 |						"line1": {
                                 |							"$": "Line 1"
                                 |						},
                                 |						"line2": {
                                 |							"$": "Line 2"
                                 |						},
                                 |						"line3": {
                                 |							"$": "Line 3"
                                 |						},
                                 |						"postalCode": {
                                 |							"$": "DE6 23QH"
                                 |						}
                                 |					},
                                 |					"referenceNumber": {
                                 |						"$": "REF1234"
                                 |					},
                                 |					"telephoneNumber": {
                                 |						"$": "0121 569 1478"
                                 |					}
                                 |				}
                                 |       }""".stripMargin

  val twoIndividualLeadtrustees = """
                                    |{
                                    |				"individual": {
                                    |					"correspondenceAddress": {
                                    |						"countryCode": {
                                    |							"$": "AD"
                                    |						},
                                    |						"line1": {
                                    |							"$": "Line 1"
                                    |						},
                                    |						"line2": {
                                    |							"$": "Line 2"
                                    |						},
                                    |						"line3": {
                                    |							"$": "Line 3"
                                    |						},
                                    |						"line4": {
                                    |							"$": "Line 4"
                                    |						},
                                    |						"postalCode": {
                                    |							"$": "NE24 1BR"
                                    |						}
                                    |					},
                                    |					"dateOfBirth": {
                                    |						"$": "2016-12-14"
                                    |					},
                                    |					"familyName": {
                                    |						"$": "Bloggs"
                                    |					},
                                    |					"givenName": {
                                    |						"$": "Joe"
                                    |					},
                                    |					"isUkNationalOrNonUkWithANino": {
                                    |						"$": true
                                    |					},
                                    |					"nino": {
                                    |						"$": "WA123456A"
                                    |					},
                                    |					"otherName": {
                                    |						"$": "Fred"
                                    |					},
                                    |					"passportOrIdCard": {
                                    |						"countryOfIssue": {
                                    |							"$": "AD"
                                    |						},
                                    |						"expiryDate": {
                                    |							"$": "2018-12-14"
                                    |						},
                                    |						"referenceNumber": {
                                    |							"$": "123456789dsfg"
                                    |						}
                                    |					},
                                    |					"telephoneNumber": {
                                    |						"$": "0191 265 1234"
                                    |					},
                                    |					"title": {
                                    |						"$": "Mr"
                                    |					}
                                    |				},
                                    |				"individual": {
                                    |					"correspondenceAddress": {
                                    |						"countryCode": {
                                    |							"$": "AD"
                                    |						},
                                    |						"line1": {
                                    |							"$": "Line 6"
                                    |						},
                                    |						"line2": {
                                    |							"$": "Line 7"
                                    |						},
                                    |						"line3": {
                                    |							"$": "Line 8"
                                    |						},
                                    |						"line4": {
                                    |							"$": "Line 9"
                                    |						},
                                    |						"postalCode": {
                                    |							"$": "NE24 1BR"
                                    |						}
                                    |					},
                                    |					"dateOfBirth": {
                                    |						"$": "2016-12-14"
                                    |					},
                                    |					"familyName": {
                                    |						"$": "Bloggs*************************************************"
                                    |					},
                                    |					"givenName": {
                                    |						"$": "Joe"
                                    |					},
                                    |					"isUkNationalOrNonUkWithANino": {
                                    |						"$": true
                                    |					},
                                    |					"nino": {
                                    |						"$": "WA123456A"
                                    |					},
                                    |					"otherName": {
                                    |						"$": "Fred"
                                    |					},
                                    |					"passportOrIdCard": {
                                    |						"countryOfIssue": {
                                    |							"$": "AD"
                                    |						},
                                    |						"expiryDate": {
                                    |							"$": "2018-12-14"
                                    |						},
                                    |						"referenceNumber": {
                                    |							"$": "123456789dsfg"
                                    |						}
                                    |					},
                                    |					"telephoneNumber": {
                                    |						"$": "0191 265 1234"
                                    |					},
                                    |					"title": {
                                    |						"$": "Mr"
                                    |					}
                                    |				}
                                    |       }""".stripMargin

  val mixedLeadtrustees = """
                            |{
                            |				"individual": {
                            |					"correspondenceAddress": {
                            |						"countryCode": {
                            |							"$": "AD"
                            |						},
                            |						"line1": {
                            |							"$": "Line 1"
                            |						},
                            |						"line2": {
                            |							"$": "Line 2"
                            |						},
                            |						"line3": {
                            |							"$": "Line 3"
                            |						},
                            |						"line4": {
                            |							"$": "Line 4"
                            |						},
                            |						"postalCode": {
                            |							"$": "NE24 1BR"
                            |						}
                            |					},
                            |					"dateOfBirth": {
                            |						"$": "2016-12-14"
                            |					},
                            |					"familyName": {
                            |						"$": "Bloggs"
                            |					},
                            |					"givenName": {
                            |						"$": "Joe"
                            |					},
                            |					"isUkNationalOrNonUkWithANino": {
                            |						"$": true
                            |					},
                            |					"nino": {
                            |						"$": "WA123456A"
                            |					},
                            |					"otherName": {
                            |						"$": "Fred"
                            |					},
                            |					"passportOrIdCard": {
                            |						"countryOfIssue": {
                            |							"$": "AD"
                            |						},
                            |						"expiryDate": {
                            |							"$": "2018-12-14"
                            |						},
                            |						"referenceNumber": {
                            |							"$": "123456789dsfg"
                            |						}
                            |					},
                            |					"telephoneNumber": {
                            |						"$": "0191 265 1234"
                            |					},
                            |					"title": {
                            |						"$": "Mr"
                            |					}
                            |				},
                            |				"company": {
                            |					"companyName": {
                            |						"$": "New Company Ltd"
                            |					},
                            |					"correspondenceAddress": {
                            |						"countryCode": {
                            |							"$": "AD"
                            |						},
                            |						"line1": {
                            |							"$": "Line 1"
                            |						},
                            |						"line2": {
                            |							"$": "Line 2"
                            |						},
                            |						"line3": {
                            |							"$": "Line 3"
                            |						},
                            |						"postalCode": {
                            |							"$": "DE6 23QH"
                            |						}
                            |					},
                            |					"referenceNumber": {
                            |						"$": "REF1234"
                            |					},
                            |					"telephoneNumber": {
                            |						"$": "0121 569 1478"
                            |					}
                            |				}
                            |       }""".stripMargin

  val invalidLeadtrustee = """
                             |{
                             |				"individual": {
                             |					"correspondenceAddress": {
                             |						"countryCode": {
                             |							"$": "AD"
                             |						},
                             |						"line1": {
                             |							"$": "Line 1"
                             |						},
                             |						"line2": {
                             |							"$": "Line 2"
                             |						},
                             |						"line3": {
                             |							"$": "Line 3"
                             |						},
                             |						"line4": {
                             |							"$": "Line 4"
                             |						},
                             |						"postalCode": {
                             |							"$": "NE24 1BR"
                             |						}
                             |					},
                             |					"dateOfBirth": {
                             |						"$": "2016-12-14"
                             |					},
                             |					"familyName": {
                             |						"$": "Invalid Extra Long Family Name"
                             |					},
                             |					"givenName": {
                             |						"$": "Joe"
                             |					},
                             |					"isUkNationalOrNonUkWithANino": {
                             |						"$": true
                             |					},
                             |					"nino": {
                             |						"$": "WA123456A"
                             |					},
                             |					"otherName": {
                             |						"$": "Fred"
                             |					},
                             |					"passportOrIdCard": {
                             |						"countryOfIssue": {
                             |							"$": "AD"
                             |						},
                             |						"expiryDate": {
                             |							"$": "2018-12-14"
                             |						},
                             |						"referenceNumber": {
                             |							"$": "123456789dsfg"
                             |						}
                             |					},
                             |					"telephoneNumber": {
                             |						"$": "0191 265 1234"
                             |					},
                             |					"title": {
                             |						"$": "Mr"
                             |					}
                             |				}
                             |          }""".stripMargin
}

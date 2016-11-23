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

class JsonValidatorSpec extends PlaySpec with  ValidatorBase{

  "JsonValidator" must {
    //Happy Path
    "read the schema and return a SuccessfulValidation" when {
      "given a valid trust" in {

        val result = schemaValidator.validate(validTrust,"")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"validTrust =>${messages.map(_.as[String])}")
            messages
          }
        }
        result mustBe SuccessfulValidation
      }
      "given a valid estate" in {
        val result = schemaValidator.validate(validEstate,"")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"validEstate =>${messages.map(_.as[String])}")
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
      "given an invalid trust" in {
        val result = schemaValidator.validate(invalidTrustNoName, "")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"invalidTrustNoName =>${messages.map(_.as[String])}")
            messages.map(_.as[String]) must contain ("object has missing required properties ([\"trustName\"])")
          }
        }
      }
      "given an invalid estate" in {
        def schemaValidator = JsonSchemaValidator("trustestate-21-11-2016.json")
        val result = schemaValidator.validate(invalidEstateNoName, "")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val messages: Seq[JsValue] = Json.parse(f.errors.toStream.mkString) \\ "message"
            println(s"invalidEstateNoName =>${messages.map(_.as[String])}")
            messages.map(_.as[String]) must contain ("object has missing required properties ([\"estateName\"])")
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
            messages.map(_.as[String]) must contain ("string \"Invalid Extra Long Family Name\" is too long (length: 30, maximum allowed: 25)")
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
            messages.map(_.as[String]) must contain ("instance failed to match exactly one schema (matched 2 out of 2)")
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
            messages.map(_.as[String]) must contain ("instance failed to match exactly one schema (matched 0 out of 1)")
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
            messages.map(_.as[String]) must contain ("instance failed to match exactly one schema (matched 0 out of 1)")
          }
        }
      }
    }
  }

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

  val validTrust = """{
      |	"@xmlns:xsi": "a",
      |	"trustEstate": {
      |		"trust": {
      |			"commencementDate": {
      |				"$": "a"
      |			},
      |			"correspondenceAddress": {
      |				"countryCode": {
      |					"$": "AD"
      |				},
      |				"line1": {
      |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |				},
      |				"line2": {
      |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |				},
      |				"line3": {
      |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |				},
      |				"line4": {
      |					"$": "aaaaaaaaaaaaaaaaa"
      |				},
      |				"postalCode": {
      |					"$": "aaaaaaaaaa"
      |				}
      |			},
      |			"currentYear": {
      |				"$": "aaaa"
      |			},
      |			"declaration": {
      |				"capacity": {
      |					"$": "0001"
      |				},
      |				"confirmation": {
      |					"$": true
      |				},
      |				"dateOfDeclaration": {
      |					"$": "a"
      |				},
      |				"familyName": {
      |					"$": "a"
      |				},
      |				"givenName": {
      |					"$": "a"
      |				},
      |				"otherName": {
      |					"$": "a"
      |				},
      |				"title": {
      |					"$": "a"
      |				}
      |			},
      |			"isNonResTypeIHTA84S218": {
      |				"$": true
      |			},
      |			"isS218IHTA84": {
      |				"$": true
      |			},
      |			"isTCGA925A": {
      |				"$": true
      |			},
      |			"isTrustUkResident": {
      |				"$": true
      |			},
      |			"leadTrustee": {
      |				"individual": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			},
      |			"legality": {
      |				"administrationCountryCode": {
      |					"$": "AD"
      |				},
      |				"governingCountryCode": {
      |					"$": "AD"
      |				},
      |				"isEstablishedUnderScottishLaw": {
      |					"$": true
      |				},
      |				"previousOffshoreCountryCode": {
      |					"$": "AD"
      |				}
      |			},
      |			"naturalPeople": {
      |				"individuals": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			},
      |			"nonResidentType": {
      |				"$": "0001"
      |			},
      |			"protectors": {
      |				"companies": {
      |					"companyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"referenceNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					}
      |				},
      |				"individuals": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			},
      |			"telephoneNumber": {
      |				"$": "aaaaaaaaaaaaaaaaaaa"
      |			},
      |			"trustName": {
      |				"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |			},
      |			"trustTypeType": {
      |				"employmentTrust": {
      |					"assets": {
      |						"businessAssets": {
      |							"businessAsset": {
      |								"businessDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessPayeRef": {
      |									"$": "aaaaaaaaaaaaa"
      |								},
      |								"businessValue": {
      |									"$": 0
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								}
      |							}
      |						},
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"otherAssets": {
      |							"otherAsset": {
      |								"OtherAssetDescription": {
      |									"$": "a"
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 1.1
      |								}
      |							}
      |						},
      |						"propertyAssets": {
      |							"propertyAsset": {
      |								"buildingLandName": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"propertyLandEvalDate": {
      |									"$": "a"
      |								},
      |								"propertyLandValue": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"shareAssets": {
      |							"shareAsset": {
      |								"numberShares": {
      |									"$": 1
      |								},
      |								"shareClass": {
      |									"$": "a"
      |								},
      |								"shareCompanyRegistrationNumber": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"shareType": {
      |									"$": "0001"
      |								},
      |								"shareValue": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"directorBeneficiaries": {
      |							"directorBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"employeeBeneficiaries": {
      |							"employeeBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"individualBeneficiaries": {
      |							"individualBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"employerFinancedRetirementBenefitSchemeStartDate": {
      |						"$": "a"
      |					},
      |					"isEmployerFinancedRetirementBenefitScheme": {
      |						"$": true
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				},
      |				"flatManagementSinkingFundTrust": {
      |					"assets": {
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"buildingLandBeneficiary": {
      |							"buildingBeneficiary": {
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				},
      |				"heritageMaintenanceFundTrust": {
      |					"assets": {
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"otherAssets": {
      |							"otherAsset": {
      |								"OtherAssetDescription": {
      |									"$": "a"
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 1.1
      |								}
      |							}
      |						},
      |						"propertyAssets": {
      |							"propertyAsset": {
      |								"buildingLandName": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"propertyLandEvalDate": {
      |									"$": "a"
      |								},
      |								"propertyLandValue": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"shareAssets": {
      |							"shareAsset": {
      |								"numberShares": {
      |									"$": 1
      |								},
      |								"shareClass": {
      |									"$": "a"
      |								},
      |								"shareCompanyRegistrationNumber": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"shareType": {
      |									"$": "0001"
      |								},
      |								"shareValue": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"buildingLandBeneficiary": {
      |							"buildingBeneficiary": {
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"isMultiPurposeIncome": {
      |						"$": true
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				},
      |				"interVivoTrust": {
      |					"assets": {
      |						"businessAssets": {
      |							"businessAsset": {
      |								"businessDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessPayeRef": {
      |									"$": "aaaaaaaaaaaaa"
      |								},
      |								"businessValue": {
      |									"$": 0
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								}
      |							}
      |						},
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"otherAssets": {
      |							"otherAsset": {
      |								"OtherAssetDescription": {
      |									"$": "a"
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 1.1
      |								}
      |							}
      |						},
      |						"partnershipAssets": {
      |							"partnershipAsset": {
      |								"startOfPartnership": {
      |									"$": "a"
      |								},
      |								"tradeOrProfession": {
      |									"$": "a"
      |								},
      |								"utr": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"propertyAssets": {
      |							"propertyAsset": {
      |								"buildingLandName": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"propertyLandEvalDate": {
      |									"$": "a"
      |								},
      |								"propertyLandValue": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"shareAssets": {
      |							"shareAsset": {
      |								"numberShares": {
      |									"$": 1
      |								},
      |								"shareClass": {
      |									"$": "a"
      |								},
      |								"shareCompanyRegistrationNumber": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"shareType": {
      |									"$": "0001"
      |								},
      |								"shareValue": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"charityBeneficiaries": {
      |							"charityBeneficiary": {
      |								"charityName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"charityNumber": {
      |									"$": "aaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						},
      |						"individualBeneficiaries": {
      |							"individualBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"isHoldOverClaimed": {
      |						"$": true
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				},
      |				"willIntestacyTrust": {
      |					"assets": {
      |						"businessAssets": {
      |							"businessAsset": {
      |								"businessDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessPayeRef": {
      |									"$": "aaaaaaaaaaaaa"
      |								},
      |								"businessValue": {
      |									"$": 0
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								}
      |							}
      |						},
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"otherAssets": {
      |							"otherAsset": {
      |								"OtherAssetDescription": {
      |									"$": "a"
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 1.1
      |								}
      |							}
      |						},
      |						"propertyAssets": {
      |							"propertyAsset": {
      |								"buildingLandName": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"propertyLandEvalDate": {
      |									"$": "a"
      |								},
      |								"propertyLandValue": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"shareAssets": {
      |							"shareAsset": {
      |								"numberShares": {
      |									"$": 1
      |								},
      |								"shareClass": {
      |									"$": "a"
      |								},
      |								"shareCompanyRegistrationNumber": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"shareType": {
      |									"$": "0001"
      |								},
      |								"shareValue": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"charityBeneficiaries": {
      |							"charityBeneficiary": {
      |								"charityName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"charityNumber": {
      |									"$": "aaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						},
      |						"individualBeneficiaries": {
      |							"individualBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"deceased": {
      |						"correspondenceAddress": {
      |							"countryCode": {
      |								"$": "AD"
      |							},
      |							"line1": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"line2": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"line3": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"line4": {
      |								"$": "aaaaaaaaaaaaaaaaa"
      |							},
      |							"postalCode": {
      |								"$": "aaaaaaaaaa"
      |							}
      |						},
      |						"dateOfDeath": {
      |							"$": "a"
      |						},
      |						"individual": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				}
      |			},
      |			"trustees": {
      |				"companies": {
      |					"companyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"referenceNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					}
      |				},
      |				"individuals": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			}
      |		}
      |	}
      |}""".stripMargin

  val invalidTrustNoName = """{
      |	"@xmlns:xsi": "a",
      |	"trustEstate": {
      |		"estate": {
      |			"adminPeriodFinishedDate": {
      |				"$": true
      |			},
      |			"deceased": {
      |				"correspondenceAddress": {
      |					"countryCode": {
      |						"$": "AD"
      |					},
      |					"line1": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"line2": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"line3": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"line4": {
      |						"$": "aaaaaaaaaaaaaaaaa"
      |					},
      |					"postalCode": {
      |						"$": "aaaaaaaaaa"
      |					}
      |				},
      |				"dateOfDeath": {
      |					"$": "a"
      |				},
      |				"individual": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			},
      |			"estateCriteriaMet": {
      |				"$": true
      |			},
      |			"incomeTaxDueMoreThan10000": {
      |				"$": true
      |			},
      |			"isCreatedByWill": {
      |				"$": true
      |			},
      |			"personalRepresentative": {
      |				"correspondenceAddress": {
      |					"countryCode": {
      |						"$": "AD"
      |					},
      |					"line1": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"line2": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"line3": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"line4": {
      |						"$": "aaaaaaaaaaaaaaaaa"
      |					},
      |					"postalCode": {
      |						"$": "aaaaaaaaaa"
      |					}
      |				},
      |				"individual": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				},
      |				"isExecutor": {
      |					"$": true
      |				},
      |				"telephoneNumber": {
      |					"$": "aaaaaaaaaaaaaaaaaaa"
      |				}
      |			},
      |			"saleOfEstateAssetsMoreThan250000": {
      |				"$": true
      |			},
      |			"saleOfEstateAssetsMoreThan500000": {
      |				"$": true
      |			},
      |			"worthMoreThanTwoAndHalfMillionAtTimeOfDeath": {
      |				"$": true
      |			}
      |		},
      |		"trust": {
      |			"commencementDate": {
      |				"$": "a"
      |			},
      |			"correspondenceAddress": {
      |				"countryCode": {
      |					"$": "AD"
      |				},
      |				"line1": {
      |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |				},
      |				"line2": {
      |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |				},
      |				"line3": {
      |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |				},
      |				"line4": {
      |					"$": "aaaaaaaaaaaaaaaaa"
      |				},
      |				"postalCode": {
      |					"$": "aaaaaaaaaa"
      |				}
      |			},
      |			"currentYear": {
      |				"$": "aaaa"
      |			},
      |			"declaration": {
      |				"capacity": {
      |					"$": "0001"
      |				},
      |				"confirmation": {
      |					"$": true
      |				},
      |				"dateOfDeclaration": {
      |					"$": "a"
      |				},
      |				"familyName": {
      |					"$": "a"
      |				},
      |				"givenName": {
      |					"$": "a"
      |				},
      |				"otherName": {
      |					"$": "a"
      |				},
      |				"title": {
      |					"$": "a"
      |				}
      |			},
      |			"isNonResTypeIHTA84S218": {
      |				"$": true
      |			},
      |			"isS218IHTA84": {
      |				"$": true
      |			},
      |			"isTCGA925A": {
      |				"$": true
      |			},
      |			"isTrustUkResident": {
      |				"$": true
      |			},
      |			"leadTrustee": {
      |				"company": {
      |					"companyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"referenceNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					}
      |				},
      |				"individual": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			},
      |			"legality": {
      |				"administrationCountryCode": {
      |					"$": "AD"
      |				},
      |				"governingCountryCode": {
      |					"$": "AD"
      |				},
      |				"isEstablishedUnderScottishLaw": {
      |					"$": true
      |				},
      |				"previousOffshoreCountryCode": {
      |					"$": "AD"
      |				}
      |			},
      |			"naturalPeople": {
      |				"individuals": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			},
      |			"nonResidentType": {
      |				"$": "0001"
      |			},
      |			"protectors": {
      |				"companies": {
      |					"companyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"referenceNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					}
      |				},
      |				"individuals": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			},
      |			"telephoneNumber": {
      |				"$": "aaaaaaaaaaaaaaaaaaa"
      |			},
      |			"trustTypeType": {
      |				"employmentTrust": {
      |					"assets": {
      |						"businessAssets": {
      |							"businessAsset": {
      |								"businessDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessPayeRef": {
      |									"$": "aaaaaaaaaaaaa"
      |								},
      |								"businessValue": {
      |									"$": 0
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								}
      |							}
      |						},
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"otherAssets": {
      |							"otherAsset": {
      |								"OtherAssetDescription": {
      |									"$": "a"
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 1.1
      |								}
      |							}
      |						},
      |						"propertyAssets": {
      |							"propertyAsset": {
      |								"buildingLandName": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"propertyLandEvalDate": {
      |									"$": "a"
      |								},
      |								"propertyLandValue": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"shareAssets": {
      |							"shareAsset": {
      |								"numberShares": {
      |									"$": 1
      |								},
      |								"shareClass": {
      |									"$": "a"
      |								},
      |								"shareCompanyRegistrationNumber": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"shareType": {
      |									"$": "0001"
      |								},
      |								"shareValue": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"directorBeneficiaries": {
      |							"directorBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"employeeBeneficiaries": {
      |							"employeeBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"individualBeneficiaries": {
      |							"individualBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"employerFinancedRetirementBenefitSchemeStartDate": {
      |						"$": "a"
      |					},
      |					"isEmployerFinancedRetirementBenefitScheme": {
      |						"$": true
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				},
      |				"flatManagementSinkingFundTrust": {
      |					"assets": {
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"buildingLandBeneficiary": {
      |							"buildingBeneficiary": {
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				},
      |				"heritageMaintenanceFundTrust": {
      |					"assets": {
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"otherAssets": {
      |							"otherAsset": {
      |								"OtherAssetDescription": {
      |									"$": "a"
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 1.1
      |								}
      |							}
      |						},
      |						"propertyAssets": {
      |							"propertyAsset": {
      |								"buildingLandName": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"propertyLandEvalDate": {
      |									"$": "a"
      |								},
      |								"propertyLandValue": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"shareAssets": {
      |							"shareAsset": {
      |								"numberShares": {
      |									"$": 1
      |								},
      |								"shareClass": {
      |									"$": "a"
      |								},
      |								"shareCompanyRegistrationNumber": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"shareType": {
      |									"$": "0001"
      |								},
      |								"shareValue": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"buildingLandBeneficiary": {
      |							"buildingBeneficiary": {
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"isMultiPurposeIncome": {
      |						"$": true
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				},
      |				"interVivoTrust": {
      |					"assets": {
      |						"businessAssets": {
      |							"businessAsset": {
      |								"businessDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessPayeRef": {
      |									"$": "aaaaaaaaaaaaa"
      |								},
      |								"businessValue": {
      |									"$": 0
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								}
      |							}
      |						},
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"otherAssets": {
      |							"otherAsset": {
      |								"OtherAssetDescription": {
      |									"$": "a"
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 1.1
      |								}
      |							}
      |						},
      |						"partnershipAssets": {
      |							"partnershipAsset": {
      |								"startOfPartnership": {
      |									"$": "a"
      |								},
      |								"tradeOrProfession": {
      |									"$": "a"
      |								},
      |								"utr": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"propertyAssets": {
      |							"propertyAsset": {
      |								"buildingLandName": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"propertyLandEvalDate": {
      |									"$": "a"
      |								},
      |								"propertyLandValue": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"shareAssets": {
      |							"shareAsset": {
      |								"numberShares": {
      |									"$": 1
      |								},
      |								"shareClass": {
      |									"$": "a"
      |								},
      |								"shareCompanyRegistrationNumber": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"shareType": {
      |									"$": "0001"
      |								},
      |								"shareValue": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"charityBeneficiaries": {
      |							"charityBeneficiary": {
      |								"charityName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"charityNumber": {
      |									"$": "aaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						},
      |						"individualBeneficiaries": {
      |							"individualBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"isHoldOverClaimed": {
      |						"$": true
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				},
      |				"willIntestacyTrust": {
      |					"assets": {
      |						"businessAssets": {
      |							"businessAsset": {
      |								"businessDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"businessPayeRef": {
      |									"$": "aaaaaaaaaaaaa"
      |								},
      |								"businessValue": {
      |									"$": 0
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								}
      |							}
      |						},
      |						"monetaryAssets": {
      |							"monetaryAsset": {
      |								"value": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"otherAssets": {
      |							"otherAsset": {
      |								"OtherAssetDescription": {
      |									"$": "a"
      |								},
      |								"lastValuationDate": {
      |									"$": "a"
      |								},
      |								"value": {
      |									"$": 1.1
      |								}
      |							}
      |						},
      |						"propertyAssets": {
      |							"propertyAsset": {
      |								"buildingLandName": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"propertyLandEvalDate": {
      |									"$": "a"
      |								},
      |								"propertyLandValue": {
      |									"$": 0
      |								}
      |							}
      |						},
      |						"shareAssets": {
      |							"shareAsset": {
      |								"numberShares": {
      |									"$": 1
      |								},
      |								"shareClass": {
      |									"$": "a"
      |								},
      |								"shareCompanyRegistrationNumber": {
      |									"$": "aaaaaaaaaa"
      |								},
      |								"shareType": {
      |									"$": "0001"
      |								},
      |								"shareValue": {
      |									"$": 0
      |								}
      |							}
      |						}
      |					},
      |					"beneficiaries": {
      |						"charityBeneficiaries": {
      |							"charityBeneficiary": {
      |								"charityName": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"charityNumber": {
      |									"$": "aaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						},
      |						"individualBeneficiaries": {
      |							"individualBeneficiary": {
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								},
      |								"individual": {
      |									"correspondenceAddress": {
      |										"countryCode": {
      |											"$": "AD"
      |										},
      |										"line1": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line2": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line3": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										},
      |										"line4": {
      |											"$": "aaaaaaaaaaaaaaaaa"
      |										},
      |										"postalCode": {
      |											"$": "aaaaaaaaaa"
      |										}
      |									},
      |									"dateOfBirth": {
      |										"$": "a"
      |									},
      |									"familyName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"givenName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"isUkNationalOrNonUkWithANino": {
      |										"$": true
      |									},
      |									"nino": {
      |										"$": "aaaaaaaaa"
      |									},
      |									"otherName": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"passportOrIdCard": {
      |										"countryOfIssue": {
      |											"$": "AD"
      |										},
      |										"expiryDate": {
      |											"$": "a"
      |										},
      |										"referenceNumber": {
      |											"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |										}
      |									},
      |									"telephoneNumber": {
      |										"$": "aaaaaaaaaaaaaaaaaaa"
      |									},
      |									"title": {
      |										"$": "a"
      |									}
      |								},
      |								"isVulnerable": {
      |									"$": true
      |								}
      |							}
      |						},
      |						"otherBeneficiaries": {
      |							"otherBeneficiary": {
      |								"beneficiaryDescription": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"correspondenceAddress": {
      |									"countryCode": {
      |										"$": "AD"
      |									},
      |									"line1": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line2": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line3": {
      |										"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |									},
      |									"line4": {
      |										"$": "aaaaaaaaaaaaaaaaa"
      |									},
      |									"postalCode": {
      |										"$": "aaaaaaaaaa"
      |									}
      |								},
      |								"income": {
      |									"isIncomeAtTrusteeDiscretion": {
      |										"$": true
      |									},
      |									"shareOfIncome": {
      |										"$": 0
      |									}
      |								}
      |							}
      |						}
      |					},
      |					"deceased": {
      |						"correspondenceAddress": {
      |							"countryCode": {
      |								"$": "AD"
      |							},
      |							"line1": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"line2": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"line3": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"line4": {
      |								"$": "aaaaaaaaaaaaaaaaa"
      |							},
      |							"postalCode": {
      |								"$": "aaaaaaaaaa"
      |							}
      |						},
      |						"dateOfDeath": {
      |							"$": "a"
      |						},
      |						"individual": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					},
      |					"settlors": {
      |						"companies": {
      |							"companyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"referenceNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							}
      |						},
      |						"individuals": {
      |							"correspondenceAddress": {
      |								"countryCode": {
      |									"$": "AD"
      |								},
      |								"line1": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line2": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line3": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								},
      |								"line4": {
      |									"$": "aaaaaaaaaaaaaaaaa"
      |								},
      |								"postalCode": {
      |									"$": "aaaaaaaaaa"
      |								}
      |							},
      |							"dateOfBirth": {
      |								"$": "a"
      |							},
      |							"familyName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"givenName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"isUkNationalOrNonUkWithANino": {
      |								"$": true
      |							},
      |							"nino": {
      |								"$": "aaaaaaaaa"
      |							},
      |							"otherName": {
      |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |							},
      |							"passportOrIdCard": {
      |								"countryOfIssue": {
      |									"$": "AD"
      |								},
      |								"expiryDate": {
      |									"$": "a"
      |								},
      |								"referenceNumber": {
      |									"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |								}
      |							},
      |							"telephoneNumber": {
      |								"$": "aaaaaaaaaaaaaaaaaaa"
      |							},
      |							"title": {
      |								"$": "a"
      |							}
      |						}
      |					}
      |				}
      |			},
      |			"trustees": {
      |				"companies": {
      |					"companyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"referenceNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					}
      |				},
      |				"individuals": {
      |					"correspondenceAddress": {
      |						"countryCode": {
      |							"$": "AD"
      |						},
      |						"line1": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line2": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line3": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						},
      |						"line4": {
      |							"$": "aaaaaaaaaaaaaaaaa"
      |						},
      |						"postalCode": {
      |							"$": "aaaaaaaaaa"
      |						}
      |					},
      |					"dateOfBirth": {
      |						"$": "a"
      |					},
      |					"familyName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"givenName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"isUkNationalOrNonUkWithANino": {
      |						"$": true
      |					},
      |					"nino": {
      |						"$": "aaaaaaaaa"
      |					},
      |					"otherName": {
      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
      |					},
      |					"passportOrIdCard": {
      |						"countryOfIssue": {
      |							"$": "AD"
      |						},
      |						"expiryDate": {
      |							"$": "a"
      |						},
      |						"referenceNumber": {
      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
      |						}
      |					},
      |					"telephoneNumber": {
      |						"$": "aaaaaaaaaaaaaaaaaaa"
      |					},
      |					"title": {
      |						"$": "a"
      |					}
      |				}
      |			}
      |		}
      |	}
      |}""".stripMargin

  val validEstate = """{
                      |	"@xmlns:xsi": "a",
                      |	"trustEstate": {
                      |		"estate": {
                      |			"estateName": {
                      |				"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |			},
                      |			"adminPeriodFinishedDate": {
                      |				"$": true
                      |			},
                      |			"deceased": {
                      |				"correspondenceAddress": {
                      |					"countryCode": {
                      |						"$": "AD"
                      |					},
                      |					"line1": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line2": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line3": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line4": {
                      |						"$": "aaaaaaaaaaaaaaaaa"
                      |					},
                      |					"postalCode": {
                      |						"$": "aaaaaaaaaa"
                      |					}
                      |				},
                      |				"dateOfDeath": {
                      |					"$": "a"
                      |				},
                      |				"individual": {
                      |					"correspondenceAddress": {
                      |						"countryCode": {
                      |							"$": "AD"
                      |						},
                      |						"line1": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line2": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line3": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line4": {
                      |							"$": "aaaaaaaaaaaaaaaaa"
                      |						},
                      |						"postalCode": {
                      |							"$": "aaaaaaaaaa"
                      |						}
                      |					},
                      |					"dateOfBirth": {
                      |						"$": "a"
                      |					},
                      |					"familyName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"givenName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"isUkNationalOrNonUkWithANino": {
                      |						"$": true
                      |					},
                      |					"nino": {
                      |						"$": "aaaaaaaaa"
                      |					},
                      |					"otherName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"passportOrIdCard": {
                      |						"countryOfIssue": {
                      |							"$": "AD"
                      |						},
                      |						"expiryDate": {
                      |							"$": "a"
                      |						},
                      |						"referenceNumber": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						}
                      |					},
                      |					"telephoneNumber": {
                      |						"$": "aaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"title": {
                      |						"$": "a"
                      |					}
                      |				}
                      |			},
                      |			"estateCriteriaMet": {
                      |				"$": true
                      |			},
                      |			"incomeTaxDueMoreThan10000": {
                      |				"$": true
                      |			},
                      |			"isCreatedByWill": {
                      |				"$": true
                      |			},
                      |			"personalRepresentative": {
                      |				"correspondenceAddress": {
                      |					"countryCode": {
                      |						"$": "AD"
                      |					},
                      |					"line1": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line2": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line3": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line4": {
                      |						"$": "aaaaaaaaaaaaaaaaa"
                      |					},
                      |					"postalCode": {
                      |						"$": "aaaaaaaaaa"
                      |					}
                      |				},
                      |				"individual": {
                      |					"correspondenceAddress": {
                      |						"countryCode": {
                      |							"$": "AD"
                      |						},
                      |						"line1": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line2": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line3": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line4": {
                      |							"$": "aaaaaaaaaaaaaaaaa"
                      |						},
                      |						"postalCode": {
                      |							"$": "aaaaaaaaaa"
                      |						}
                      |					},
                      |					"dateOfBirth": {
                      |						"$": "a"
                      |					},
                      |					"familyName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"givenName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"isUkNationalOrNonUkWithANino": {
                      |						"$": true
                      |					},
                      |					"nino": {
                      |						"$": "aaaaaaaaa"
                      |					},
                      |					"otherName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"passportOrIdCard": {
                      |						"countryOfIssue": {
                      |							"$": "AD"
                      |						},
                      |						"expiryDate": {
                      |							"$": "a"
                      |						},
                      |						"referenceNumber": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						}
                      |					},
                      |					"telephoneNumber": {
                      |						"$": "aaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"title": {
                      |						"$": "a"
                      |					}
                      |				},
                      |				"isExecutor": {
                      |					"$": true
                      |				},
                      |				"telephoneNumber": {
                      |					"$": "aaaaaaaaaaaaaaaaaaa"
                      |				}
                      |			},
                      |			"saleOfEstateAssetsMoreThan250000": {
                      |				"$": true
                      |			},
                      |			"saleOfEstateAssetsMoreThan500000": {
                      |				"$": true
                      |			},
                      |			"worthMoreThanTwoAndHalfMillionAtTimeOfDeath": {
                      |				"$": true
                      |			}
                      |		}
                      |	}
                      |}""".stripMargin

  val invalidEstateNoName = """{
                      |	"@xmlns:xsi": "a",
                      |	"trustEstate": {
                      |		"estate": {
                      |			"adminPeriodFinishedDate": {
                      |				"$": true
                      |			},
                      |			"deceased": {
                      |				"correspondenceAddress": {
                      |					"countryCode": {
                      |						"$": "AD"
                      |					},
                      |					"line1": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line2": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line3": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line4": {
                      |						"$": "aaaaaaaaaaaaaaaaa"
                      |					},
                      |					"postalCode": {
                      |						"$": "aaaaaaaaaa"
                      |					}
                      |				},
                      |				"dateOfDeath": {
                      |					"$": "a"
                      |				},
                      |				"individual": {
                      |					"correspondenceAddress": {
                      |						"countryCode": {
                      |							"$": "AD"
                      |						},
                      |						"line1": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line2": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line3": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line4": {
                      |							"$": "aaaaaaaaaaaaaaaaa"
                      |						},
                      |						"postalCode": {
                      |							"$": "aaaaaaaaaa"
                      |						}
                      |					},
                      |					"dateOfBirth": {
                      |						"$": "a"
                      |					},
                      |					"familyName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"givenName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"isUkNationalOrNonUkWithANino": {
                      |						"$": true
                      |					},
                      |					"nino": {
                      |						"$": "aaaaaaaaa"
                      |					},
                      |					"otherName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"passportOrIdCard": {
                      |						"countryOfIssue": {
                      |							"$": "AD"
                      |						},
                      |						"expiryDate": {
                      |							"$": "a"
                      |						},
                      |						"referenceNumber": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						}
                      |					},
                      |					"telephoneNumber": {
                      |						"$": "aaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"title": {
                      |						"$": "a"
                      |					}
                      |				}
                      |			},
                      |			"estateCriteriaMet": {
                      |				"$": true
                      |			},
                      |			"incomeTaxDueMoreThan10000": {
                      |				"$": true
                      |			},
                      |			"isCreatedByWill": {
                      |				"$": true
                      |			},
                      |			"personalRepresentative": {
                      |				"correspondenceAddress": {
                      |					"countryCode": {
                      |						"$": "AD"
                      |					},
                      |					"line1": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line2": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line3": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"line4": {
                      |						"$": "aaaaaaaaaaaaaaaaa"
                      |					},
                      |					"postalCode": {
                      |						"$": "aaaaaaaaaa"
                      |					}
                      |				},
                      |				"individual": {
                      |					"correspondenceAddress": {
                      |						"countryCode": {
                      |							"$": "AD"
                      |						},
                      |						"line1": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line2": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line3": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						},
                      |						"line4": {
                      |							"$": "aaaaaaaaaaaaaaaaa"
                      |						},
                      |						"postalCode": {
                      |							"$": "aaaaaaaaaa"
                      |						}
                      |					},
                      |					"dateOfBirth": {
                      |						"$": "a"
                      |					},
                      |					"familyName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"givenName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"isUkNationalOrNonUkWithANino": {
                      |						"$": true
                      |					},
                      |					"nino": {
                      |						"$": "aaaaaaaaa"
                      |					},
                      |					"otherName": {
                      |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"passportOrIdCard": {
                      |						"countryOfIssue": {
                      |							"$": "AD"
                      |						},
                      |						"expiryDate": {
                      |							"$": "a"
                      |						},
                      |						"referenceNumber": {
                      |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                      |						}
                      |					},
                      |					"telephoneNumber": {
                      |						"$": "aaaaaaaaaaaaaaaaaaa"
                      |					},
                      |					"title": {
                      |						"$": "a"
                      |					}
                      |				},
                      |				"isExecutor": {
                      |					"$": true
                      |				},
                      |				"telephoneNumber": {
                      |					"$": "aaaaaaaaaaaaaaaaaaa"
                      |				}
                      |			},
                      |			"saleOfEstateAssetsMoreThan250000": {
                      |				"$": true
                      |			},
                      |			"saleOfEstateAssetsMoreThan500000": {
                      |				"$": true
                      |			},
                      |			"worthMoreThanTwoAndHalfMillionAtTimeOfDeath": {
                      |				"$": true
                      |			}
                      |		}
                      |	}
                      |}""".stripMargin

}

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


import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jackson.JsonLoader
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsValue, Json}

import scala.util.{Failure, Success, Try}


class JsonValidatorSpec extends PlaySpec {

  "JsonValidator" must {
    "read the schema and return a SuccessfulValidation" when {
      "given a valid trust" in {
        def schemaValidator = JsonSchemaValidator("trustestate-21-11-2016.json")
        val result = schemaValidator.validate(validTrust,"")
        result mustBe SuccessfulValidation
      }
    }
    "read the schema and return a FailedValidation" when {
      "given an invalid trust" in {
        def schemaValidator = JsonSchemaValidator("trustestate-21-11-2016.json")
        val result = schemaValidator.validate(invalidTrustNoName, "")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val message = (Json.parse(f.errors.toStream.mkString) \ "message" ).get
            message
          }
        }
        res.toString mustBe "\"object has missing required properties ([\\\"trustName\\\"])\""
      }
    }
    "read the schema and return a SuccessfulValidation" when {
      "given a valid leadtrustee" in {
        def schemaValidator = JsonSchemaValidator("trustestate-21-11-2016.json")
        val result = schemaValidator.validate(validLeadtrustee, "/definitions/leadTrusteeType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val message = (Json.parse(f.errors.toStream.mkString) \ "message" ).get
            message
          }
        }
        res mustBe SuccessfulValidation
      }
    }
    "read the schema and return a FailedValidation" when {
      "given an invalid leadtrustee" in {
        def schemaValidator = JsonSchemaValidator("trustestate-21-11-2016.json")
        val result = schemaValidator.validate(invalidLeadtrustee, "/definitions/leadTrusteeType")
        val res = result match {
          case SuccessfulValidation => SuccessfulValidation
          case f: FailedValidation => {
            val message = (Json.parse(f.errors.toStream.mkString) \ "message" ).get
            message
          }
        }
        res.toString mustBe "\"string \\\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaa\\\" is too long (length: 29, maximum allowed: 28)\""
      }
    }
  }


//  def asJsonNode(jsonString: String):JsonNode = {
//   val regDocAsString = Try(JsonLoader.fromString(jsonString))
//
//  regDocAsString match {
//    case Failure(ex) => throw ex
//    case Success(json) => json
//    }
//  }

//  "individual": {
//    "title": "Mr",
//    "givenName": "Joe",
//    "otherName": "Fred",
//    "familyName": "Bloggs",
//    "dateOfBirth": "10/10/1967",
//    "nino": "WA123456A",
//    "passport": {
//    "identifier": "123435egxb",
//    "expiryDate": "12/12/2017",
//    "countryOfIssue": "UK"
//  },
//    "correspondenceAddress": {
//    "isNonUkAddress": false,
//    "addressLine1": "Line 1",
//    "addressLine2": "Line 2",
//    "addressLine3": "Line 3",
//    "addressLine4": "Line 4",
//    "postcode": "NE1 2BR",
//    "country": "UK"
//  },
//    "telephoneNumber": "0191 234 5678"
//  }

  val validLeadtrustee = """
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
 |       }""".stripMargin

  val invalidLeadtrustee = """
                           |{
                           |				"individual": {
                           |					"correspondenceAddress": {
                           |						"countryCode": {
                           |							"$": "AD"
                           |						},
                           |						"line1": {
                           |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
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
                           |       }""".stripMargin

  val validTrust =
    """{
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

  val invalidTrustNoName =
    """{
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
}

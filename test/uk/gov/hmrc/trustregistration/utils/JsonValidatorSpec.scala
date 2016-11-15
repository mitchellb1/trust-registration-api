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
import uk.gov.hmrc.trustregistration.ScalaDataExamples

import scala.concurrent.Future
import scala.util.{Failure, Right, Success, Try}


class JsonValidatorSpec extends PlaySpec {

  "JsonValidator" must {
    "read the schema" when {
      "the correct path" in {
        def schemaValidator = JsonSchemaValidator("trust.json")
        val result = schemaValidator.validate(asJsonNode(leadtrustee))
        result mustBe Right("success")
      }
    }
  }

  def asJsonNode(jsonString: String):JsonNode = {
   val regDocAsString = Try(JsonLoader.fromString(jsonString))

  regDocAsString match {
    case Failure(ex) => throw ex
    case Success(json) => json
    }
  }

  val leadtrustee = """{
                        "individual": {
                          "title": "Mr",
                          "givenName": "Joe",
                          "otherName": "Fred",
                          "familyName": "Bloggs",
                          "dateOfBirth": "10/10/1967",
                          "nino": "WA123456A",
                          "passport": {
                            "identifier": "123435egxb",
                            "expiryDate": "12/12/2017",
                            "countryOfIssue": "UK"
                          },
                          "correspondenceAddress": {
                            "isNonUkAddress": false,
                            "addressLine1": "Line 1",
                            "addressLine2": "Line 2",
                            "addressLine3": "Line 3",
                            "addressLine4": "Line 4",
                            "postcode": "NE1 2BR",
                            "country": "UK"
                          },
                          "telephoneNumber": "0191 234 5678"
                        },
                        "_links": {
                          "self": {
                            "href": "/trusts/2234567890/lead-trustee"
                          },
                          "trust": {
                            "href": "/trusts/2234567890"
                          }
                        }
                      }"""
  val completeTrust =
    """|{
       |  "trust": {
       |    "name": "Sample Will / Intestacy Trust",
       |    "correspondenceAddress": {
       |      "isNonUkAddress": true,
       |      "addressLine1": "Address Line 1",
       |      "addressLine2": "Address Line 2",
       |      "addressLine3": "Address Line 3",
       |      "addressLine4": "Address Line 4",
       |      "postcode": "NE1 2BR",
       |      "country": "FR"
       |    },
       |    "telephoneNumber": "0191 357 1596",
       |    "currentYear": "2016",
       |    "commencementDate": "2016-12-25",
       |    "legality": {
       |      "governingCountry": "UK",
       |      "administrationCountry": "UK",
       |      "previousOffshoreCountry": "",
       |      "isEstablishedUnderScottishLaw": false
       |    },
       |    "isTrustUkResident": true,
       |    "isSchedule5A": false,
       |    "nonResidentType": "N/A",
       |    "leadTrustee": {
       |      "individual": {
       |        "title": "Mr",
       |        "givenName": "Joe",
       |        "otherName": "John",
       |        "familyName": "Doe",
       |        "dateOfBirth": "1989-12-31",
       |        "nino": "WA123456A",
       |        "passport": {
       |          "identifier": "b12344b23b45",
       |          "expiryDate": "2018-12-25",
       |          "countryOfIssue": "UK"
       |        },
       |        "correspondenceAddress": {
       |          "isNonUkAddress": false,
       |          "addressLine1": "Address Line 1",
       |          "addressLine2": "Address Line 2",
       |          "addressLine3": "Address Line 3",
       |          "addressLine4": "Address Line 4",
       |          "postcode": "NE1 2BR",
       |          "country": "UK"
       |        },
       |        "telephoneNumber": "0191 357 1596"
       |      }
       |    },
       |    "trustees": [
       |      {
       |        "title": "Mr",
       |        "givenName": "Joe",
       |        "otherName": "John",
       |        "familyName": "Doe",
       |        "dateOfBirth": "1989-12-31",
       |        "nino": "WA123456A",
       |        "passport": {
       |          "identifier": "b12344b23b45",
       |          "expiryDate": "2018-12-25",
       |          "countryOfIssue": "UK"
       |        },
       |        "correspondenceAddress": {
       |          "isNonUkAddress": false,
       |          "addressLine1": "Address Line 1",
       |          "addressLine2": "Address Line 2",
       |          "addressLine3": "Address Line 3",
       |          "addressLine4": "Address Line 4",
       |          "postcode": "NE1 2BR",
       |          "country": "UK"
       |        },
       |        "telephoneNumber": "0191 357 1596"
       |      }
       |    ],
       |    "protectors": {
       |      "individuals": [
       |        {
       |          "title": "Mr",
       |          "givenName": "Joe",
       |          "otherName": "John",
       |          "familyName": "Doe",
       |          "dateOfBirth": "1989-12-31",
       |          "nino": "WA123456A",
       |          "passport": {
       |            "identifier": "b12344b23b45",
       |            "expiryDate": "2018-12-25",
       |            "countryOfIssue": "UK"
       |          },
       |          "correspondenceAddress": {
       |            "isNonUkAddress": false,
       |            "addressLine1": "Address Line 1",
       |            "addressLine2": "Address Line 2",
       |            "addressLine3": "Address Line 3",
       |            "addressLine4": "Address Line 4",
       |            "postcode": "NE1 2BR",
       |            "country": "UK"
       |          },
       |          "telephoneNumber": "0191 357 1596"
       |        },
       |        {
       |          "title": "Mr",
       |          "givenName": "Joe",
       |          "otherName": "John",
       |          "familyName": "Doe",
       |          "dateOfBirth": "1989-12-31",
       |          "nino": "WA123456A",
       |          "passport": {
       |            "identifier": "b12344b23b45",
       |            "expiryDate": "2018-12-25",
       |            "countryOfIssue": "UK"
       |          },
       |          "correspondenceAddress": {
       |            "isNonUkAddress": false,
       |            "addressLine1": "Address Line 1",
       |            "addressLine2": "Address Line 2",
       |            "addressLine3": "Address Line 3",
       |            "addressLine4": "Address Line 4",
       |            "postcode": "NE1 2BR",
       |            "country": "UK"
       |          },
       |          "telephoneNumber": "0191 357 1596"
       |        }
       |      ],
       |      "companies": [
       |        {
       |          "name": "Company Name",
       |          "referenceNumber": "AAA4546BN",
       |          "correspondenceAddress": {
       |            "isNonUkAddress": false,
       |            "addressLine1": "Address Line 1",
       |            "addressLine2": "Address Line 2",
       |            "addressLine3": "Address Line 3",
       |            "addressLine4": "Address Line 4",
       |            "postcode": "NE1 2BR",
       |            "country": "UK"
       |          },
       |          "telephoneNumber": "0191 357 1596"
       |        }
       |      ]
       |    },
       |    "naturalPeople": [
       |      {
       |        "title": "Mr",
       |        "givenName": "Joe",
       |        "otherName": "John",
       |        "familyName": "Doe",
       |        "dateOfBirth": "1989-12-31",
       |        "nino": "WA123456A",
       |        "passport": {
       |          "identifier": "b12344b23b45",
       |          "expiryDate": "2018-12-25",
       |          "countryOfIssue": "UK"
       |        },
       |        "correspondenceAddress": {
       |          "isNonUkAddress": false,
       |          "addressLine1": "Address Line 1",
       |          "addressLine2": "Address Line 2",
       |          "addressLine3": "Address Line 3",
       |          "addressLine4": "Address Line 4",
       |          "postcode": "NE1 2BR",
       |          "country": "UK"
       |        },
       |        "telephoneNumber": "0191 357 1596"
       |      }
       |    ],
       |    "declaration": {
       |      "title": "Mr",
       |      "givenName": "Joe",
       |      "otherName": "John",
       |      "familyName": "Doe",
       |      "capacity": "Executor",
       |      "date": "2016-12-25"
       |    },
       |    "willIntestacyTrust": {
       |      "assets": {
       |        "monetaryAssets": [
       |          10000
       |        ],
       |        "propertyAssets": [
       |          {
       |            "buildingLandName": "",
       |            "correspondenceAddress": {
       |              "isNonUkAddress": false,
       |              "addressLine1": "Address Line 1",
       |              "addressLine2": "Address Line 2",
       |              "addressLine3": "Address Line 3",
       |              "addressLine4": "Address Line 4",
       |              "postcode": "NE1 2BR",
       |              "country": "UK"
       |            },
       |            "propertyLandValue": 20000000,
       |            "propertyLandEvalDate": "2016-12-25"
       |          }
       |        ],
       |        "shareAssets": [
       |          {
       |            "shareNumber": 4356,
       |            "shareClass": "A",
       |            "shareType": "FTSE 100",
       |            "companyRegistrationNumber": "GGG2314dfs",
       |            "shareValue": 256000
       |          }
       |        ],
       |        "businessAssets": [
       |          {
       |            "businessName": "business Asset Name",
       |            "payeRef": "23456",
       |            "businessDescription": "We make things",
       |            "correspondenceAddress": {
       |              "isNonUkAddress": false,
       |              "addressLine1": "Address Line 1",
       |              "addressLine2": "Address Line 2",
       |              "addressLine3": "Address Line 3",
       |              "addressLine4": "Address Line 4",
       |              "postcode": "NE1 2BR",
       |              "country": "UK"
       |            },
       |            "value": 1000000,
       |            "lastValuationDate": "2016-12-25"
       |          }
       |        ],
       |        "otherAssets": [
       |          {
       |            "OtherAssetDescription": "Another Asset",
       |            "value": 10,
       |            "lastValuationDate": "2016-12-25"
       |          }
       |        ]
       |      },
       |      "beneficiaries": {
       |        "individualBeneficiaries": [
       |          {
       |            "individual": {
       |              "title": "Mr",
       |              "givenName": "Joe",
       |              "otherName": "John",
       |              "familyName": "Doe",
       |              "dateOfBirth": "1989-12-31",
       |              "nino": "WA123456A",
       |              "passport": {
       |                "identifier": "b12344b23b45",
       |                "expiryDate": "2018-12-25",
       |                "countryOfIssue": "UK"
       |              },
       |              "correspondenceAddress": {
       |                "isNonUkAddress": false,
       |                "addressLine1": "Address Line 1",
       |                "addressLine2": "Address Line 2",
       |                "addressLine3": "Address Line 3",
       |                "addressLine4": "Address Line 4",
       |                "postcode": "NE1 2BR",
       |                "country": "UK"
       |              },
       |              "telephoneNumber": "0191 357 1596"
       |            },
       |            "isVulnerable": false,
       |            "income": {
       |              "isIncomeAtTrusteeDiscretion": true,
       |              "shareOfIncome": 50
       |            }
       |          }
       |        ],
       |        "charityBeneficiaries": [
       |          {
       |            "name": "",
       |            "number": "",
       |            "correspondenceAddress": {
       |              "isNonUkAddress": false,
       |              "addressLine1": "Address Line 1",
       |              "addressLine2": "Address Line 2",
       |              "addressLine3": "Address Line 3",
       |              "addressLine4": "Address Line 4",
       |              "postcode": "NE1 2BR",
       |              "country": "UK"
       |            },
       |            "income": {
       |              "isIncomeAtTrusteeDiscretion": true,
       |              "shareOfIncome": 10
       |            }
       |          }
       |        ],
       |        "otherBeneficiaries": [
       |          {
       |            "beneficiaryDescription": "",
       |            "correspondenceAddress": {
       |              "isNonUkAddress": false,
       |              "addressLine1": "Address Line 1",
       |              "addressLine2": "Address Line 2",
       |              "addressLine3": "Address Line 3",
       |              "addressLine4": "Address Line 4",
       |              "postcode": "NE1 2BR",
       |              "country": "UK"
       |            },
       |            "income": {
       |              "isIncomeAtTrusteeDiscretion": false,
       |              "shareOfIncome": 40
       |            }
       |          }
       |        ]
       |      },
       |      "deceased": {
       |        "title": "Mr",
       |        "givenName": "Joe",
       |        "otherName": "John",
       |        "familyName": "Doe",
       |        "dateOfBirth": "1989-12-31",
       |        "nino": "WA123456A",
       |        "passport": {
       |          "identifier": "b12344b23b45",
       |          "expiryDate": "2018-12-25",
       |          "countryOfIssue": "UK"
       |        },
       |        "correspondenceAddress": {
       |          "isNonUkAddress": false,
       |          "addressLine1": "Address Line 1",
       |          "addressLine2": "Address Line 2",
       |          "addressLine3": "Address Line 3",
       |          "addressLine4": "Address Line 4",
       |          "postcode": "NE1 2BR",
       |          "country": "UK"
       |        },
       |        "telephoneNumber": "0191 357 1596"
       |      },
       |      "dateOfDeath" : "2018-12-25"
       |    }
       |  }
       |}""".stripMargin
}

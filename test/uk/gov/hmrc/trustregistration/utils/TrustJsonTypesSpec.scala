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

package uk.gov.hmrc.trustregistration.utils

import org.scalatestplus.play.PlaySpec


class TrustJsonTypesSpec extends PlaySpec  {

//  "JsonValidator" must {
//    //Happy Path
//    "read the schema and return a SuccessfulValidation" when {
//      "given a valid WillIntestacy Trust" in {
//
//        val result = schemaValidator.validateAgainstSchema(validWillIntestacyTrust, "/definitions/willIntestacyTrustType")
//
//        result match {
//          case SuccessfulValidation => // yay
//          case f: FailedValidation => {
//            val messages: Seq[JsValue] = Json.parse(f.validationErrors.mkString) \\ "message"
//            fail(s"validWillIntestacyTrust =>${messages.map(_.as[String])}")
//          }
//        }
//
//        result mustBe SuccessfulValidation
//
//      }
//    }
//
//    //Sad Path
//  }
//
//  val validWillIntestacyTrust =
//    """{
//
//                                  |		"assets": {
//                                  |			"businessAssets": {
//                                  |				"businessAsset": {
//                                  |					"businessDescription": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"businessName": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"businessPayeRef": {
//                                  |						"$": "aaaaaaaaaaaaa"
//                                  |					},
//                                  |					"businessValue": {
//                                  |						"$": 0
//                                  |					},
//                                  |					"correspondenceAddress": {
//                                  |						"countryCode": {
//                                  |							"$": "AD"
//                                  |						},
//                                  |						"line1": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line2": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line3": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line4": {
//                                  |							"$": "aaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"postalCode": {
//                                  |							"$": "aaaaaaaaaa"
//                                  |						}
//                                  |					},
//                                  |					"lastValuationDate": {
//                                  |						"$": "a"
//                                  |					}
//                                  |				}
//                                  |			},
//                                  |			"monetaryAssets": {
//                                  |				"monetaryAsset": {
//                                  |					"value": {
//                                  |						"$": 0
//                                  |					}
//                                  |				}
//                                  |			},
//                                  |			"otherAssets": {
//                                  |				"otherAsset": {
//                                  |					"OtherAssetDescription": {
//                                  |						"$": "a"
//                                  |					},
//                                  |					"lastValuationDate": {
//                                  |						"$": "a"
//                                  |					},
//                                  |					"value": {
//                                  |						"$": 1.1
//                                  |					}
//                                  |				}
//                                  |			},
//                                  |			"propertyAssets": {
//                                  |				"propertyAsset": {
//                                  |					"buildingLandName": {
//                                  |						"$": "aaaaaaaaaa"
//                                  |					},
//                                  |					"correspondenceAddress": {
//                                  |						"countryCode": {
//                                  |							"$": "AD"
//                                  |						},
//                                  |						"line1": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line2": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line3": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line4": {
//                                  |							"$": "aaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"postalCode": {
//                                  |							"$": "aaaaaaaaaa"
//                                  |						}
//                                  |					},
//                                  |					"propertyLandEvalDate": {
//                                  |						"$": "a"
//                                  |					},
//                                  |					"propertyLandValue": {
//                                  |						"$": 0
//                                  |					}
//                                  |				}
//                                  |			},
//                                  |			"shareAssets": {
//                                  |				"shareAsset": {
//                                  |					"numberShares": {
//                                  |						"$": 1
//                                  |					},
//                                  |					"shareClass": {
//                                  |						"$": "a"
//                                  |					},
//                                  |					"shareCompanyRegistrationNumber": {
//                                  |						"$": "aaaaaaaaaa"
//                                  |					},
//                                  |					"shareType": {
//                                  |						"$": "0001"
//                                  |					},
//                                  |					"shareValue": {
//                                  |						"$": 0
//                                  |					}
//                                  |				}
//                                  |			}
//                                  |		},
//                                  |		"beneficiaries": {
//                                  |			"charityBeneficiaries": {
//                                  |				"charityBeneficiary": {
//                                  |					"charityName": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"charityNumber": {
//                                  |						"$": "aaaaaaaa"
//                                  |					},
//                                  |					"correspondenceAddress": {
//                                  |						"countryCode": {
//                                  |							"$": "AD"
//                                  |						},
//                                  |						"line1": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line2": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line3": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line4": {
//                                  |							"$": "aaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"postalCode": {
//                                  |							"$": "aaaaaaaaaa"
//                                  |						}
//                                  |					},
//                                  |					"income": {
//                                  |						"isIncomeAtTrusteeDiscretion": {
//                                  |							"$": true
//                                  |						},
//                                  |						"shareOfIncome": {
//                                  |							"$": 0
//                                  |						}
//                                  |					}
//                                  |				}
//                                  |			},
//                                  |			"individualBeneficiaries": {
//                                  |				"individualBeneficiary": {
//                                  |					"income": {
//                                  |						"isIncomeAtTrusteeDiscretion": {
//                                  |							"$": true
//                                  |						},
//                                  |						"shareOfIncome": {
//                                  |							"$": 0
//                                  |						}
//                                  |					},
//                                  |					"individual": {
//                                  |						"correspondenceAddress": {
//                                  |							"countryCode": {
//                                  |								"$": "AD"
//                                  |							},
//                                  |							"line1": {
//                                  |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |							},
//                                  |							"line2": {
//                                  |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |							},
//                                  |							"line3": {
//                                  |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |							},
//                                  |							"line4": {
//                                  |								"$": "aaaaaaaaaaaaaaaaa"
//                                  |							},
//                                  |							"postalCode": {
//                                  |								"$": "aaaaaaaaaa"
//                                  |							}
//                                  |						},
//                                  |						"dateOfBirth": {
//                                  |							"$": "a"
//                                  |						},
//                                  |						"familyName": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"givenName": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"isUkNationalOrNonUkWithANino": {
//                                  |							"$": true
//                                  |						},
//                                  |						"nino": {
//                                  |							"$": "aaaaaaaaa"
//                                  |						},
//                                  |						"otherName": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"passportOrIdCard": {
//                                  |							"countryOfIssue": {
//                                  |								"$": "AD"
//                                  |							},
//                                  |							"expiryDate": {
//                                  |								"$": "a"
//                                  |							},
//                                  |							"referenceNumber": {
//                                  |								"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |							}
//                                  |						},
//                                  |						"telephoneNumber": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"title": {
//                                  |							"$": "a"
//                                  |						}
//                                  |					},
//                                  |					"isVulnerable": {
//                                  |						"$": true
//                                  |					}
//                                  |				}
//                                  |			},
//                                  |			"otherBeneficiaries": {
//                                  |				"otherBeneficiary": {
//                                  |					"beneficiaryDescription": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"correspondenceAddress": {
//                                  |						"countryCode": {
//                                  |							"$": "AD"
//                                  |						},
//                                  |						"line1": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line2": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line3": {
//                                  |							"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"line4": {
//                                  |							"$": "aaaaaaaaaaaaaaaaa"
//                                  |						},
//                                  |						"postalCode": {
//                                  |							"$": "aaaaaaaaaa"
//                                  |						}
//                                  |					},
//                                  |					"income": {
//                                  |						"isIncomeAtTrusteeDiscretion": {
//                                  |							"$": true
//                                  |						},
//                                  |						"shareOfIncome": {
//                                  |							"$": 0
//                                  |						}
//                                  |					}
//                                  |				}
//                                  |			}
//                                  |		},
//                                  |		"deceased": {
//                                  |			"correspondenceAddress": {
//                                  |				"countryCode": {
//                                  |					"$": "AD"
//                                  |				},
//                                  |				"line1": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"line2": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"line3": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"line4": {
//                                  |					"$": "aaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"postalCode": {
//                                  |					"$": "aaaaaaaaaa"
//                                  |				}
//                                  |			},
//                                  |			"dateOfDeath": {
//                                  |				"$": "a"
//                                  |			},
//                                  |			"individual": {
//                                  |				"correspondenceAddress": {
//                                  |					"countryCode": {
//                                  |						"$": "AD"
//                                  |					},
//                                  |					"line1": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line2": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line3": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line4": {
//                                  |						"$": "aaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"postalCode": {
//                                  |						"$": "aaaaaaaaaa"
//                                  |					}
//                                  |				},
//                                  |				"dateOfBirth": {
//                                  |					"$": "a"
//                                  |				},
//                                  |				"familyName": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"givenName": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"isUkNationalOrNonUkWithANino": {
//                                  |					"$": true
//                                  |				},
//                                  |				"nino": {
//                                  |					"$": "aaaaaaaaa"
//                                  |				},
//                                  |				"otherName": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"passportOrIdCard": {
//                                  |					"countryOfIssue": {
//                                  |						"$": "AD"
//                                  |					},
//                                  |					"expiryDate": {
//                                  |						"$": "a"
//                                  |					},
//                                  |					"referenceNumber": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					}
//                                  |				},
//                                  |				"telephoneNumber": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"title": {
//                                  |					"$": "a"
//                                  |				}
//                                  |			}
//                                  |		},
//                                  |		"settlors": {
//                                  |			"companies": {
//                                  |				"companyName": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"correspondenceAddress": {
//                                  |					"countryCode": {
//                                  |						"$": "AD"
//                                  |					},
//                                  |					"line1": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line2": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line3": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line4": {
//                                  |						"$": "aaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"postalCode": {
//                                  |						"$": "aaaaaaaaaa"
//                                  |					}
//                                  |				},
//                                  |				"referenceNumber": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"telephoneNumber": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaa"
//                                  |				}
//                                  |			},
//                                  |			"individuals": {
//                                  |				"correspondenceAddress": {
//                                  |					"countryCode": {
//                                  |						"$": "AD"
//                                  |					},
//                                  |					"line1": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line2": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line3": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"line4": {
//                                  |						"$": "aaaaaaaaaaaaaaaaa"
//                                  |					},
//                                  |					"postalCode": {
//                                  |						"$": "aaaaaaaaaa"
//                                  |					}
//                                  |				},
//                                  |				"dateOfBirth": {
//                                  |					"$": "a"
//                                  |				},
//                                  |				"familyName": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"givenName": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"isUkNationalOrNonUkWithANino": {
//                                  |					"$": true
//                                  |				},
//                                  |				"nino": {
//                                  |					"$": "aaaaaaaaa"
//                                  |				},
//                                  |				"otherName": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"passportOrIdCard": {
//                                  |					"countryOfIssue": {
//                                  |						"$": "AD"
//                                  |					},
//                                  |					"expiryDate": {
//                                  |						"$": "a"
//                                  |					},
//                                  |					"referenceNumber": {
//                                  |						"$": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                                  |					}
//                                  |				},
//                                  |				"telephoneNumber": {
//                                  |					"$": "aaaaaaaaaaaaaaaaaaa"
//                                  |				},
//                                  |				"title": {
//                                  |					"$": "a"
//                                  |				}
//                                  |			}
//                                  |		}
//
//                                  |}
//""".stripMargin

}

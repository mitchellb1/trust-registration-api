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

package uk.gov.hmrc.models

case class NoUtrException(message: String = "Missing required property utr") extends Exception(message)
case class PostCodeMissingForGBAddressException(message: String = "missing field ([\\\"postalCode\\\"])") extends Exception(message)
case class PostalCodeNotPresentForNonGbAddressException(message: String = "not required field ([\\\\\\\"postalCode\\\\\\\"])") extends Exception(message)
case class AtLeastOneTypeOfTrusteeException(message: String = "Must have either an individual or company lead trustee") extends Exception(message)
case class OnlyOneTypeOfTrusteeException(message: String = "Must have only an individual or company lead trustee") extends Exception(message)
case class NoMoreThanTwoNaturalPeopleException(message: String = "object has too many elements ([\\\"naturalPeople\\\"])") extends Exception(message)
case class NoMoreThanTwoProtectorsException(message: String = "object has too many elements ([\\\"protectors\\\"])") extends Exception(message)
case class MoreThanZeroSettlorsException(message: String = "object has missing required properties ([\\\"settlors\\\"])") extends Exception(message)
case class ShareOfIncomeMissingException(message: String = "missing field ([\\\"shareOfIncome\\\"])") extends Exception(message)
case class ShareOfIncomeNotRequiredException(message: String = "shareOfIncome field not required") extends Exception(message)
case class NoAssetsException(message: String = "Must have at least one type of required Asset") extends Exception(message)
case class NoOtherTypeOfAssetsException(message: String = "Must have no other types of Asset") extends Exception(message)
case class NoBeneficiariesException(message: String = "Must have at least one type of required Beneficiary") extends Exception(message)
case class NoOtherTypeOfBeneficiariesException(message: String = "Must have no other types of Beneficiary") extends Exception(message)
case class IsHoldOverClaimedException(message: String = "isHoldOverClaimed must be true") extends Exception(message)
case class PartnershipAssetsNotAllowedException(message: String = "partnership assets not allowed when Inter Vivo Trust is created by a deed of variation") extends Exception(message)
case class NoTrustTypeException(message: String = "Must have a Trust type") extends Exception(message)
case class OnlyOneTrustTypeAllowedException(message: String = "Must have only one Trust type") extends Exception(message)

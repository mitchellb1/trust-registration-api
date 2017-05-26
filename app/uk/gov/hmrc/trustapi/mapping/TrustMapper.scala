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

package uk.gov.hmrc.trustapi.mapping

import org.joda.time.DateTime
import uk.gov.hmrc.common.des._
import uk.gov.hmrc.common.mapping.DesDeclarationMapper
import uk.gov.hmrc.estateapi.mapping.DesCorrespondenceMapper
import uk.gov.hmrc.trustapi.rest.resources.core.Trust

object TrustMapper {

  def toDes(domainTrust: Trust): DesTrustEstate = {

    //val deceased: DesWill = DesWillMapper.toDes(domainTrust.deceased)

    val correspondence: DesCorrespondence = DesCorrespondenceMapper.toDes(domainTrust)

    val yearReturns = Some(DesYearsReturns(Some(true), None))

    val declaration = DesDeclarationMapper.toDes(domainTrust.declaration.get)

    val ukres: DesUkResidentialStatus = DesUkResidentialStatus(true, None)
    val details: DesTrustDetails = DesTrustDetails(startDate = domainTrust.commencementDate,
      lawCountry = domainTrust.legality.governingCountryCode,
      administrationCountry = domainTrust.legality.administrationCountryCode,
      residentialStatus = Some(DesResidentialStatus(Some(ukres))),
      typeOfTrust = "",
      deedOfVariation = Some(""),
      interVivos = Some(true),
      efrbsStartDate = Some(new DateTime()))

    val beneficiary: DesBeneficiary = DesBeneficiary(
      individualDetails =  List[DesIndividualDetails](),
      company =  List[DesCompany](),
      trust =  List[DesBeneficiaryTrust](),
      charity =  List[DesCharity](),
      unidentified =  List[DesUnidentified](),
      large =  List[DesLarge](),
      other =  List[DesOther]())

    val identification :  DesOrgIdentification = DesOrgIdentification(utr = Some("123456"), address = None)
    val leadTrusteeOrg = DesLeadTrusteeOrg(name="", phoneNumber = "", email = None, identification =  identification)
    val leadTrustee: DesLeadTrustee = DesLeadTrustee(leadTrusteeOrg = Some(leadTrusteeOrg), leadTrusteeInd = None)
    val settlors: DesSettlorType = DesSettlorType(settlor = None, settlorCompany = None)

    val entities: DesTrustEntities = DesTrustEntities(
      naturalPerson = None,
      beneficiary: DesBeneficiary,
      deceased = None,
      leadTrustees = leadTrustee,
      trustees = None,
      protectors = None,
      settlors = settlors
    )


    //TODO fix trusts to take assets
    val desTrust: DesTrust = DesTrust(details, entities)

    DesTrustEstate(
      None,
      correspondence,
      yearReturns,
      declaration,
      DesDetails(estate = None, trust = Some(desTrust))
    )
  }
}

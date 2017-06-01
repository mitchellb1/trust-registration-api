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

package uk.gov.hmrc.trustapi.mapping.todes

import org.joda.time.DateTime
import uk.gov.hmrc.common.des._
import uk.gov.hmrc.common.mapping.todes.{DesCorrespondenceMapper, DesDeclarationMapper, DesProtectorsMapper, DesYearReturnsMapper}
import uk.gov.hmrc.trustapi.rest.resources.core.Trust

object TrustMapper {


  def toDes(domainTrust: Trust): DesTrustEstate = {

    val admin: Option[DesAdmin] = domainTrust.utr.map(utr => DesAdmin(utr))

    val correspondence: DesCorrespondence = DesCorrespondenceMapper.toDes(domainTrust)

    val yearReturns: Option[DesYearsReturns] = DesYearReturnsMapper.toDes(domainTrust.yearsOfTaxConsequence)

    val declaration: DesDeclaration = DesDeclarationMapper.toDes(domainTrust.declaration)

    //TODO DOMAIN SCHEMA CHANGE : Mappings for deed of variation not matching to des schema
    val deedOfVariation: Option[String] = {
      domainTrust.trustType.willIntestacyTrust.map[Option[String]](wi => {
        if (wi.isDovTypeAddition) Some("Addition to the will trust") else None
      }).getOrElse(None)
    }

    //TODO DOMAIN SCHEMA CHANGE : Check intervivo business logic
    val intervivos: Option[Boolean] = {
      domainTrust.trustType.interVivoTrust.map[Option[Boolean]](interVivoTrust => Some(true)).getOrElse(Some(false))
    }

    //TODO DOMAIN SCHEMA CHANGE : Check efrbsStartDate business logic
    val efrbsStartDate: Option[DateTime] = {
          domainTrust.trustType.employmentTrust.map[Option[DateTime]](emp =>
            emp.employerFinancedRetirementBenefitSchemeStartDate).getOrElse(None)
      }


    //TODO  Replace hardcoded values below with mappers
    //val ukres: DesUkResidentialStatus = DesUkResidentialStatus(true, None)
    val details: DesTrustDetails = DesTrustDetails(
      startDate = domainTrust.commencementDate,
      lawCountry = domainTrust.legality.governingCountryCode,
      administrationCountry = domainTrust.legality.administrationCountryCode,
      residentialStatus = None,
      typeOfTrust = DesTrustTypeMapper.toDes(domainTrust),
      deedOfVariation = deedOfVariation,
      interVivos = intervivos,
      efrbsStartDate = None)

    //val uBen = DesUnidentified(description = "d", beneficiaryDiscretion = None, beneficiaryShareOfIncome = None)

    val beneficiary: DesBeneficiary = DesBeneficiary(
      individualDetails = None,
      company = None,
      trust = None,
      charity = None,
      unidentified = None,
      large = None,
      other = None)

    val identification: DesOrgIdentification = DesOrgIdentification(utr = Some("123456"), address = None)

    val leadTrusteeOrg: DesLeadTrusteeOrg = DesLeadTrusteeOrg(name="some company", phoneNumber = "0", email = None, identification =  identification)

    val leadTrustee: DesLeadTrustee = leadTrusteeOrg

    val trustees: Option[List[DesTrustee]] = None

    //val protectors: Option[DesProtectorType] = Some(DesProtectorsMapper.toDes(domainTrust.protectors))

    val settlors: DesSettlorType = DesSettlorType(settlor = None, settlorCompany = None)

    val entities: DesTrustEntities = DesTrustEntities(
      naturalPerson = None,
      beneficiary = beneficiary,
      deceased = None,
      leadTrustees = leadTrustee,
      trustees = trustees,
      protectors = Some(DesProtectorsMapper.toDes(domainTrust.protectors)),
      settlors = settlors
    )

    val money = DesMonetary(assetMonetaryAmount = 0)

    val assets = DesAssets(
      monetary = None,
      propertyOrLand = None,
      shares = None,
      business = None,
      partnerShip = None,
      other = None)

    val desTrust: DesTrust = DesTrust(details, entities, assets)

    DesTrustEstate(
      admin,
      correspondence,
      yearReturns,
      declaration,
      DesDetails(estate = None, trust = Some(desTrust))
    )
  }
}

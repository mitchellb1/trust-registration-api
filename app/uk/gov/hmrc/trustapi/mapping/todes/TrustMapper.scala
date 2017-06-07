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
import uk.gov.hmrc.common.mapping.todes.{DesCorrespondenceMapper, DesDeclarationMapper, DesWillMapper, DesYearReturnsMapper}
import uk.gov.hmrc.trustapi.rest.resources.core.Trust

object TrustMapper {

  def toDes(domainTrust: Trust): DesTrustEstate = {

    //DesTrustEstate variables
    val admin: Option[DesAdmin] = domainTrust.utr.map(utr => DesAdmin(utr))

    val correspondence: DesCorrespondence = DesCorrespondenceMapper.toDes(domainTrust)

    val yearReturns: Option[DesYearsReturns] = DesYearReturnsMapper.toDes(domainTrust.yearsOfTaxConsequence)

    val declaration: DesDeclaration = DesDeclarationMapper.toDes(domainTrust.declaration)

    val deceased: Option[DesWill] = {
      domainTrust.trustType.willIntestacyTrust.map(t => t.deceased)
        .map[DesWill](d => DesWillMapper.toDes(d))
    }
    //End DesTrustEstate variables

    //DesTrustDetails variables
    val residentialStatus = DesResidentialStatusMapper.toDes(domainTrust)

    val typeOfTrust = DesTrustTypeMapper.toDes(domainTrust)

    val deedOfVariation: Option[String] = {
      if (domainTrust.trustType.willIntestacyTrust != None) {
        domainTrust.trustType.willIntestacyTrust.map[Option[String]](wi => {
          if (wi.isDovTypeAddition) Some("Addition to the will trust") else None
        }).getOrElse(None)
      }
      else if (domainTrust.trustType.interVivoTrust != None) {
        domainTrust.trustType.interVivoTrust.map[Option[String]](iv => iv.dovType match {
          case Some("dovTypeAbsolute") => Some("Previously there was only an absolute interest under the will")
          case Some("dovTypeReplace") => Some("Replaced the will trust")
        }).getOrElse(None)
      }
      else {
        None
      }
    }

    //TODO POTENTIAL DOMAIN SCHEMA CHANGE : Check intervivo business logic
    val intervivos: Option[Boolean] = {
      domainTrust.trustType.interVivoTrust.map[Option[Boolean]](interVivoTrust => Some(true)).getOrElse(Some(false))
    }

    //TODO POTENTIAL DOMAIN SCHEMA CHANGE : Check efrbsStartDate business logic
    val efrbsStartDate: Option[DateTime] = {
      domainTrust.trustType.employmentTrust.map[Option[DateTime]](emp =>
        emp.employerFinancedRetirementBenefitSchemeStartDate).getOrElse(None)
    }
    //End DesTrustDetails variables

    val details: DesTrustDetails = DesTrustDetails(
      startDate = domainTrust.commencementDate,
      lawCountry = domainTrust.legality.governingCountryCode,
      administrationCountry = domainTrust.legality.administrationCountryCode,
      residentialStatus = residentialStatus,
      typeOfTrust = typeOfTrust,
      deedOfVariation = deedOfVariation,
      interVivos = intervivos,
      efrbsStartDate = efrbsStartDate)

    //DesEntities variables  -------------------------------------------------------------------------------------------
    //val naturalPerson = None
    val naturalPerson: Option[List[DesNaturalPerson]] = DesNaturalPersonMapper.toDes(domainTrust.naturalPeople)

    val leadTrustee: DesLeadTrustee = DesLeadTrusteesMapper.toDes(domainTrust.leadTrustee)

    val trustees: Option[List[DesTrustee]] = None

    val protectors: Option[DesProtectorType] = None
    //    val protectors: Option[DesProtectorType] = Some(DesProtectorsMapper.toDes(domainTrust.protectors))

    val settlors: DesSettlorType = DesSettlorTypeMapper.toDes(domainTrust.settlors)

    val beneficiary: DesBeneficiary = DesBeneficiary(
      individualDetails = None,
      company = None,
      trust = None,
      charity = None,
      unidentified = None,
      large = None,
      other = None)

    //End DesEntities variables  ---------------------------------------------------------------------------------------

    val entities: DesTrustEntities = DesTrustEntities(
      naturalPerson = naturalPerson,
      beneficiary = beneficiary,
      deceased = deceased,
      leadTrustees = leadTrustee,
      trustees = trustees,
      protectors = protectors,
      settlors = settlors
    )

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

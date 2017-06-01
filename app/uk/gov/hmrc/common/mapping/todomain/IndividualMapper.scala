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

package uk.gov.hmrc.common.mapping.todomain

import org.joda.time.DateTime
import uk.gov.hmrc.common.des._
import uk.gov.hmrc.common.mapping.AddressMapper
import uk.gov.hmrc.common.rest.resources.core.{Address, Individual, Passport}


object IndividualMapper {
  def toDomain(desName: DesName,
               dateOfBirth: DateTime,
               telephoneNumber: Option[String] = None,
               identification: Option[DesMappableIdentification] = None) : Individual = {

    identification match {
      case Some(id) => {
        id match {
          case desIdentification:DesIdentification => {
            addIdentificationToIndividual(
              createIndividualWithBasicProperties(desName,
                dateOfBirth,telephoneNumber))(desIdentification.nino,
              desIdentification.passport.map(p => PassportMapper.toDomain(p)),
              desIdentification.address.map(a => AddressMapper.toDomain(a)))
          }
          case desWillIdentification:DesWillIdentification => {
            addIdentificationToIndividual(
              createIndividualWithBasicProperties(desName,dateOfBirth,telephoneNumber))(desWillIdentification.nino,
              address = desWillIdentification.address.map(a => AddressMapper.toDomain(a)))
          }
        }
      }
      case None => {
        createIndividualWithBasicProperties(desName, dateOfBirth, telephoneNumber)
      }
    }
  }



  private def createIndividualWithBasicProperties(desName: DesName, dateOfBirth: DateTime, telephoneNumber: Option[String]) = {
    Individual(desName.firstName,
      desName.lastName,
      dateOfBirth,
      desName.middleName,
      telephoneNumber = telephoneNumber)
  }

  private def addIdentificationToIndividual(individual: Individual)(referenceNumber: Option[String], passport: Option[Passport] = None, address: Option[Address]): Individual ={
    individual.copy(nino = referenceNumber, passportOrIdCard = passport, correspondenceAddress = address)
  }
}


trait DesMappableIdentification

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

package uk.gov.hmrc.common.mapping

import uk.gov.hmrc.common.des._
import uk.gov.hmrc.common.rest.resources.core.Individual

object DesIdentificationMapper {

  def toDes(individual: Individual): DesIdentification = {
    individual.nino match {
      case Some(nino) => {
        DesIdentification(
          nino = Some(nino),
          passport = None,
          address = None
        )
      }
      case None => {
        individual.passportOrIdCard match {
          case Some(passportOrIdCard) => {
            individual.correspondenceAddress match {
              case Some(address) => {
                DesIdentification(
                  nino = None,
                  passport = DesPassportTypeMapper.toDes(individual),
                  address = Some(AddressMapper.toDes(address)))
              }
              case None => {
                throw new MissingPropertyException("Mapping to Des error : DesIdentificationMapper : Individual has missing Nino and Address")
              }
            }
          }
          case None => throw new MissingPropertyException("Mapping to Des error : DesIdentificationMapper : Individual has missing Nino and Passport")
        }
      }
    }
  }
}

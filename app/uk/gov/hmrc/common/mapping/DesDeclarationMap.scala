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

import uk.gov.hmrc.common.des.{DesDeclaration, DesName}
import uk.gov.hmrc.common.rest.resources.core.Declaration

trait DesDeclarationMap {

  def toDes(declaration: Declaration): DesDeclaration = {

    DesDeclaration(
      name = {
        DesName(
          firstName = declaration.givenName,
          middleName = declaration.otherName,
          lastName = declaration.familyName)
      },
      address = AddressMap.toDes(declaration.correspondenceAddress)
    )
  }
}
object DesDeclarationMap extends DesDeclarationMap

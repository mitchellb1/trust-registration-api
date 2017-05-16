package uk.gov.hmrc.common.mapping

import uk.gov.hmrc.common.des.DesAddress
import uk.gov.hmrc.common.rest.resources.core.Address


class AddressMap()  {

  def toDes(address: Address): DesAddress = {
    new DesAddress(
      line1 = address.line1,
      line2 = address.line2.getOrElse(""),
      line3 = address.line3,
      line4 = address.line4,
      postCode = address.postalCode,
      country = address.countryCode
    )
  }

  def toDomain(address: DesAddress):Address {
  new Address(
    line1 = address.line1,
    line2 = address.line2.getOrElse(""),
    line3 = address.line3,
    line4 = address.line4,
    postCode = address.postalCode,
    country = address.countryCode
    )
  }
}


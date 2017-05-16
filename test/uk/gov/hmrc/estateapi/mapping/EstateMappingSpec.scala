package uk.gov.hmrc.estateapi.mapping


import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.utils.ScalaDataExamples


class EstateMappingSpec extends PlaySpec with ScalaDataExamples {
  "EstateMapping" must {
    "accept a valid domain Estates case class" when {
      "and return a valid DesEstates case class" in {
        val result = EstateMapper.toDes()
      }
    }
  }
}

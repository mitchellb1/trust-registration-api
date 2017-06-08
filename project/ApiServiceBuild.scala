import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object ApiServiceBuild extends Build with MicroService {

  val appName = "trust-registration-api"

  override lazy val plugins: Seq[Plugins] = Seq(
    SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin
  )

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.core.PlayVersion
  import play.sbt.PlayImport._

  private val microserviceBootstrapVersion = "5.14.0"
  private val playHealthVersion = "2.1.0"
  private val playAuthVersion = "4.3.0"
  //private val playUrlBindersVersion = "2.0.0"
  private val playJsonLoggerVersion = "3.1.0"
  private val playConfigVersion = "4.3.0"
  private val hmrcTestVersion = "2.3.0"
//  private val playReactivemongoVersion = "5.1.0"
  private val domainVersion = "4.0.0"

  private val playHmrcApiVersion = "0.5.0"

  private val scalaTestVersion = "2.2.6"
  private val scalaTestPlusVersion = "1.5.1"
  private val mockitoVersion = "1.10.19"
  private val wireMockVersion = "1.58"
  private val pegDownVersion = "1.6.0"

  val compile = Seq(
   // "uk.gov.hmrc" %% "play-reactivemongo" % playReactivemongoVersion,

    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % microserviceBootstrapVersion,
    "uk.gov.hmrc" %% "play-authorisation" % playAuthVersion,
    "uk.gov.hmrc" %% "play-health" % playHealthVersion,

    "uk.gov.hmrc" %% "play-config" % playConfigVersion,
    "uk.gov.hmrc" %% "play-json-logger" % playJsonLoggerVersion,
    "uk.gov.hmrc" %% "domain" % domainVersion,
    "uk.gov.hmrc" %% "play-hmrc-api" % playHmrcApiVersion,
    "com.github.tomakehurst" % "wiremock" % wireMockVersion % "test,it",
    "com.github.fge" % "json-schema-validator" % "2.2.6"
  )
 // "uk.gov.hmrc" %% "play-url-binders" % playUrlBindersVersion,
  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test: Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
        "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
        "org.mockito" % "mockito-core" % mockitoVersion % scope,
        "org.pegdown" % "pegdown" % pegDownVersion % scope,
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusVersion % scope
      )
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {

      override lazy val scope: String = "it"

      override lazy val test = Seq(
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
        "org.scalatest" %% "scalatest" % "2.2.6" % scope,
        "org.pegdown" % "pegdown" % "1.5.0" % scope,
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}


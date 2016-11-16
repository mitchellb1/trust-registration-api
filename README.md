
# Trust & Estates API

[![Build Status](https://travis-ci.org/hmrc/trust-registration-api.svg?branch=master)](https://travis-ci.org/hmrc/trust-registration-api) [ ![Download](https://api.bintray.com/packages/hmrc/releases/trust-registration-api/images/download.svg) ](https://bintray.com/hmrc/releases/trust-registration-api/_latestVersion)

This REST API allows clients to register information related to a Trust or Estate, and to update existing registrations.

All end points are User Restricted (see [authorisation](https://developer.service.hmrc.gov.uk/api-documentation/docs/authorisation). Versioning, data formats etc follow the API Platform standards (see [the reference guide](https://developer.service.hmrc.gov.uk/api-documentation/docs/reference-guide)).

The API makes use of HATEOAS/HAL resource links. Your application does not need to store a catalogue of all URLs.

You can dive deeper into the documentation in the [API Developer Hub](https://developer.service.hmrc.gov.uk/api-documentation/docs/api#self-assessment-api).

## Running Locally

Install [Service Manager](https://github.com/hmrc/service-manager), then start dependencies:

    sm --start TRUSTS_STUBS -f
    sm --start DATASTREAM -f

Start the app:

    sbt run -Dhttp.port=9801

Now you can test sandbox:

    curl -X PUT -v http://localhost:9801/sandbox/trusts/$id/no-change -H 'Accept: application/vnd.hmrc.1.0+json'

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
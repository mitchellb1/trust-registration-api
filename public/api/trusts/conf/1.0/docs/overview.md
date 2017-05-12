Trusts API

Build Status  Download

This REST API allows clients to register information related to a Trust, and to update existing registrations.

All end points are User Restricted (see authorisation. Versioning, data formats etc follow the API Platform standards (see the reference guide).

The API makes use of HATEOAS/HAL resource links. Your application does not need to store a catalogue of all URLs.

You can dive deeper into the documentation in the API Developer Hub.

Running Locally

Install Service Manager, then start dependencies:

sm --start TRUSTS_STUBS -f
sm --start DATASTREAM -f
Start the app:

sbt run -Dhttp.port=9801
Now you can test sandbox:

curl -X PUT -v http://localhost:9801/sandbox/trusts/$id/no-change -H ‘Accept: application/vnd.hmrc.1.0+json’
License

This code is open source software licensed under the Apache 2.0 License
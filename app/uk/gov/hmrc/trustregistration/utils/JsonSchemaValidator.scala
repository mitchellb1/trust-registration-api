/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.trustregistration.utils

import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.fasterxml.jackson.databind.JsonNode

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

trait ValidationResult
case object SuccessfulValidation extends ValidationResult
case class FailedValidation(errors : Seq[String]) extends ValidationResult


trait JsonSchemaValidator {
  def validate(jsonString : String, node: String) : ValidationResult
}

object JsonSchemaValidator {
  def apply(schemaFile : String) = new JsonSchemaValidator {

    override def validate(jsonString : String, node: String): ValidationResult = {

      val jsonAsNode: Try[JsonNode] = Try(JsonLoader.fromString(jsonString))

     // #/definitions/leadTrusteeType"

      jsonAsNode match {
        case Failure(ex) => FailedValidation(Seq("Failed to parse Json"))
        case Success(json) => {
          val schema = JsonLoader.fromResource(s"/public/api/conf/2.0/schemas/$schemaFile")
          val factory = JsonSchemaFactory.byDefault.getJsonSchema(schema, node)
          val result = factory.validate(json)

          if (result.isSuccess) {
            SuccessfulValidation
          } else {
            FailedValidation(result.iterator().asScala.toSeq.map(pm => pm.asJson().toString))
          }
        }
      }
    }
  }
}

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
import com.github.fge.jsonschema.core.report.ProcessingReport
import play.api.libs.json.{JsError, JsPath, JsValue}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

trait ValidationResult
case class FailedValidation(errors : Seq[String]) extends ValidationResult
case class SuccessfulValidation() extends ValidationResult
object SuccessfulValidation extends ValidationResult

trait JsonSchemaValidator {
  val schemaFilename: String

  def validateIsJson(value: String): Option[JsonNode] = {
    val result: Try[JsonNode] = Try(JsonLoader.fromString(value))

    result match {
      case Success(node) => Some(node)
      case _ => None
    }
  }

  def validateAgainstSchema(jsonNode : JsonNode, schemaNode: String) : ValidationResult = {
    val schema: JsonNode = JsonLoader.fromResource(s"/public/api/conf/$schemaFilename")
    val factory = JsonSchemaFactory.byDefault.getJsonSchema(schema, schemaNode)
    val result: ProcessingReport = factory.validate(jsonNode)

    if (result.isSuccess) {
      SuccessfulValidation
    } else {
      FailedValidation(result.iterator().asScala.toSeq.map(pm => pm.asJson().toString))
    }
  }
}

object JsonSchemaValidator extends JsonSchemaValidator {
  val schemaFilename = "1.0/schemas/trusts.json"
  //val schemaFilename = "2.0/schemas/trustestate-21-11-2016.json"
}

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

package uk.gov.hmrc.trustregistration

import scala.io.Source


trait SchemaValidationExamples {
  lazy val threeItemSchema = Source.fromFile(getClass.getResource("/SchemaValidation/ThreeItemSchema.json").getPath).mkString
  lazy val maxLengthSchema = Source.fromFile(getClass.getResource("/SchemaValidation/MaxLengthSchema.json").getPath).mkString
  lazy val nestedItemSchema = Source.fromFile(getClass.getResource("/SchemaValidation/NestedItemSchema.json").getPath).mkString
  lazy val postcodeSchema = Source.fromFile(getClass.getResource("/SchemaValidation/PostcodeSchema.json").getPath).mkString
}

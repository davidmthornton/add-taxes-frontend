/*
 * Copyright 2019 HM Revenue & Customs
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

package forms.vat

import forms.behaviours.FormBehaviours
import models._
import models.vat._

class RegisterForVATOnlineFormProviderSpec extends FormBehaviours {

  val validData: Map[String, String] = Map(
    "value" -> RegisterForVATOnline.options.head.value
  )

  val form = new RegisterForVATOnlineFormProvider()()

  "RegisterForVATOnline form" must {

    behave like questionForm[RegisterForVATOnline](RegisterForVATOnline.values.head)

    behave like formWithOptionField(
      Field("value", Required -> "registerForVATOnline.error.required", Invalid -> "error.invalid"),
      RegisterForVATOnline.options.toSeq.map(_.value): _*)
  }
}

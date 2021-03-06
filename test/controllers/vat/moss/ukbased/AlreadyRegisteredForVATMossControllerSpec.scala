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

package controllers.vat.moss.ukbased

import controllers._
import controllers.actions._
import forms.vat.moss.AlreadyRegisteredForVATMossFormProvider
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.HtmlFormat
import utils.FakeNavigator
import viewmodels.ViewAction
import views.html.vat.moss.alreadyRegisteredForVATMoss

class AlreadyRegisteredForVATMossControllerSpec extends ControllerSpecBase {

  def onwardRoute = controllers.routes.IndexController.onPageLoad()

  val formProvider = new AlreadyRegisteredForVATMossFormProvider()
  val form = formProvider()
  lazy val viewAction =
    ViewAction(routes.AlreadyRegisteredForVATMossController.onSubmit(), "VatMossUkVatRegistered")

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new AlreadyRegisteredForVATMossController(
      frontendAppConfig,
      messagesApi,
      FakeAuthAction,
      new FakeNavigator[Call](desiredRoute = onwardRoute),
      FakeServiceInfoAction,
      formProvider)

  def viewAsString(form: Form[_] = form) =
    alreadyRegisteredForVATMoss(frontendAppConfig, form, viewAction)(HtmlFormat.empty)(fakeRequest, messages).toString

  "AlreadyRegisteredForVATMoss Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody("value" -> "Yes")
      val result = controller().onSubmit(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return BadRequest when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody("value" -> "")
      val boundForm = form.bind(Map("value" -> ""))

      val result = controller().onSubmit(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }
  }
}

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

package controllers.deenrolment

import controllers._
import controllers.actions._
import forms.deenrolment.DoYouNeedToStopVatMossNUFormProvider
import handlers.FakeErrorHandler
import models.deenrolment.DoYouNeedToStopVatMossNU
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.HtmlFormat
import utils.{FakeLoggingHelper, FakeNavigator}
import views.html.deenrolment.doYouNeedToStopVatMossNU

class DoYouNeedToStopVatMossNUControllerSpec extends ControllerSpecBase {

  def onwardRoute = controllers.routes.IndexController.onPageLoad()
  val serverErrorTemplate = "An error has occurred"

  val formProvider = new DoYouNeedToStopVatMossNUFormProvider()
  val form = formProvider()

  def controller(
    dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap,
    desiredRoute: Either[String, Call] = Right(onwardRoute)
  ) =
    new DoYouNeedToStopVatMossNUController(
      frontendAppConfig,
      messagesApi,
      new FakeNavigator[Either[String, Call]](desiredRoute = desiredRoute),
      FakeAuthAction,
      FakeServiceInfoAction,
      formProvider,
      new FakeErrorHandler(serverErrorTemplate = serverErrorTemplate),
      new FakeLoggingHelper
    )

  def viewAsString(form: Form[_] = form) =
    doYouNeedToStopVatMossNU(frontendAppConfig, form)(HtmlFormat.empty)(fakeRequest, messages).toString

  "DoYouNeedToStopVatMossNU Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", DoYouNeedToStopVatMossNU.options.head.value))

      val result = controller().onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "return OK" in {
      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustBe OK
    }

    for (option <- DoYouNeedToStopVatMossNU.options) {
      s"redirect to next page when '${option.value}' is submitted" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", (option.value)))
        val result = controller().onSubmit()(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }
  }
}

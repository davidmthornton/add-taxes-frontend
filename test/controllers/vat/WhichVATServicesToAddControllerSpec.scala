/*
 * Copyright 2018 HM Revenue & Customs
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

package controllers.vat

import play.api.data.Form
import utils.{FakeNavigator, HmrcEnrolmentType, RadioOption}
import connectors.FakeDataCacheConnector
import controllers.actions.{FakeServiceInfoAction, _}
import controllers._
import play.api.test.Helpers._
import forms.vat.WhichVATServicesToAddFormProvider
import models.vat.WhichVATServicesToAdd
import play.twirl.api.HtmlFormat
import views.html.vat.whichVATServicesToAdd

class WhichVATServicesToAddControllerSpec extends ControllerSpecBase {

  def onwardRoute = controllers.routes.IndexController.onPageLoad()

  val formProvider = new WhichVATServicesToAddFormProvider()
  val form = formProvider()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap)(enrolments: HmrcEnrolmentType*) =
    new WhichVATServicesToAddController(
      frontendAppConfig,
      messagesApi,
      FakeDataCacheConnector,
      new FakeNavigator(desiredRoute = onwardRoute),
      FakeAuthAction,
      FakeServiceInfoAction(enrolments: _*),
      formProvider
    )

  def viewAsString(form: Form[_] = form, radioOptions: Seq[RadioOption] = WhichVATServicesToAdd.options) =
    whichVATServicesToAdd(frontendAppConfig, form, radioOptions)(HtmlFormat.empty)(fakeRequest, messages).toString

  "WhichVATServicesToAdd Controller" must {
    "return OK and the correct view for a GET" in {
      val result = controller()().onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", WhichVATServicesToAdd.options.head.value))

      val result = controller()().onSubmit()(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller()().onSubmit()(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "return OK if no existing data is found" in {
      val result = controller(dontGetAnyData)().onPageLoad()(fakeRequest)

      status(result) mustBe OK
    }

    for (option <- WhichVATServicesToAdd.options) {
      s"redirect to next page when '${option.value}' is submitted and no existing data is found" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", (option.value)))
        val result = controller(dontGetAnyData)().onSubmit()(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "not display vat radio option" when {
      val radioOptions = WhichVATServicesToAdd.options.filterNot(_.value == "vat")

      "page is loaded and vat is enrolled" in {
        val result = controller()(HmrcEnrolmentType.VAT).onPageLoad()(fakeRequest)

        contentAsString(result) mustBe viewAsString(radioOptions = radioOptions)
      }

      "page errors and vat is enrolled" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
        val boundForm = form.bind(Map("value" -> "invalid value"))
        val result = controller()(HmrcEnrolmentType.VAT).onSubmit()(postRequest)

        contentAsString(result) mustBe viewAsString(boundForm, radioOptions)
      }
    }
  }
}
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

package controllers.other.importexports.ics

import javax.inject.Inject

import config.FrontendAppConfig
import controllers.actions._
import forms.other.importexports.DoYouHaveEORINumberFormProvider
import identifiers.DoYouHaveEORINumberId
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.{Enumerable, Navigator}
import viewmodels.ViewAction
import views.html.other.importexports.doYouHaveEORINumber
import controllers.other.importexports.ics.routes._
import play.api.mvc.Call

import scala.concurrent.Future

class DoYouHaveEORINumberController @Inject()(
  appConfig: FrontendAppConfig,
  override val messagesApi: MessagesApi,
  navigator: Navigator[Call],
  authenticate: AuthAction,
  serviceInfoData: ServiceInfoAction,
  formProvider: DoYouHaveEORINumberFormProvider)
    extends FrontendController
    with I18nSupport
    with Enumerable.Implicits {

  val form = formProvider()

  def onPageLoad() = (authenticate andThen serviceInfoData) { implicit request =>
    Ok(
      doYouHaveEORINumber(appConfig, form, ViewAction(DoYouHaveEORINumberController.onSubmit(), "AddICSTax"))(
        request.serviceInfoContent))
  }

  def onSubmit() = (authenticate andThen serviceInfoData).async { implicit request =>
    form
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[_]) =>
          Future.successful(
            BadRequest(
              doYouHaveEORINumber(
                appConfig,
                formWithErrors,
                ViewAction(DoYouHaveEORINumberController.onSubmit(), "AddICSTax"))(request.serviceInfoContent))),
        (value) => Future.successful(Redirect(navigator.nextPage(DoYouHaveEORINumberId.ICS, value)))
      )
  }
}

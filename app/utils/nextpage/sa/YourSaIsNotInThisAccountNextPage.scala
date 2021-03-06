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

package utils.nextpage.sa

import config.FrontendAppConfig
import controllers.sa.routes.SelectSACategoryController
import identifiers.YourSaIsNotInThisAccountId
import models.sa.YourSaIsNotInThisAccount
import play.api.mvc.{Call, Request}
import utils.NextPage

trait YourSaIsNotInThisAccountNextPage {

  implicit val yourSaIsNotInThisAccount: NextPage[YourSaIsNotInThisAccountId.type, YourSaIsNotInThisAccount, Call] = {
    new NextPage[YourSaIsNotInThisAccountId.type, YourSaIsNotInThisAccount, Call] {
      override def get(b: YourSaIsNotInThisAccount)(implicit appConfig: FrontendAppConfig, request: Request[_]): Call =
        b match {
          case YourSaIsNotInThisAccount.LookInOtherAccount =>
            Call("GET", appConfig.getBusinessAccountUrl("wrong-credentials"))
          case YourSaIsNotInThisAccount.AddToThisAccount => SelectSACategoryController.onPageLoadHasUTR()

        }
    }
  }
}

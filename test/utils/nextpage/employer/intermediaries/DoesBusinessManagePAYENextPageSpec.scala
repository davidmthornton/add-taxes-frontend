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

package utils.nextpage.employer.intermediaries

import models.employer.DoesBusinessManagePAYE
import utils.NextPage
import utils.nextpage.NextPageSpecBase

class DoesBusinessManagePAYENextPageSpec extends NextPageSpecBase {

  "doesBusinessManagePAYEController" when {
    behave like nextPage(
      NextPage.eiDoesBusinessManagePAYE,
      DoesBusinessManagePAYE.Yes,
      "/business-account/add-tax/employer/intermediaries/epaye/other-account"
    )

    behave like nextPage(
      NextPage.eiDoesBusinessManagePAYE,
      DoesBusinessManagePAYE.No,
      "/employment-intermediary-report/not-enrolled"
    )
  }
}

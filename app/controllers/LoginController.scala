/**
 * Copyright (C) 2012 Kaj Magnus Lindberg (born 1979)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package controllers

import actions.SafeActions._
import com.debiki.core._
import com.debiki.core.Prelude._
import debiki._
import debiki.DebikiHttp._
import com.mohiva.play.silhouette
import java.{util => ju}
import play.api._
import play.api.mvc.{Action => _, _}
import play.api.mvc.BodyParsers.parse.empty
import scala.concurrent.Future
import Utils.{OkHtml}


/**
 * Handles login; delegates to: LoginAsGuestController, LoginWithOpenIdController
 * and LoginWithPasswordController and LoginWithSilhouetteController.
 *
 * Usage:
 * You could use views.html.login.loginPage to how a login page in place of the
 * page that requires login. Then, views.html.login.loginPage will post to
 * this class, AppLogin, which will eventually redirect back to the
 * returnToUrl.
 */
object LoginController extends mvc.Controller {


  def loginWith(provider: String, returnToUrl: String) = ExceptionAction.async(empty) {
        implicit reqest =>
    asyncLogin(provider = provider, returnToUrl = returnToUrl)
  }


  def asyncLogin(provider: String, returnToUrl: String)
        (implicit request: Request[Unit]): Future[Result] = {

    /*
    def loginWithOpenId(identifier: String): Future[Result] = {
      LoginWithOpenIdController.asyncLogin(openIdIdentifier = identifier,
        returnToUrl = returnToUrl)(request)
    } */

    provider match {
      case "yahoo" =>
        ??? /*
        loginWithOpenId(IdentityOpenId.ProviderIdentifier.Yahoo)
        */
      case provider =>
        LoginWithOpenAuthController.startAuthenticationImpl(provider, returnToUrl, request)
    }
  }


  /**
   * Clears login related cookies and OpenID and OpenAuth stuff.
   */
  def logout = mvc.Action(parse.empty) { request =>
    // COULD save logout timestamp to database.
    // Keep the xsrf cookie, so login dialog works:
    Ok.discardingCookies(
      DiscardingCookie("dwCoSid"),
      DiscardingCookie(ConfigUserController.ConfigCookie))
  }

}

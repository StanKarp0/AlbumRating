package view

import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.stage.Window
import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.layout.GridPane

/**
  * Created by wojciech on 19.02.17.
  */

case class LoginResult(user: String, password: String)

class LoginDialog(window: Window) extends Dialog[LoginResult]{

  initOwner(window)
  title = "Login Dialog"
  headerText = "Login"

  private val loginButtonType = new ButtonType("Login", ButtonData.OKDone)
  dialogPane().buttonTypes = Seq(loginButtonType, ButtonType.Cancel)

  private val username = new TextField() {
    promptText = "Username"
  }
  private val password = new PasswordField() {
    promptText = "Password"
  }

  dialogPane().content = new GridPane() {
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 10)

    add(new Label("Username:"), 0, 0)
    add(username, 1, 0)
    add(new Label("Password:"), 0, 1)
    add(password, 1, 1)
  }

  resultConverter = dialogButton =>
    if (dialogButton == loginButtonType) LoginResult(username.text(), password.text())
    else null
}

package view

import java.text.SimpleDateFormat
import java.util.Date

import models.{Album, Rating}

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, Spinner, TextField}
import scalafx.scene.layout.GridPane

/**
  * Created by wojciech on 19.02.17.
  */
class AlbumPane(handler: Album => Unit) extends GridPane {

  hgap = 8
  vgap = 8
  margin = Insets(8)

  val artist = new TextField() {
    promptText = "Wykonawca"
  }
  val title = new TextField() {
    promptText = "Tytuł"
  }
  val year = new Spinner[Int](1900,2100,1980)
  val button = new Button("Zapisz") {
    onAction = (e: ActionEvent) => handler(Album(
      artist.text(), title.text(), year.value()
    ))
  }
  add(new Label("Wykonawca: "), 0,0)
  add(artist,                   1,0)
  add(new Label("Tytuł: "),     0,1)
  add(title,                    1,1)
  add(new Label("Rok: "),       0,2)
  add(year,                     1,2)
  add(button,                   0,3,1,2)

}
class RatingPane(handler: Rating => Unit) extends GridPane {

  hgap = 8
  vgap = 8
  margin = Insets(8)

  val desc = new TextField() {
    promptText = "Wpisz opis"
  }
  val value = new Spinner[Double](0,10,5,0.5)
  val button = new Button("Zapisz") {
    onAction = (e: ActionEvent) => handler(Rating(
      new SimpleDateFormat("yyyy-MM-dd").format(new Date()), value.value(), desc.text()
    ))
  }
  add(new Label("Ocena: "), 0,0)
  add(value,                1,0)
  add(new Label("Opis: "),  0,1)
  add(desc,                 1,1)
  add(button,               0,2,1,2)

}

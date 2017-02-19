package view

import models.Album
import view.Table.{AlbumView, ArtistView, RatingView}

import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.beans.value.ObservableValue
import scalafx.scene.control.{TableColumn, TableView}

/**
  * Created by wojciech on 19.02.17.
  */
object Table {
  def column[A,T](str: String, f: A => ObservableValue[T,T]): TableColumn[A,T] = {
    new TableColumn[A,T](str) {
      cellValueFactory = {s => f(s.value)}
    }
  }
  case class AlbumView(artist: String, title: String, year: Int, average: Double)
  case class RatingView(date: String, artist: String, title: String, year: Int, value: Double, desc: String)
  case class ArtistView(artist: String, average: Double)
}
class AlbumTable extends TableView[AlbumView]{
  columns ++= List(
    Table.column("Wykonawca",(a: AlbumView) => StringProperty(a.artist)),
    Table.column("Tytuł",(a: AlbumView) => StringProperty(a.title)),
    Table.column("Rok",(a: AlbumView) => ObjectProperty[Int](a.year)),
    Table.column("Średnia",(a: AlbumView) => ObjectProperty[Double](a.average))
  )

}
class RatingsTable extends TableView[RatingView]{
  columns ++= List(
    Table.column("Data",(a: RatingView) => StringProperty(a.date)),
    Table.column("Wykonawca",(a: RatingView) => StringProperty(a.artist)),
    Table.column("Tytuł",(a: RatingView) => StringProperty(a.title)),
    Table.column("Rok",(a: RatingView) => ObjectProperty[Int](a.year)),
    Table.column("Ocena",(a: RatingView) => ObjectProperty[Double](a.value)),
    Table.column("Opis",(a: RatingView) => StringProperty(a.desc))
  )
}
class ArtistTable extends TableView[ArtistView]{
  columns ++= List(
    Table.column("Wykonawca",(a: ArtistView) => StringProperty(a.artist)),
    Table.column("Średnia",(a: ArtistView) => ObjectProperty[Double](a.average))
  )
}

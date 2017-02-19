package main

import models.DBConnection
import view.Table.{AlbumView, ArtistView, RatingView}
import view._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try
import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.{JFXApp, Platform}
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.TextField
import scalafx.scene.layout.{BorderPane, ColumnConstraints, GridPane, Priority}
import scalafx.stage.WindowEvent

/**
  * Created by wojciech on 19.02.17.
  */
object Main extends JFXApp {

  val connectionTry: Option[Try[DBConnection]] = new LoginDialog(stage).showAndWait().collect{
    case LoginResult(user, password) => DBConnection(user, password)
  }

  val connection = connectionTry.flatMap(_.toOption)

  connection.foreach{ db =>

    val albumTable = new AlbumTable
    val ratingTable = new RatingsTable
    val artistTable = new ArtistTable

    val albums = ObjectProperty[Iterable[AlbumView]](Iterable())
    val ratings = ObjectProperty[Iterable[RatingView]](Iterable())
    val artists = ObjectProperty[Iterable[ArtistView]](Iterable())

    albums.onChange{(_,_, nv) => Platform.runLater(albumTable.items = ObservableBuffer(nv.toSeq))}
    ratings.onChange{(_,_, nv) => Platform.runLater(ratingTable.items = ObservableBuffer(nv.toSeq))}
    artists.onChange{(_,_, nv) => Platform.runLater(artistTable.items = ObservableBuffer(nv.toSeq))}

    def reload(): Unit ={
      db.find.foreach{ iterable =>
        albums() = DataConverter.resolveAlbums(iterable)
        ratings() = DataConverter.resolveRatings(iterable)
        artists() = DataConverter.resolveArtists(iterable)
      }
    }
    reload()

    val searchField = new TextField() {
      promptText = "Wyszukaj"
      text.onChange((_,_,nv) => {
        val lv = nv.toLowerCase
        albumTable.items = ObservableBuffer(
          albums().filter(a => a.title.toLowerCase.contains(lv) || a.artist.toLowerCase.contains(lv)).toSeq
        )
        ratingTable.items = ObservableBuffer(
          ratings().filter(r => r.title.toLowerCase.contains(lv) || r.artist.toLowerCase.contains(lv)).toSeq
        )
        artistTable.items = ObservableBuffer(
          artists().filter(r => r.artist.toLowerCase.contains(lv)).toSeq
        )
      })
    }

    val albumPane = new AlbumPane( album => {
      db.insertAlbum(album).foreach{ res =>
        println("insert album: "+res)
        reload()
      }

    })
    val ratingPane = new RatingPane( rating => {
      val sm = albumTable.selectionModel()
      if(!sm.isEmpty) {
        val albumTool = DataConverter.resolveAlbumTool(sm.getSelectedItem)
        db.insertRating(albumTool, rating).foreach{ res =>
          println("insert rating: "+res)
          reload()
        }
      }
    })

    stage = new PrimaryStage {
      onCloseRequest = (_: WindowEvent) => db.close()
      scene = new Scene() {
        root = new BorderPane() {
          left = new GridPane {
            add(albumPane,0,0)
            add(ratingPane,0,1)
          }
          center = new GridPane {
            hgap = 8
            vgap = 8
            margin = Insets(8)

            columnConstraints = List(new ColumnConstraints() {
              vgrow = Priority.Always
              hgrow = Priority.Always
            },new ColumnConstraints() {
              vgrow = Priority.Always
              hgrow = Priority.Always
            })

            add(searchField,0,0,2,1)
            add(artistTable,0,1)
            add(albumTable,1,1)
            add(ratingTable,0,2,2,1)
          }
        }
      }
    }
  }
}

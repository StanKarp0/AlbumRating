package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

/**
  * Created by wojciech on 19.02.17.
  */
case class Rating(date: String, value: Double, desc: String)

object Rating {

  implicit object RatingWriterReader extends BSONDocumentReader[Rating] with BSONDocumentWriter[Rating] {
    def write(rating: Rating): BSONDocument = BSONDocument(
      "date" -> rating.date,
      "value" -> rating.value,
      "desc" -> rating.desc
    )
    def read(doc: BSONDocument): Rating = Rating(
      doc.getAs[String]("date").getOrElse(""),
      doc.getAs[Double]("value").getOrElse(0.0),
      doc.getAs[String]("desc").getOrElse("")
    )
  }

}

case class AlbumSearchTool(artist: String, title: String, year: Int)

object AlbumSearchTool {
  implicit object AlbumSearchToolWriter extends BSONDocumentWriter[AlbumSearchTool] {
    def write(album: AlbumSearchTool): BSONDocument = BSONDocument(
      "artist" -> album.artist,
      "title" -> album.title,
      "year" -> album.year
    )
  }
}

case class Album(artist: String, title: String, year: Int, ratings: List[Rating] = Nil)

object Album {

  implicit object AlbumWriterReader extends BSONDocumentWriter[Album] with BSONDocumentReader[Album] {
    def write(album: Album): BSONDocument = BSONDocument(
      "artist" -> album.artist,
      "title" -> album.title,
      "year" -> album.year,
      "ratings" -> album.ratings
    )
    def read(doc: BSONDocument): Album = Album(
      doc.getAs[String]("artist").getOrElse("None"),
      doc.getAs[String]("title").getOrElse("None"),
      doc.getAs[Int]("year").getOrElse(0),
      doc.getAs[List[Rating]]("ratings").getOrElse(Nil)
    )
  }

}

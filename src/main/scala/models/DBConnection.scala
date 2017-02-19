package models

import reactivemongo.api.collections.bson.BSONCollection

import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api._
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSON, BSONDocument}

import scala.concurrent.Future

/**
  * Created by wojciech on 19.02.17.
  */
abstract class DBConnection {

  protected val mongoDriver: MongoDriver
  protected val collection: Future[BSONCollection]

  def close(): Unit ={
    mongoDriver.close()
  }

  def insertAlbum(album: Album): Future[Boolean] = {
    collection.flatMap{ coll =>
      coll.insert(album).map(_.ok)
    }
  }

  def insertRating(album: AlbumSearchTool, rating: Rating): Future[Boolean] = {
    collection.flatMap{ coll =>
      val update = BSONDocument("$push" -> BSONDocument("ratings" -> rating))
      val res = coll.findAndUpdate(album, update)
      res.map(_.lastError.exists(_.updatedExisting))
    }
  }

  def find: Future[Iterable[Album]] = {
    collection.flatMap{ coll =>
      coll.find(BSONDocument())
        .cursor[Album](ReadPreference.primary)
        .collect[Iterable]()
    }
  }


}

object DBConnection {

  def apply(login: String, pass: String): Try[DBConnection] = {
    val driver = new reactivemongo.api.MongoDriver
    val uri = s"mongodb://$login:$pass@ds031597.mlab.com:31597/musicrating?authMode=scram-sha1"
    MongoConnection.parseURI(uri).map{
      driver.connection
    }.flatMap { c =>
      Try(c.database("musicrating"))
    }.flatMap { db =>
      Try(db.map{ d =>
        println(d)
        d.collection[BSONCollection]("albums")
      })
    }.map { coll =>
      coll.foreach(println)
      println(coll)
      new DBConnection {
        protected val mongoDriver: MongoDriver = driver
        protected val collection: Future[BSONCollection] = coll
      }
    }
  }
}

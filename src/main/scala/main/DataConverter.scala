package main

import models.{Album, AlbumSearchTool}
import view.Table.{AlbumView, ArtistView, RatingView}

/**
  * Created by wojciech on 19.02.17.
  */
object DataConverter {

  def resolveAlbums(iterable: Iterable[Album]): Iterable[AlbumView] = {
    iterable.map{album =>
      AlbumView(album.artist, album.title, album.year,
        if(album.ratings.isEmpty) 0 else album.ratings.map(_.value).sum / album.ratings.size)
    }
  }

  def resolveRatings(iterable: Iterable[Album]): Iterable[RatingView] = {
    iterable.flatMap{ album =>
      album.ratings.map{ rating =>
        RatingView(rating.date, album.artist, album.title, album.year, rating.value, rating.desc)
      }
    }
  }

  def resolveArtists(iterable: Iterable[Album]): Iterable[ArtistView] = {
    iterable.flatMap { album =>
      album.ratings.map(_.value).map(album.artist -> _)
    }.groupBy(_._1).mapValues { ratings =>
      ratings.map(_._2).sum / ratings.size
    }.map{ case (artist, avg) =>
      ArtistView(artist, avg)
    }
  }

  def resolveAlbumTool(album: AlbumView): AlbumSearchTool =
    AlbumSearchTool(album.artist, album.title, album.year)
}

name := "AlbumRatingMongo"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "8.0.102-R11",
  "org.reactivemongo" %% "reactivemongo" % "0.12.1",
  "org.slf4j" % "slf4j-log4j12" % "1.7.23"
)

unmanagedJars in Compile += {
  val ps = new sys.SystemProperties
  val jh = ps("java.home")
  Attributed.blank(file(jh) / "lib/ext/jfxrt.jar")
}
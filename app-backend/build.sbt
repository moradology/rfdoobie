name := "rf-backend"

addCommandAlias("mg", "migrations/run")

scalaVersion := Version.scala

lazy val commonSettings = Seq(
  organization := "com.azavea",
  version := Version.rasterFoundry,
  cancelable in Global := true,
  scapegoatVersion in ThisBuild := Version.scapegoat,
  scapegoatIgnoredFiles := Seq(".*/datamodel/.*"),
  scalaVersion in ThisBuild := Version.scala,
  scalacOptions := Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:existentials",
    "-language:experimental.macros",
    "-Ypartial-unification",
    "-Ypatmat-exhaust-depth", "100"
  ),
  resolvers ++= Seq(
    //Resolver.sonatypeRepo("snapshots"),
    Resolver.bintrayRepo("lonelyplanet", "maven"),
    //Resolver.bintrayRepo("guizmaii", "maven"),
    //Resolver.bintrayRepo("kwark", "maven"), // Required for Slick 3.1.1.2, see https://github.com/azavea/raster-foundry/pull/1576
    "locationtech-releases" at "https://repo.locationtech.org/content/groups/releases",
    "locationtech-snapshots" at "https://repo.locationtech.org/content/groups/snapshots"
    //Resolver.bintrayRepo("naftoligug", "maven")
  ),
  shellPrompt := { s => Project.extract(s).currentProject.id + " > " },
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)

// Create a new MergeStrategy for aop.xml files
val aopMerge = new sbtassembly.MergeStrategy {
  val name = "aopMerge"
  import scala.xml._
  import scala.xml.dtd._

  def apply(tempDir: File, path: String, files: Seq[File]): Either[String, Seq[(File, String)]] = {
    val dt = DocType("aspectj", PublicID("-//AspectJ//DTD//EN", "http://www.eclipse.org/aspectj/dtd/aspectj.dtd"), Nil)
    val file = MergeStrategy.createMergeTarget(tempDir, path)
    val xmls: Seq[Elem] = files.map(XML.loadFile)
    val aspectsChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "aspects" \ "_")
    val weaverChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "weaver" \ "_")
    val options: String = xmls.map(x => (x \\ "aspectj" \ "weaver" \ "@options").text).mkString(" ").trim
    val weaverAttr = if (options.isEmpty) Null else new UnprefixedAttribute("options", options, Null)
    val aspects = new Elem(null, "aspects", Null, TopScope, false, aspectsChildren: _*)
    val weaver = new Elem(null, "weaver", weaverAttr, TopScope, false, weaverChildren: _*)
    val aspectj = new Elem(null, "aspectj", Null, TopScope, false, aspects, weaver)
    XML.save(file.toString, aspectj, "UTF-8", xmlDecl = false, dt)
    IO.append(file, IO.Newline.getBytes(IO.defaultCharset))
    Right(Seq(file -> path))
  }
}

lazy val loggingDependencies = List(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" %  "logback-classic" % "1.1.7"
)

lazy val dbDependencies = List(
  Dependencies.hikariCP,
  Dependencies.postgres
)

lazy val root = Project("root", file("."))
  .aggregate(database, datamodel)
  .settings(commonSettings:_*)

lazy val database = Project("database", file("database"))
  .dependsOn(datamodel)
  .settings(commonSettings:_*)
  .settings({
     libraryDependencies ++= dbDependencies ++ loggingDependencies ++ Seq(
       Dependencies.scalatest,
       Dependencies.doobieCore,
       Dependencies.doobieHikari,
       Dependencies.doobieSpecs,
       Dependencies.doobieScalatest,
       Dependencies.doobiePostgres,
       "net.postgis" % "postgis-jdbc" % "2.2.1",
       "net.postgis" % "postgis-jdbc-jtsparser" % "2.2.1",
       "org.locationtech.jts" % "jts-core" % "1.15.0",
       "com.lonelyplanet" %% "akka-http-extensions" % "0.4.15"
     )
  })

lazy val datamodel = Project("datamodel", file("datamodel"))
  .dependsOn(bridge)
  .settings(commonSettings:_*)
  .settings({
    libraryDependencies ++= loggingDependencies ++ Seq(
      Dependencies.scalatest,
      Dependencies.geotrellisSlick,
      Dependencies.geotrellisRaster,
      Dependencies.geotrellisSpark,
      Dependencies.circeCore,
      Dependencies.circeGenericExtras,
      Dependencies.akka,
      Dependencies.akkahttp
    )
  })

lazy val bridge = Project("bridge", file("bridge"))
  .settings(commonSettings:_*)
  .settings({
    libraryDependencies ++= loggingDependencies ++ Seq(
      Dependencies.circeCore,
      Dependencies.circeGeneric,
      Dependencies.circeParser,
      Dependencies.geotrellisVector
    )
  })

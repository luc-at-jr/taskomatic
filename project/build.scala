import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object TaskomaticBuild extends Build {
  val Organization = "com.janrain"
  val Name = "Taskomatic"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.2"
  val ScalatraVersion = "2.2.1"

  lazy val project = Project (
    "taskomatic",
    file("."),
    settings = Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers ++= Seq(
        Classpaths.typesafeReleases,
        "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
        "gideondk-repo"     at "https://raw.github.com/gideondk/gideondk-mvn-repo/master",
        "spray"             at "http://repo.spray.io/"
      ),
      libraryDependencies ++= Seq(
        
        // Built-in dependencies
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar")),

        // Additional dependencies
        "org.slf4j" % "slf4j-nop" % "1.6.4",
        "com.typesafe.akka" %% "akka-actor" % "2.2.0",
        "com.typesafe.slick" %% "slick" % "1.0.0",
        "org.slf4j" % "slf4j-nop" % "1.6.4",
        "com.h2database" % "h2" % "1.3.166",
        "org.scalatra" %% "scalatra-auth" % "2.2.1",
        "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
        "com.github.tototoshi" %% "slick-joda-mapper" % "0.3.0",
        "c3p0" % "c3p0" % "0.9.1.2",
        "nl.gideondk" %% "raiku" % "0.6.1",
        "io.spray" %% "spray-json" % "1.2.5",
        "com.github.tminglei" % "slick-pg_2.10.1" % "0.1.3"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}

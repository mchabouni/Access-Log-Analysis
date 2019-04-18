import sbt._
import Dependencies._

name := "accesslog-analysis"

version := "0.1"

scalaVersion := "2.12.8"

organization := "com.ebiznext"

organizationName := "Ebiznext"

organizationHomepage := Some(url("http://www.ebiznext.com"))

version := "0.1"

scalaVersion in ThisBuild := "2.11.9"



lazy val root = project
  .in(file("."))
  .aggregate(
    sparkDataSetsTraining,
    scalaApacheAccessLogParser,
    uapScala
  )

lazy val sparkDataSetsTraining=project
                                .settings(
                                name := "SparkDatasetsTraining",
                                version := "0.1",
                                libraryDependencies ++= commonDependencies ++ sparkDataTrainDependencies, 
                                dependencyOverrides ++= Seq("com.maxmind.geoip2" % "geoip2" % Versions.geoip)
                                )
                                .dependsOn(scalaApacheAccessLogParser)

lazy val scalaApacheAccessLogParser=project
                                      .settings(
                                        name := "ScalaApacheAccessLogParser",
                                    
                                        version := "1.0",
                                    
                                        resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                                    
                                        scalacOptions += "-deprecation",
                                    
                                        libraryDependencies ++=commonDependencies
                                      )
                                      .dependsOn(uapScala)

lazy val uapScala= project.in(file("uapScala"))
                          .settings(
                            name := "uapScala",
                            version := "0.6.1-SNAPSHOT",
                            organization := "org.uaparser",
                            mimaPreviousArtifacts := Set("org.uaparser" %% "uapScala" % "0.3.0"),
                            scalacOptions ++= Seq(
                              "-deprecation",
                              "-encoding", "UTF-8",
                              "-feature",
                              "-unchecked",
                              "-Yno-adapted-args",
                              "-Ywarn-dead-code",
                              "-Ywarn-numeric-widen",
                              "-Xfuture"
                            ),
                            unmanagedResourceDirectories in Compile += baseDirectory.value / "core",
                            includeFilter in (Compile, unmanagedResources) := "regexes.yaml",
                            unmanagedResourceDirectories in Test += baseDirectory.value / "core",
                            includeFilter in (Test, unmanagedResources) := "*.yaml",
                            libraryDependencies ++= commonDependencies ++ userAgentParserDependencies
                          )
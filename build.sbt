ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"
val pekkoVersion = "1.1.2"
val redisVersion = "3.42"

lazy val root = (project in file("."))
  .settings(
    name := "notification-server",
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion,
      "org.apache.pekko" %% "pekko-cluster-typed" % pekkoVersion,
      "org.apache.pekko" %% "pekko-cluster-sharding-typed" % pekkoVersion,

      "dev.profunktor" %% "redis4cats-effects" % "1.4.3",
      "dev.profunktor" %% "redis4cats-streams" % "1.4.3"
    ),

    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-deprecation"
    )
  )



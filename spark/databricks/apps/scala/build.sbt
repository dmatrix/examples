name := "main/scala/iot"

version := "1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.0",
  "org.apache.spark" %% "spark-sql"  % "1.6.0"
)


scalaVersion := "2.10.5"

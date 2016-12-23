name := "main/scala/zips"

version := "1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.0",
  "org.apache.spark" %% "spark-sql"  % "2.0.0"
)


scalaVersion := "2.10.5"

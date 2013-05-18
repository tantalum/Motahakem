import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Motahakem"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
	// SSH Dependencies
	"net.schmizz" % "sshj" % "0.7.0",

	//MySQL
	"mysql" % "mysql-connector-java" % "5.1.18"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}

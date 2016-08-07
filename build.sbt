name := "Quiz"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  jdbc,
  javaJdbc,
  cache,
  "com.google.inject" % "guice" % "4.0-beta"
)     

// Database
libraryDependencies ++= Seq(
	"mysql" % "mysql-connector-java" % "5.1.18"
)

// csv writer
libraryDependencies ++= Seq(
	"org.apache.commons" % "commons-csv" % "1.1",
	"org.apache.poi" % "poi" % "3.12"
)

// Hibernate
libraryDependencies ++= Seq(
	javaJpa,
	"org.hibernate" % "hibernate-entitymanager" % "4.2.16.Final"
)

resolvers += Resolver.url("Objectify Play Repository", url("http://deadbolt.ws/releases/"))(Resolver.ivyStylePatterns)

// Deadbolt
libraryDependencies ++= Seq(
  "be.objectify" %% "deadbolt-java" % "2.2-RC5"
)

// Java Mail
libraryDependencies += "javax.mail" % "mail" % "1.4.1"

libraryDependencies += "javax.activation" % "activation" % "1.1.1"

// Play Messages in JS
libraryDependencies += "org.julienrf" %% "play-jsmessages" % "1.6.1"

// PDFBOX - tool for generating pdf
libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "1.8.10"

// HTML Parser
libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"

// HTML to PDF library
libraryDependencies += "org.xhtmlrenderer" % "flying-saucer-pdf" % "9.0.7"

// Amazon Web Services SDK
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.10.27"

play.Project.playJavaSettings

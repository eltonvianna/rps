# Summary

This is my implementation of **Rock, Paper and Scissors** puzzle. The solution is based in the client/server architecture model.

## The back end development

To implement the back end I've decided to use only the Java 8 API, except the import of some test libraries, such JUnit, JBehave, Mockito, etc, required to create and perform unit and behavior tests. A very simple socket server, **fully stateless** was implemented as way to provide the web resources and data to the front end layer. The **ResourceRequestHandler** is responsible to handle all http resource requests and the **RestRequestHandler** is responsible to handle all http service requests.

## The front end development

To implement the front end layer I've decided to use Html5, Css3 and JavaScript. The import of some JavaScript libraries, like jQuery and Bootstrap are necessary to offer a responsive UI supporting to different browsers and devices and also as way to implement i18n. The puzzle, for now, has support for **English (en)** and **Portuguese (pt)** languages. The front end development was conducted using Google Chrome and Mozilla Firefox web design emulators. All page elements rendering and service responses handling are processed using native JavaScript at the main.js file.

## Tdd aproach

The TDD approach was applied using JUnit as way to cover the game rules and the JBehave/Mockito to implements the integration and page tests as well. These tests implementation certainly not cover all scenarios or test cases, but are necessary to show some ability to implement automation tests.

## Prerequisites

Installation of **Java 8** and **Maven 3**. Add **JAVA_HOME/jre/bin** and **M2_HOME/bin** to the **PATH** environment variable. Also is required access to the internet as way to access the Maven Central repository.

## Getting Started

After cloned this project, change to **rps** directory and run the command: **mvn clean install**. This command will perform the build, run the tests and packaging. After finished the build,
go to **target** directory (cd target) and run the command: **java -jar rps-0.0.1-SNAPSHOT-jar-with-dependencies.jar** to start the micro server.

### Micro server

 The micro server listens **80** TCP port by default. Running locally you just need to open a browser such Chrome or Firefox with the URL **http://localhost**.
 
### ResourceRequestHandler

To avoid **404** response code any resource request under **/** context will return the content of **/main.html** page. This behavior not applies if the path level is greater than 1, it means that the current request URI contains more than one **/**.

### RestRequestHandler
Only the **GET** http request method is allowed. The rest end points supported are: **/rest/{rock, paper, scissors, computer, configuration}**. Any different endpoint called under the context **/rest** will return the json message: **{"message":"Invalid endpoint: /rest/?"}**. The rest service requests **must** have the http header **Accept: "application/json**, without this header the request will be handled by **ResourceRequestHandler** returning the **404** http response code.

## Configuration

All game configuration can be found at **application.properties** file:
````
# Micro server port (optional)
#microserver.port=80

# Micro server max threads (optional)
#microserver.max.threads=100

# Request handlers (required): *comma separated
request.handlers=com.esv.rps.net.ResourceRequestHandler,com.esv.rps.net.RestRequestHandler

# Default page name (optional): *file name only, without /.
#default.page=main.html

# Resources directory name (optional): *directory name only, without /. **only / is not allowed
#resources.dir=htdocs

# Client http cache max age
cache.maxAge=604800

# Rock Paper Scissors Max Score
rps.max.score=7

# Default hotification timeout
rps.note.timeout=1e3

# Wellcome messages
rps.wellcome.messages=5

# The global logger (optional. Default: com.esv.utile.logging.core.logger.AsyncLogger)
#logging.logger=com.esv.utile.logging.core.logger.AsyncLogger

# The log file name (optional. Default: logging.log)
#logging.fileName=logging.log

# The rollover period. *IN SECONDS* (optional. Default: 3600s = 60 mins). The mininal rollver time is 5s.
#logging.rolloverPeriod=3600

# Enable compression after the log file rollover (optional. Default: true) 
#logging.rolloverGzipEnabled=true

# The timestamp sufix pattern after log rollover (optional. Default: yyyyMMddHHmmss)
#logging.timestampPattern=yyyyMMddHHmmss

# The global logging level (optional. Default: INFO).
logging.level=ALL

# The date pattern (optional. Default: yyyy-MM-dd'T'HH:mm:ss.SSSZ)
#logging.datePattern=yyyy-MM-dd'T'HH:mm:ss.SSSZ

# The log appenders (optional. Default: com.esv.utile.logging.core.appender.RollingLogAppender) See also: com.esv.utile.logging.core.appender.ConsoleLogAppender
#logging.appenders=com.esv.utile.logging.core.appender.RollingLogAppender

# The time waiting to consume the enqueued LogEvents. *IN MILLISECONDS* (optional. Default value: 100). The minimal time wait value is 100ms
#logging.async.logEventTimeWait=100

# The log layout (optional. Default: com.esv.utile.logging.core.StaticLogLayout)
#logging.logLayout=com.esv.utile.logging.core.StaticLogLayout
````

### Tests

Go to **rps** directory and run the command: **mvn test** to perform all tests. The JBehave reports are generated under the directory **rps/target/jbehave/view/index.html**

### Known issues

The current implementation to support i18n is based in a jQuery plugin called **jquery.i18n.properties.js**. The first impression it was very good, seems be very simple to implement, but some problems was found in the minified version and also a bug was fixed when a resource bundle is not found in the properties file. After enabling the browser caching using Cache-Control http header, 
is possible to verify that this plugin keeps doing calls to the server to get the resource bundle.

### Unexpected errors

You can open the log file to see more details about unexpected errors in the server side.

## Author

Elton S. Vianna <elton.vianna@yahoo.co.uk>.

## License

Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.

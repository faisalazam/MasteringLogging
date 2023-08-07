<!-- TOC -->
* [Logging With Slf4j and Log4j2](#logging-with-slf4j-and-log4j2)
  * [Required dependencies](#required-dependencies)
  * [Code for testing logs](#code-for-testing-logs)
  * [Customised Logging](#customised-logging)
  * [Sample `log4j2.properties` file](#sample-log4j2properties-file)
  * [Where should the configuration files such as `log4j2.properties` be located on the classpath?](#where-should-the-configuration-files-such-as-log4j2properties-be-located-on-the-classpath)
  * [Why `log4j2.properties` or config file ignored or not picked up or doesn't work?](#why-log4j2properties-or-config-file-ignored-or-not-picked-up-or-doesnt-work)
      * [For Maven](#for-maven)
      * [For running the application in the IDE](#for-running-the-application-in-the-ide)
<!-- TOC -->

[Go to main](..)
# Logging With Slf4j and Log4j2
In this modules we'll configure [Log4j][log4j2-url] with [SLF4J][slf4j-url] for logging in a simple Java project.

## Required dependencies
As this is a simple Java project and does not use any build tools like [Maven][maven-url] or [Gradle][gradle-url] etc., 
so we'll have to add the required dependencies explicitly. Following are the dependencies required for logging:
* [slf4j-api-xxx.jar][slf4j-api-jar-url]
* [log4j-api-xxx.jar][log4j-api-jar-url]
* [log4j-core-xxx.jar][log4j-core-jar-url]
* [log4j-slf4j2-impl-xxx.jar][log4j-slf4j2-impl-jar-url]

## Code for testing logs
Following is the simple sample code to test different log levels:
```java
package au.com.lucidtech.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExample {
    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);

    public static void main(String[] args) {
        final String parameter = "logging";
        if (logger.isTraceEnabled()) {
            logger.trace("This is trace " + parameter);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("This is debug " + parameter);
        }

        if (logger.isInfoEnabled()) {
            logger.info("This is info " + parameter);
        }

        logger.warn("This is warn " + parameter);
        logger.error("This is error " + parameter);
    }
}
```
Now, if we run this code, we should be able to see the logs with the default settings/configurations.

## Customised Logging
If we want to customize logging in order to be able to:
* log to console
* log to file
* log to multiple files/console or both
* rolling the logs
* enable/disable different log levels
* etc.

then we'll need to add the [log4j2.properties](src/main/resources/log4j2.properties) file somewhere on the classpath.

## Sample `log4j2.properties` file
The following [log4j2.properties](src/main/resources/log4j2.properties) can be placed in the `src/main/resources` folder:
```properties
status = warn

appender.console.type = Console
appender.console.name = LogToConsole
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36}:%L - %msg%n

# Log to console
logger.app.name = au.com.lucidtech
logger.app.level = debug
logger.app.additivity = false
logger.app.appenderRef.console.ref = LogToConsole

rootLogger.level = info
rootLogger.appenderRef.stdout.ref = LogToConsole
```

## Where should the configuration files such as `log4j2.properties` be located on the classpath?
Configuration files such as `log4j2.properties` can be located directly under any folder
declared in the class path. For example, if the class path reads `c:/java/jdk15/lib/rt.jar;c:/mylibs/`, then the
`log4j2.properties` file should be located directly under `c:/mylibs/`, that is as `c:/mylibs/log4j2.properties`.
Placing it under a sub-folder of `c:/mylibs/`, say, `c:/mylibs/other/`, will not work.

For web-applications, configuration files can be placed directly under `WEB-INF/classes/`.
So you need to put `log4j2.properties` in the classpath.


## Why `log4j2.properties` or config file ignored or not picked up or doesn't work?
We need to ensure that the config file is on the class path. We might have created the file in the `src/main/resources`
folder and might assume that, that's it, it should work now. But unfortunately that's not enough as we need to ensure
that it gets copied over to the `target` folder (or wherever you're compiling your project files). 
#### For Maven
So, if we are using some build tool like [Maven][maven-url] then we can use the maven's `resource` plugin to copy them over.
#### For running the application in the IDE
When running the application in IDEs, ensure that the `resources` folder is marked as resources in project setting,
as that'll ensure that the `log4j2.properties` inside resources folder is copied over to the target folder 
(and that's where the config file will be searched for configuration when the application is started).
![mark-resources.png](../assets/images/mark-resources.png)


[Go to main](..)


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[slf4j-url]:https://www.slf4j.org/
[maven-url]:https://maven.apache.org/
[gradle-url]:https://gradle.org/
[log4j2-url]:https://logging.apache.org/log4j/2.x/
[slf4j-api-jar-url]:https://mvnrepository.com/artifact/org.slf4j/slf4j-api
[log4j-api-jar-url]:https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api
[log4j-core-jar-url]:https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
[log4j-slf4j2-impl-jar-url]:https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j2-impl

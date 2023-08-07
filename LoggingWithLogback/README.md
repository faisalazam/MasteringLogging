<!-- TOC -->
* [Logging With Slf4j and Logback](#logging-with-slf4j-and-logback)
  * [Required dependencies](#required-dependencies)
  * [Code for testing logs](#code-for-testing-logs)
  * [Customised Logging](#customised-logging)
  * [Sample `logback.xml` file](#sample-logbackxml-file)
  * [Where should the configuration files such as `logback.groovy`, `logback-test.xml` or `logback.xml` be located on the classpath?](#where-should-the-configuration-files-such-as-logbackgroovy-logback-testxml-or-logbackxml-be-located-on-the-classpath)
  * [Where does the logback looks for the `logback.xml` file?](#where-does-the-logback-looks-for-the-logbackxml-file)
      * [From logback documentation:](#from-logback-documentation)
  * [Why `logback.xml` or config file ignored or not picked up or doesn't work?](#why-logbackxml-or-config-file-ignored-or-not-picked-up-or-doesnt-work)
      * [For Maven](#for-maven)
      * [For running the application in the IDE](#for-running-the-application-in-the-ide)
  * [Setting the path of the config file](#setting-the-path-of-the-config-file)
      * [Setting config through command line argument](#setting-config-through-command-line-argument)
      * [Setting config through code](#setting-config-through-code)
  * [To see how the configuration is done during startup](#to-see-how-the-configuration-is-done-during-startup)
  * [To configure from xml file explicitly](#to-configure-from-xml-file-explicitly)
  * [How to stop logback status INFO at the start of every log?](#how-to-stop-logback-status-info-at-the-start-of-every-log)
<!-- TOC -->

[Go to main](..)
# Logging With Slf4j and Logback
In this modules we'll configure [LOGBACK][logback-url] with [SLF4J][slf4j-url] for logging in a simple Java project.

## Required dependencies
As this is a simple Java project and does not use any build tools like [Maven][maven-url] or [Gradle][gradle-url] etc., 
so we'll have to add the required dependencies explicitly. Following are the dependencies required for logging:
* [slf4j-api-xxx.jar][slf4j-api-jar-url]
* [logback-core-xxx.jar][logback-core-jar-url]
* [logback-classic-xxx.jar][logback-classic-jar-url]

## Code for testing logs
Following is the simple sample code to test different log levels:
```java
package au.com.lucidtech.logback;

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

then we'll need to add the `logback.xml` file somewhere on the classpath.

## Sample `logback.xml` file
The following `logback.xml` can be placed in the `src/main/resources` folder:
```xml
<configuration>
    <variable name="LOGS_DIR" value="out/logs"/>

    <!-- Stop output INFO at start -->
    <!-- As it won't show any start up logs, so consider commenting it out in case things are not working as expected
    in order to see if there are any issues during start up -->
    <statusListener class="ch.qos.logback.core.status.NopStatusListener"/>

    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>${LOGS_DIR}/application.log</file>

        <encoder>
            <pattern>%date %level [%thread] %logger{10} [%file:%line] -%kvp- %msg%n</pattern>
        </encoder>
    </appender>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36}:%L -%kvp- %msg%n</pattern>
        </encoder>
    </appender>
    <appender name="ColorAppender" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %green([%thread]) %highlight(%-5level) %logger{50}:%L - %msg%n
            </pattern>
        </encoder>
    </appender>

    <root level="DEBUG">
        <appender-ref ref="FILE"/>
        <appender-ref ref="ColorAppender"/>
    </root>
</configuration>
```

## Where should the configuration files such as `logback.groovy`, `logback-test.xml` or `logback.xml` be located on the classpath?
Configuration files such as `logback.groovy`, `logback-test.xml` or `logback.xml` can be located directly under any folder
declared in the class path. For example, if the class path reads `c:/java/jdk15/lib/rt.jar;c:/mylibs/`, then the
`logback.xml` file should be located directly under `c:/mylibs/`, that is as `c:/mylibs/logback.xml`.
Placing it under a sub-folder of `c:/mylibs/`, say, `c:/mylibs/other/`, will not work.

For web-applications, configuration files can be placed directly under `WEB-INF/classes/`.
So you need to put `logback.xml` in the classpath. On one project, faced a similar problem although `logback.xml` was
in the right place. Renaming it to `logback-test.xml` helped.

## Where does the logback looks for the `logback.xml` file?
#### From logback documentation:

1. Logback tries to find a file called `logback.groovy` in the classpath.
2. If no such file is found, logback tries to find a file called `logback-test.xml` in the classpath.
3. If no such file is found, it checks for the file `logback.xml` in the classpath..
4. If neither file is found, logback configures itself automatically using the BasicConfigurator which will cause 
logging output to be directed to the console.

## Why `logback.xml` or config file ignored or not picked up or doesn't work?
We need to ensure that the config file is on the class path. We might have created the file in the `src/main/resources`
folder and might assume that, that's it, it should work now. But unfortunately that's not enough as we need to ensure
that it gets copied over to the `target` folder (or wherever you're compiling your project files). 
#### For Maven
So, if we are using some build tool like [Maven][maven-url] then we can use the maven's `resource` plugin to copy them over.
#### For running the application in the IDE
When running the application in IDEs, ensure that the `resources` folder is marked as resources in project setting,
as that'll ensure that the `logback.xml` inside resources folder is copied over to the target folder (and that's where
the config file will be searched for configuration when the application is started).
![mark-resources.png](../assets/images/mark-resources.png)

## Setting the path of the config file
If the file is located at the right location, then there is no need to setting the path explicitly,
but if for some reason, you need to set the path explicitly, then here it is how to do it:

#### Setting config through command line argument
```java -Dlogback.configurationFile=/path/to/config.xml chapters.configuration.MyApp1```

#### Setting config through code
```System.setProperty("logback.configurationFile", "/path/to/config.xml");```

Or:

```System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "/path/to/config.xml");```

Modifying environment variables might not always be feasible. To do it properly see [logback manual][logback-manual-url].


## To see how the configuration is done during startup
```
LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
StatusPrinter.print(loggerContext);
```

## To configure from xml file explicitly
```java
class CustomLogbackConfigurer {
    static void configureLogback() {
        // assume SLF4J is bound to logback in the current environment
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            // Call context.reset() to clear any previous configuration, e.g. default
            // configuration. For multi-step configuration, omit calling context.reset().
            context.reset();
            configurator.doConfigure("src/main/resources/logback.xml");
        } catch (JoranException je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }
}
```

## How to stop logback status INFO at the start of every log?

The logbook will output its own status (INFO or WARN) at the start of the program, it’s really annoying!

```
15:34:46,181 |-INFO in ch.qos.logback.classic.LoggerContext[default] - Could NOT find resource [logback.groovy]
15:34:46,181 |-INFO in ch.qos.logback.classic.LoggerContext[default] - Could NOT find resource [logback-test.xml]
15:34:46,181 |-INFO in ch.qos.logback.classic.LoggerContext[default] - Found resource [logback.xml] at [file:/E:/spring-boot-simple/target/classes/logback.xml]
15:34:46,247 |-INFO in ch.qos.logback.classic.joran.action.ConfigurationAction - debug attribute not set
15:34:46,260 |-INFO in ch.qos.logback.core.joran.action.AppenderAction - About to instantiate appender of type [ch.qos.logback.core.ConsoleAppender]
15:34:46,267 |-INFO in ch.qos.logback.core.joran.action.AppenderAction - Naming appender as [STDOUT]
15:34:46,319 |-WARN in ch.qos.logback.core.ConsoleAppender[STDOUT] - This appender no longer admits a layout as a sub-component, set an encoder instead.
15:34:46,319 |-WARN in ch.qos.logback.core.ConsoleAppender[STDOUT] - To ensure compatibility, wrapping your layout in LayoutWrappingEncoder.
15:34:46,319 |-WARN in ch.qos.logback.core.ConsoleAppender[STDOUT] - See also http://logback.qos.ch/codes.html#layoutInsteadOfEncoder for details
15:34:46,320 |-INFO in ch.qos.logback.classic.joran.action.LoggerAction - Setting level of logger [org.springframework.web] to ERROR
15:34:46,320 |-INFO in ch.qos.logback.classic.joran.action.LoggerAction - Setting additivity of logger [org.springframework.web] to false
15:34:46,320 |-INFO in ch.qos.logback.core.joran.action.AppenderRefAction - Attaching appender named [STDOUT] to Logger[org.springframework.web]
15:34:46,320 |-INFO in ch.qos.logback.classic.joran.action.LoggerAction - Setting level of logger [com.mkyong] to ERROR
15:34:46,320 |-INFO in ch.qos.logback.classic.joran.action.LoggerAction - Setting additivity of logger [com.mkyong] to false
15:34:46,320 |-INFO in ch.qos.logback.core.joran.action.AppenderRefAction - Attaching appender named [STDOUT] to Logger[com.mkyong]
15:34:46,320 |-INFO in ch.qos.logback.classic.joran.action.RootLoggerAction - Setting level of ROOT logger to ERROR
15:34:46,321 |-INFO in ch.qos.logback.core.joran.action.AppenderRefAction - Attaching appender named [STDOUT] to Logger[ROOT]
15:34:46,321 |-INFO in ch.qos.logback.classic.joran.action.ConfigurationAction - End of configuration.
15:34:46,322 |-INFO in ch.qos.logback.classic.joran.JoranConfigurator@7bb11784 - Registering current configuration as safe fallback point
```

To fix it, add a `NopStatusListener` like this:

```
<!-- Stop output INFO at start -->
<!-- As it won't show any start up logs, so consider commenting it out in case things are not working as expected
in order to see if there are any issues during start up -->
<statusListener class="ch.qos.logback.core.status.NopStatusListener" />
```

[Go to main](..)


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[slf4j-url]:https://www.slf4j.org/
[logback-url]:https://logback.qos.ch/
[maven-url]:https://maven.apache.org/
[gradle-url]:https://gradle.org/
[slf4j-api-jar-url]:https://mvnrepository.com/artifact/org.slf4j/slf4j-api
[logback-core-jar-url]:https://mvnrepository.com/artifact/ch.qos.logback/logback-core
[logback-classic-jar-url]:https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
[logback-manual-url]:http://logback.qos.ch/manual/configuration.html#joranDirectly

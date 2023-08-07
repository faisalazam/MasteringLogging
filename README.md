
A tutorial like project to guide how to configure different logging frameworks for Java applications.

* [Logging with SLF4J and LOGBACK](LoggingWithLogback): Implementation of the SLF4J API for Logback, a reliable, generic, fast and flexible logging framework.
* [Logging with SLF4J and Slf4j-Simple](LoggingWithSlf4jSimple): Binding/provider for SLF4J, which outputs all events to System.err. Only messages of level INFO and higher are printed.
* [Logging with SLF4J and Log4j](LoggingWithLog4j): API for Apache Log4J, a highly configurable logging tool that focuses on performance and low garbage generation. It has a plugin architecture that makes it extensible and supports asynchronous logging based on LMAX Disruptor.


## Troubleshooting
[SLF4J warning or error messages and their meanings][slf4j-warning-error-url]
#### No SLF4J providers were found.
This warning, i.e. not an error, message is reported when no SLF4J providers could be found on the class path.
Placing one (and only one) of the many available providers such as slf4j-nop.jar slf4j-simple.jar, slf4j-reload4j.jar, 
slf4j-jdk14.jar or logback-classic.jar on the class path should solve the problem.

In the absence of a provider, SLF4J will default to a no-operation (NOP) logger provider.

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[slf4j-warning-error-url]:https://www.slf4j.org/codes.html#ignoredBindings
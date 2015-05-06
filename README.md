# freeportfinder

[![Build Status](https://travis-ci.org/alexpanov/freeportfinder.svg?branch=master)](https://travis-ci.org/alexpanov/freeportfinder)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.alexpanov/free-port-finder/badge.png)](https://maven-badges.herokuapp.com/maven-central/me.alexpanov/free-port-finder/)

A micro Java library that does one thing and one thing only: finds a free local port (mainly) for testing purposes.

Finding a free local port is as easy as:
```
int port = FreePortFinder.findFreeLocalPort();
```

Which can then be used to set up local Jetty without hitting
```
java.net.BindException: Address already in use
```

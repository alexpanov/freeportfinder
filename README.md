# freeportfinder

A micro Java library that does one thing and one thing only: finds a free local port (mainly) for testing purposes.

Finding a free local port is as easy as:
```
int port = FreePortFinder.findFreeLocalPort();
```

Which can then be used to set up local Jetty without hitting
```
java.net.BindException: Address already in use
```

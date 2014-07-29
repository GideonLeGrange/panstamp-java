panstamp-java
=============

A Java library for working with panStamp Arduino devices

:warning: This library is under active development and not currently fit for use.

Examples
========

#### Opening the modem connecting to the panStamp network
```java
Gateway gw = Gateway.open("/dev/tty.usbserial-A800HNMV", 38400);
```
This connects to the modem connected to the USB serial port named, at 38400 bps.

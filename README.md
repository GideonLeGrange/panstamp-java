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

Now that the gateway is open, there are three ways of getting hold of panStamp device objects:
* Get a device based on it's network address
* Get the collection of all known devices
* Listen for detected devices

#### Get a specific device 

Before retrieving a device, you can heck if it is known: 
```java
  if (gw.hasDevice(100)) {
    // do something if device 100 is known 
  }
```

You can retrieve the device object:
```java 
  PanStamp dev = gw.getDevice(100);
```

#### Retrieve an endpoint for a device

Once you have a panStamp device object, you can get an endpoint for it using the endpoint name from the XML file:

```java
  Endpoint ep0 = (Endpoint<Double>) ps.getEndpoint("Temperature");
```

If the device doesn't exist however, the gateway will throw a NodeNotFoundException. So the best way to use getDevice() is usually to first call hasDevice().

#### Get a list of known devices 

Retrieving the list of all known panStamp devices is simple.

```java
  List<PanStamp> all = gw.getDevices();
```

#### Listen for devices 

We can listen for new devices. An event will be fired if a new panStamp is detected on the network. 

```java
  gw.addListener(new GatewayListener() {

            @Override
            public void deviceDetected(PanStamp ps) {
              System.out.println("Found new panStamp with address " + ps.getAddress());
            }
            
   });

package me.legrange.panstamp.def;

import java.io.InputStream;

/**
 * A device library that uses the Java class loader to load XML definitions.
 * @author gideon
 */
public final class ClassLoaderLibrary extends DeviceLibrary  {

 
    @Override
      public InputStream getStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(base + "/" + path);
    }
    
      private final String base = "devices";
}

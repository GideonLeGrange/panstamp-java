package me.legrange.panstamp.device;

import java.io.InputStream;

/**
 *
 * @author gideon
 */
public class ClassLoaderLibrary extends DeviceLibrary implements StreamSource {

    /**
     *
     * @return
     */
    @Override
    protected StreamSource getSource() {
        return this;
    }
    
    @Override
      public InputStream getStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(base + "/" + path);
    }
    
      private final String base = "devices";
}

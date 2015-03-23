package me.legrange.panstamp.xml;

import java.io.InputStream;

/**
 * A device library that uses the Java class loader to load XML definitions.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class ClassLoaderLibrary extends XmlDeviceLibrary {

    @Override
    InputStream getStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(base + "/" + path);
    }

    private final String base = "devices";
}

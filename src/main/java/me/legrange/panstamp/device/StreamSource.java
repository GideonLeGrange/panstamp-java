package me.legrange.panstamp.device;

import java.io.InputStream;

/**
 * Find streams based on a file name
 * @author gideon
 */
interface StreamSource {
    
    public InputStream getStream(String path);
    
}

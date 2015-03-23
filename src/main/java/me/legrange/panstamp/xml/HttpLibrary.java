/*
 * Copyright 2015 gideon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.legrange.panstamp.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.definition.DefinitionException;

/**
 * An implementation of XMLDeviceLibrary that loads the XML definitions from a web server.
 * @author GideonLeGrange 
 */
public class HttpLibrary extends XMLDeviceLibrary {
    
    public HttpLibrary(URL url) {
        siteUrl = url;
    }

    @Override
    InputStream getStream(String path) throws XMLLoadException {
        try {
            return new URL(siteUrl, path).openStream();
        } catch (MalformedURLException ex) {
            throw new XMLLoadException(ex.getMessage(),ex);
        } catch (IOException ex) {
            throw new XMLLoadException(ex.getMessage(),ex);
        }
    }
    
    private final URL siteUrl;
    private final static Logger log = Logger.getLogger(FileLibrary.class.getName());
}

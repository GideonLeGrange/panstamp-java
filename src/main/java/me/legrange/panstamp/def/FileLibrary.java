package me.legrange.panstamp.def;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 *
 * @author gideon
 */
public class FileLibrary extends DeviceLibrary {

    public FileLibrary(File dir) {
        this.dir = dir;
    }

    @Override
    public InputStream getStream(String path) {
        try {
            return new FileInputStream(dir.getAbsolutePath() + "/" + path);
        } catch (FileNotFoundException ex) {
            log.warning(String.format("File '%s' for does not exist under '%s'.", path, dir.getAbsolutePath()));
            return null;
        }
    }

    private final File dir;
    private final static Logger log = Logger.getLogger(FileLibrary.class.getName());

}

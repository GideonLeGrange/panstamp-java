package me.legrange.panstamp.event;

import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.Register;

/**
 * An abstract implementation for a PanStampListener that developers can extend if 
 * they only what to implement one method in the PanStampListener interface. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public abstract class AbstractPanStampListener implements PanStampListener {

    @Override
    public void productCodeChange(PanStamp dev, int manufacturerId, int productId) {
    }

    @Override
    public void syncStateChange(PanStamp dev, int syncState) {
    }

    @Override
    public void registerDetected(PanStamp dev, Register reg) {
    }

    @Override
    public void syncRequired(PanStamp dev) {
    }
    
}

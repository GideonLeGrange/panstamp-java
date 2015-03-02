package me.legrange.panstamp.core;

import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.Register;

/**
 *
 * @author gideon
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

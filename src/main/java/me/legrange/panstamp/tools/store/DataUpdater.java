package me.legrange.panstamp.tools.store;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayListener;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampEvent;
import me.legrange.panstamp.PanStampListener;

/**
 * Updates panStamp data to a data store
 *
 * @author gideon
 */
public class DataUpdater {

    public DataUpdater(Store store) {
        this.store = store;
    }

    public void addGateway(Gateway gw) {
        gw.addListener(new GatewayListener() {

            @Override
            public void gatewayUpdated(GatewayEvent ev) {
                switch (ev.getType()) {
                    case DEVICE_DETECTED:
                        final PanStamp ps = ev.getDevice();
                        ps.addListener(new PanStampListener() {

                            @Override
                            public void deviceUpdated(PanStampEvent ev) {
                                switch (ev.getType()) {
                                    case PRODUCT_CODE_UPDATE: {
                                        try {
                                            store.storePanStamp(ps);
                                        } catch (DataStoreException ex) {
                                            Logger.getLogger(DataUpdater.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    break;

                                }
                            }
                        });
                         {
                            try {
                                store.storePanStamp(ps);
                            } catch (DataStoreException ex) {
                                Logger.getLogger(DataUpdater.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                }
            }
        });
    }

    private final Store store;

}

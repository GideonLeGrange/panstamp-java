package me.legrange.panstamp.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampEvent;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.impl.StandardEndpoint;
import me.legrange.panstamp.impl.StandardRegister;

/**
 *
 * @author gideon
 */
public class OnWake implements PanStampListener {

    public OnWake(PanStamp ps) throws GatewayException {
        this.ps = ps;
        init();
    }

    public void queue(Update update) {
        updates.offer(update);
    }

    @Override
    public void deviceUpdated(PanStampEvent ev) {
        switch (ev.getType()) {
            case SYNC_STATE_CHANGE: {
                try {
                    if (!updates.isEmpty()) {
                        switch (ep.getValue()) {
                            case 1:
                            case 3:
                                sendUpdates();
                        }
                    }
                } catch (GatewayException ex) {
                    Logger.getLogger(OnWake.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void sendUpdates() throws GatewayException {
        while (!updates.isEmpty()) {
            Update update = updates.remove();
            update.getEndpoint().setValue(update.getValue());
            System.out.println("Sent update");
        }
    }

    private void init() throws GatewayException {
        ps.addListener(this);
        ep = ps.getRegister(StandardRegister.SYSTEM_STATE.getId()).getEndpoint(StandardEndpoint.SYSTEM_STATE.getName());
    }

    private final PanStamp ps;
    private Endpoint<Integer> ep;
    private final Queue<Update> updates = new LinkedList<>();

}

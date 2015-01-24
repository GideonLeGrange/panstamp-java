
package me.legrange.panstamp.store;

/**
 *
 * @author gideon
 */
public interface DataStore {
    
    void save(int addr, PanStampState state) throws DataStoreException;
    
    PanStampState load(int addr) throws DataStoreException;
    
}

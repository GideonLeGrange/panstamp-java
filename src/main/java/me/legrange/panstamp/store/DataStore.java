
package me.legrange.panstamp.store;

import java.util.List;

/**
 * Application developers can implement the DataStore interface to provide the 
 * library with a custom method of loading and storing panStamp state between sessions. 
 * 
 * @author gideon
 */
public interface DataStore {

    /** Get the list of device addresses for which we have saved state. 
     * 
     * @return The list of addresses. 
     */
    List<Integer> getAddresses(int networkId) throws DataStoreException;
    
    /** Save the state supplied for the given address. 
     * 
     * @param addr Address of the panStamp to which this state applies
     * @param state The state
     * @throws DataStoreException Thrown if there is a problem storing the state.
     */ 
    void save(RegisterState state) throws DataStoreException;
    
    /** Load the state stored for the given address. 
     * 
     * @param addr The address for which to load the state.
     * @return The state found. If nothing is found, a valid entry with empty states for all standard registers must be returned.     * @throws DataStoreException 
     * @throws me.legrange.panstamp.store.DataStoreException Thrown if there is a problem loading the state.
     */
    RegisterState load(Integer networkId, Integer addr) throws DataStoreException;
    
}

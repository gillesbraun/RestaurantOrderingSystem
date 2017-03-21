package lu.btsi.bragi.ros.client.settings;

/**
 * Created by gillesbraun on 21/03/2017.
 */
public class ConnectionSettings {
    private String hostAddress = "127.0.0.1";
    private boolean autoDiscovery = true;
    private boolean firstConnection = true;

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public boolean isAutoDiscovery() {
        return autoDiscovery;
    }

    public void setAutoDiscovery(boolean autoDiscovery) {
        this.autoDiscovery = autoDiscovery;
    }

    public boolean isFirstConnection() {
        return firstConnection;
    }

    public void setFirstConnection(boolean firstConnection) {
        this.firstConnection = firstConnection;
    }
}

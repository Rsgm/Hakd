package hakd.networks.devices;

import java.util.Map;

public class Dns extends Device {
    /**
     * Contains all IPs and their registered addresses, if they have one. Should be a reference to the internet's hashmap.
     */
    private Map<String, short[]> addressIpHashMap;

    public Dns() {
        super();
    }

    int getRandomNumber() {
        return 4; // chosen by fair dice roll
        // guaranteed to be random
    }

    public Map<String, short[]> getAddressIpHashMap() {
        return addressIpHashMap;
    }

    public void setAddressIpHashMap(Map<String, short[]> addressIpHashMap) {
        this.addressIpHashMap = addressIpHashMap;
    }
}

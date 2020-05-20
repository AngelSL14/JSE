package us.gonet.nabhi.jse.core.memory.atm;


import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;

import java.util.LinkedHashMap;
import java.util.Map;

public class ATMWrapper {

    private Map < String, ATD > atds = new LinkedHashMap <>();
    private Map < String, ATD > atdsForIP = new LinkedHashMap <>();

    ATMWrapper( Map < String, ATD > atds, Map < String, ATD > atdsForIP ) {
        this.atds = atds;
        this.atdsForIP = atdsForIP;
    }

    public Map < String, ATD > getAtds() {
        return atds;
    }

    public void setAtds( Map < String, ATD > atds ) {
        this.atds = atds;
    }

    public Map < String, ATD > getAtdsForIP() {
        return atdsForIP;
    }

    public void setAtdsForIP( Map < String, ATD > atdsForIP ) {
        this.atdsForIP = atdsForIP;
    }
}

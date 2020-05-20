package us.gonet.nabhi.jse.core.memory.style;


import us.gonet.nabhi.misc.model.jdbc.jdb.BankStyle;

import java.util.LinkedHashMap;
import java.util.Map;

public class BankWrapper {

    private Map < String, BankStyle > banks = new LinkedHashMap <>();

    BankWrapper( Map < String, BankStyle > banks ) {
        this.banks = banks;
    }

    public Map < String, BankStyle > getBanks() {
        return banks;
    }

    public void setBanks( Map < String, BankStyle > banks ) {
        this.banks = banks;
    }
}

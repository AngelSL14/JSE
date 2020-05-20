package us.gonet.nabhi.jse.core.memory.idf;


import us.gonet.nabhi.misc.model.jdbc.jdb.IDF;

import java.util.LinkedHashMap;
import java.util.Map;

public class IDFWrapper {

    private Map < String, IDF > idfs = new LinkedHashMap <>();

    public IDFWrapper( Map < String, IDF > idfs ) {
        this.idfs = idfs;
    }

    public Map < String, IDF > getIdfs() {
        return idfs;
    }

    public void setIdfs( Map < String, IDF > idfs ) {
        this.idfs = idfs;
    }
}

package us.gonet.nabhi.jse.core.memory.bin;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class BinTable {

    private List < PanLenWrapper > panList = new ArrayList <>();
    private boolean empty = false;

    BinTable() {
        panList.add( new PanLenWrapper( 10 ) );
        panList.add( new PanLenWrapper( 11 ) );
        panList.add( new PanLenWrapper( 12 ) );
        panList.add( new PanLenWrapper( 13 ) );
        panList.add( new PanLenWrapper( 14 ) );
        panList.add( new PanLenWrapper( 15 ) );
        panList.add( new PanLenWrapper( 16 ) );
        panList.add( new PanLenWrapper( 17 ) );
        panList.add( new PanLenWrapper( 18 ) );
        panList.add( new PanLenWrapper( 19 ) );
    }

    public List < PanLenWrapper > getPanList() {
        return panList;
    }

    public void setPanList( List < PanLenWrapper > panList ) {
        this.panList = panList;
    }

    PanLenWrapper getForPanLen( int len ) {
        for ( PanLenWrapper p : panList ) {
            if ( p.getPanLen() == len ) {
                return p;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty( boolean empty ) {
        this.empty = empty;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson( this );
    }
}

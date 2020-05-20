package us.gonet.nabhi.jse.core.memory.bin;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class PanLenWrapper {

    private int panLen;
    private List < BinLenWrapper > binList = new ArrayList <>();

    PanLenWrapper( int panLen ) {
        this.panLen = panLen;
        binList.add( new BinLenWrapper( 1 ) );
        binList.add( new BinLenWrapper( 2 ) );
        binList.add( new BinLenWrapper( 3 ) );
        binList.add( new BinLenWrapper( 4 ) );
        binList.add( new BinLenWrapper( 5 ) );
        binList.add( new BinLenWrapper( 6 ) );
        binList.add( new BinLenWrapper( 7 ) );
        binList.add( new BinLenWrapper( 8 ) );
        binList.add( new BinLenWrapper( 9 ) );
        binList.add( new BinLenWrapper( 10 ) );
        binList.add( new BinLenWrapper( 11 ) );
    }

    int getPanLen() {
        return panLen;
    }

    public void setPanLen( int panLen ) {
        this.panLen = panLen;
    }

    public List < BinLenWrapper > getBinList() {
        return binList;
    }

    public void setBinList( List < BinLenWrapper > binList ) {
        this.binList = binList;
    }

    BinLenWrapper getForBinLen( int len ) {
        for ( BinLenWrapper b : binList ) {
            if ( b.getBinLen() == len ) {
                return b;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson( this );
    }
}

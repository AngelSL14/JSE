package us.gonet.nabhi.jse.core.memory.bin;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class BinLenWrapper {

    private int binLen;
    private List < BinWrapper > bines = new ArrayList <>();

    BinLenWrapper( int binLen ) {
        this.binLen = binLen;
    }

    int getBinLen() {
        return binLen;
    }

    public void setBinLen( int binLen ) {
        this.binLen = binLen;
    }

    List < BinWrapper > getBines() {
        return bines;
    }

    public void setBines( List < BinWrapper > bines ) {
        this.bines = bines;
    }

    void addBines( String bin, String fiid ) {
        bines.add( new BinWrapper( bin, fiid ) );
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson( this );
    }
}

package us.gonet.nabhi.jse.core.memory.bin;

import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.model.jdbc.jdb.BIN;

import java.util.Collections;
import java.util.List;

@Component
public class BinarySearch {

    private BinTable table;

    public void buildTable( List < BIN > bins ) {
        table = new BinTable();
        for ( BIN b : bins ) {
            PanLenWrapper panLen = table.getForPanLen( b.getBinId().getPanLen() );
            BinLenWrapper binLen = panLen.getForBinLen( b.getBinId().getBinLen() );
            binLen.addBines( b.getBinId().getBin(), b.getIdf().getFiid() );
        }
    }

    public String search( int panLen, int binLen, String target ) {
        if ( table.isEmpty() ) {
            return "PROS";
        }
        if ( target.length() > 11 ) {
            target = target.substring( 0, 11 );
        }
        List < BinWrapper > bines;
        PanLenWrapper pWrapper = table.getForPanLen( panLen );
        String tmp = target;
        int index;
        for ( int i = binLen; i > 0; i-- ) {
            BinLenWrapper bWrapper = pWrapper.getForBinLen( i );
            bines = bWrapper.getBines();
            Collections.sort( bines );
            index = Collections.binarySearch( bines, new BinWrapper( tmp ) );
            if ( index < 0 ) {
                tmp = tmp.substring( 0, i - 1 );
            } else {
                return bines.get( index ).getFiid();
            }
        }
        return "PROS";
    }

    public BinTable getTable() {
        return table;
    }

    public void setTable( BinTable table ) {
        this.table = table;
    }
}

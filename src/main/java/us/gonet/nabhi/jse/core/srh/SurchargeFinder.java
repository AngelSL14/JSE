package us.gonet.nabhi.jse.core.srh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.jse.core.memory.bin.BinarySearch;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.SurchargeException;
import us.gonet.nabhi.misc.model.jdbc.composite.SurchargeId;
import us.gonet.nabhi.misc.model.jdbc.jdb.Surcharge;
import us.gonet.nabhi.misc.model.srh.RequestSurcharge;

import java.util.ArrayList;
import java.util.List;

@Component
public class SurchargeFinder {

    private BinarySearch binarySearch;
    private ATMSearch atmSearch;

    @Autowired
    public SurchargeFinder( BinarySearch binarySearch, ATMSearch atmSearch ) {
        this.binarySearch = binarySearch;
        this.atmSearch = atmSearch;
    }

    public Surcharge getSurcharge( RequestSurcharge surcharge ) throws SurchargeException, ATMException {
        String pan = surcharge.getTrack().substring( 0, surcharge.getTrack().indexOf( '=' ) );
        String fiid = binarySearch.search( pan.length(), 11, pan );
        List < Surcharge > surcharges = atmSearch.searchByIP( surcharge.getIp() ).getIdf().getSurcharges();
        for ( Surcharge s : surcharges ) {
            SurchargeId id = s.getSurchargeId();
            if ( id.getFiidIssuing().equals( fiid ) && id.getTranCode().equals( surcharge.getTransactionCode() ) ) {
                return s;
            }
        }
        for ( Surcharge s : surcharges ) {
            SurchargeId id = s.getSurchargeId();
            if ( id.getFiidIssuing().equals( "****" ) && id.getTranCode().equals( surcharge.getTransactionCode() ) ) {
                return s;
            }
        }
        List < ErrorWS > errors = new ArrayList <>();
        errors.add( new ErrorWS( "SRH-01", "Surcharge not found" ) );
        throw new SurchargeException( "Surcharge not found", errors );
    }
}

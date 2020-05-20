package us.gonet.nabhi.jse.core.adp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.model.adp.BillsModel;
import us.gonet.nabhi.misc.model.devices.cdm.Cassette;
import us.gonet.nabhi.misc.model.devices.cdm.Dispenser;

import java.util.List;
import java.util.Map;

@Component
public class Dispensed {


    private ATMSearch atmSearch;
    private FourCashUnits fourCashUnits;
    private ValidationCassettes validation;

    @Autowired
    public Dispensed( ATMSearch atmSearch, FourCashUnits fourCashUnits, ValidationCassettes validation ) {
        this.atmSearch = atmSearch;
        this.fourCashUnits = fourCashUnits;
        this.validation = validation;
    }

    public BillsModel dispenseFourUnits( String ip, int amount ) throws ServerException {
        try {
            Dispenser dispenser = atmSearch.searchByIP( ip ).getAtm().getDevices().getTerminalDevices().getDispenser();
            BillsModel bills = new BillsModel();
            bills.setBills( fourCashUnits.getBills( amount, validation.validateCashUnits( dispenser.getCassettes() ), dispenser.getCassettes() ) );
            return bills;
        } catch ( ServerException | ATMException e ) {
            e.getErrors().add( new ErrorWS( "-JXI10", "The selected amount cannot be dispensed: " + amount ) );
            throw new ServerException( "Invalid Amount", e.getErrors() );
        }
    }

    public Cassette getMinimumAmount( String ip ) throws ServerException {
        try {
            Dispenser dispenser = atmSearch.searchByIP( ip ).getAtm().getDevices().getTerminalDevices().getDispenser();
            List < Map < String, Object > > atm = validation.validateCashUnits( dispenser.getCassettes() );
            return validation.getLowCassette( atm, dispenser.getCassettes() );
        } catch ( ServerException | ATMException e ) {
            e.getErrors().add( new ErrorWS( "-JXI11", "The minimum amount cannot be dispensed: " + ip ) );
            throw new ServerException( "Cannot be dispensed", e.getErrors() );
        }
    }

}

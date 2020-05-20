package us.gonet.nabhi.jse.i8583.reversal;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.iso8583.constants.ReversalCodes;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.jse.journal.JournalWriter;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.ISOException;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jse.request.AtmNotificationModel;
import us.gonet.nabhi.misc.model.reversal.ATMReversalModel;

@Component
public class Reversal {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( Reversal.class );

    private ReversalHandler reversalHandler;
    private JournalWriter journalWriter;
    private ATMSearch atmSearch;

    @Autowired
    public Reversal( ReversalHandler reversalHandler, ATMSearch atmSearch, JournalWriter journalWriter ) {
        this.reversalHandler = reversalHandler;
        this.atmSearch = atmSearch;
        this.journalWriter = journalWriter;
    }

    public void makeReversal( AtmNotificationModel model, int amount ) {
        String reverseType = "Reverso Parcial";
        if ( amount == 0 ) {
            reverseType = "Reverso Total";
        }
        try {
            ATD atd = atmSearch.searchByIP( model.getIp() );
            ATMReversalModel atmReversalModel = new ATMReversalModel();
            atmReversalModel.setMessage( atd.getAtm().getLastTrx() );
            atmReversalModel.setDispensedAmount( amount );
            atmReversalModel.setReversalCode( ReversalCodes.SUSPECTED_MALFUNCTION.getValue() );
            reversalHandler.sendMessage( atmReversalModel );
            String response = reverseType + " Exitoso ";
            LOG.info( response );
            journalWriter.writeJournal( atd.getTerminalId(), response );
        } catch ( ISOException | ATMException e ) {
            journalWriter.writeJournal( model.getTermId(), "Hubo un error al realizar el " + reverseType + e.getErrors().toString() );
        }
    }
}

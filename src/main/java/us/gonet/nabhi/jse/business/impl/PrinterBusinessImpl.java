package us.gonet.nabhi.jse.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.IPrinterBusiness;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.jse.journal.JournalWriter;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.response.Ticket;

@Component( "prntrBus" )
public class PrinterBusinessImpl implements IPrinterBusiness {

    private JournalWriter journalWriter;
    private ATMSearch atmSearch;

    @Autowired
    public PrinterBusinessImpl( JournalWriter journalWriter, ATMSearch atmSearch ) {
        this.journalWriter = journalWriter;
        this.atmSearch = atmSearch;
    }

    @Override
    public Ticket printingTicket( Generic generic ) {
        Ticket printingTicket = new Ticket();
        try {
            ATD atd = atmSearch.searchByIP( generic.getIp() );
            printingTicket.setBodyTicket( atd.getAtm().getReceipt() );
            if ( printingTicket.getBodyTicket().startsWith( "ERROR" ) ) {
                printingTicket.setCode( "-500" );
                printingTicket.setBodyTicket( "" );
            } else {
                printingTicket.setCode( "00" );
                journalWriter.writeJournal( generic.getTermId(), printingTicket.getBodyTicket() );
            }
        } catch ( ATMException e ) {
            printingTicket.setCode( "-500" );
            printingTicket.setBodyTicket( "" );
        }
        return printingTicket;
    }
}

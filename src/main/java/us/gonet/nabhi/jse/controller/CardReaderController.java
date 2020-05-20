package us.gonet.nabhi.jse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import us.gonet.nabhi.jse.business.ICardReaderBusiness;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.jse.journal.JournalWriter;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.model.jse.request.AtmInfo;
import us.gonet.nabhi.misc.model.jse.response.CardInfo;

@RestController
@RequestMapping( "crdrdr" )
@CrossOrigin( origins = { "*" }, maxAge = 6000 )
public class CardReaderController {

    private ATMSearch atmSearch;
    private ICardReaderBusiness business;
    private JournalWriter journalWriter;

    @Autowired
    public CardReaderController( ATMSearch atmSearch, ICardReaderBusiness business, JournalWriter journalWriter ) {
        this.atmSearch = atmSearch;
        this.business = business;
        this.journalWriter = journalWriter;
    }

    private static final String[] ALLOWED_FIELDS = new String[]{ "ip", "track", "termId" };

    @InitBinder( "AtmInfo" )
    public void initBinder( WebDataBinder binder ) {
        binder.setAllowedFields( ALLOWED_FIELDS );
    }


    @ResponseBody
    @PostMapping( "/crdincmg" )
    public boolean atmVerification( @RequestBody AtmInfo generic ) {
        try {
            atmSearch.searchByIP( generic.getIp() );
            return true;
        } catch ( ATMException e ) {
            return false;
        }

    }

    @ResponseBody
    @PostMapping( "/crdvldt" )
    public ResponseWrapper < CardInfo > validateCard( @RequestBody AtmInfo atmInfo ) {
        journalWriter.writeJournal( atmInfo.getTermId(), "Tarjeta introducida. Inicia transaccion" );
        return business.validatingCard( atmInfo );
    }

    @ResponseBody
    @PostMapping( "/crdrmvd" )
    public boolean cardRemoved( @RequestBody AtmInfo atmInfo ) {
        business.cardRemoved( atmInfo );
        journalWriter.writeJournal( atmInfo.getTermId(), "Tarjeta retirada. Termina transaccion" );
        return true;
    }
}

package us.gonet.nabhi.jse.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.ICardReaderBusiness;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.jse.core.memory.bin.BinarySearch;
import us.gonet.nabhi.jse.core.memory.idf.IDFSearch;
import us.gonet.nabhi.jse.core.memory.style.BankSearch;
import us.gonet.nabhi.misc.exception.*;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jdbc.jdb.BankStyle;
import us.gonet.nabhi.misc.model.jdbc.jdb.IDF;
import us.gonet.nabhi.misc.model.jdbc.jdb.ScreenGroup;
import us.gonet.nabhi.misc.model.jse.StylesBank;
import us.gonet.nabhi.misc.model.jse.request.AtmInfo;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.model.jse.response.CardInfo;

@Component
public class CardReaderBusinessImpl implements ICardReaderBusiness {

    private static final Logger LOG = LoggerFactory.getLogger( CardReaderBusinessImpl.class );
    private BinarySearch binarySearch;
    private BankSearch bankSearch;
    private IDFSearch idfSearch;
    private ATMSearch atmSearch;

    @Autowired
    public CardReaderBusinessImpl( BinarySearch binarySearch, BankSearch bankSearch, IDFSearch idfSearch, ATMSearch atmSearch ) {
        this.binarySearch = binarySearch;
        this.bankSearch = bankSearch;
        this.idfSearch = idfSearch;
        this.atmSearch = atmSearch;
    }

    @Override
    public ResponseWrapper < Generic > incomingCard( Generic generic ) {
        ResponseWrapper < Generic > response = new ResponseWrapper <>();
        response.setCode( "00" );
        return response;
    }

    @Override
    public ResponseWrapper < CardInfo > validatingCard( AtmInfo atmInfo ) {
        ResponseWrapper < CardInfo > response = new ResponseWrapper <>();
        try {
            ATD atd = atmSearch.searchInDataBase( atmInfo.getTermId() );
            if ( atmInfo.getTrack().length() < 16 || atmInfo.getTrack() == null ) {
                response.setCode( "-203" );
                response.addError( new ErrorWS( "-JXI20", "Invalid Track 2" ) );
                return response;
            } else {
                atd.getAtm().setActiveTrx( true );
                String pan = atmInfo.getTrack().substring( 0, atmInfo.getTrack().indexOf( '=' ) );
                String discretionary = atmInfo.getTrack().substring( atmInfo.getTrack().indexOf( '=' ) + 1 );
                return validateCard( pan, discretionary, atd );
            }
        } catch ( ATMException e ) {
            response.setCode( "-204" );
            response.addError( new ErrorWS( "-JXI21", "Invalid ATM" ) );
            return response;
        }
    }

    @Override
    public void cardRemoved( AtmInfo atmInfo ) {
        try {
            atmSearch.search( atmInfo.getTermId() ).getAtm().setActiveTrx( false );
        } catch ( ATMException e ) {
            LOG.error( "Invalid ATM" );
        }
    }

    private ResponseWrapper < CardInfo > validateCard( String pan, String discretionary, ATD atd ) {
        try {
            String fiidI = binarySearch.search( pan.length(), 11, pan );
            if ( validateAgreement( atd.getIdf().getAgreement(), fiidI ) ) {
                IDF idf = idfSearch.search( fiidI );
                return cardResponse( idf, atd, discretionary );
            } else {
                return cardDefault( atd, discretionary );
            }
        } catch ( IDFException e ) {
            return cardDefault( atd, discretionary );
        }
    }

    private ResponseWrapper < CardInfo > cardResponse( IDF entity, ATD atd, String discretionary ) {
        ResponseWrapper < CardInfo > wrapper = new ResponseWrapper <>();
        try {
            BankStyle bankStyle = bankSearch.search( entity.getName() );
            wrapper.setCode( "200" );
            wrapper.addBody(
                    new CardInfo(
                            bankStyle.getId(),
                            getCardCapabilities( discretionary ),
                            new StylesBank(
                                    bankStyle.getButtons(),
                                    bankStyle.getDashboard(), bankStyle.getBackgroundImage(), "" ) ) );
            return wrapper;
        } catch ( BankException e ) {
            return cardDefault( atd, discretionary );
        }
    }

    private ResponseWrapper < CardInfo > cardDefault( ATD atd, String discretionary ) {
        ResponseWrapper < CardInfo > wrapper = new ResponseWrapper <>();
        wrapper.setCode( "200" );
        ScreenGroup screenGroup = atd.getAtm().getScreen().getScreenGroup();
        CardInfo cardInfo = new CardInfo();
        cardInfo.setBank( atd.getIdf().getName() );
        cardInfo.setMessage( getCardCapabilities( discretionary ) );
        StylesBank stylesBank = new StylesBank();
        stylesBank.setButtons( screenGroup.getButtonsStyle() );
        stylesBank.setDashboard( screenGroup.getBodyStyle() );
        stylesBank.setBackgroundImage( screenGroup.getBackGround() );
        stylesBank.setSections( "" );
        cardInfo.setStyles( stylesBank );
        wrapper.addBody( cardInfo );
        return wrapper;
    }

    private String getCardCapabilities( String discretionary ) {
        String capabilities = discretionary.substring( 4, 5 );
        if ( capabilities.equals( "2" ) || capabilities.equals( "6" ) ) {
            return "CHIP";
        } else {
            return "BAND";
        }
    }

    private boolean validateAgreement( String data, String fiidI ) {
        if ( data == null || data.isEmpty() ) {
            return false;
        }
        String[] fiids = data.split( "," );
        for ( String fiidA : fiids ) {
            if ( fiidA.equals( fiidI ) ) {
                return true;
            }
        }
        return false;
    }

}

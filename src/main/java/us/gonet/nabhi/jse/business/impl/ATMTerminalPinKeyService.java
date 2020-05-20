package us.gonet.nabhi.jse.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.IAtmTerminalPinKey;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jke.TPK;
import us.gonet.nabhi.misc.model.jke.TmkEntity;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.rest.jke.JKERequester;

import java.sql.Timestamp;

@Component
public class ATMTerminalPinKeyService implements IAtmTerminalPinKey {

    private static final String KEY_TMK = "MTVK";

    private JKERequester jkeRequester;
    private ATMSearch atmSearch;

    @Autowired
    public ATMTerminalPinKeyService( JKERequester jkeRequester, ATMSearch atmSearch ) {
        this.jkeRequester = jkeRequester;
        this.atmSearch = atmSearch;
    }

    @Override
    public ResponseWrapper < TmkEntity > tpkRequest( Generic atmIp ) {
        ResponseWrapper < TmkEntity > wrapper = new ResponseWrapper <>();
        ATD entity;
        try {
            entity = atmSearch.searchByIP( atmIp.getIp() );
        } catch ( ATMException e ) {
            try {
                entity = atmSearch.searchInDataBase( atmIp.getTermId() );
            } catch ( ATMException e1 ) {
                wrapper.setCode( "-500" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            TPK tpk = new TPK();
            tpk.setTermType( entity.getDeviceType() );
            tpk.setAtmRemote( entity.getTerminalId() );
            tpk.setAtmLocal( KEY_TMK );
            TmkEntity tmkEntity = jkeRequester.getTMKForSpecificATM( tpk );
            tmkEntity.setError( String.valueOf( new Timestamp( System.currentTimeMillis() ).getTime() / 1000L ) );
            tmkEntity.setSequence( entity.getSequence() );
            wrapper.setCode( "200" );
            wrapper.addBody( tmkEntity );
        } catch ( ServerException e ) {
            wrapper.setCode( "-500" );
            wrapper.addAllError( e.getErrors() );
        }
        return wrapper;
    }
}
package us.gonet.nabhi.jse.core.memory.atm;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.jdb.entity.ATDEntity;
import us.gonet.nabhi.misc.jdb.repository.ATDRepository;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;

import java.util.*;

@Component
public class ATMSearch {

    private ATMWrapper atmWrapper;
    private ATDRepository atdRepository;
    private ModelMapper mapper;
    private long lastUpdate;
    private static final Logger LOG = LoggerFactory.getLogger( ATMSearch.class );
    private static final String INVALID_TERMINAL = "Invalid Terminal";
    private static final String CAUSE = "ATD-01";

    @Autowired
    public ATMSearch( ATDRepository atdRepository, ModelMapper mapper ) {
        this.atdRepository = atdRepository;
        this.mapper = mapper;
    }

    public void buildTable( List < ATD > atds ) {
        Map < String, ATD > atdMap = new LinkedHashMap <>();
        Map < String, ATD > atdIpMap = new LinkedHashMap <>();
        for ( ATD i : atds ) {
            atdMap.put( i.getTerminalId(), i );
            atdIpMap.put( i.getAtm().getIp(), i );
        }
        atmWrapper = new ATMWrapper( atdMap, atdIpMap );
    }

    public void updateATMTable( List < ATD > atds ) {
        for ( ATD a : atds ) {
            ATD a2 = atmWrapper.getAtds().get( a.getTerminalId() );
            if ( a2 != null ) {
                if ( !a2.getAtm().isActiveTrx() ) {
                    atmWrapper.getAtds().replace( a.getTerminalId(), a );
                    atmWrapper.getAtdsForIP().replace( a.getAtm().getIp(), a );
                } else {
                    tranInProgress( a.getTerminalId() );
                }
            } else {
                logAddAtm( a.getTerminalId() );
                atmWrapper.getAtds().put( a.getTerminalId(), a );
                atmWrapper.getAtdsForIP().put( a.getAtm().getIp(), a );
            }
        }
    }

    public ATD searchInDataBase( String terminalId ) throws ATMException {
        Optional < ATDEntity > oa = atdRepository.findById( terminalId );
        if ( oa.isPresent() ) {
            List < ATD > atds = new ArrayList <>();
            ATD atd = mapper.map( oa.get(), ATD.class );
            atds.add( atd );
            updateATMTable( atds );
            return atd;
        } else {
            List < ErrorWS > errors = new ArrayList <>();
            errors.add( new ErrorWS( CAUSE, INVALID_TERMINAL ) );
            throw new ATMException( INVALID_TERMINAL, errors );
        }
    }

    public ATD search( String terminalId ) throws ATMException {
        updateATM( terminalId );
        ATD atd = atmWrapper.getAtds().get( terminalId );
        if ( atd != null ) {
            return atd;
        } else {
            List < ErrorWS > errors = new ArrayList <>();
            errors.add( new ErrorWS( CAUSE, INVALID_TERMINAL ) );
            throw new ATMException( INVALID_TERMINAL, errors );
        }
    }

    public ATD searchByIP( String ip ) throws ATMException {
        ATD atd = atmWrapper.getAtdsForIP().get( ip );
        if ( atd != null ) {
            return atd;
        } else {
            List < ErrorWS > errors = new ArrayList <>();
            errors.add( new ErrorWS( CAUSE, INVALID_TERMINAL ) );
            throw new ATMException( INVALID_TERMINAL, ( errors ) );
        }
    }

    private void updateATM( String terminalId ) {
        long currentTime = new Date().getTime() / 1000;
        if ( currentTime > +( lastUpdate + 84600 ) ) {
            Optional < ATDEntity > oa = atdRepository.findById( terminalId );
            if ( oa.isPresent() ) {
                atmWrapper.getAtds().replace( terminalId, mapper.map( oa.get(), ATD.class ) );
                lastUpdate = currentTime;
            }
        }
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate( long lastUpdate ) {
        this.lastUpdate = lastUpdate;
    }

    private void tranInProgress( String terminal ) {
        if ( LOG.isInfoEnabled() ) {
            LOG.info( String.format( "Transaction in progress for ATM: %s", terminal ) );
        }
    }

    private void logAddAtm( String terminal ) {
        if ( LOG.isInfoEnabled() ) {
            LOG.info( String.format( "ADD new ATM: %s", terminal ) );
        }
    }

    public List < ATD > findAll() {
        return new ArrayList <>( atmWrapper.getAtds().values() );
    }

}

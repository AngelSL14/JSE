package us.gonet.nabhi.jse.i8583.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.iso8583.constants.atm.FromAccount;
import us.gonet.iso8583.constants.atm.TranCodes;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.jse.i8583.misc.ATMRequestBuilder;
import us.gonet.nabhi.jse.i8583.misc.ProcessingTransaction;
import us.gonet.nabhi.jse.i8583.reversal.Reversal;
import us.gonet.nabhi.misc.exception.ATMException;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ISOException;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.jdb.entity.ATDEntity;
import us.gonet.nabhi.misc.jdb.repository.ATDRepository;
import us.gonet.nabhi.misc.model.ATMRequestModel;
import us.gonet.nabhi.misc.model.ATMResponseModel;
import us.gonet.nabhi.misc.model.jdbc.composite.APCId;
import us.gonet.nabhi.misc.model.jdbc.jdb.APC;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jse.request.Generic;
import us.gonet.nabhi.misc.task.ForcedCutOver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TransactionManagement {

    private ProcessingTransaction processingTransaction;
    private ATMRequestBuilder atmRequestBuilder;
    private ATMSearch atmSearch;
    private ForcedCutOver forcedCutOver;
    private ATDRepository atdRepository;
    private ModelMapper mapper;
    private Reversal reversal;

    @Autowired
    public TransactionManagement( ProcessingTransaction processingTransaction, ATMRequestBuilder atmRequestBuilder, ATMSearch atmSearch, ForcedCutOver forcedCutOver, ATDRepository atdRepository, ModelMapper mapper, Reversal reversal ) {
        this.processingTransaction = processingTransaction;
        this.atmRequestBuilder = atmRequestBuilder;
        this.atmSearch = atmSearch;
        this.forcedCutOver = forcedCutOver;
        this.atdRepository = atdRepository;
        this.mapper = mapper;
        this.reversal = reversal;
    }

    public ATMResponseModel performTransaction( Generic e, String code ) throws ServerException {
        TranCodes tranCode = TranCodes.getValue( code );
        try {
            ATD atd = atmSearch.searchByIP( e.getIp() );
            //verifyPostingDate( atd );
            //TODO Ajuste de posting date
            APC apc = findAPC( atd, tranCode, e );
            ATMRequestModel atmRequestModel = atmRequestBuilder.build( e, atd, apc, tranCode.getValue() );
            return processingTransaction.sendTransaction( tranCode, atmRequestModel );
        } catch ( ATMException | ISOException ex ) {
            throw new ServerException( ex.getMessage(), ex.getErrors() );
        }
    }

    private APC findAPC( ATD atd, TranCodes code, Generic e ) {
        for ( APC a : atd.getIdf().getApcs() ) {
            APCId id = a.getApcId();
            if ( id.getTranCode().equals( code.getValue() ) && id.getFormAcct().equals( e.getTipoCuenta() ) && id.getToAcct().equals( FromAccount.NO_ACCOUNT.getValue() ) ) {
                return a;
            }
        }
        return null;
    }

    public void saveAuthorizationInfo( ATMResponseModel atmResponseModel, Generic generic ) throws ServerException {
        try {
            ATD atd = atmSearch.searchByIP( generic.getIp() );
            atd.getAtm().setLastTrx( atmResponseModel.getMessage().getMessage() );
            atd.getAtm().setReceipt( atmResponseModel.getReceipt() );
            atd.setJorunal( Collections.emptySet() );
            ATDEntity entity = atdRepository.save( mapper.map( atd, ATDEntity.class ) );
            atmSearch.updateATMTable( Collections.singletonList( mapper.map( entity, ATD.class ) ) );
            //TODO Make Reversal
        } catch ( ATMException e ) {
            throw new ServerException( "Cannot update the ATM", e.getErrors() );
        }
    }

    private void verifyPostingDate( ATD atd ) throws ATMException {
        if ( forcedCutOver.isValidDate( atd.getPostingDay(), atd.getIdf().getCurrentBusinessDay() ) ) {
            List < ErrorWS > errors = new ArrayList <>();
            errors.add( new ErrorWS( "ATM-04", "Invalid Posting Date for ATM: " + atd.getTerminalId() ) );
            throw new ATMException( "Invalid Posting Date ", errors );
        }

    }

}

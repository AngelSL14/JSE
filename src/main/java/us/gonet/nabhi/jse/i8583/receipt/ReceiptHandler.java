package us.gonet.nabhi.jse.i8583.receipt;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.iso8583.constants.atm.TranCodes;
import us.gonet.nabhi.jse.i8583.receipt.utils.RCPTDummy;
import us.gonet.nabhi.misc.exception.ReceiptException;
import us.gonet.nabhi.misc.iso.IReceipt;
import us.gonet.nabhi.misc.jdb.entity.RCPTEntity;
import us.gonet.nabhi.misc.jdb.entity.composite.RCPTIdentity;
import us.gonet.nabhi.misc.jdb.repository.RCPTRepository;
import us.gonet.nabhi.misc.model.ATMRequestModel;
import us.gonet.nabhi.misc.model.jdbc.jdb.RCPT;
import us.gonet.nabhi.misc.model.receipt.Receipt;
import us.gonet.nabhi.misc.util.ReceiptBuilder;
import us.gonet.serializable.data.ISO;
import us.gonet.utils.DecodeISO8583;

import java.util.Optional;


@Component
public class ReceiptHandler implements IReceipt {

    private ReceiptBuilder receiptBuilder;
    private RCPTRepository rcptRepository;
    private ModelMapper mapper;

    @Autowired
    public ReceiptHandler( ReceiptBuilder receiptBuilder, RCPTRepository rcptRepository, ModelMapper mapper ) {
        this.receiptBuilder = receiptBuilder;
        this.rcptRepository = rcptRepository;
        this.mapper = mapper;
    }

    @Override
    public Receipt createReceipt( ATMRequestModel ar, String message ) throws ReceiptException {
        ISO i = new DecodeISO8583( message ).getIso();
        RCPT rcpt;
        TranCodes tranCode = TranCodes.getValue( i.getDataElements().get( 2 ).getContentField().substring( 0, 2 ) );
        RCPTIdentity rcptId = new RCPTIdentity( ar.getTermFiid(), tranCode.getValue() );
        Optional < RCPTEntity > or = rcptRepository.findById( rcptId );
        if ( or.isPresent() ) {
            rcpt = mapper.map( or.get(), RCPT.class );
        } else {
            rcptId = new RCPTIdentity( "****", tranCode.getValue() );
            or = rcptRepository.findById( rcptId );
            if ( or.isPresent() ) {
                rcpt = mapper.map( or.get(), RCPT.class );
            } else {
                rcpt = RCPTDummy.getScripRcpt( "****" + tranCode.getValue() );
            }
        }
        return receiptBuilder.build( ar, i, rcpt.getHeader(), rcpt.getBody(), rcpt.getTrailer() );
    }
}

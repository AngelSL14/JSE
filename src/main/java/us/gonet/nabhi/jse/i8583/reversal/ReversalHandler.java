package us.gonet.nabhi.jse.i8583.reversal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.iso8583.constants.MessageTypes;
import us.gonet.iso8583.constants.ReversalCodes;
import us.gonet.iso8583.constants.atm.ResponseCodes;
import us.gonet.iso8583.constants.atm.TranCodes;
import us.gonet.iso8583.message.Reversal0420;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ISOException;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.iso.ResponseISOBuilder;
import us.gonet.nabhi.misc.model.ATMResponseModel;
import us.gonet.nabhi.misc.model.reversal.ATMReversalModel;
import us.gonet.nabhi.misc.rest.iso.ISORequester;
import us.gonet.serializable.data.ISO;
import us.gonet.utils.DecodeISO8583;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReversalHandler {

    private MessageTypes response = MessageTypes.FINALCIAL_RESPONSE_MSG;
    private TranCodes withdrawal = TranCodes.WITHDRAWAL;
    private ResponseCodes approved = ResponseCodes.APPROVED;
    private ISORequester isoRequester;
    private ResponseISOBuilder responseISOBuilder;

    @Autowired
    public ReversalHandler( ISORequester isoRequester, ResponseISOBuilder responseISOBuilder ) {
        this.isoRequester = isoRequester;
        this.responseISOBuilder = responseISOBuilder;
    }

    public ATMResponseModel sendMessage( ATMReversalModel arm ) throws ISOException {
        ResponseWrapper < ATMResponseModel > responseWrapper = new ResponseWrapper <>();
        try {
            ISO i = new DecodeISO8583( arm.getMessage() ).getIso();
            if ( !i.getHeader().get( 6 ).getContentField().equals( response.getValue() ) ) {
                List < ErrorWS > errors = new ArrayList <>();
                errors.add( new ErrorWS( "REV-01", "Invalid message type" ) );
                throw new ISOException( "Invalid message type", errors );
            }
            TranCodes tranCode = TranCodes.getValue( i.getDataElements().get( 2 ).getContentField().substring( 0, 2 ) );
            if ( tranCode != withdrawal ) {
                List < ErrorWS > errors = new ArrayList <>();
                errors.add( new ErrorWS( "REV-02", "Invalid tran code, only financial transactions are allowed" ) );
                throw new ISOException( "Invalid tran code", errors );
            }
            if ( !approved.getValue().equals( i.getDataElements().get( 38 ).getContentField() ) ) {
                List < ErrorWS > errors = new ArrayList <>();
                errors.add( new ErrorWS( "REV-03", "Only approved transaction reversals are allowed" ) );
                throw new ISOException( "Only approved transaction", errors );
            }
            ReversalCodes reversalCodes = ReversalCodes.RESERVED_U4;
            for ( ReversalCodes r : ReversalCodes.values() ) {
                if ( r.getValue().equals( arm.getReversalCode() ) ) {
                    reversalCodes = r;
                }
            }
            ISO r0420 = new Reversal0420( i, reversalCodes, arm.getDispensedAmount() ).getIso();
            ISO r0430 = isoRequester.sendMessage( r0420 );
            if ( !r0430.isTimeOut() ) {
                return handlerResponse( r0430 );
            } else {
                responseISOBuilder.responseReceivedToLate();
                List < ErrorWS > errors = new ArrayList <>();
                errors.add( new ErrorWS( "ISO-" + ResponseCodes.ISSUER_INOPERATIVE.getValue(), "Response received to late" ) );
                throw new ISOException( "Timeout", errors );
            }
        } catch ( ServerException e ) {
            responseISOBuilder.responseSystemError( e );
        }
        return null;
    }

    private ATMResponseModel handlerResponse( ISO r0430 ) {
        ATMResponseModel model = new ATMResponseModel();
        model.setMessage( r0430 );
        return model;
    }
}

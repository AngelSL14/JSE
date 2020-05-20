package us.gonet.nabhi.jse.i8583.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.iso8583.constants.atm.TranCodes;
import us.gonet.iso8583.message.Request0200;
import us.gonet.nabhi.jse.utils.EntryMode;
import us.gonet.nabhi.misc.exception.EncoderException;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ISOException;
import us.gonet.nabhi.misc.exception.ServerException;
import us.gonet.nabhi.misc.iso.IReceipt;
import us.gonet.nabhi.misc.iso.ISOBuilder;
import us.gonet.nabhi.misc.iso.ResponseISOBuilder;
import us.gonet.nabhi.misc.iso.token.TokenID;
import us.gonet.nabhi.misc.model.ATMRequestModel;
import us.gonet.nabhi.misc.model.ATMResponseModel;
import us.gonet.nabhi.misc.rest.iso.ISORequester;
import us.gonet.serializable.data.ISO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static us.gonet.nabhi.misc.iso.token.TokenID.*;

@Component
public class ProcessingTransaction {

    private static final String ERROR_1 = "Invalid transaction";

    private ISOBuilder isoBuilder;
    private ResponseISOBuilder responseISOBuilder;
    private ISORequester isoRequester;
    private IReceipt receipt;

    @Autowired
    public ProcessingTransaction( ISOBuilder isoBuilder, ResponseISOBuilder responseISOBuilder, ISORequester isoRequester, IReceipt receipt ) {
        this.isoBuilder = isoBuilder;
        this.responseISOBuilder = responseISOBuilder;
        this.isoRequester = isoRequester;
        this.receipt = receipt;
    }

    public ATMResponseModel sendTransaction( TranCodes tranCode, ATMRequestModel atmRequestModel ) throws ISOException {
        List < TokenID > tokenIDS = new ArrayList <>();
        if ( atmRequestModel.getEmv() != null && atmRequestModel.getEntryMode().startsWith( EntryMode.CHIP.getValue() ) ) {
            tokenIDS.add( TOKEN_B2 );
            tokenIDS.add( TOKEN_B3 );
        }
        tokenIDS.add( TOKEN_B4 );
        switch ( tranCode ) {
            case WITHDRAWAL:
            case STATEMENT_PRINT:
            case BALANCE_INQUIRY:
                break;
            case GENERIC_SALE:
                tokenIDS.add( TOKEN_P1 );
                tokenIDS.add( TOKEN_QV );
                break;
            case PIN_CHANGE:
                tokenIDS.add( TOKEN_06 );
                break;
            default:
                List < ErrorWS > errors = new ArrayList <>();
                errors.add( new ErrorWS( "ISO-01", ERROR_1 ) );
                throw new ISOException( ERROR_1, errors );
        }

        try {
            Map < String, String > dataElements = isoBuilder.buildDataElement( tranCode, atmRequestModel, null );
            dataElements = isoBuilder.addTokens( dataElements,
                    null,
                    null,
                    null,
                    atmRequestModel,
                    tokenIDS );
            ISO r0200 = new Request0200( dataElements ).getIso();
            ISO r0210 = isoRequester.sendMessage( r0200 );
            if ( !r0210.isTimeOut() ) {
                return responseISOBuilder.genericResponse( atmRequestModel, r0210, receipt );
            } else {
                responseISOBuilder.responseReceivedToLate();
            }
        } catch ( ServerException | EncoderException e ) {
            responseISOBuilder.responseSystemError( e );
        }
        return null;
    }


}

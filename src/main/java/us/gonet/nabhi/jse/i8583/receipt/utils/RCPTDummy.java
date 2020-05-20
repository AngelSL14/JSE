package us.gonet.nabhi.jse.i8583.receipt.utils;


import us.gonet.nabhi.misc.model.jdbc.jdb.RCPT;

public enum RCPTDummy {

    DUMMY_01( "****01", new RCPT( null, Dummy.HEADER, "%[LF],@TARJETA:[IT1],#PAN[9HT],%[LF],@FOLIO:[IT1],#AC[7HT],%[LF],#TC[IT0],%[LF],@TIPO CUENTA:[IT1],#FC[HT1],%[LF],@SALDO ANTERIOR:[IT1],#OB[16HT],%[LF],@MONTO RETIRADO:[IT1],#AMT[16HT],%[LF],@COMISION:[IT1],#SRH[10HT],%[LF],@IVA:[IT1],#IVA[5HT],%[LF],@TOTAL:[IT1],#TT[7HT],%[LF],@SALDO ACTUAL:[IT1],#CB[14HT],%[LF],@AID:[IT1],#AID[5HT],%[LF],@ARQC:[IT1],#ARQC[6HT],%[LF],%[LF],", Dummy.TRAILER ) ),
    DUMMY_31( "****31", new RCPT( null, Dummy.HEADER, "%[LF],@TARJETA:[IT1],#PAN[9HT],%[LF],@FOLIO:[IT1],#AC[7HT],%[LF],#TC[IT0],%[LF],@TIPO CUENTA:[IT1],#FC[HT1],%[LF],@SALDO ANTERIOR:[IT1],#OB[16HT],%[LF],@COMISION:[IT1],#SRH[10HT],%[LF],@IVA:[IT1],#IVA[5HT],%[LF],@SALDO ACTUAL:[IT1],#CB[14HT],%[LF],@AID:[IT1],#AID[5HT],%[LF],@ARQC:[IT1],#ARQC[6HT],%[LF],%[LF],", Dummy.TRAILER ) ),
    DUMMY_65( "****65", new RCPT( null, Dummy.HEADER, "%[LF],@TARJETA:[IT1],#PAN[9HT],%[LF],@FOLIO:[IT1],#AC[7HT],%[LF],#TC[IT0],%[LF],@TIPO CUENTA:[IT1],#FC[HT1],%[LF],@MONTO RECARGADO:[IT1],#AMT[17HT],%[LF],@TOTAL:[IT1],#TT[7HT],%[LF],%[LF],@TELEFONO:[IT1],#PHN[10HT],%[LF],@COMPANIA:[IT1],#COMP[10HT],%[LF],%[LF],", Dummy.TRAILER ) ),
    DUMMY_94( "****94", new RCPT( null, Dummy.HEADER, "%[LF],@TARJETA:[IT1],#PAN[9HT],%[LF],@FOLIO:[IT1],#AC[7HT],%[LF],#TC[IT0],%[LF],@MOVIMIENTOS:[IT1],%[LF],#STMT[13HT],%[LF],%[LF],", Dummy.TRAILER ) ),
    DUMMY_96( "****96", new RCPT( null, Dummy.HEADER, "%[LF],@TARJETA:[IT1],#PAN[9HT],%[LF],@FOLIO:[IT1],#AC[7HT],%[LF],#TC[IT0],%[LF],@AID:[IT1],#AID[5HT],%[LF],@ARQC:[IT1],#ARQC[6HT],%[LF],%[LF],@SU NIP A CAMBIADO[IT0],%[LF],", Dummy.TRAILER ) ),
    DUMMY_00( "****00", new RCPT( null, Dummy.HEADER, "%[LF],@TRANSACCION EXITOSA[IT0],%[LF],", Dummy.TRAILER ) ),
    ;

    private String key;
    private RCPT rcpt;

    RCPTDummy( String key, RCPT rcpt ) {
        this.key = key;
        this.rcpt = rcpt;
    }

    public String getKey() {
        return key;
    }

    public RCPT getRcpt() {
        return rcpt;
    }

    public static RCPT getScripRcpt( String key ) {
        for ( RCPTDummy r : RCPTDummy.values() ) {
            if ( r.getKey().equals( key ) ) {
                return r.getRcpt();
            }
        }
        return DUMMY_00.getRcpt();
    }

    private class Dummy {
        private static final String HEADER = "%[LF],@FECHA[IT9],@HORA[IT5],@CAJERO[IT10],%[LF],#DA[IT6],#T1[IT4],#AT[IT0],%[LF],#OWNER[IT0],%[LF],#LOC[IT0],%[LF],";
        private static final String TRAILER = "@GRACIAS POR UTILIZAR LOS CAJEROS[IT0],%[LF],@VUELVA PRONTO[IT0],%[LF],@ATM CLOUD[IT0],%[LF],";
    }
}

package us.gonet.nabhi.jse.utils;

public enum TRAN_CDE {
    WITHDRAWAL( "01", "Retiro de efectivo" ),
    CHECK_GUARANTEE( "03", "" ),
    CHECK_VERIFICATION( "04", "" ),
    DEPOSIT( "21", "Deposito" ),//*
    DEPOSIT_CREDIT( "22", "Deposito Credito" ),//*
    BALANCE_INQUIRY( "31", "Consulta de Saldo" ),
    QUERY_SPEI( "37", "Consulta de SPEI" ),
    QUERY_ACCOUNTS( "38", "Consulta de Cuentas" ),
    CARDHOLDER_ACCOUNTS_TRANSFER( "40", "" ),//*
    SPEI( "42", "SPEI" ),
    GENERIC_SALE( "65", "Compra de Tiempo Aire" ),
    PAYMENT_ENCLOSED( "90", "" ),//*
    MESSAGE_TO_FINANCIAL_INSTITUTION( "91", "" ),
    CHECK_CASH( "92", "" ),
    LOG_ONLY_TRANSACTION( "93", "" ),
    STATEMENT_PRINT( "94", "Consulta de Movimientos" ),
    PIN_CHANGE( "96", "Cambio de NIP" ),
    EMV_PIN_UNBLOCK( "97", "" );

    private final String value;
    private final String operation;

    TRAN_CDE( String value, String operation ) {
        this.value = value;
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public String getOperation() {
        return operation;
    }

    public static String getOperation( String code ) {
        for ( TRAN_CDE c : values() ) {
            if ( c.getValue().equals( code ) ) {
                return c.getOperation();
            }
        }
        return null;
    }

    public static TRAN_CDE getTranCde( String code ) {
        for ( TRAN_CDE c : values() ) {
            if ( c.getValue().equals( code ) ) {
                return c;
            }
        }
        return null;
    }
}

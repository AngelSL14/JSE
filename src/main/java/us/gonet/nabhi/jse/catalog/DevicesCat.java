package us.gonet.nabhi.jse.catalog;

public enum DevicesCat {
    PRINTER( "01" ),
    CARD_READER( "02" ),
    DISPENSER( "03" ),
    PINPAD( "04" ),
    SCANNER( "05" ),
    DEPOSITORY( "06" ),
    TEXT_TERMINAL( "07" ),
    SENSORS( "08" ),
    VENDOR_DEPENDANT( "09" ),
    CAMERA( "10" ),
    CASH_IN( "13" ),
    BARCODE_READER( "15" );

    private final String code;

    DevicesCat( String code ) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static String getDeviceName( String code ) {
        for ( DevicesCat d : values() ) {
            if ( d.getCode().equals( code ) ) {
                return d.name();
            }
        }
        return null;
    }
}

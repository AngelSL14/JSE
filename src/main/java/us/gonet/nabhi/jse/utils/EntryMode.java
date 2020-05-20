package us.gonet.nabhi.jse.utils;

public enum EntryMode {

    UNKNOWN( "00" ),
    MANUAL( "01" ),
    MAGNETIC_STRIPE( "02" ),
    BARCODE( "03" ),
    OCR( "04" ),
    CHIP( "05" ),
    CHIP_ERROR( "80" ),
    ;

    private String value;

    EntryMode( String value ) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

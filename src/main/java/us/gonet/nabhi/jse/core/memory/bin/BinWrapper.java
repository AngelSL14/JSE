package us.gonet.nabhi.jse.core.memory.bin;

import com.google.gson.GsonBuilder;

import java.util.Objects;


public class BinWrapper implements Comparable < BinWrapper > {

    private String bin;
    private String fiid;

    BinWrapper( String bin, String fiid ) {
        this.bin = bin;
        this.fiid = fiid;
    }

    BinWrapper( String bin ) {
        this.bin = bin;
    }

    public String getBin() {
        return bin;
    }

    public void setBin( String bin ) {
        this.bin = bin;
    }

    public String getFiid() {
        return fiid;
    }

    public void setFiid( String fiid ) {
        this.fiid = fiid;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson( this );
    }

    @Override
    public int compareTo( BinWrapper o ) {
        return ( this.bin.hashCode() - o.getBin().hashCode() );
    }

    @Override
    @SuppressWarnings( "EqualsWhichDoesntCheckParameterClass" )
    public boolean equals( Object o ) {
        return Objects.equals( this.getBin(), o );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( this.getBin() );
    }
}

package us.gonet.nabhi.jse.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

@Component
public class BinaryUtils {

    private static final Logger LOG = LoggerFactory.getLogger( BinaryUtils.class );
    private static final String ERROR_PER = "Error building a persistent object";
    private static final String ERROR_DE_PER = "Error retrieved a persistent object";

    public Blob serializable( Object o ) {
        Blob blob;
        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream( bos ) ) {
            out.writeObject( o );
            byte[] bytes = bos.toByteArray();
            blob = new SerialBlob( bytes );
        } catch ( IOException | SQLException e ) {
            LOG.error( ERROR_PER );
            return null;
        }
        return blob;
    }

    public Object deserializable( Blob b ) {
        try {
            ObjectInputStream ois = new ObjectInputStream( b.getBinaryStream() );
            return ois.readObject();
        } catch ( IOException | SQLException | ClassNotFoundException e ) {
            LOG.error( ERROR_DE_PER );
            return null;
        }
    }
}

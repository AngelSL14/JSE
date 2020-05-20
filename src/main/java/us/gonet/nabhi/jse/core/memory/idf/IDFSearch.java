package us.gonet.nabhi.jse.core.memory.idf;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.IDFException;
import us.gonet.nabhi.misc.jdb.repository.IDFRepository;
import us.gonet.nabhi.misc.model.jdbc.jdb.IDF;
import us.gonet.nabhi.misc.task.ForcedCutOver;
import us.gonet.nabhi.misc.task.ISearchListener;

import java.util.*;

@Component
public class IDFSearch implements ISearchListener {

    private String ownerFiid;
    private IDF ownerIDF;
    private static final Logger LOG = LoggerFactory.getLogger( IDFSearch.class );
    private IDFWrapper idfWrapper;
    private static final String ERROR_IDF = "Invalid FIID";
    private ForcedCutOver cutOver;
    private IDFRepository idfRepository;
    private ModelMapper mapper;
    private int hour;
    private int minutes;

    @Autowired
    public IDFSearch( ForcedCutOver cutOver, IDFRepository idfRepository, ModelMapper mapper ) {
        this.cutOver = cutOver;
        this.idfRepository = idfRepository;
        this.mapper = mapper;
    }

    public void buildTable( List < IDF > idfs ) {
        cutOver.setListener( this );
        Map < String, IDF > idfMap = new LinkedHashMap <>();
        for ( IDF i : idfs ) {
            idfMap.put( i.getFiid(), i );
        }
        idfWrapper = new IDFWrapper( idfMap );
        //try {
        //    ownerIDF = search( ownerFiid );
        //    parseCutOver( ownerIDF.getForcedCutOver() );
        //    cutOver.build( hour, minutes, 0, ownerIDF.getCurrentBusinessDay() );
        //    startTask();
        //} catch ( IDFException e ) {
        //    LOG.error( "Invalid configuration" );
        //}

    }

    public void updateIDF( IDF idf ){
        if ( idfWrapper.getIdfs().get( idf.getFiid() ) != null ){
            idfWrapper.getIdfs().replace( idf.getFiid(), idf );
        }else {
            idfWrapper.getIdfs().put( idf.getFiid(), idf );
        }
    }

    public IDF search( String fiid ) throws IDFException {
        IDF idf = idfWrapper.getIdfs().get( fiid );
        if ( idf != null ) {
            return idf;
        } else {
            throw new IDFException( ERROR_IDF, Collections.singletonList( new ErrorWS( "IDF-01", ERROR_IDF ) ) );
        }
    }

    private void parseCutOver( String cutOver ) {
        int indexOf = cutOver.indexOf( ':' );
        hour = Integer.parseInt( cutOver.substring( 0, indexOf ) );
        cutOver = cutOver.substring( indexOf + 1 );
        minutes = Integer.parseInt( cutOver.substring( 0, indexOf ) );
    }


    @Override
    public void notifyCutOver( String newDate ) {
        try {
            String nextDate = cutOver.getNextDate();
            if ( cutOver.isValidDate( newDate, nextDate ) ) {
                ownerIDF.setCurrentBusinessDay( newDate );
                ownerIDF.setNextBusinessDay( cutOver.getNextDate() );
                //idfRepository.save( mapper.map( ownerIDF, IDFEntity.class ) );
            } else {
                if ( LOG.isErrorEnabled() ) {
                    LOG.error( "IDF cannot be update" );
                }
            }
        } catch ( ClassCastException e ) {
            LOG.error( "IDF cannot be update" );
        }
    }

    private void startTask() {
        Timer timer = new Timer( "Forced Cut Over" );
        timer.schedule( cutOver, cutOver.getCalendar().getTime(), 86400000 );
    }
}

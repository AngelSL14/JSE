package us.gonet.nabhi.jse.business.jdb.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.gonet.nabhi.jse.business.jdb.ISaveService;
import us.gonet.nabhi.jse.core.jdb.Repository;
import us.gonet.nabhi.jse.core.jdb.SanitizeModel;
import us.gonet.nabhi.jse.core.memory.idf.IDFSearch;
import us.gonet.nabhi.misc.exception.ErrorWS;
import us.gonet.nabhi.misc.exception.ResponseWrapper;
import us.gonet.nabhi.misc.exception.SanitazeException;
import us.gonet.nabhi.misc.jdb.entity.*;
import us.gonet.nabhi.misc.jdb.entity.node.NodeProsaEntity;
import us.gonet.nabhi.misc.model.jdbc.jdb.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SaveService implements ISaveService {

    private static final Logger LOG = LoggerFactory.getLogger( SaveService.class );
    private Repository repository;
    private ModelMapper mapper;
    private SanitizeModel sanitize;
    private IDFSearch idfSearch;

    @Autowired
    public SaveService( Repository repository, ModelMapper mapper, SanitizeModel sanitize, IDFSearch idfSearch ) {
        this.repository = repository;
        this.mapper = mapper;
        this.sanitize = sanitize;
        this.idfSearch = idfSearch;
    }

    @Override
    public ResponseWrapper < List < BIN > > saveBins( List< BIN> bines ){
        ResponseWrapper < List < BIN > > wrapper = new ResponseWrapper <>();
        for ( BIN e : bines ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < BINEntity > >() {
            }.getType();
            List < BINEntity > entities = mapper.map( bines, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getBinRepository().saveAll( entities );
                listType = new TypeToken< List < BIN> > (){
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            }else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < List < State > > saveStates( List < State > states ) {
        ResponseWrapper < List < State > > wrapper = new ResponseWrapper <>();
        for ( State e : states ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < StateEntity > >() {
            }.getType();
            List < StateEntity > entities = mapper.map( states, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getStateRepository().saveAll( entities );
                listType = new TypeToken < List < State > >() {
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < List < County > > saveCountys( List < County > countys ) {
        ResponseWrapper < List < County > > wrapper = new ResponseWrapper <>();
        for ( County e : countys ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < CountyEntity > >() {
            }.getType();
            List < CountyEntity > entities = mapper.map( countys, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getCountyRepository().saveAll( entities );
                listType = new TypeToken < List < County > >() {
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < IDF > updateIdf( IDF idf ) {
        ResponseWrapper < IDF > wrapper = new ResponseWrapper <>();
        try {
            sanitize.sanitize( idf );
        } catch ( SanitazeException e ) {
            LOG.error( "Error IDF PARAM" );
            wrapper.setCode( "96" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        IDFEntity entity = mapper.map( idf, IDFEntity.class );
        entity = repository.getIdfRepository().save( entity );
        idf = mapper.map( entity, IDF.class );
        wrapper.setCode( "00" );
        wrapper.addBody( idf );
        return wrapper;
    }

    @Override
    public ResponseWrapper < IDF > saveIdf( IDF idf ) {
        ResponseWrapper < IDF > wrapper = new ResponseWrapper <>();
        try {
            sanitize.sanitize( idf );
        } catch ( SanitazeException e ) {
            wrapper.setCode( "96" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        Optional < CountryEntity > ce = repository.getCountryRepository().findById( idf.getCountry().getCountryCode() );
        if ( ce.isPresent() ) {
            IDFEntity entity = mapper.map( idf, IDFEntity.class );
            entity.setCountry( ce.get() );
            entity = repository.getIdfRepository().save( entity );
            idf = mapper.map( entity, IDF.class );
            wrapper.setCode( "00" );
            wrapper.addBody( idf );
        } else {
            invalidParams( wrapper, "Invalid Country" );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < IDF > > saveIdfs( Map < String, Object > entrys ) {
        ResponseWrapper < List < IDF > > wrapper = new ResponseWrapper <>();
        Country country = mapper.map( entrys.get( "countryCode" ), Country.class );
        try {
            sanitize.sanitize( country );
        } catch ( SanitazeException e ) {
            wrapper.setCode( "96" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        Optional < CountryEntity > optional = repository.getCountryRepository().findById( country.getCountryCode() );
        CountryEntity countryEntity;
        if ( !optional.isPresent() ) {
            countryEntity = mapper.map( country, CountryEntity.class );
            repository.getCountryRepository().save( countryEntity );
        } else {
            countryEntity = optional.get();
        }
        Type listType = new TypeToken < List < IDF > >() {
        }.getType();
        List < IDF > idfs = mapper.map( entrys.get( "idfs" ), listType );
        for ( IDF i : idfs ) {
            try {
                sanitize.sanitize( i );
            } catch ( SanitazeException e ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e.getErrors() );
                return wrapper;
            }
        }
        listType = new TypeToken < List < IDFEntity > >() {
        }.getType();
        List < IDFEntity > entities = mapper.map( idfs, listType );
        if ( entities != null && !entities.isEmpty() ) {
            for ( IDFEntity idf : entities ) {
                idf.setCountry( countryEntity );
            }
            repository.getIdfRepository().saveAll( entities );
            listType = new TypeToken < List < IDF > >() {
            }.getType();
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            invalidData( wrapper );
        }
        for ( IDF idf : wrapper.getBody().get( 0 ) ){
            idfSearch.updateIDF( idf );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < Button > > saveButtons( List < Button > buttons ) {
        ResponseWrapper < List < Button > > wrapper = new ResponseWrapper <>();
        for ( Button e : buttons ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < ButtonEntity > >() {
            }.getType();
            List < ButtonEntity > entities = mapper.map( buttons, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getButtonRepository().saveAll( entities );
                listType = new TypeToken < List < Button > >() {
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < List < APC > > saveApcs( List < APC > apcs ) {
        ResponseWrapper < List < APC > > wrapper = new ResponseWrapper <>();
        for ( APC e : apcs ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < APCEntity > >() {
            }.getType();
            List < APCEntity > entities = mapper.map( apcs, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getApcRepository().saveAll( entities );
                listType = new TypeToken < List < APC > >() {
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < List < TranAllowed > > saveTranAllowes( List < TranAllowed > tranAlloweds ) {
        ResponseWrapper < List < TranAllowed > > wrapper = new ResponseWrapper <>();
        for ( TranAllowed e : tranAlloweds ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < TranAllowedEntity > >() {
            }.getType();
            List < TranAllowedEntity > entities = mapper.map( tranAlloweds, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getTranAllowedRepository().saveAll( entities );
                listType = new TypeToken < List < TranAllowed > >() {
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < List < NodeProsa > > saveNodes( List < NodeProsa > nodes ) {
        ResponseWrapper < List < NodeProsa > > wrapper = new ResponseWrapper <>();
        for ( NodeProsa e : nodes ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < NodeProsaEntity > >() {
            }.getType();
            List < NodeProsaEntity > entities = mapper.map( nodes, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getNodeProsaRepository().saveAll( entities );
                listType = new TypeToken < List < NodeProsa > >() {
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < List < Surcharge > > saveSurcharges( List < Surcharge > surcharges ) {
        ResponseWrapper < List < Surcharge > > wrapper = new ResponseWrapper <>();
        for ( Surcharge e : surcharges ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < SurchargeEntity > >() {
            }.getType();
            List < SurchargeEntity > entities = mapper.map( surcharges, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getSurchargeRepository().saveAll( entities );
                listType = new TypeToken < List < Surcharge > >() {
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < List < ATD > > saveAtms( List < ATD > atds, boolean create ) {
        ResponseWrapper < List < ATD > > wrapper = new ResponseWrapper <>();
        for ( ATD e : atds ) {
            try {
                sanitize.sanitize( e, create );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        List < ATDEntity > entities = new ArrayList <>();
        for ( ATD atd : atds ) {
            ATDEntity entity = mapper.map( atd, ATDEntity.class );
            if ( !verifyIDF( entity, create, wrapper ) ) {
                return wrapper;
            }
            if ( !verifyCounty( entity, create, wrapper ) ) {
                return wrapper;
            }
            if ( !verifyNode( entity, create, wrapper ) ) {
                return wrapper;
            }
            if ( !verifyScreen( entity, wrapper ) ) {
                return wrapper;
            }
            entities.add( entity );
        }
        if ( !entities.isEmpty() ) {
            entities = repository.getAtdRepository().saveAll( entities );
            Type listType = new TypeToken < List < ATD > >() {
            }.getType();
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            invalidData( wrapper );
        }
        return wrapper;
    }

    private boolean verifyScreen( ATDEntity entity, ResponseWrapper < List < ATD > > wrapper ) {
        ScreenEntity screenEntity = entity.getAtm().getScreen();
        Optional < ScreenGroupEntity > og = repository.getScreenGroupRepository().findById( screenEntity.getScreenGroup().getGroupId() );
        if ( og.isPresent() ) {
            screenEntity.setScreenGroup( og.get() );
            return true;
        } else {
            invalidParams( wrapper, "Screen Group not exist" );
            return false;
        }
    }

    private boolean verifyIDF( ATDEntity entity, boolean create, ResponseWrapper < List < ATD > > wrapper ) {
        Optional < IDFEntity > oi = repository.getIdfRepository().findById( entity.getIdf().getFiid() );
        if ( oi.isPresent() ) {
            if ( create ) {
                entity.setIdf( oi.get() );
            }
            return true;
        } else {
            invalidParams( wrapper, "IDF not exist" );
            return false;
        }
    }

    private boolean verifyCounty( ATDEntity entity, boolean create, ResponseWrapper < List < ATD > > wrapper ) {
        Optional < CountyEntity > oc = repository.getCountyRepository().findById( entity.getCounty().getCountyCodeId() );
        if ( oc.isPresent() ) {
            if ( create ) {
                entity.setCounty( oc.get() );
            }
            return true;
        } else {
            invalidParams( wrapper, "County not exist" );
            return false;
        }
    }

    private boolean verifyNode( ATDEntity entity, boolean create, ResponseWrapper < List < ATD > > wrapper ) {
        Optional < NodeProsaEntity > on = repository.getNodeProsaRepository().findById( entity.getNodeProsa().getIdNode() );
        if ( on.isPresent() ) {
            if ( create ) {
                entity.setNodeProsa( on.get() );
            }
            return true;
        } else {
            invalidParams( wrapper, "Node not exists" );
            return false;
        }
    }


    private void invalidData( ResponseWrapper wrapper ) {
        wrapper.setCode( "96" );
        wrapper.addError( new ErrorWS( "JDB-03", "Invalid Data" ) );
    }

    private void invalidParams( ResponseWrapper wrapper, String message ) {
        wrapper.setCode( "96" );
        wrapper.addError( new ErrorWS( "JDB-04", message ) );
    }


    @Override
    public ResponseWrapper < List < RCPT > > saveRcpts( List < RCPT > rcpts ) {
        ResponseWrapper < List < RCPT > > wrapper = new ResponseWrapper <>();
        for ( RCPT e : rcpts ) {
            try {
                sanitize.sanitize( e );
            } catch ( SanitazeException e1 ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e1.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < RCPTEntity > >() {
            }.getType();
            List < RCPTEntity > entities = mapper.map( rcpts, listType );
            if ( entities != null && !entities.isEmpty() ) {
                entities = repository.getRcptRepository().saveAll( entities );
                listType = new TypeToken < List < RCPT > >() {
                }.getType();
                wrapper.setCode( "00" );
                wrapper.addBody( mapper.map( entities, listType ) );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }

    @Override
    public ResponseWrapper < String > saveImages( List < Image64 > image64s ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        for ( Image64 i : image64s ) {
            try {
                sanitize.sanitize( i );
            } catch ( SanitazeException e ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < ImageEntity > >() {
            }.getType();
            List < ImageEntity > entities = mapper.map( image64s, listType );
            if ( entities != null && !entities.isEmpty() ) {
                repository.getImageRepository().saveAll( entities );
                wrapper.setCode( "00" );
                wrapper.addBody( "00" );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }

    }

    @Override
    public ResponseWrapper < String > saveScreen( List < ScreenGroup > screenGroups ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        for ( ScreenGroup i : screenGroups ) {
            try {
                sanitize.sanitize( i );
            } catch ( SanitazeException e ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < ScreenGroupEntity > >() {
            }.getType();
            List < ScreenGroupEntity > entities = mapper.map( screenGroups, listType );
            if ( entities != null && !entities.isEmpty() ) {
                repository.getScreenGroupRepository().saveAll( entities );
                wrapper.setCode( "00" );
                wrapper.addBody( "00" );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }

    }

    @Override
    public ResponseWrapper < String > saveBankStyle( List < BankStyle > bankStyles ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        for ( BankStyle i : bankStyles ) {
            try {
                sanitize.sanitize( i );
            } catch ( SanitazeException e ) {
                wrapper.setCode( "96" );
                wrapper.addAllError( e.getErrors() );
                return wrapper;
            }
        }
        try {
            Type listType = new TypeToken < List < BankStyleEntity > >() {
            }.getType();
            List < BankStyleEntity > entities = mapper.map( bankStyles, listType );
            if ( entities != null && !entities.isEmpty() ) {
                repository.getBankStyleRepository().saveAll( entities );
                wrapper.setCode( "00" );
                wrapper.addBody( "00" );
            } else {
                invalidData( wrapper );
            }
            return wrapper;
        } catch ( ClassCastException e ) {
            invalidData( wrapper );
            return wrapper;
        }
    }
}
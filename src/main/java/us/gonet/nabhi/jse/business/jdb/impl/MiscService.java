package us.gonet.nabhi.jse.business.jdb.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.business.jdb.IMiscService;
import us.gonet.nabhi.jse.config.Initializer;
import us.gonet.nabhi.jse.core.jdb.Repository;
import us.gonet.nabhi.jse.core.jdb.SanitizeModel;
import us.gonet.nabhi.jse.utils.StreamFilter;
import us.gonet.nabhi.misc.exception.*;
import us.gonet.nabhi.misc.jdb.entity.*;
import us.gonet.nabhi.misc.jdb.entity.composite.APCIdentity;
import us.gonet.nabhi.misc.jdb.entity.composite.RCPTIdentity;
import us.gonet.nabhi.misc.jdb.entity.composite.SurchargeIdentity;
import us.gonet.nabhi.misc.jdb.entity.node.NodeProsaEntity;
import us.gonet.nabhi.misc.jdb.entity.personalized.bin.PBINEntity;
import us.gonet.nabhi.misc.jdb.entity.personalized.bin.PIDFEntity;
import us.gonet.nabhi.misc.model.jdbc.composite.APCId;
import us.gonet.nabhi.misc.model.jdbc.composite.RCPTId;
import us.gonet.nabhi.misc.model.jdbc.composite.SurchargeId;
import us.gonet.nabhi.misc.model.jdbc.jdb.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Component
public class MiscService implements IMiscService {

    private static final String CAUSE = "JDB-02";
    private Repository repository;
    private ModelMapper mapper;
    private StreamFilter filter;
    private SanitizeModel sanitize;
    private Initializer initializer;


    @Autowired
    public MiscService(Repository repository, ModelMapper mapper, StreamFilter filter, SanitizeModel sanitize, Initializer initializer) {
        this.repository = repository;
        this.mapper = mapper;
        this.filter = filter;
        this.sanitize = sanitize;
        this.initializer = initializer;
    }

    @Override
    public ResponseWrapper < List < BIN > > findAllBin() {
        ResponseWrapper < List < BIN > > wrapper = new ResponseWrapper <>();
        List < PBINEntity > entities = repository.getPbinRepository().findAll( new Sort( Sort.Direction.ASC, "BinId_BinLen", "BinId_Bin", "BinId_PanLen" ) );
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < BIN > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "List of BINS empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < IDF > > findAllIDF() {
        ResponseWrapper < List < IDF > > wrapper = new ResponseWrapper <>();
        List < IDFEntity > entities = repository.getIdfRepository().findAll();
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < IDF > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "List of IDFS empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < IDF > findByFiid( String fiid ) {
        ResponseWrapper < IDF > wrapper = new ResponseWrapper <>();
        Optional < IDFEntity > oi = repository.getIdfRepository().findById( fiid );
        if ( oi.isPresent() ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( oi.get(), IDF.class ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "IDF not exists" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < BankStyle > > findAllBackStyle() {
        ResponseWrapper < List < BankStyle > > wrapper = new ResponseWrapper <>();
        List < BankStyleEntity > entities = repository.getBankStyleRepository().findAll();
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < BankStyle > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "List of BackStyle empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < BankStyle > findBankStyle( String id ) {
        String defaultBack = "Prosa S.A.";
        ResponseWrapper < BankStyle > wrapper = new ResponseWrapper <>();
        try {
            filter.sanitizeString( id );
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
        }
        Optional < BankStyleEntity > ob = repository.getBankStyleRepository().findById( id );
        wrapper.setCode( "00" );
        if ( ob.isPresent() ) {
            wrapper.addBody( mapper.map( ob.get(), BankStyle.class ) );
        } else {
            ob = repository.getBankStyleRepository().findById( defaultBack );
            if ( ob.isPresent() ) {
                wrapper.addBody( mapper.map( ob.get(), BankStyle.class ) );
            } else {
                wrapper.setCode( "96" );
                wrapper.addError( new ErrorWS( CAUSE, "Style Bank not exists" ) );
            }
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < NodeProsa > findNode( String id ) {
        ResponseWrapper < NodeProsa > wrapper = new ResponseWrapper <>();
        try {
            id = filter.sanitizeString( id );
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
        }
        NodeProsaEntity entity = repository.getNodeProsaRepository().findByNodeName( id );
        if ( entity != null ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( entity, NodeProsa.class ) );
        } else {
            wrapper.setCode( "96" );
            wrapper.addError( new ErrorWS( CAUSE, "Node name not exists" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < RCPT > findRCPT( RCPTId rcptId ) {
        ResponseWrapper < RCPT > wrapper = new ResponseWrapper <>();
        RCPTIdentity identity;
        try {
            sanitize.sanitize( rcptId );
            identity = mapper.map( rcptId, RCPTIdentity.class );
        } catch ( SanitazeException e ) {
            wrapper.setCode( "96" );
            wrapper.addAllError( e.getErrors() );
            return wrapper;
        }
        Optional < RCPTEntity > or = repository.getRcptRepository().findById( identity );
        if ( or.isPresent() ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( or.get(), RCPT.class ) );
        } else {
            wrapper.setCode( "96" );
            wrapper.addError( new ErrorWS( CAUSE, "RCPT not exists" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < RCPT > > findRCPTAll() {
        ResponseWrapper < List < RCPT > > wrapper = new ResponseWrapper <>();
        List < RCPTEntity > entities = repository.getRcptRepository().findAll();
        Type listType = new TypeToken < List < RCPT > >() {
        }.getType();
        if ( entities != null ) {
            wrapper.addBody( mapper.map( entities, listType ) );
            wrapper.setCode( "00" );
        } else {
            wrapper.setCode( "96" );
            wrapper.addError( new ErrorWS( CAUSE, "List of RCPT is empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < Image64 > > findAllImages() {
        ResponseWrapper < List < Image64 > > wrapper = new ResponseWrapper <>();
        List < ImageEntity > entities = repository.getImageRepository().findAll();
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < Image64 > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "List of images is empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < Image64 > > findAllImagesByFiid( String fiid ) {
        ResponseWrapper < List < Image64 > > wrapper = new ResponseWrapper <>();
        PIDFEntity entity = new PIDFEntity();
        entity.setFiid( fiid );
        List < ImageEntity > entities = repository.getImageRepository().findAllByIdfEquals( entity );
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < Image64 > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "List by fiid of images is empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < Image64 > findById( String id, String category ) {
        ResponseWrapper < Image64 > wrapper = new ResponseWrapper <>();
        if ( !filterAll( wrapper, id, category ) ) {
            return wrapper;
        }
        Optional < ImageEntity > oe = repository.getImageRepository().findByNameAndCategory( id, category );
        if ( oe.isPresent() ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( oe.get(), Image64.class ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "Image not exists" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < Image64 > findById( String id, String category, String fiid ) {
        ResponseWrapper < Image64 > wrapper = new ResponseWrapper <>();
        if ( !filterAll( wrapper, id, category, fiid ) ) {
            return wrapper;
        }
        PIDFEntity entity = new PIDFEntity();
        entity.setFiid( fiid );
        Optional < ImageEntity > oe = repository.getImageRepository().findByNameAndCategoryAndIdfEquals( id, category, entity );
        if ( oe.isPresent() ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( oe.get(), Image64.class ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "Image by fiid not exists" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < ScreenGroup > > findAllScreen() {
        ResponseWrapper < List < ScreenGroup > > wrapper = new ResponseWrapper <>();
        List < ScreenGroupEntity > entities = repository.getScreenGroupRepository().findAll();
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < ScreenGroup > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "List of screen is empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < List < ScreenGroup > > findAllScreen( String fiid ) {
        ResponseWrapper < List < ScreenGroup > > wrapper = new ResponseWrapper <>();
        if ( !filterAll( wrapper, fiid, "ww" ) ) {
            return wrapper;
        }
        PIDFEntity entity = new PIDFEntity();
        entity.setFiid( fiid );
        List < ScreenGroupEntity > entities = repository.getScreenGroupRepository().findAllByIdfEquals( entity );
        if ( !entities.isEmpty() ) {
            wrapper.setCode( "00" );
            Type listType = new TypeToken < List < ScreenGroup > >() {
            }.getType();
            wrapper.addBody( mapper.map( entities, listType ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "List of screen by fiid is empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < ScreenGroup > findScreenByFiid( String fiid ) {
        ResponseWrapper < ScreenGroup > wrapper = new ResponseWrapper <>();
        if ( !filterAll( wrapper, fiid ) ) {
            return wrapper;
        }
        PIDFEntity entity = new PIDFEntity();
        entity.setFiid( fiid );
        Optional < ScreenGroupEntity > si = repository.getScreenGroupRepository().findByIdfEquals( entity );
        if ( si.isPresent() ) {
            wrapper.setCode( "00" );
            wrapper.addBody( mapper.map( si.get(), ScreenGroup.class ) );
        } else {
            wrapper.setCode( "01" );
            wrapper.addError( new ErrorWS( CAUSE, "Screen by fiid is empty" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < Integer > findCounty( String county, String state ) {
        ResponseWrapper < Integer > wrapper = new ResponseWrapper <>();
        try {
            filter.sanitizeString( county );
            filter.sanitizeString( state );
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
        }
        StateEntity stateEntity = repository.getStateRepository().findByStateName( state );
        CountyEntity countyEntity = repository.getCountyRepository().findByCountyNameAndState( county, stateEntity );
        wrapper.setCode( "00" );
        wrapper.addBody( countyEntity.getCountyCodeId() );
        return wrapper;
    }

    @Override
    public ResponseWrapper < String > deleteApc( APCId apcId ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        try {
            filter.sanitizeString( apcId.getFiid() );
            filter.sanitizeString( apcId.getTranCode() );
            filter.sanitizeString( apcId.getFormAcct() );
            filter.sanitizeString( apcId.getToAcct() );
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
            return wrapper;
        }
        Optional < APCEntity > oa = repository.getApcRepository().findById( mapper.map( apcId, APCIdentity.class ) );
        if ( oa.isPresent() ) {
            repository.getApcRepository().delete( oa.get() );
            wrapper.setCode( "00" );
            wrapper.addBody( "OK" );
        } else {
            wrapper.setCode( "96" );
            wrapper.addError( new ErrorWS( CAUSE, "APC not exists" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper < String > deleteSrh( SurchargeId surchargeId ) {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        try {
            filter.sanitizeString( surchargeId.getFiidAcquirer() );
            filter.sanitizeString( surchargeId.getFiidIssuing() );
            filter.sanitizeString( surchargeId.getTranCode() );
        } catch ( ServerException e ) {
            addErrors( e, wrapper );
            return wrapper;
        }
        Optional < SurchargeEntity > oa = repository.getSurchargeRepository().findById( mapper.map( surchargeId, SurchargeIdentity.class ) );
        if ( oa.isPresent() ) {
            repository.getSurchargeRepository().delete( oa.get() );
            wrapper.setCode( "00" );
            wrapper.addBody( "OK" );
        } else {
            wrapper.setCode( "96" );
            wrapper.addError( new ErrorWS( CAUSE, "Surcharge not exists" ) );
        }
        return wrapper;
    }

    @Override
    public ResponseWrapper<String> refreshMemory() {
        ResponseWrapper < String > wrapper = new ResponseWrapper <>();
        initializer.init();
        wrapper.setCode( "00" );
        wrapper.addBody( "OK" );
        return wrapper;
    }

    private void addErrors(IException e, ResponseWrapper wrapper ) {
        wrapper.setCode( "96" );
        wrapper.addAllError( e.getErrors() );
    }

    private boolean filterAll( ResponseWrapper wrapper, String... many ) {
        for ( String s : many ) {
            try {
                filter.sanitizeString( s );
            } catch ( ServerException e ) {
                addErrors( e, wrapper );
                return false;
            }
        }
        return true;
    }


}

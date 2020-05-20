package us.gonet.nabhi.jse.core.memory;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.jse.core.jdb.Repository;
import us.gonet.nabhi.jse.core.memory.atm.ATMSearch;
import us.gonet.nabhi.jse.core.memory.bin.BinarySearch;
import us.gonet.nabhi.jse.core.memory.idf.IDFSearch;
import us.gonet.nabhi.jse.core.memory.style.BankSearch;
import us.gonet.nabhi.misc.jdb.entity.ATDEntity;
import us.gonet.nabhi.misc.jdb.entity.BINEntity;
import us.gonet.nabhi.misc.jdb.entity.BankStyleEntity;
import us.gonet.nabhi.misc.jdb.entity.IDFEntity;
import us.gonet.nabhi.misc.model.jdbc.jdb.ATD;
import us.gonet.nabhi.misc.model.jdbc.jdb.BIN;
import us.gonet.nabhi.misc.model.jdbc.jdb.BankStyle;
import us.gonet.nabhi.misc.model.jdbc.jdb.IDF;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MemoryTableBuilder {

    private BinarySearch binarySearch;
    private IDFSearch idfSearch;
    private BankSearch bankSearch;
    private ATMSearch atmSearch;
    private Repository repository;
    private ModelMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger( MemoryTableBuilder.class );

    @Autowired
    public MemoryTableBuilder( BinarySearch binarySearch, IDFSearch idfSearch, BankSearch bankSearch, ATMSearch atmSearch, Repository repository, ModelMapper mapper ) {
        this.binarySearch = binarySearch;
        this.idfSearch = idfSearch;
        this.bankSearch = bankSearch;
        this.atmSearch = atmSearch;
        this.repository = repository;
        this.mapper = mapper;
    }

    public void buildBinTable() {
        Type listType = new TypeToken < List < BIN > >() {
        }.getType();
        List < BIN > binList;
        try {
            List < BINEntity > entities = repository.getBinRepository().findAll( new Sort( Sort.Direction.ASC, "BinId_BinLen", "BinId_Bin", "BinId_PanLen" ) );
            binarySearch.buildTable( mapper.map( entities, listType ) );
            if ( LOG.isInfoEnabled() ) {
                LOG.info( "List BIN retrieve" );
            }
        } catch ( ClassCastException e ) {
            binList = new ArrayList <>();
            binarySearch.buildTable( binList );
            binarySearch.getTable().setEmpty( true );
            LOG.error( "Cannot retrieve Bines list" );
        }
    }

    public void buildIDFTable() {
        Type listType = new TypeToken < List < IDF > >() {
        }.getType();
        List < IDF > idfs;
        try {
            List < IDFEntity > entities = repository.getIdfRepository().findAll();
            idfSearch.buildTable( mapper.map( entities, listType ) );
            LOG.info( "List IDF retrieve" );
        } catch ( ClassCastException e ) {
            idfs = new ArrayList <>();
            idfSearch.buildTable( idfs );
            LOG.error( "Cannot retrieve IDFs list" );
        }
    }

    public void buildBankTable() {
        Type listType = new TypeToken < List < BankStyle > >() {
        }.getType();
        List < BankStyle > banks;
        try {
            List < BankStyleEntity > entities = repository.getBankStyleRepository().findAll();
            bankSearch.buildTable( mapper.map( entities, listType ) );
            LOG.info( "List BANK retrieve" );
        } catch ( ClassCastException e ) {
            banks = new ArrayList <>();
            bankSearch.buildTable( banks );
            LOG.error( "Cannot retrieve Styles list" );
        }
    }

    public void buildATMTable() {
        Type listType = new TypeToken < List < ATD > >() {
        }.getType();
        List < ATD > atds;
        try {
            List < ATDEntity > entities = repository.getAtdRepository().findAll();
            atmSearch.buildTable( mapper.map( entities, listType ) );
            atmSearch.setLastUpdate( new Date().getTime() / 1000 );
            LOG.info( "List ATM retrieve" );
        } catch ( ClassCastException e ) {
            atds = new ArrayList <>();
            atmSearch.buildTable( atds );
            LOG.error( "Cannot retrieve ATMS list" );
        }
    }

    public BinarySearch getBinarySearch() {
        return binarySearch;
    }

    public IDFSearch getIdfSearch() {
        return idfSearch;
    }

    public BankSearch getBankSearch() {
        return bankSearch;
    }

    public ATMSearch getAtmSearch() {
        return atmSearch;
    }
}

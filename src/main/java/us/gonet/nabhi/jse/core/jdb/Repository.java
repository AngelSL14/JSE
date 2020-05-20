package us.gonet.nabhi.jse.core.jdb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.gonet.nabhi.misc.jdb.repository.*;
import us.gonet.nabhi.misc.jdb.repository.compose.ATDRepositoryCompose;
import us.gonet.nabhi.misc.jdb.repository.dash.DashboardUserRepository;
import us.gonet.nabhi.misc.jdb.repository.node.NodeProsaRepository;
import us.gonet.nabhi.misc.jdb.repository.personalized.PBINRepository;
import us.gonet.nabhi.misc.jdb.repository.personalized.up.ATDUpTimeRepository;
import us.gonet.nabhi.misc.jdb.repository.token.TokenUsersDetailRepository;
import us.gonet.nabhi.misc.jdb.repository.token.TokenUsersRepository;

@Component
public class Repository {

    private APCRepository apcRepository;
    private ATDRepository atdRepository;
    private ATMRepository atmRepository;
    private AuditRepository auditRepository;
    private BINRepository binRepository;
    private ButtonRepository buttonRepository;
    private CountryRepository countryRepository;
    private CountyRepository countyRepository;
    private DashboardUserRepository dashboardUserRepository;
    private DeviceCatRepository deviceCatRepository;
    private DeviceRepository deviceRepository;
    private IDFRepository idfRepository;
    private JournalRepository journalRepository;
    private NodeProsaRepository nodeProsaRepository;
    private RCPTRepository rcptRepository;
    private ScreenRepository screenRepository;
    private ScreenGroupRepository screenGroupRepository;
    private StateRepository stateRepository;
    private SurchargeRepository surchargeRepository;
    private TokenUsersRepository tokenUsersRepository;
    private TokenUsersDetailRepository tokenUsersDetailRepository;
    private TranAllowedRepository tranAllowedRepository;
    private PBINRepository pbinRepository;
    private BankStyleRepository bankStyleRepository;
    private ImageRepository imageRepository;
    private ATDUpTimeRepository atdUpTimeRepository;
    private ATDRepositoryCompose atdRepositoryCompose;


    public APCRepository getApcRepository() {
        return apcRepository;
    }

    @Autowired
    public void setApcRepository( APCRepository apcRepository ) {
        this.apcRepository = apcRepository;
    }

    public ATDRepository getAtdRepository() {
        return atdRepository;
    }

    @Autowired
    public void setAtdRepository( ATDRepository atdRepository ) {
        this.atdRepository = atdRepository;
    }

    public ATMRepository getAtmRepository() {
        return atmRepository;
    }

    @Autowired
    public void setAtmRepository( ATMRepository atmRepository ) {
        this.atmRepository = atmRepository;
    }

    public AuditRepository getAuditRepository() {
        return auditRepository;
    }

    @Autowired
    public void setAuditRepository( AuditRepository auditRepository ) {
        this.auditRepository = auditRepository;
    }

    public BINRepository getBinRepository() {
        return binRepository;
    }

    @Autowired
    public void setBinRepository( BINRepository binRepository ) {
        this.binRepository = binRepository;
    }

    public ButtonRepository getButtonRepository() {
        return buttonRepository;
    }

    @Autowired
    public void setButtonRepository( ButtonRepository buttonRepository ) {
        this.buttonRepository = buttonRepository;
    }

    public CountryRepository getCountryRepository() {
        return countryRepository;
    }

    @Autowired
    public void setCountryRepository( CountryRepository countryRepository ) {
        this.countryRepository = countryRepository;
    }

    public CountyRepository getCountyRepository() {
        return countyRepository;
    }

    @Autowired
    public void setCountyRepository( CountyRepository countyRepository ) {
        this.countyRepository = countyRepository;
    }

    public DashboardUserRepository getDashboardUserRepository() {
        return dashboardUserRepository;
    }

    @Autowired
    public void setDashboardUserRepository( DashboardUserRepository dashboardUserRepository ) {
        this.dashboardUserRepository = dashboardUserRepository;
    }

    public DeviceCatRepository getDeviceCatRepository() {
        return deviceCatRepository;
    }

    @Autowired
    public void setDeviceCatRepository( DeviceCatRepository deviceCatRepository ) {
        this.deviceCatRepository = deviceCatRepository;
    }

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    @Autowired
    public void setDeviceRepository( DeviceRepository deviceRepository ) {
        this.deviceRepository = deviceRepository;
    }

    public IDFRepository getIdfRepository() {
        return idfRepository;
    }

    @Autowired
    public void setIdfRepository( IDFRepository idfRepository ) {
        this.idfRepository = idfRepository;
    }

    public JournalRepository getJournalRepository() {
        return journalRepository;
    }

    @Autowired
    public void setJournalRepository( JournalRepository journalRepository ) {
        this.journalRepository = journalRepository;
    }

    public NodeProsaRepository getNodeProsaRepository() {
        return nodeProsaRepository;
    }

    @Autowired
    public void setNodeProsaRepository( NodeProsaRepository nodeProsaRepository ) {
        this.nodeProsaRepository = nodeProsaRepository;
    }

    public RCPTRepository getRcptRepository() {
        return rcptRepository;
    }

    @Autowired
    public void setRcptRepository( RCPTRepository rcptRepository ) {
        this.rcptRepository = rcptRepository;
    }

    public ScreenRepository getScreenRepository() {
        return screenRepository;
    }

    @Autowired
    public void setScreenRepository( ScreenRepository screenRepository ) {
        this.screenRepository = screenRepository;
    }

    public StateRepository getStateRepository() {
        return stateRepository;
    }

    @Autowired
    public void setStateRepository( StateRepository stateRepository ) {
        this.stateRepository = stateRepository;
    }

    public SurchargeRepository getSurchargeRepository() {
        return surchargeRepository;
    }

    @Autowired
    public void setSurchargeRepository( SurchargeRepository surchargeRepository ) {
        this.surchargeRepository = surchargeRepository;
    }

    public TokenUsersRepository getTokenUsersRepository() {
        return tokenUsersRepository;
    }

    @Autowired
    public void setTokenUsersRepository( TokenUsersRepository tokenUsersRepository ) {
        this.tokenUsersRepository = tokenUsersRepository;
    }

    public TokenUsersDetailRepository getTokenUsersDetailRepository() {
        return tokenUsersDetailRepository;
    }

    @Autowired
    public void setTokenUsersDetailRepository( TokenUsersDetailRepository tokenUsersDetailRepository ) {
        this.tokenUsersDetailRepository = tokenUsersDetailRepository;
    }

    public TranAllowedRepository getTranAllowedRepository() {
        return tranAllowedRepository;
    }

    @Autowired
    public void setTranAllowedRepository( TranAllowedRepository tranAllowedRepository ) {
        this.tranAllowedRepository = tranAllowedRepository;
    }


    public PBINRepository getPbinRepository() {
        return pbinRepository;
    }

    @Autowired
    public void setPbinRepository( PBINRepository pbinRepository ) {
        this.pbinRepository = pbinRepository;
    }

    public BankStyleRepository getBankStyleRepository() {
        return bankStyleRepository;
    }

    @Autowired
    public void setBankStyleRepository( BankStyleRepository bankStyleRepository ) {
        this.bankStyleRepository = bankStyleRepository;
    }

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    @Autowired
    public void setImageRepository( ImageRepository imageRepository ) {
        this.imageRepository = imageRepository;
    }

    public ScreenGroupRepository getScreenGroupRepository() {
        return screenGroupRepository;
    }

    @Autowired
    public void setScreenGroupRepository( ScreenGroupRepository screenGroupRepository ) {
        this.screenGroupRepository = screenGroupRepository;
    }

    public ATDUpTimeRepository getAtdUpTimeRepository() {
        return atdUpTimeRepository;
    }

    @Autowired
    public void setAtdUpTimeRepository( ATDUpTimeRepository atdUpTimeRepository ) {
        this.atdUpTimeRepository = atdUpTimeRepository;
    }

    public ATDRepositoryCompose getAtdRepositoryCompose() {
        return atdRepositoryCompose;
    }

    @Autowired
    public void setAtdRepositoryCompose( ATDRepositoryCompose atdRepositoryCompose ) {
        this.atdRepositoryCompose = atdRepositoryCompose;
    }
}

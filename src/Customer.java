import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rohitramesh on 3/4/17.
 */
public class Customer extends Event {
    enum Verb{
        NEW, UPDATE
    }
    Verb verb;

    String last_name;
    String adr_city;
    String adr_state;

    private Integer totalNumOfSiteVisits;


    double expendituresPerVisit;
    double totalExpenditures;
    //members which are calculated and not exposed to the outside world
    private double LTV;
    private double numOfSiteVisitsPerWeek;
    private double averageCustomerValPerWeek;

    int T = 10;  //Average Shutterfly life time

    Date customerFirstVisitDate;

    Customer(){

    }

    Customer(String key, String eventTime, String last_name, String adr_city, String adr_state){
        this.key = key;
        this.eventTime = eventTime;
        this.last_name = last_name;
        this.adr_city = adr_city;
        this.adr_state = adr_state;
        this.LTV = 0.0;  //set to 0 when customer is just created
        this.totalNumOfSiteVisits = 0;
        this.totalExpenditures = 0.0;
        this.setType(Type.CUSTOMER);

        try{
            customerFirstVisitDate = new SimpleDateFormat("yyyy-MM-dd").parse(eventTime);

        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public Verb getVerb() {
        return verb;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public Double getLTV() {
        return LTV;
    }

    public double getNumOfSiteVisitsPerWeek() {
        return numOfSiteVisitsPerWeek;
    }

    public Integer getTotalNumOfSiteVisits() {
        return totalNumOfSiteVisits;
    }

    public void updateCustomerForSiteVisit(){
        //this increment can be made thread safe using volatile or synchronous block;
        this.setTotalNumOfSiteVisits(this.getTotalNumOfSiteVisits()+1);
        this.calculateExpendituresPerVisit();
        this.calculateSiteVisitsPerWeek();
        this.calculateCustomerValPerWeek();
        this.calculateATV();

    }
    public void updateCustomerForOrder(Order order){
        this.setTotalExpenditures(this.totalExpenditures+order.getTotal_amount());
        this.calculateExpendituresPerVisit();
        this.calculateCustomerValPerWeek();
        this.calculateATV();
    }

    private void setTotalNumOfSiteVisits(Integer totalNumOfSiteVisits) {
        this.totalNumOfSiteVisits = totalNumOfSiteVisits;

    }

    private void setTotalExpenditures(Double totalExpenditures) {
        this.totalExpenditures = totalExpenditures;

    }

    public String getLast_name() {
        return last_name;
    }

    public String getAdr_state() {
        return adr_state;
    }

    public String getAdr_city() {
        return adr_city;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setAdr_state(String adr_state) {
        this.adr_state = adr_state;
    }

    public void setAdr_city(String adr_city) {
        this.adr_city = adr_city;
    }

    //A simple LTV can be calculated using the following equation:
    // `52(a) x t`. Where `a` is the average customer value per week (customer expenditures per visit (USD)
    // x number of site visits per week) and
    // `t` is the average customer lifespan. The average lifespan for Shutterfly is 10 years.


    void calculateSiteVisitsPerWeek(){
        //event time is guaranteed to be between first time customer is created and now
        DateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
        Date d = new Date();
        String curTime = dateFormat.format(d);
        Date curDate = null;
        try {
            curDate = new SimpleDateFormat("MM/dd/yyyy").parse(curTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long numOfDays = Event.getDaysBetweenDates(this.customerFirstVisitDate, curDate );

        int numOfWeeks=(int)(numOfDays/7);

        this.numOfSiteVisitsPerWeek = (double)this.totalNumOfSiteVisits / numOfWeeks;
    }

    void calculateExpendituresPerVisit(){
        this.expendituresPerVisit = this.totalExpenditures / this.totalNumOfSiteVisits;
    }

    private void calculateCustomerValPerWeek(){ // not exposed outside
        this.averageCustomerValPerWeek = this.expendituresPerVisit * this.numOfSiteVisitsPerWeek;
    }

    private void calculateATV(){   // not exposed outside
        double LTV;

        LTV = 52 * this.averageCustomerValPerWeek * this.T;
        this.LTV = LTV;
    }

    @Override
    public String toString(){
        String ret = "";
        ret += this.getKey() + " " + this.getLast_name();
        return ret;
    }
}

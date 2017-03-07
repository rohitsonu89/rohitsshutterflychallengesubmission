/**
 * Created by rohitramesh on 3/5/17.
 */
public class Order extends Event{
    enum Verb{
        NEW, UPDATE
    }
    String customer_id;
    double total_amount;
    Verb verb;

    Order(){

    }

    Order(String key, String eventTime, String customer_id, Double total_amount){
        this.key = key;  //cannot be changed
        this.eventTime = eventTime;
        this.customer_id = customer_id;
        this.total_amount = total_amount;
        this.setType(Type.ORDER);
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public Verb getVerb() {
        return verb;
    }
}

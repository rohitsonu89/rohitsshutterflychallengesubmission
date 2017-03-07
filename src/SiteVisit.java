import java.util.List;

/**
 * Created by rohitramesh on 3/5/17.
 */
public class SiteVisit extends Event {
    enum Verb{
        NEW
    }
    String customer_id;
    String tags;
    Verb verb;
    SiteVisit(){

    }
    SiteVisit(String key, String eventTime, String customer_id, String tags ){
        this.key = key;
        this.eventTime = eventTime;
        this.customer_id = customer_id;
        this.tags = tags;
        this.setType(Type.SITE_VISIT);
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getTags() {
        return tags;
    }


}

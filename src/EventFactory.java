import org.json.simple.JSONObject;

import java.util.List;

/**
 * Created by rohitramesh on 3/5/17.
 */
public class EventFactory {

    //Encapsulating event creation in the factory instead of client side.

    public static Event getEvent(JSONObject jsonObject, Data D){

        if(jsonObject.get("type") == null)
            return null;

        if(jsonObject.get("type").equals(Event.Type.CUSTOMER.toString())){
            if(jsonObject.get("verb").equals("NEW")){
                Customer customer = new Customer((String)jsonObject.get("key"), (String)jsonObject.get("event_time"),
                        (String)jsonObject.get("last_name"), (String)jsonObject.get("adr_city"),
                                (String)jsonObject.get("adr_state"));
                customer.setVerb(Customer.Verb.NEW);
                return customer;
            }
            else if(jsonObject.get("verb").equals("UPDATE"))   //get the customer object fom data
            {
                Customer customer = D.getCustomerHashMap().get((String)jsonObject.get("key")); //Get the existing customer and update
                customer.setAdr_city((String)jsonObject.get("adr_city"));
                customer.setLast_name((String)jsonObject.get("last_name"));
                customer.setAdr_state((String)jsonObject.get("adr_state"));
                customer.setVerb(Customer.Verb.UPDATE);
                return customer;
            }

        }
        else if(jsonObject.get("type").equals(Event.Type.SITE_VISIT.toString())){
            SiteVisit siteVisit = new SiteVisit((String)jsonObject.get("key"), (String)jsonObject.get("event_time"),
            (String)jsonObject.get("customer_id"), jsonObject.get("tags").toString());

            return siteVisit;
        }
        else if(jsonObject.get("type").equals(Event.Type.IMAGE.toString())){
             ImageUpload imageUpload = new ImageUpload((String)jsonObject.get("key"), (String)jsonObject.get("event_time"),
                     (String)jsonObject.get("customer_id"), (String)jsonObject.get("camera_make"),
                        (String)jsonObject.get("camera_model"));
             return imageUpload;
        }
        else if(jsonObject.get("type").equals(Event.Type.ORDER.toString())){
            Order order=null;
            if(jsonObject.get("verb").equals("NEW")){
                String total_amount = (String)jsonObject.get("total_amount");

                order = new Order((String)jsonObject.get("key"), (String)jsonObject.get("event_time"),
                        (String) jsonObject.get("customer_id"),Double.parseDouble(total_amount.split(" ")[0]));
                order.setVerb(Order.Verb.NEW);
            }
            else if(jsonObject.get("verb").equals("UPDATE")){
                order = D.getOrderHashMap().get((String)jsonObject.get("key"));  //Get the existing order and update
                order.setCustomer_id((String)jsonObject.get("customer_id"));
                order.setTotal_amount((Double)jsonObject.get("total_amount"));
                order.setEventTime((String)jsonObject.get("event_time"));
                order.setVerb(Order.Verb.UPDATE);

            }

            return order;
        }
        else
            throw new IllegalArgumentException("No such event");
        return null;
    }

}

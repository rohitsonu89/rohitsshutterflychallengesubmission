

import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;


/**
 * Created by rohitramesh on 3/4/17.
 */
public class EventAnalyzer {

    JSONArray readFile(){
        File file = new File("input/input.txt");
        JSONParser parser = new JSONParser();
        JSONArray jsonArray=null;
        try{
            FileReader fileReader = new FileReader(file);
            Object obj = parser.parse(fileReader);
            jsonArray = (JSONArray) obj;


        } catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray;

    }

    void writeOutput(List<Customer> topCustomers){
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            String content = "";
            for(Customer customer: topCustomers)
                content += customer.toString() + "\n";

            fw = new FileWriter("output/output.txt");
            bw = new BufferedWriter(fw);
            bw.write(content);


        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

    }




    public static void main(String[] args){
        EventAnalyzer eventAnalyzer = new EventAnalyzer();
        JSONArray jsonArray = eventAnalyzer.readFile();
        Iterator<JSONObject> iterator = jsonArray.iterator();
        Data D = new Data();

        // read events and create appropriate objects
        while (iterator.hasNext()){
            JSONObject cur =  iterator.next();
            Event e = EventFactory.getEvent(cur, D);
            eventAnalyzer.Ingest(e, D);

        }

        List<Customer> topSimpleLTVCustomers = eventAnalyzer.topSimpleLTVCustomers(2,D);
        eventAnalyzer.writeOutput(topSimpleLTVCustomers);
    }

    public void Ingest(Event E, Data D){

        if(E instanceof Customer){
            //calculate/update LTV initially 0
            Customer customer = (Customer)E;
            /*if(customer.getVerb() == Customer.Verb.UPDATE)
                D.updateCustomer(customer);*/ //no need to update as Max Heap wont be affected
            if(customer.getVerb() == Customer.Verb.NEW)
                D.addCustomer(customer);
        }
        else if(E instanceof SiteVisit){

            SiteVisit siteVisit = (SiteVisit)E;
            D.addSiteVisit(siteVisit);
            //update customer stats for the current Visit
            Customer visitingCustomer =  D.getCustomerHashMap().get(siteVisit.getCustomer_id());
            visitingCustomer.updateCustomerForSiteVisit();
            //update the customer in the Max Heap and the map
            D.updateCustomer(visitingCustomer);

        }
        else if(E instanceof Order){
            //update ATV, customer
            Order order = (Order)E ;
            if(D.getOrderHashMap().containsKey(order.getKey()))
                D.updateOrder(order);
            else
                D.addOrder(order);
            Customer orderedCustomer = D.getCustomerHashMap().get(order.getCustomer_id());
            orderedCustomer.updateCustomerForOrder(order);
            //update the customer in the Max Heap
            D.updateCustomer(orderedCustomer);
        }
        else if(E instanceof ImageUpload){
            D.addImageUpload((ImageUpload)E);
        }


    }


    public List<Customer> topSimpleLTVCustomers(int X, Data D){
        //will maintain a max heap of customers and extra top X elements from this data structure .
        // We are looking at a time complexity of  Xlog n for this method and n for creating/maintaining the heap

         // Another solution is to create a Minheap of size X on the fly in this method with a time
        // complexity of n , but it is slower than Xlogn with MaxHeap solution since we are only
        // extracting the top customers X times from the already maintained MaxHeap

        PriorityQueue<Customer> allCustomers  = D.getCustomers();

        int max = X;
        if(allCustomers.size()<X)     //if total number of customers less than X, then just extract all customers
            max = allCustomers.size();

        Customer[] topXCustomers = new Customer[max];

        for(int i=0;i<max;i++){
            topXCustomers[i] = D.getCustomers().poll();   // extract from the max heap  max number of times
        }
        return Arrays.asList(topXCustomers);
    }
}

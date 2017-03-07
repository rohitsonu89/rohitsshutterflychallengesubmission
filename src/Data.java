
import java.util.*;

/**
 * Created by rohitramesh on 3/4/17.
 */
public class Data {

    TreeSet<ImageUpload> imageUploads;
    TreeSet<Order> orders;
    TreeSet<SiteVisit> siteVisits;
    PriorityQueue<Customer> customers;

    HashMap<String, Customer> customerHashMap;
    HashMap<String, Order> orderHashMap;

    //Comparator to maintain the max heap property
    Comparator<Customer> comparator = new Comparator<Customer>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            if(o1.getLTV() < o2.getLTV()) {
                return 1;
            } else if(o1.getLTV() > o2.getLTV()) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    Data(){
        imageUploads = new TreeSet<>();
        orders = new TreeSet<>();
        siteVisits = new TreeSet<>();

        // maintaining a max heap of customers
        customers = new PriorityQueue<Customer>(comparator);
        //could be replaced with tree map depending on the frequency of updates
        customerHashMap = new HashMap<>();  //Look up customer by customer_id
        orderHashMap = new HashMap<>();  //Look up order by order_id

    }

    public HashMap<String, Customer> getCustomerHashMap() {
        return customerHashMap;
    }

    public HashMap<String, Order> getOrderHashMap() {
        return orderHashMap;
    }

    public void setCustomers(PriorityQueue<Customer> customers) {
        this.customers = customers;
    }
    public PriorityQueue<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer){
        //straightforward addition of the customer to the map and the heap
        this.customers.add(customer);
        this.customerHashMap.put(customer.getKey(),customer);
    }

    public void updateCustomer(Customer customer){
        //get current customer in the Max heap and delete it
        Customer curCustomer = customerHashMap.get(customer.getKey());
        this.customers.remove(curCustomer);  // note deletions in priority queue in Java takes O(n) . Would probably use

        //adding back the updated customer to the heap
        this.customers.add(customer);

        // the deletion and adding back the updated customer object is needed to maintain the maxheap property.
    }

    public void updateOrder(Order order){
        Order curOrder = this.getOrderHashMap().get(order.getKey());
        //get current order and delete from the tree set
        this.orders.remove(curOrder);

        //add back the updated order to the treeset
        this.getOrders().add(order);


    }

    public TreeSet<ImageUpload> getImageUploads() {
        return imageUploads;
    }

    public void setImageUploads(TreeSet<ImageUpload> imageUploads) {
        this.imageUploads = imageUploads;
    }

    public void addImageUpload(ImageUpload imageUpload){
        this.imageUploads.add(imageUpload);
    }

    public TreeSet<Order> getOrders() {
        return orders;
    }

    public void setOrders(TreeSet<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order){
        this.orders.add(order);
        this.getOrderHashMap().put(order.getKey(),order);
    }

    public TreeSet<SiteVisit> getSiteVisits() {
        return siteVisits;
    }

    public void setSiteVisits(TreeSet<SiteVisit> siteVisits) {
        this.siteVisits = siteVisits;
    }

    public void addSiteVisit(SiteVisit siteVisit){
        this.siteVisits.add(siteVisit);
    }
}

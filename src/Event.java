import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;


/**
 * Created by rohitramesh on 3/4/17.
 */

abstract class Event implements Comparable<Event> {

    public enum  Type{
        CUSTOMER, SITE_VISIT, IMAGE, ORDER ;
    }
    Type type;
    String key;
    String eventTime;
    Event(){

    }


    Event(Type type){
        this.type = type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getKey() {
        return key;
    }



    // Sort all events by timestamp in the tree set.
    @Override
    public int compareTo(Event other){
        Timestamp otherTimeStamp = getTimeStamp(other.getEventTime());
        Timestamp curTimeStamp = getTimeStamp(this.getEventTime());

        if(curTimeStamp.after(otherTimeStamp))
            return 1;
        else if(curTimeStamp.before(otherTimeStamp))
            return -1;
        else
            return 0;
    }

    // 2 events are equal if they have the same key and event time
    @Override
    public boolean equals(Object e){
        if (!(e instanceof Event)) {
            return false;
        }
        Event event = (Event) e;
        if(this.key.equals(event.key) && this.getEventTime().equals(event.getEventTime()))
            return true;
        return false;
    }
    //overriding hashcode since all our Event Datastructures are Tree Sets
    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + this.getEventTime().hashCode();
        result = 31 * result + this.key.hashCode();
        return result;
    }

    public static Timestamp getTimeStamp(String eventTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date parsedDate;
        Timestamp timestamp = null;
        try {
            parsedDate = dateFormat.parse(eventTime);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return timestamp;
    }

    public static Date getDate(String eventTime){
        Date eventDate = null;
        try {
            eventDate = new SimpleDateFormat("MM/dd/yyyy").parse(eventTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return eventDate;

    }

    public static long getDaysBetweenDates(Date D1, Date D2){
        long diff = D2.getTime() - D1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

}

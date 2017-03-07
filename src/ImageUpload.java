/**
 * Created by rohitramesh on 3/5/17.
 */
public class ImageUpload extends Event {
    enum Verb{
        UPLOAD
    }
    Verb verb;
    String customer_id;
    String camera_make;
    String camera_model;

    ImageUpload(){

    }

    ImageUpload(String key, String eventTime, String customer_id, String camera_make, String camera_model){
        this.key = key;
        this.eventTime = eventTime;
        this.customer_id = customer_id;
        this.camera_make = camera_make;
        this.camera_model = camera_model;
        this.setType(Type.IMAGE);
    }
}

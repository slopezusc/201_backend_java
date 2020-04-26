

import java.util.ArrayList;


public class Event {
    private String createdBy;
    private String description;
    private ArrayList<String> emails;
    private String endDate;
    private String startDate;
    private String location;
    private String eventName;
    private ArrayList<String> busyEvents;


    //getters
    public String getCreatedBy() { return this.createdBy; }
    public String getDescription() { return this.description; }
    public ArrayList<String> getEmails() { return this.emails; }
    public String getEndDate() { return this.endDate; }
    public String getStartDate() { return this.startDate; }
    public String getLocation() { return this.location; }
    public String getEventName() { return this.eventName; }
    public ArrayList<String> getBusyEvents() { return this.busyEvents; }
     

};

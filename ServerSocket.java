import java.io.IOException;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws")
public class ServerSocket {

    private static Vector<Session> sessionVector = new Vector<Session>();

    private validate(String message){

        //this will be a json message
        Gson gson = new Gson();
        Event event = gson.fromJson(message, Event.class);
        
        String location = event.getLocation();        
        String eventLength = event.getEventLength();
        String endDate = event.getEndDate();
        String description = event.getDescription();
        String startDate = event.getStartDate();
        String eventName = event.getEventName();

        //validate stuff here
            



         
    }

    @OnOpen
    public void open(Session session){
        System.out.println("Connection made"); 
        sessionVector.add(session);
    }


    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println(message);
        try {
            for(Session s : sessionVector){
                s.getBasicRemote().sendText(message); 
            } 
        } catch (IOException ioe){
            System.out.println("ioe: " + ioe.getMessage()); 
            close(session);
        } 
    }

    @OnClose
    public void close(Session session){
        System.out.println("Disconnecting!"); 
        sessionVector.remove(session);
    }
    
}

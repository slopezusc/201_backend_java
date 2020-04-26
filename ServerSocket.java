
import java.io.IOException;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.ArrayList;
import com.sun.mail.smtp.SMTPTransport;
import com.google.gson.Gson;

@ServerEndpoint(value = "/ws")
public class ServerSocket {

    private static Vector<javax.websocket.Session> sessionVector = new Vector<javax.websocket.Session>();


    private void sendEmail(ArrayList<String> emails) {
        //javax.mail.Session session = new javax.mail.Session();
        String username = "";
        String password = "";

        String senderEmail = "sebastpez@gmail.com";

        //init receiverEmail
        String receiverEmail = "";
        for(String email: emails) receiverEmail += email;

        String SMTP_SERVER = "smtp server ";

        String emailSubject = "Event Created!";
        String emailText = "Hello, your event has been created!";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", SMTP_SERVER);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "25");

        javax.mail.Session session = javax.mail.Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(senderEmail));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receiverEmail, false));

            msg.setSubject(emailSubject);
            msg.setText(emailText);
            msg.setSentDate(new Date());

            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            t.connect(SMTP_SERVER, username, password);

            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } 
    }

    private boolean validate(String message){

        //this will be a json message
        Gson gson = new Gson();
        Event event = gson.fromJson(message, Event.class);
        
        String createdBy = event.getCreatedBy();
        String location = event.getLocation();        
        //String eventLength = event.getEventLength();
        String description = event.getDescription();
        String eventName = event.getEventName();
        String startDate = event.getStartDate();
        String endDate = event.getEndDate();
        ArrayList<String> emails = event.getEmails();

        //validate startDate, endDate
        if(startDate.length() != 10 || endDate.length() != 10) return false;

        for(int i = 0; i < 10; ++i){
            boolean isDigit1 = (Character.isDigit(startDate.charAt(i)));
            boolean isDigit2 = (Character.isDigit(endDate.charAt(i)));
            boolean onDash = (i == 4 || i == 7);

            if( !(isDigit1 && isDigit2) && !onDash){
                //misformated 
                return false;
            }
        } 
        sendEmail(emails);
        return true;

    }

    @OnOpen
    public void open(javax.websocket.Session session){
        System.out.println("Connection made"); 
        sessionVector.add(session);
    }


    @OnMessage
    public void onMessage(String message, javax.websocket.Session session){
        System.out.println(message);
        try {
            for(javax.websocket.Session s : sessionVector){
                s.getBasicRemote().sendText(message); 
            } 
        } catch (IOException ioe){
            System.out.println("ioe: " + ioe.getMessage()); 
            close(session);
        } 
    }

    @OnClose
    public void close(javax.websocket.Session session){
        System.out.println("Disconnecting!"); 
        sessionVector.remove(session);
    }
    
}

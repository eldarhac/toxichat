package model_lib;
import type.MessageType;


public class AlertMessage extends TextMessage {
//    private boolean toxicMsg;
//    private String user;
//    private String message;

//    private void parseUsrMsg(String txt) {
//        String[] splt = txt.split(";", 2);
//        user = splt[0];
//        message = splt[1];
//    }

    public AlertMessage(String text) {
        super(text);
        setToxicMsg(false);
//        parseUsrMsg(text);
    }

    public AlertMessage() {
        super();
    }

    @Override
    public MessageType getType() {
        return MessageType.ALERT;
    }

    public boolean isToxicMsg() {return getToxicMessage();}

    public void setToxicMsg(boolean b) {super.setToxicMessage(b);}
//
//    public String getUser() {
//        return user;
//    }
//
//    public String getMessage() {
//        return message;
//    }
}

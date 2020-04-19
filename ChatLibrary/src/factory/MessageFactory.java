package factory;

import model_lib.AlertMessage;
import model_lib.ImageMessage;
import model_lib.QuitMessage;
import model_lib.TextMessage;
import type.IMessage;
import type.MessageType;

import java.util.ArrayList;
import java.util.List;

public class MessageFactory {

	public static List<IMessage> getMessages(){
		List<IMessage> list = new ArrayList<>();
		
		list.add(new ImageMessage(new ImageHandler()));
		list.add(new TextMessage());
        list.add(new QuitMessage());
        list.add(new AlertMessage());

        return list;
	}

	public static IMessage getMessage(String text) {
		
		List<IMessage> list = getMessages();
		
		for (IMessage msg : list){
			if (msg.IsType(text)){
				msg.setData(text);
				return msg;
			}
		}
		
		return null;
	}

	public static IMessage getMessage(MessageType messageType) {
		List<IMessage> list = getMessages();
		
		for (IMessage msg : list){
			if (msg.getType() == messageType){
				return msg;
			}
		}
		
		return null;
	}
}

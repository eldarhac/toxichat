package socket;

import factory.MessageFactory;
import listener.IReadSocketListener;
import main.Util;
import model_lib.AlertMessage;
import model_lib.BaseThread;
import type.*;

import java.io.IOException;
import java.net.Socket;

public class ReadSocketThread extends BaseThread implements IReadSocket {
    private final IReadSocketListener listener;
    private final ISocketProtocol protocol;
    private final ILogger logger;
    private boolean run;

    public ReadSocketThread(Socket socket, IReadSocketListener listener, ILogger logger, ISocketProtocol protocol) {
        super(socket, protocol);
        this.listener = listener;
        this.protocol = protocol;
        this.logger = logger;
        this.run = true;
    }

    @Override
    public void run() {
        try {
            IMessage msg = null;

            setUp();

            while (!socket.isClosed() && run) {

                byte[] data = fetch();

//                if (protocol.isHandShake(data)) {
//
//                    msg = MessageFactory.getMessage(protocol.getMessageType(data));
//
//                    if (msg != null) {
//                        msg.setSender(protocol.getSender(data));
////                        msg.setData(data);
//////                        msg.setData("ha!!! " + new String(data));
////                        if (msg.getType() == MessageType.QUIT) exitChat();
//                        if (msg.getType() == MessageType.ALERT) {
//                            String msg_data = new String(msg.getData());
//                        } else {
//                            listener.printToScreen(msg);
//                        }
//                        msg = null;
//                    }
//
//                } else {
//
//                    if (msg != null) {
//                        msg.setData(data);
//                        listener.printToScreen(msg);
//
//                        if (msg.getType() == MessageType.QUIT) exitChat();
//
//                        msg = null;
//
//                    }
//                }

                if (protocol.isHandShake(data)) {

                    msg = MessageFactory.getMessage(protocol.getMessageType(data));
                    boolean isTox = protocol.getToxicMsg(data).equals("1");
                    if (msg != null && msg.getType() != MessageType.ALERT) {
                        if(protocol.getSender(data).equals("server_sent_this"))
                            msg.setSender("");
                        else
                            msg.setSender(protocol.getSender(data));
                    }
                    else if(msg != null && msg.getType() == MessageType.ALERT)
                    {
                        msg.setToxicMessage(isTox);
                    }
//                        } else {
//                            listener.printToScreen(msg);
//                        }
                }
                else {

                    if (msg != null) {
                        msg.setData(data);
                        if (msg.getType() == MessageType.ALERT) {
                            AlertMessage alt_msg = (AlertMessage)msg;
                            String s = new String(msg.getData());
                            if (s.startsWith("chat_client"))
                            {
                                listener.getListener().getUser().setUsername(s);
                            }
//                            boolean isTox = protocol.getToxicMsg(data).equals("1");
                            else if(alt_msg.isToxicMsg())
                            {
                                listener.popThePopup(new String(msg.getData(), Util.getEncoding()));
//                                alt_msg.setToxicMessage(isTox);
                            }
                            else
                            {
                                listener.printMyMessage(msg);
                            }
                        }
                        else if (msg.getType() == MessageType.QUIT) exitChat();
                        else
                        {
                            listener.printToScreen(msg);
                        }
                        msg = null;
                    }
                }
            }

        } catch (IOException e) {
            try {
                exitChat();
            } catch (IOException ignored) {

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setUp() {
        // Get chat title
        try {
            byte[] data = fetch();
            String s = new java.lang.String(data, Util.getEncoding());
            String[] tu = s.split(",");
            listener.setChatTitle(tu[0]);
            listener.getListener().getUser().setUsername(tu[1]);
            listener.getListener().getUser().setNick(tu[1]);
            listener.onChatStarted();
        } catch (IOException e) {
            logger.logError(e);
        }
    }

    private void exitChat() throws IOException {
        end();
        listener.onChatExit();
    }

    @Override
    public void  end() {
        run = false;
    }
}
package model_lib;

import listener.IChatListener;
import type.IChat;
import type.ILogger;
import type.ISocketProtocol;

/**
 * Created by okori on 19-Apr-17.
 */
public interface IChatManager {
    IChat getActiveChat();

    void setActiveChat(IChat chat);

    void setActiveChat(int point);

    void removeChat(IChat activeChat);

    IChat getNewClientChat(ILogger logger, ISocketProtocol protocol, IChatListener listener);

    IChat getNewServerChat(ILogger logger, IChatListener listener, ISocketProtocol protocol);

    void addChat(IChat chat);

    IChat getChat(int guiId);

    boolean isCurrentChat(int hash);

    boolean chatExists(String port);

    IChat getChatByPort(int port);

    boolean belongsToActiveChat(int port);

    boolean IsChatAvailable();

    IChat getNextChat();
}

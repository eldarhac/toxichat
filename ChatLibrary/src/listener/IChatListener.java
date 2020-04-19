package listener;

import model_lib.User;
import type.IChat;
import type.IMessage;

import java.io.IOException;

/**
 * Created by okori on 06-Apr-17.
 */
public interface IChatListener {
    void printToScreen(IMessage msg, int port);

    void printMineToScreen(IMessage msg, int port);

    void showPopup(String text) throws IOException, InterruptedException;

    User getUser();

    void onChatStarted(IChat chat);

    void onServerClosed();
}

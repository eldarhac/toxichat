package listener;

import type.IMessage;

import java.io.IOException;

public interface IReadSocketListener {
    void printToScreen(IMessage msg);

    void printMyMessage(IMessage msg);

    void popThePopup(String text) throws IOException, InterruptedException;

    void onChatExit() throws IOException;

    void setChatTitle(String title);

    void onChatStarted();

    IChatListener getListener();
}

package listener;

import java.io.IOException;

/**
 * Created by okori on 06-Apr-17.
 */
public interface ChatListPanelListener {
    void joinChatRoom();

    void createChatRoom();

    void popup(String text, String my_name) throws IOException, InterruptedException;

}

package gui;

import factory.MessageFactory;
import listener.IGuiListener;
import main.Constant;
import main.Util;
import model_lib.AlertMessage;
import model_lib.ImageFilter;
import model_lib.ImageMessage;
import net.miginfocom.swing.MigLayout;
import type.ILogger;
import type.IMessage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel {
    private final IGuiListener listener;
    private final ILogger logger;
    private JTextArea msgBox;
    private JPanel msgWindow;
    private static IMessage last_msg;

    public ChatPanel(IGuiListener listener, ILogger logger) {
        this.listener = listener;
        this.logger = logger;
        this.setLayout(new MigLayout("insets 0 0 4 0"));

        this.add(addMenuBar(), "growx, pushx, wrap");
        this.add(addMessageWindow(), "push, grow, wrap");
        this.add(addMessageField(), "growx, pushx, split, wmin 10");
        this.add(addSendButton());
    }

    private JMenuBar addMenuBar() {
        JMenuBar bar = new ChatMenuBar(Constant.MENU_BG);

        // exit
//        JButton item = new ImageButton("Exit", "ic_power_settings_new_black_24dp.png");
//        item.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                listener.quitChat();
//            }
//        });
//        bar.add(item);

        return bar;
    }

    private JButton addSendButton() {
        JButton btn = new ImageButton("Attach a file", "ic_attachment_black_24dp.png");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JButton open = new JButton();
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
                        fileChooser.setDialogTitle("Attach an image");
                        fileChooser.addChoosableFileFilter(new ImageFilter());
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        if (fileChooser.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
                            msgBox.setText(fileChooser.getSelectedFile().getAbsolutePath());
                            setFocusToChatWindow();
                        }
                    }
                }).start();
            }
        });
        return btn;
    }

    private JScrollPane addMessageWindow() {
        msgWindow = new JPanel();       
        msgWindow.setLayout(new MigLayout("fillx", "[grow]"));
        msgWindow.setBackground(Constant.MSG_BG);
        msgWindow.setBorder(new CompoundBorder(msgWindow.getBorder(), new EmptyBorder(Constant.MAG_16, Constant.MAG_16, Constant.MAG_16, Constant.MAG_16)));

        JScrollPane scroll = new JScrollPane();
        scroll.getViewport().add(msgWindow);

        return scroll;
    }

    private JTextArea addMessageField() {
        msgBox = new JTextArea();
        msgBox.setLineWrap(true);       
        msgBox.setWrapStyleWord(true);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        msgBox.setBorder(border);

        msgBox.addKeyListener(new KeyListener() {
        	
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = msgBox.getText();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sendText(text);
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            } catch (UnsupportedEncodingException ex) {
                                ex.printStackTrace();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }).start();

                    clearText(text);
                    setFocusToChatWindow();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        return msgBox;
    }

    private void sendText(String text) throws IOException, InterruptedException {

        // this is where the message is being sent
        text = text.replaceAll("\n", "");
        final int YES = 0, NO = 1;
        if (text.isEmpty() || text.trim().isEmpty()) return;


//        if (Util.isToxic(text))
//        {
//            int ans = YES;
//            //popup
//            JFrame parent = new JFrame();
//
//            ans = JOptionPane.showConfirmDialog(parent, Constant.POPUP_MSG_PT1 + text + Constant.POPUP_MSG_PT2,
//                    "Warning",0);
//            if (ans == NO)
//            {
//                return;
//            }
//        }

        IMessage message = MessageFactory.getMessage(text.trim());

        if (message != null) {
            listener.sendMessage(message);
//                        if (listener.IsChatAvailable()) {
//                displayMessage(message, Constant.DOCK_EAST, Constant.USER_BG);
//            }
        }
    }

    private void clearText(String text) {
        text.trim();
    	msgBox.setText("");	
    	msgBox.setVisible(true);
    }

    public void applyMessage(IMessage message){
            if (listener.IsChatAvailable()) {
                displayMessage(message, Constant.DOCK_EAST, Constant.USER_BG);
            }

    }


    public void displayMyMessage(IMessage msg, String alignment, Color color) {
        msg.setSender("");
        JPanel panel = new JPanel(new MigLayout("fill", "[grow]", "[]"));
        panel.add(msg.getMessagePanel(color), alignment);
        panel.setOpaque(false);
        addListener(msg, panel);
        msgWindow.add(panel, "wrap, spanx, growx");

        msgWindow.revalidate();
    }


    public void displayMessage(IMessage msg, String alignment, Color color) {
        JPanel panel = new JPanel(new MigLayout("fill", "[grow]", "[]"));
        panel.add(msg.getMessagePanel(color), alignment);
        panel.setOpaque(false);
        addListener(msg, panel);
        msgWindow.add(panel, "wrap, spanx, growx");

        msgWindow.revalidate();
    }

    private void addListener(IMessage msg, Component ct) {
        if (msg instanceof ImageMessage) {
            ct.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            ct.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String path = new String(msg.getData(), Util.getEncoding());
                    new ImageDialog(path);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
    }

    public void displayPopup(String text, String its_me) throws IOException, InterruptedException {
        int ans = Constant.NO;
        //popup
        JFrame parent = new JFrame();

        ans = JOptionPane.showConfirmDialog(parent, Constant.POPUP_MSG_PT1 + text + Constant.POPUP_MSG_PT2,
                "Warning",0);
        if (ans == Constant.YES)
        {
            // what needs to happen here?!

            AlertMessage ret_msg = new AlertMessage(text);
            ret_msg.setSender(its_me);
            listener.sendMessage(ret_msg);
            displayMyMessage(ret_msg, Constant.DOCK_EAST, Constant.USER_BG);
//            sendText(text);
        }
    }

    public void setFocusToChatWindow() {
        msgBox.requestFocus();
    }

    public void clearMessageWindow() {
        msgWindow.removeAll();
        msgWindow.revalidate();
        msgWindow.repaint();
    }
}

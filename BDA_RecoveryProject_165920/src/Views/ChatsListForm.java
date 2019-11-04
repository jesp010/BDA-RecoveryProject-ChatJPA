package Views;

import BusinessObjects.Chat;
import BusinessObjects.User;
import BusinessObjects.UserChat;
import Control.ChatControl;
import Control.UserChatControl;
import Control.UserControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class ChatsListForm extends javax.swing.JFrame implements ActionListener {

    private final UserControl userControl;
    private final UserChatControl userChatControl;
    private final ChatControl chatControl;
    private User user = null;
    private ArrayList<User> activeChatUsers = null;
    private ArrayList<User> availableUsers = null;

    public ChatsListForm() {
        userControl = new UserControl();
        this.userChatControl = new UserChatControl();
        this.chatControl = new ChatControl();
        activeChatUsers = new ArrayList<>();
        availableUsers = new ArrayList<>();
        initComponents();
        setVisible(true);
    }

    public ChatsListForm(User user) {
        this.userControl = new UserControl();
        this.userChatControl = new UserChatControl();
        this.chatControl = new ChatControl();
        this.user = user;
        activeChatUsers = new ArrayList<>();
        availableUsers = new ArrayList<>();

        initComponents();
        setVisible(true);

        jButtonUpdate.addActionListener(this);
        jButtonUpdate.setActionCommand("Update");

        jListActiveChats.setModel(new DefaultListModel<>());
        jListAvailableUsers.setModel(new DefaultListModel<>());

        fillActiveChatsList();
        fillAvailableList();
        
        MouseListener mouseListenerActiveChats = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedChat = jListActiveChats.getSelectedValue().toString();
                    User clickedUser = userControl.findByUsername(selectedChat);

                    ArrayList<UserChat> userChat1 = userChatControl.findAllByUserID(user.getId());
                    ArrayList<UserChat> userChat2 = userChatControl.findAllByUserID(clickedUser.getId());

                    Chat chat = null;
                    for (UserChat uc1 : userChat1) {
                        Integer id = uc1.getChat().getId();
                        if (containsID(userChat2, id)) {
                            chat = chatControl.findByID(id);
                        }
                    }

                    if (chat != null) {
                        ChatForm cf = new ChatForm(chat);
                    }
                }
            }
        };
        jListActiveChats.addMouseListener(mouseListenerActiveChats);

        MouseListener mouseListenerAvailableChats = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedChat = (String) jListAvailableUsers.getSelectedValue().toString();

                    System.out.println(selectedChat);
                    User clickedUser = userControl.findByUsername(selectedChat);

                    ArrayList<UserChat> uc = new ArrayList<>();
                    Chat chat = new Chat(uc, new Date());
                    uc.add(new UserChat(user, chat));
                    uc.add(new UserChat(clickedUser, chat));

                    System.out.println("clicked user id: " + clickedUser.getId());
                    System.out.println("user id: " + user.getId());
                    //chat.setUsers(uc);
                    chatControl.save(chat);
                    ChatForm cf = new ChatForm(chat);
                    
                    refreshContentsAvailableList();
                }

            }
        };
        jListAvailableUsers.addMouseListener(mouseListenerAvailableChats);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();

        switch (action) {

            case "Update":
                UpdateForm uf = new UpdateForm();
                break;
        }
    }

    private boolean containsID(ArrayList<UserChat> uc, Integer id) {
        if (uc.size() > 0) {
            for (UserChat u : uc) {
                if (u.getChat().getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    private void fillAvailableList() {
        DefaultListModel<String> model = (DefaultListModel) jListAvailableUsers.getModel();
        model.clear();
        ArrayList<User> users = userControl.findAll(user);

        //remove activeChatUsers from availableUsers ArrayList
        if (users.size() > 0) {
            for (User u : users) {
                if (!activeUserContains(u)) {
                    availableUsers.add(u);
                }
            }
        }

        //fill list
        if (availableUsers.size() > 0) {
            for (User u : availableUsers) {
                model.addElement(u.getUserName());
            }
        }

    }
    
    private void refreshContentsAvailableList(){
         DefaultListModel<String> model =new DefaultListModel<>();
         ArrayList<User> users = userControl.findAll(user);
         
        //remove activeChatUsers from availableUsers ArrayList
        if (users.size() > 0) {
            for (User u : users) {
                if (!activeUserContains(u)) {
                    availableUsers.add(u);
                }
            }
        }
        
        //remove duplicates
                //remove duplicates of arraylist
        Set<User> av_users_temp = new HashSet<>();
        av_users_temp.addAll(availableUsers);
        availableUsers.clear();
        availableUsers.addAll(av_users_temp);

        //fill list
        if (availableUsers.size() > 0) {
            for (User u : availableUsers) {
                model.addElement(u.getUserName());
            }
        }
        
        jListAvailableUsers.setModel(model);
    }

    private boolean activeUserContains(User user) {
        if (activeChatUsers.size() > 0) {
            for (User u : activeChatUsers) {
                if (u.getId() == user.getId()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void fillActiveChatsList() {
        DefaultListModel<String> model = (DefaultListModel) jListActiveChats.getModel();
//        jListActiveChats.setModel(new DefaultListModel<String>());
//        ListModel model = jListActiveChats.getModel();
        model.clear();

        //Get users objs from active chats
        ArrayList<UserChat> usersChats = userChatControl.findAllByUserID(this.user.getId());
        System.out.println(usersChats.size());
        if (usersChats.size() > 0) {
            for (UserChat uc : usersChats) {
                if (uc.getUser().getId() != user.getId()) {
                    activeChatUsers.add(uc.getUser());
                }
            }
        }

        if (activeChatUsers.size() > 0) {
            for (User u : activeChatUsers) {
                model.addElement(u.getUserName());
            }
        }
        System.out.println("inside fillActiveChatsList");
        System.out.println(Arrays.toString(activeChatUsers.toArray()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        jScrollPaneActiveChats = new javax.swing.JScrollPane();
        jListActiveChats = new javax.swing.JList<>();
        jScrollPaneNewChats = new javax.swing.JScrollPane();
        jListAvailableUsers = new javax.swing.JList<>();
        jLabelActiveChats = new javax.swing.JLabel();
        jLabelNewChat = new javax.swing.JLabel();
        jButtonUpdate = new javax.swing.JButton();
        jLabelTitle = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Select Chat");
        setResizable(false);

        jListActiveChats.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jScrollPaneActiveChats.setViewportView(jListActiveChats);

        jListAvailableUsers.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jScrollPaneNewChats.setViewportView(jListAvailableUsers);

        jLabelActiveChats.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelActiveChats.setText("Active Chats");

        jLabelNewChat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelNewChat.setText("New Chat");

        jButtonUpdate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonUpdate.setText("Update User");

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitle.setText("Welcome User!");

        javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
        rootPanel.setLayout(rootPanelLayout);
        rootPanelLayout.setHorizontalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPaneActiveChats, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabelActiveChats)))
                        .addGap(18, 18, 18)
                        .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelNewChat)
                            .addComponent(jScrollPaneNewChats, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rootPanelLayout.setVerticalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelActiveChats)
                    .addComponent(jLabelNewChat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPaneNewChats)
                    .addComponent(jScrollPaneActiveChats, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatsListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatsListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatsListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatsListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatsListForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JLabel jLabelActiveChats;
    private javax.swing.JLabel jLabelNewChat;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JList<String> jListActiveChats;
    private javax.swing.JList<String> jListAvailableUsers;
    private javax.swing.JScrollPane jScrollPaneActiveChats;
    private javax.swing.JScrollPane jScrollPaneNewChats;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel rootPanel;
    // End of variables declaration//GEN-END:variables
}

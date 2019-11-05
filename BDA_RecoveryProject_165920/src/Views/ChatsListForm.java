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
import java.util.Date;
import javax.swing.DefaultListModel;

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

        jButtonRefresh.addActionListener(this);
        jButtonRefresh.setActionCommand("Refresh");

        jListActiveChats.setModel(new DefaultListModel<>());
        jListAvailableUsers.setModel(new DefaultListModel<>());

        refreshUsersArrayLists();
        refreshListsContents();

        MouseListener mouseListenerActiveChats = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedChat = jListActiveChats.getSelectedValue();
                    User clickedUser = userControl.findByUsername(selectedChat);

                    ArrayList<UserChat> userChatSender = userChatControl.findAllByUserID(user.getId());
                    ArrayList<UserChat> userChatReceiver = userChatControl.findAllByUserID(clickedUser.getId());

                    Chat chat = null;
                    for (UserChat uc1 : userChatSender) {
                        Integer id = uc1.getChat().getId();
                        if (containsID(userChatReceiver, id)) {
                            chat = chatControl.findByID(id);
                        }
                    }

                    refreshUsersArrayLists();
                    refreshListsContents();

                    if (chat != null) {
                        ChatForm cf = new ChatForm(chat, user, clickedUser);
                    }
                }
            }
        };
        jListActiveChats.addMouseListener(mouseListenerActiveChats);

        //Event listener for availableChats list 
        MouseListener mouseListenerAvailableChats = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //Selected username to chat
                    String selectedChat = (String) jListAvailableUsers.getSelectedValue();
                    //Get user of selected list item
                    User clickedUser = userControl.findByUsername(selectedChat);

                    //Create new chat and add UserChat objs
                    ArrayList<UserChat> uc = new ArrayList<>();
                    Chat chat = new Chat(uc, new Date());
                    uc.add(new UserChat(user, chat));
                    uc.add(new UserChat(clickedUser, chat));

                    //save chat and launch chatForm
                    chatControl.save(chat);

                    refreshUsersArrayLists();
                    refreshListsContents();

                    ChatForm cf = new ChatForm(chat, user, clickedUser);

                }

            }
        };
        jListAvailableUsers.addMouseListener(mouseListenerAvailableChats);

        jLabelTitle.setText("Welcome " + this.user.getUserName() + "!!");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();

        switch (action) {

            case "Update":
                UpdateForm uf = new UpdateForm(this.user);
                break;
            case "Refresh":
                refreshUsersArrayLists();
                refreshListsContents();
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

    private void refreshListsContents() {
        //Refresh ActiveChatUsers JList model
        DefaultListModel activeChatUsersModel = new DefaultListModel();

        //Check if ArrayList isn't empty
        if (activeChatUsers.size() > 0) {
            //Loop through activeChatUsers ArrayList and append every index to the model
            for (User u : activeChatUsers) {
                activeChatUsersModel.addElement(u.getUserName());
            }
        }
        jListActiveChats.setModel(activeChatUsersModel);

        DefaultListModel availableChatUsersModel = new DefaultListModel();
        if (availableUsers.size() > 0) {
            for (User u : availableUsers) {
                availableChatUsersModel.addElement(u.getUserName());
            }
        }
        jListAvailableUsers.setModel(availableChatUsersModel);
    }

    //refresh activeChatUsers and availableUsers ArrayLists
    //From all users list remove all the active ones
    //The rest will be available to chat
    private void refreshUsersArrayLists() {

        //Get all users except current user
        ArrayList<User> users = userControl.findAll(user);
        //Clear active user chats list
        activeChatUsers.clear();

        //Clear available users
        availableUsers.clear();

        //append all users to available users
//        availableUsers.addAll(users);
        //how to check if this.user have a chat with users?
        //Loop through all users and search for the relationship
        //After a new active user, ArrayLists have to refresh and then the lists refresh based on ArrayLists contents
        //Get UserChats from User active chats
        ArrayList<UserChat> usersChats = userChatControl.findAllByUserID(this.user.getId());

        //Get all the UserChats with the same Chat.id as this.user and get the User obj of 
        //users with an active chat with this.user and add them to activeUsersChat
        if (usersChats.size() > 0) {
            //loop all
            for (UserChat uc : usersChats) {
                User u = uc.getUser();
                //If found user with an active chat, add it to activeChatUsers ArrayList
                if (u.getId() != this.user.getId()) {
                    activeChatUsers.add(u);
                }
            }
        }

        //remove activeChatUsers from availableUsers ArrayList
        if (users.size() > 0) {
            for (User u : users) {
                if (!activeUserContains(u)) {
                    availableUsers.add(u);
                }
            }
        }
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
        jButtonRefresh = new javax.swing.JButton();

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

        jButtonRefresh.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonRefresh.setText("Refresh");

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
                            .addComponent(jScrollPaneNewChats, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(rootPanelLayout.createSequentialGroup()
                                .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        rootPanelLayout.setVerticalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRefresh))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRefresh;
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

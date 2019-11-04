package Control;

import BusinessObjects.UserChat;
import DataAccess.UsersChatsDAO;
import java.util.ArrayList;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class UserChatControl {

    UsersChatsDAO usersChatsDAO;

    public UserChatControl() {
        usersChatsDAO = new UsersChatsDAO();
    }

    public UserChat findByID(Integer id) {
        return usersChatsDAO.findByID(id);
    }
    
    public ArrayList<UserChat> findAllByUserID(Integer id) {
        return usersChatsDAO.findAllByUserID(id);
    }

    public ArrayList<UserChat> findAll() {
        return usersChatsDAO.findAll();
    }

    public void save(UserChat userChat) {
        usersChatsDAO.save(userChat);
    }

    public boolean update(UserChat userChat) {
        return usersChatsDAO.update(userChat);
    }

    public boolean delete(Integer id) {
        return usersChatsDAO.delete(id);
    }
}

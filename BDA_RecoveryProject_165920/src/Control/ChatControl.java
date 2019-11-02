package Control;

import BusinessObjects.Chat;
import DataAccess.ChatsDAO;
import java.util.ArrayList;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class ChatControl {

    ChatsDAO chatsDAO;

    public ChatControl() {
        chatsDAO = new ChatsDAO();
    }

    public Chat findByID(Integer id) {
        return chatsDAO.findByID(id);
    }

    public ArrayList<Chat> findAll() {
        return chatsDAO.findAll();
    }

    public void save(Chat chat) {
        chatsDAO.save(chat);
    }

    public boolean update(Chat chat) {
        return chatsDAO.update(chat);
    }

    public boolean delete(Integer id) {
        return chatsDAO.delete(id);
    }
}

package Control;

import BusinessObjects.Message;
import DataAccess.MessagesDAO;
import java.util.ArrayList;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class MessageControl {

    MessagesDAO messagesDAO;

    public MessageControl() {
        messagesDAO = new MessagesDAO();
    }

    public Message findByID(Integer id) {
        return messagesDAO.findByID(id);
    }

    public ArrayList<Message> findAll() {
        return messagesDAO.findAll();
    }
    
    public ArrayList<Message> findAllByChatID(Integer chatID) {
        return messagesDAO.findAllByChatID(chatID);
    }

    public void save(Message message) {
        messagesDAO.save(message);
    }

    public boolean update(Message message) {
        return messagesDAO.update(message);
    }

    public boolean delete(Integer id) {
        return messagesDAO.delete(id);
    }
}

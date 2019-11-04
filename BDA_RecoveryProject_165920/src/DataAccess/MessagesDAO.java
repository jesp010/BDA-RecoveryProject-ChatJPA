package DataAccess;

import BusinessObjects.Message;
import java.util.ArrayList;
import java.util.Collections;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class MessagesDAO {

    private EntityManagerFactory factory = null;
    private EntityManager em = null;

    public MessagesDAO() {
        factory = Persistence.createEntityManagerFactory(BaseDAO.PU_JUATSAPP);
        em = factory.createEntityManager();
    }

    public Message findByID(Integer id) {
        // get length of id (Can't be > 11
        int length = Integer.toString(id).length();
        if (length <= 11) {
            em.getTransaction().begin();
            Message message = em.find(Message.class, id);
            em.getTransaction().commit();

            if (message != null) {
                return message;
            }
        }
        return null;
    }

    public ArrayList<Message> findAll() {
        em.getTransaction().begin();
        //Creates the query constructor
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        //Builds the object
        cq.select(cq.from(Message.class));
        //Creates the query ready to execute
        Query q = em.createQuery(cq);
        //Execute the query and stores the result in an ArrayList
        ArrayList<Message> messages = new ArrayList<>(q.getResultList());
        //Transaction ends
        em.getTransaction().commit();
        //If suppliers isn't null it is returned otherwise the method returns null
        if (messages != null) {
            return messages;
        }
        return null;
    }

    public ArrayList<Message> findAllByChatID(Integer chatID) {
        ArrayList<Message> allMessages = findAll();
        ArrayList<Message> chatMessages = new ArrayList<>();
        
        //Check if ArrayList isn't empty
        if (allMessages.size() > 0) {
            //Add to chatMessages all messages with matching Chat ID
            for (Message m : allMessages) {
                if (chatID == m.getChat().getId()) {
                    chatMessages.add(m);
                }
            }
            Collections.sort(chatMessages);
        }

        return chatMessages;
    }

    public ArrayList<Message> findAllChatMessages(Integer chatID) {
        em.getTransaction().begin();
        //Creates the query constructor
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        //Builds the object
        cq.select(cq.from(Message.class));
        //Creates the query ready to execute
        Query q = em.createQuery(cq);
        //Execute the query and stores the result in an ArrayList
        ArrayList<Message> messages = new ArrayList<>(q.getResultList());

        ArrayList<Message> chatMessages = new ArrayList<>();
        for (Message m : messages) {
            if (chatID == m.getChat().getId()) {
                chatMessages.add(m);
            }
        }

        //Transaction ends
        em.getTransaction().commit();

        return chatMessages;
    }

    public void save(Message message) {
        em.getTransaction().begin();
        em.persist(message);
        em.getTransaction().commit();
    }

    public boolean update(Message message) {
        if (message != null) {
            em.getTransaction().begin();

            Message message_tmp = em.find(Message.class, message.getId());
            if (message_tmp != null) {
                message_tmp.setChat(message.getChat());
                message_tmp.setDate(message.getDate());
                message_tmp.setMessage(message.getMessage());
                message_tmp.setUser(message.getUser());
                em.persist(message_tmp);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().commit();
            }
        }
        return false;
    }

    public boolean delete(Integer id) {
        em.getTransaction().begin();
        Message message = em.find(Message.class, id);

        if (message != null) {
            try {
                em.remove(message);
                em.getTransaction().commit();

            } catch (javax.persistence.RollbackException ex) {
                ex.printStackTrace();
                return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        } else {
            em.getTransaction().commit();
            return false;
        }
    }
}

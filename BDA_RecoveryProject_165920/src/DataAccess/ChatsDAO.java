package DataAccess;

import BusinessObjects.Chat;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class ChatsDAO {

    private EntityManagerFactory factory = null;
    private EntityManager em = null;

    public ChatsDAO() {
        factory = Persistence.createEntityManagerFactory(BaseDAO.PU_JUATSAPP);
        em = factory.createEntityManager();
    }

    public Chat findByID(Integer id) {
        // get length of id (Can't be > 11
        int length = Integer.toString(id).length();
        if (length <= 11) {
            em.getTransaction().begin();
            Chat chat = em.find(Chat.class, id);
            em.getTransaction().commit();

            if (chat != null) {
                return chat;
            }
        }
        return null;
    }
    
    
    public ArrayList<Chat> findAll() {
        em.getTransaction().begin();
        //Creates the query constructor
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        //Builds the object
        cq.select(cq.from(Chat.class));
        //Creates the query ready to execute
        Query q = em.createQuery(cq);
        //Execute the query and stores the result in an ArrayList
        ArrayList<Chat> chats = new ArrayList<>(q.getResultList());
        //Transaction ends
        em.getTransaction().commit();
        //If suppliers isn't null it is returned otherwise the method returns null
        if (chats != null) {
            return chats;
        }
        return null;
    }

    public void save(Chat chat) {
        em.getTransaction().begin();
        em.persist(chat);
        em.getTransaction().commit();
    }

    public boolean update(Chat chat) {
        if (chat != null) {
            em.getTransaction().begin();

            Chat chat_tmp = em.find(Chat.class, chat.getId());
            if (chat_tmp != null) {
                chat_tmp.setUsers(chat.getUsers());
                chat_tmp.setCreationDate(chat.getCreationDate());
                em.persist(chat_tmp);
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
        Chat chat = em.find(Chat.class, id);

        if (chat != null) {
            try {
                em.remove(chat);
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

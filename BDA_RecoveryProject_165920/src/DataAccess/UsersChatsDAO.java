package DataAccess;

import BusinessObjects.UserChat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class UsersChatsDAO {

    private EntityManagerFactory factory = null;
    private EntityManager em = null;

    public UsersChatsDAO() {
        factory = Persistence.createEntityManagerFactory(BaseDAO.PU_JUATSAPP);
        em = factory.createEntityManager();
    }

    public UserChat findByID(Integer id) {
        // get length of id (Can't be > 11
        int length = Integer.toString(id).length();
        if (length <= 11) {
            em.getTransaction().begin();
            UserChat uc_tmp = em.find(UserChat.class, id);
            em.getTransaction().commit();

            if (uc_tmp != null) {
                return uc_tmp;
            }
        }
        return null;
    }

    public ArrayList<UserChat> findAllByUserID(Integer id) {
        ArrayList<UserChat> uc_tmp = findAll();
        ArrayList<UserChat> uc = new ArrayList<>();

        //get all userchats with provided id
        for (UserChat u : uc_tmp) {
            Integer uID = u.getUser().getId();
            if (uID == id) {
                uc.add(u);
            }
        }

        //get all chats ids
        ArrayList<Integer> chatIDs = new ArrayList<>();
        for (UserChat u : uc_tmp) {
            for (UserChat u2 : uc) {
                if (u.getChat().getId() == u2.getChat().getId()) {
                    chatIDs.add(u.getChat().getId());
                }
            }
        }

        //remove duplicates of arraylist
        Set<Integer> chat_ids = new HashSet<>();
        chat_ids.addAll(chatIDs);
        chatIDs.clear();
        chatIDs.addAll(chat_ids);

        //add all userchats with same chat id
        ArrayList<UserChat> uc2 = new ArrayList<>();
        for (UserChat u : uc_tmp) {
            for (Integer i : chatIDs) {
                if (u.getChat().getId() == i) {
                    uc2.add(u);
                }
            }
        }
        return uc2;
    }

    public ArrayList<UserChat> findAll() {
        em.getTransaction().begin();
        //Creates the query constructor
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        //Builds the object
        cq.select(cq.from(UserChat.class));
        //Creates the query ready to execute
        Query q = em.createQuery(cq);
        //Execute the query and stores the result in an ArrayList
        ArrayList<UserChat> usersChats = new ArrayList<>(q.getResultList());
        //Transaction ends
        em.getTransaction().commit();
        //If suppliers isn't null it is returned otherwise the method returns null
        if (usersChats != null) {
            return usersChats;
        }
        return null;
    }

    public void save(UserChat userChat) {
        em.getTransaction().begin();
        em.persist(userChat);
        em.getTransaction().commit();
    }

    public boolean update(UserChat userChat) {
        if (userChat != null) {
            em.getTransaction().begin();

            UserChat userChat_tmp = em.find(UserChat.class, userChat.getId());
            if (userChat_tmp != null) {
                userChat_tmp.setChat(userChat.getChat());
                userChat_tmp.setUser(userChat.getUser());
                em.persist(userChat_tmp);
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
        UserChat userChat = em.find(UserChat.class, id);

        if (userChat != null) {
            try {
                em.remove(userChat);
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

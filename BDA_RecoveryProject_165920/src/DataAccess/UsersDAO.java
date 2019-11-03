package DataAccess;

import BusinessObjects.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class UsersDAO {
    private EntityManagerFactory factory = null;
    private EntityManager em = null;

    public UsersDAO() {
        factory = Persistence.createEntityManagerFactory(BaseDAO.PU_JUATSAPP);
        em = factory.createEntityManager();
    }
    
    public User findByID(Integer id) {
        // get length of id (Can't be > 11
        int length = Integer.toString(id).length();
        if (length <= 11) {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            em.getTransaction().commit();

            if (user != null) {
                return user;
            }
        }
        return null;
    }
    
    public User findByEmail(String email){
        em.getTransaction().begin();
        String jpqlQuery = "SELECT u FROM User u  WHERE u.email = :email";
        TypedQuery<User> query = em.createQuery(jpqlQuery, User.class);
        query.setParameter("email", email);
        List<User> user = query.getResultList();
        em.getTransaction().commit();
        
        if(user.size() > 0) return user.get(0);
        else return null;
    }

    public ArrayList<User> findAll() {
        em.getTransaction().begin();
        //Creates the query constructor
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        //Builds the object
        cq.select(cq.from(User.class));
        //Creates the query ready to execute
        Query q = em.createQuery(cq);
        //Execute the query and stores the result in an ArrayList
        ArrayList<User> users = new ArrayList<>(q.getResultList());
        //Transaction ends
        em.getTransaction().commit();
        //If suppliers isn't null it is returned otherwise the method returns null
        if (users != null) {
            return users;
        }
        return null;
    }

    public void save(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public boolean update(User user) {
        if (user != null) {
            em.getTransaction().begin();

            User user_tmp = em.find(User.class, user.getId());
            if (user_tmp != null) {
                user_tmp.setBirthDate(user.getBirthDate());
                user_tmp.setChats(user.getChats());
                user_tmp.setCreationDate(user.getCreationDate());
                user_tmp.setEmail(user.getEmail());
                user_tmp.setMessages(user_tmp.getMessages());
                user_tmp.setSex(user.getSex());
                user_tmp.setUserName(user.getUserName());
                user_tmp.setPassword(user.getPassword());
                em.persist(user_tmp);
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
        User user = em.find(User.class, id);

        if (user != null) {
            try {
                em.remove(user);
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

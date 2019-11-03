package Testing;

import BusinessObjects.*;
import DataAccess.BaseDAO;
import DataAccess.UsersDAO;
import Enums.Sex;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class MappingTest {

    public static void main(String[] args) {
        EntityManagerFactory factory = null;
        EntityManager em = null;

        factory = Persistence.createEntityManagerFactory(BaseDAO.PU_JUATSAPP);
        em = factory.createEntityManager();

        ArrayList<Object> objects = new ArrayList<>();
        User u1 = new User("juanenriqqweue1", "123123qwe", "juaan@gail.com", new Date(1997 - 1900, 7, 11), new Date(), Sex.MALE, null, null);
        User u2 = new User("dianagabriqwe1ela", "123123qwe", "diaana@gmil.com", new Date(1996 - 1900, 1, 16), new Date(), Sex.FEMALE, null, null);
        objects.add(u1);
        objects.add(u2);

        Chat c1 = new Chat(null, new Date());
        objects.add(c1);

        Message m1 = new Message(new Date(), "mensaje 1XD", u1, c1);
        Message m2 = new Message(new Date(), "mensaje2 XDXD", u2, c1);
        objects.add(m1);
        objects.add(m2);

//        ArrayList<UserChat> chats_u1 = new ArrayList<>();
//        chats_u1.add(new UserChat(u1, c1));
//        
//        ArrayList<UserChat> chats_u2 = new ArrayList<>();
//        chats_u2.add(new UserChat(u2, c1));
//        
//        u1.setChats(chats_u1);
//        u2.setChats(chats_u2);
        ArrayList<Message> messages_u1 = new ArrayList<>();
        messages_u1.add(m1);
        u1.setMessages(messages_u1);

        ArrayList<Message> messages_u2 = new ArrayList<>();
        messages_u2.add(m2);
        u2.setMessages(messages_u2);

        ArrayList<UserChat> usersChat = new ArrayList<>();
        usersChat.add(new UserChat(u1, c1));
        usersChat.add(new UserChat(u2, c1));
        c1.setUsers(usersChat);

//        String jpqlQuery = "SELECT u FROM User u  WHERE u.email = :email";
//        TypedQuery<User> query = em.createQuery(jpqlQuery, User.class);
//        query.setParameter("email", "juan@gmail.com");
//        List<User> user = query.getResultList();
//        
//        for (User u : user) {
//            System.out.println(u.toString());
//        }

        UsersDAO udao = new UsersDAO();
        System.out.println(udao.findByEmail("juan@gmail.com").toString());
        
        em.getTransaction().begin();
        for (Object o : objects) {
            em.persist(o);
        }
        em.getTransaction().commit();
    }
}

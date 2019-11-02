package Testing;

import BusinessObjects.*;
import DataAccess.BaseDAO;
import Enums.Sex;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MappingTest {

    public static void main(String[] args) {
        EntityManagerFactory factory = null;
        EntityManager em = null;
        
        factory = Persistence.createEntityManagerFactory(BaseDAO.PU_JUATSAPP);
        em = factory.createEntityManager();

        ArrayList<Object> objects = new ArrayList<>();
        User u1 = new User("juan enrique", "juan@gmail.com", new Date(1997 - 1900, 7, 11), new Date(), Sex.MALE, null, null);
        User u2 = new User("diana gabriela", "diana@gmail.com", new Date(1996 - 1900, 1, 16), new Date(), Sex.FEMALE, null, null);
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
        
        em.getTransaction().begin();
        for(Object o: objects){
            em.persist(o);
        }
        em.getTransaction().commit();
    }
}

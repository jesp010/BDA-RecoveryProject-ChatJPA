package Control;

import BusinessObjects.User;
import DataAccess.UsersDAO;
import java.util.ArrayList;

/**
 * @author Juan Enrique Solis Perla
 * @ID: 165920 Advanced Databases Class, ISW, ITSON
 */
public class UserControl {

    UsersDAO usersDAO;

    public UserControl() {
        usersDAO = new UsersDAO();
    }

    public User findByID(Integer id) {
        return usersDAO.findByID(id);
    }
    
    public User findByEmail(String email){
        return usersDAO.findByEmail(email);
    }
    
    public User findByUsername(String username) {
        return usersDAO.findByUsername(username);
    }

    public ArrayList<User> findAll() {
        return usersDAO.findAll();
    }
    
    public ArrayList<User> findAll(User user) {
        return usersDAO.findAll(user);
    }
        
    public void save(User user) {
        usersDAO.save(user);
    }

    public boolean update(User user) {
        return usersDAO.update(user);
    }

    public boolean delete(Integer id) {
        return usersDAO.delete(id);
    }
}
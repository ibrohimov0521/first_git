package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.User;
import uz.shop.role.Role;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService implements BaseService<User> {
    ObjectMapper mapper = new ObjectMapper();
    File file = new File("src/main/resources/users.json");
    private List<User> users;

    @SneakyThrows
    public UserService() {
        users = new ArrayList<>();


        User admin = new User();
        admin.setUserName("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);
        add(admin);
    }

    @Override
    public User findById(UUID id) {

        for (User user : users) {
            if ( user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @SneakyThrows
    @Override
    public boolean add(User user) {

        for (User user1 : users) {
            if (user1.getUserName().equals(user.getUserName())) {
                return false;
            }
        }
        users.add(user);
        FileUtil.write(file,users);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(User user, UUID id) {

        for (User u : users) {
            if (u.getId().equals(id)) {
                u.setUserName(user.getUserName());
                u.setPassword(user.getPassword());
                u.setRole(user.getRole());
                FileUtil.write(file,users);
                return true;
            }
        }
        return false;
    }



    public User logIn (String username, String password) {

        for (User user : users) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
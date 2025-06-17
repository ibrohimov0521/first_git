package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.User;
import uz.shop.role.Role;

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
        rewrite();

        User admin = new User();
        admin.setUserName("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);
        add(admin);
    }

    @Override
    public User findById(UUID id) {
        rewrite();
        for (User user : users) {
            if ( user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        rewrite();
        return users;
    }

    @SneakyThrows
    @Override
    public boolean add(User user) {
        rewrite();
        for (User user1 : users) {
            if (user1.getUserName().equals(user.getUserName())) {
                return false;
            }
        }
        users.add(user);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(User user, UUID id) {
        rewrite();
        for (User u : users) {
            if (u.getId().equals(id)) {
                u.setUserName(user.getUserName());
                u.setPassword(user.getPassword());
                u.setRole(user.getRole());
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void rewrite() {
        if (file.length() == 0) {
            this.users = new ArrayList<>();
            return;
        }

        this.users = mapper.readValue(file, new TypeReference<>() {
        });
    }

    public User logIn (String username, String password) {
        rewrite();
        for (User user : users) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}

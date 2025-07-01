package uz.shop.service;

import lombok.SneakyThrows;
import uz.shop.model.User;
import uz.shop.role.Role;
import uz.shop.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService implements BaseService<User> {
    File file = new File("src/main/resources/users.json");
    private List<User> users;

    @SneakyThrows
    public UserService() {
        users = new ArrayList<>();
        users = FileUtil.read(file, User.class);

        User admin = new User();
        admin.setUserName("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);
        add(admin);
    }

    @Override
    public User findById(UUID id) {

        User currentUser = users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);

        if (currentUser != null) {
            return currentUser;
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

        boolean isUser = users.stream().anyMatch(u -> u.getUserName().equals(user.getUserName()));

        if (!isUser) {
            return false;
        }

        users.add(user);
        FileUtil.write(file, users);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(User user, UUID id) {
        User u = findById(id);

        if (u != null) {
            u.setUserName(user.getUserName());
            u.setPassword(user.getPassword());
            u.setRole(user.getRole());
            u.setUpdatedDate(user.getCreatedDate());
            FileUtil.write(file, users);
            return true;
        }

        return false;
    }


    public User logIn(String username, String password) {

        User nowUser = users.stream().filter(user -> user.getUserName().equals(username) && user.getPassword().equals(password)).findFirst().orElse(null);

        if (nowUser != null) {
            return nowUser;
        }

        return null;
    }
}
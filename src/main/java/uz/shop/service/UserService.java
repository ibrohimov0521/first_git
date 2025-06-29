package uz.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import uz.shop.model.User;
import uz.shop.role.Role;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService implements BaseService<User> {
    ObjectMapper mapper = new ObjectMapper();
    File path = new File("src/main/resources/users.json");
    private List<User> users = new ArrayList<>();

    @SneakyThrows
    public UserService() {

        readFromFile();

        User admin = new User();
        admin.setPhoneNumber("998");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);
        add(admin);
    }

    @Override
    public User getById(UUID id) {
        readFromFile();
        return users.stream()
                .filter(user -> user.isActive() && user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        readFromFile();
        return users;
    }

    @SneakyThrows
    @Override
    public boolean add(User user) {
        readFromFile();
        boolean exists = users.stream()
                .anyMatch(user1 -> user1.isActive() && user1.getPhoneNumber().equals(user.getPhoneNumber()));
        if (exists) {
            System.out.println("This user already exists");
            return false;
        }
        users.add(user);
        System.out.println("Registered succesfully!");
        saveToFile();
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(User user, UUID id) {
        readFromFile();
        Optional<User> optionalUser = users.stream()
                .filter(user1 -> user1.isActive() && user1.getId().equals(id))
                .findFirst();
        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            u.setPhoneNumber(user.getPhoneNumber());
            u.setPassword(user.getPassword());
            u.setRole(user.getRole());
            saveToFile();
            System.out.println("Updated succesfully!");
            return true;
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void readFromFile() {
        if (!path.exists() || path.length() == 0) {
            Files.writeString(path.toPath(), "[]");
        }
        users = mapper.readValue(path, new TypeReference<List<User>>() {
        });
    }

    @SneakyThrows
    @Override
    public void saveToFile() {
        mapper.writerWithDefaultPrettyPrinter().writeValue(path, users);
    }

    public User logIn(String phone, String password) {
        readFromFile();
        return users.stream()
                .filter(user -> user.getPhoneNumber().equals(phone) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}

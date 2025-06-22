package uz.shop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.shop.role.Role;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends Base {
    private String userName;
    private String password;
    private Role role;

    public User() {
        super();
        this.role = Role.USER;
    }
}

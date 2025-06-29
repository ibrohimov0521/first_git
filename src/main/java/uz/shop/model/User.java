package uz.shop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.shop.role.Role;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseModel {
    private String fullName;
    private String password;
    private Role role;
    private String phoneNumber;
    public User() {
        super();
        this.role = Role.USER;
    }

}

package application.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class UserRole {

    @Id
    @NotNull(message = "Укажите ldap сотрудника")
    private String ldap;
    @NotNull(message = "Укажите роль сотрудника")
    private String role;

    public UserRole(){

    }

    public String getLdap() {
        return ldap;
    }

    public void setLdap(String ldap) {
        this.ldap = ldap;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }




}

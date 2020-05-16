package application.services;

import application.entities.Role;
import application.entities.UserRole;
import application.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRoleService(){}

    // получение роли сотрудника
    public UserRole getUserRole(String ldap){
        Optional<UserRole> u = userRoleRepository.findById(ldap);
        return u.orElse(null);

    }

    // список доступных ролей
    public List<String> getAvailableRoles(){
        List<String> roles = new ArrayList<>();
        for(Role role: Role.values()){
            roles.add(role.getValue());
        }

        return roles;
    }

}

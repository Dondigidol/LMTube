package application.services;

import application.entities.Role;
import application.entities.User;
import application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){}

    // получение роли сотрудника
    public User getUser(String username){
        Optional<User> u = userRepository.findById(username);
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

    public void updateUser(User user){
        User userRole = new User();
        userRole.setUsername(user.getUsername());
        userRole.setFullName(user.getFullName());
        userRole.setRole(user.getRole());
        userRepository.save(userRole);
    }

}
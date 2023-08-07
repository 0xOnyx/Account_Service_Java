package account.database;

import account.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        createRoles();
    }

    private void createRoles() {
        try{
            if (roleRepository.count() > 0)
                return;
            roleRepository.save(new Role("ADMINISTRATOR", "role administrator"));
            roleRepository.save(new Role("USER", "role user"));
            roleRepository.save(new Role("ACCOUNTANT", "role accountant"));
            roleRepository.save(new Role("AUDITOR", "role auditor"));
        }
        catch(Exception e){
            System.out.println("Error creating roles: " + e.getMessage());
        }
    }

}

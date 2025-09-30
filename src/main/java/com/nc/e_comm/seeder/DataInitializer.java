package com.nc.e_comm.seeder;

import com.nc.e_comm.model.Role;
import com.nc.e_comm.model.User;
import com.nc.e_comm.repository.RoleRepository;
import com.nc.e_comm.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        roleRepository.findByRoleName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));
        roleRepository.findByRoleName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        if (userRepository.findByEmail("nitishnc123@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setName("Administrator");
            admin.setEmail("nitishnc123@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin-change-me"));
            admin.getRoles().add(roleRepository.findByRoleName("ROLE_ADMIN").get());
            userRepository.save(admin);
        }
    }
}

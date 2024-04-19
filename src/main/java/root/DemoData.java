package root;

import root.entity.User;
import root.enums.RoleEnum;
import root.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class DemoData implements ApplicationRunner {

    private final UserRepo userRepo;

    // Insert demo data
    @Override
    public void run(ApplicationArguments args) {
        if (!userRepo.existsByName("Admin")) {
            try {
                userRepo.save(
                    User.builder()
                        .name("Admin")
                        .username("admin")
                        .password(new BCryptPasswordEncoder().encode("123123"))
                        .email("syhien85@hotmail.com")
                        .roles(List.of(RoleEnum.ADMIN))
                        .build()
                );

                log.warn("Created username: admin, role: ADMIN");
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
    }
}
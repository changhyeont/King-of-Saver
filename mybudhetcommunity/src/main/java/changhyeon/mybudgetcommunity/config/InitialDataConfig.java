package changhyeon.mybudgetcommunity.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import changhyeon.mybudgetcommunity.entity.User;
import changhyeon.mybudgetcommunity.repository.UserRepository;

@Configuration
public class InitialDataConfig {

    @Bean
    public CommandLineRunner initializeData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 테스트용 계정이 없는 경우에만 생성
            if (userRepository.findByEmail("test@test.com").isEmpty()) {
                User testUser = User.builder()
                    .email("test@test.com")
                    .password(passwordEncoder.encode("test1234"))
                    .build();
                userRepository.save(testUser);
                
                System.out.println("테스트 계정이 생성되었습니다.");
                System.out.println("Email: test@test.com");
            }
        };
    }
}

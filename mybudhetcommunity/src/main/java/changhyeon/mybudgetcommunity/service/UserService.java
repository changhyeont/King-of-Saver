package changhyeon.mybudgetcommunity.service;

import changhyeon.mybudgetcommunity.dto.UserDto;
import changhyeon.mybudgetcommunity.dto.TokenDto;
import changhyeon.mybudgetcommunity.entity.User;
import changhyeon.mybudgetcommunity.exception.CustomException;
import changhyeon.mybudgetcommunity.exception.ErrorCode;
import changhyeon.mybudgetcommunity.repository.UserRepository;
import changhyeon.mybudgetcommunity.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;  // JWT 토큰 생성을 위해 필요
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public UserDto register(UserDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        return UserDto.from(userRepository.save(user));
    }

    public TokenDto login(UserDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // JWT 토큰 생성
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String token) {
        // Bearer 토큰에서 실제 JWT 추출
        String jwt = token.substring(7);
        
        // 토큰 유효성 검증
        if (!jwtProvider.validateToken(jwt)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        
        // Redis에 해당 토큰을 블랙리스트로 등록
        // 토큰의 남은 유효시간 계산
        Long expiration = jwtProvider.getExpirationTime(jwt);
        redisTemplate.opsForValue()
            .set(jwt, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }
}
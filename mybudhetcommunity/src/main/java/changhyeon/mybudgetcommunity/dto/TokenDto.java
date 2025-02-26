package changhyeon.mybudgetcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    
    // 선택적으로 토큰 만료 시간 추가
    private Long accessTokenExpiresIn;
}
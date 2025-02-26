package changhyeon.mybudgetcommunity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;
    
    public ApiResponse(T data) {
        this.data = data;
        this.message = "success";
    }
    
    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }
}

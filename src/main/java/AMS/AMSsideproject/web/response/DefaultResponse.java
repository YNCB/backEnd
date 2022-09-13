package AMS.AMSsideproject.web.response;

import lombok.Data;

@Data
public class DefaultResponse<T> {

    private String status;
    private String message;
    private T data;

    public DefaultResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

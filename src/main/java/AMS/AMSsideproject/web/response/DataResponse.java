package AMS.AMSsideproject.web.response;

import lombok.Data;

@Data
public class DataResponse<T> extends BaseResponse {
    private T data;

    public DataResponse(String status, String message, T data) {
        super(status,message);
        this.data=data;
    }
}

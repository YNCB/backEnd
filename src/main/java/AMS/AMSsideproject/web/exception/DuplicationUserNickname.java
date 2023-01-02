package AMS.AMSsideproject.web.exception;

import lombok.Data;

@Data
public class DuplicationUserNickname extends RuntimeException{

    private String nickname;

    public DuplicationUserNickname() {
        super();
    }

    public DuplicationUserNickname(String message, String nickname){
        super(message);
        this.nickname = nickname;
    }
}

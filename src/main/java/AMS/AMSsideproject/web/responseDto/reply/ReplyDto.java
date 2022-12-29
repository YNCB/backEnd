package AMS.AMSsideproject.web.responseDto.reply;

import lombok.Data;

@Data
public class ReplyDto {

    private String title;
    private String content;

    public ReplyDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

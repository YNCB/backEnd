package AMS.AMSsideproject.web.responseDto.reply;

import AMS.AMSsideproject.domain.reply.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RepliesDto {

    private Long reply_id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime redate;

    private List<RepliesDto> children = new ArrayList<>();

    static public RepliesDto createRepliesDto(Reply reply) {
        RepliesDto repliesDto = new RepliesDto();
        repliesDto.setReply_id(reply.getReply_id());
        repliesDto.setNickname(reply.getUser().getNickname());
        repliesDto.setTitle(reply.getTitle());
        repliesDto.setContent(reply.getContent());
        repliesDto.setRedate(reply.getRedate());

        return repliesDto;
    }
}

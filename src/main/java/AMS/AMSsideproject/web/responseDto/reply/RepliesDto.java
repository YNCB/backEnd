package AMS.AMSsideproject.web.responseDto.reply;

import AMS.AMSsideproject.domain.reply.Reply;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class RepliesDto {

    @ApiModelProperty(example = "1")
    private Long reply_id;
    @ApiModelProperty(example = "홍길동")
    private String nickname;
    @ApiModelProperty(example = "test")
    private String title;
    @ApiModelProperty(example = "test")
    private String content;
    @ApiModelProperty(example = "2022-2-2")
    private String redate;

    private List<RepliesDto> children = new ArrayList<>();

    static public RepliesDto createRepliesDto(Reply reply) {
        RepliesDto repliesDto = new RepliesDto();
        repliesDto.setReply_id(reply.getReply_id());
        repliesDto.setNickname(reply.getUser().getNickname());
        repliesDto.setTitle(reply.getTitle());
        repliesDto.setContent(reply.getContent());
        repliesDto.setRedate(reply.getRedate().format(DateTimeFormatter.ISO_LOCAL_DATE));

        return repliesDto;
    }
}

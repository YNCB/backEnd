package AMS.AMSsideproject.web.apiController.reply;

import AMS.AMSsideproject.domain.reply.Reply;
import AMS.AMSsideproject.domain.reply.service.ReplyService;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplyEditForm;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplySaveForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.reply.ReplyDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codebox")
@Api(tags = "댓글 관련 api")
public class ReplyController {

    private final ReplyService replyService;
    private final JwtProvider jwtProvider;

    //댓글 저장 - 로그인 사용자만 가능
    @PostMapping("/{nickname}/{postId}/saveReply")
    public BaseResponse storeReply(@PathVariable("nickname") String nickname,
                                   @PathVariable("postId") Long postId ,
                                   @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken,
                                   @RequestBody ReplySaveForm replySaveForm) {

        Long userIdToToken = jwtProvider.getUserIdToToken(accessToken);
        Reply saveReply = replyService.addReply(postId, userIdToToken, replySaveForm);

        return new BaseResponse("200", "댓글이 저장되었습니다.");
    }

    //댓글 삭제
    @DeleteMapping("/{nickname}/{postId}/{replyId}")
    public BaseResponse removeReply(@PathVariable("nickname") String nickname,
                                    @PathVariable("postId") Long postId,
                                    @PathVariable("replyId") Long replyId,
                                    @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        replyService.deleteReply(replyId, userId);

        return new BaseResponse("200", "댓글이 삭제되었습니다.");
    }

    //댓글 수정폼
    @GetMapping("/{nickname}/{postId}/{replyId}")
    public DataResponse editReplyForm(@PathVariable("nickname") String nickname,
                                      @PathVariable("postId") Long postId,
                                      @PathVariable("replyId") Long replyId,
                                      @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        Reply findReply = replyService.findReplyByReplyId(replyId);
        ReplyDto replyDto = new ReplyDto(findReply.getTitle(), findReply.getContent());

        return new DataResponse("200", "댓글 수정 항목입니다.", replyDto);
    }

    //댓글 수정
    @PostMapping("/{nickname}/{postId}/{replyId}")
    public BaseResponse editReply(@PathVariable("nickname") String nickname,
                                  @PathVariable("postId") Long postId,
                                  @PathVariable("replyId") Long replyId,
                                  @RequestBody ReplyEditForm replyEditForm,
                                  @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        replyService.updateReply(userId, replyId, replyEditForm);
        return  new BaseResponse("200", "댓글이 수정되었습니다");
    }

}

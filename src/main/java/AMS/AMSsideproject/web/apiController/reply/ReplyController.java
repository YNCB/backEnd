package AMS.AMSsideproject.web.apiController.reply;

import AMS.AMSsideproject.domain.reply.Reply;
import AMS.AMSsideproject.domain.reply.service.ReplyService;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplyEditForm;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplySaveForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.reply.ReplyDto;
import AMS.AMSsideproject.web.swagger.userController.Join_406;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codebox")
@Api(tags = "댓글 관련 api")
public class ReplyController {

    private final ReplyService replyService;
    private final JwtProvider jwtProvider;

    //댓글 저장 - 로그인 사용자만 가능
    @PostMapping("/{nickname}/{postId}/reply/add")
    @ApiOperation(value = "댓글 저장 api", notes = "댓글을 저장합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=400, message = "잘못된 요청", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    })
    public BaseResponse storeReply(@PathVariable("nickname") String nickname,
                                   @PathVariable("postId") Long postId ,
                                   @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken,
                                   @Validated @RequestBody ReplySaveForm replySaveForm) {

        Long userIdToToken = jwtProvider.getUserIdToToken(accessToken);
        Reply saveReply = replyService.addReply(postId, userIdToToken, replySaveForm);

        return new BaseResponse("200", "댓글이 저장되었습니다.");
    }

    //댓글 삭제
    @DeleteMapping("/{nickname}/{postId}/reply/{replyId}")
    @ApiOperation(value = "댓글 삭제 api", notes = "댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=400, message = "잘못된 요청", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=403, message = "권한이 없음", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
            @ApiImplicitParam(name = "replyId", value = "댓글 아이디", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    })
    public BaseResponse removeReply(@PathVariable("nickname") String nickname,
                                    @PathVariable("postId") Long postId,
                                    @PathVariable("replyId") Long replyId,
                                    @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        replyService.deleteReply(postId, replyId, userId);

        return new BaseResponse("200", "댓글이 삭제되었습니다.");
    }

    //댓글 수정폼
    @GetMapping("/{nickname}/{postId}/reply/{replyId}")
    @ApiOperation(value = "댓글 수정 폼 api", notes = "댓글 수정 항목을 보여줍니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
            @ApiImplicitParam(name = "replyId", value = "댓글 아이디", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    })
    public DataResponse editReplyForm(@PathVariable("nickname") String nickname,
                                      @PathVariable("postId") Long postId,
                                      @PathVariable("replyId") Long replyId,
                                      @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        Reply findReply = replyService.findReplyByReplyId(replyId);
        ReplyDto replyDto = new ReplyDto(findReply.getTitle(), findReply.getContent());

        return new DataResponse("200", "댓글 수정 항목입니다.", replyDto);
    }

    //댓글 수정
    @PutMapping("/{nickname}/{postId}/reply/{replyId}")
    @ApiOperation(value = "댓글 수정 api", notes = "댓글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=403, message = "권한이 없음", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
            @ApiImplicitParam(name = "replyId", value = "댓글 아이디", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    })
    public BaseResponse editReply(@PathVariable("nickname") String nickname,
                                  @PathVariable("postId") Long postId,
                                  @PathVariable("replyId") Long replyId,
                                  @Validated @RequestBody ReplyEditForm replyEditForm,
                                  @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        replyService.updateReply(userId, replyId, replyEditForm);

        return  new BaseResponse("200", "댓글이 수정되었습니다");
    }

}

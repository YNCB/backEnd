package AMS.AMSsideproject.web.apiController.like;

import AMS.AMSsideproject.domain.like.repository.query.LikeDtoRepository;
import AMS.AMSsideproject.domain.like.service.LikeService;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.like.LikeDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "좋아요 관련 api")
@RequestMapping("/codebox")
public class LikeController {

    private final JwtProvider jwtProvider;
    private final LikeService likeService;
    private final LikeDtoRepository likeDtoRepository;

    //게시물 좋아요 ,삭제
    @PostMapping("/{nickname}/{postId}/like")
    @ApiOperation(value = "게시물 좋아요 api", notes = "게시물 좋아요 추가, 삭제를 합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true),
            @ApiImplicitParam(name = "nickname", value = "유저 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 Id", required = true)
    })
    public DataResponse<LikeDto> like(@PathVariable("nickname")String nickname,
                                      @PathVariable("postId")Long postId,
                                      @RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken ) {

        Long findUserId = jwtProvider.getUserId(accessToken);

        //LikeDto likeDto = likeService.like(postId, findUserId);
        LikeDto likeDto = likeService.like_v2(postId, findUserId);

        return new DataResponse("200", "좋아요 추가 또는 삭제가 완료되었습니다.", likeDto);
    }

    //게시물 좋아요 리스트 보기
    @GetMapping("/{nickname}/{postId}/like")
    @ApiOperation(value = "게시물 좋아요 리스트 api", notes = "게시물 리스트를 보여줍니다. 리스트의 닉네임을 통해 회원페이지로 이동가능합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true),
            @ApiImplicitParam(name = "nickname", value = "유저 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 Id", required = true)
    })
    public DataResponse likeList(@PathVariable("nickname") String nickname,
                                 @PathVariable("postId") Long postId,
                                 @RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken) {

        //성능을 위해서 DTO 바로 변환 사용 - 쿼리한번
        List<String> likes = likeDtoRepository.findLikes(postId);
        return new DataResponse("200", "게시물 좋아요 닉네임 리스트 입니다.", likes);
    }

}

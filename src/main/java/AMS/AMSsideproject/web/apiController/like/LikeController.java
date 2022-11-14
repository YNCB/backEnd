package AMS.AMSsideproject.web.apiController.like;

import AMS.AMSsideproject.domain.like.repository.query.LikeDtoRepository;
import AMS.AMSsideproject.domain.like.service.LikeService;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.like.LikeDto;
import AMS.AMSsideproject.web.responseDto.like.LikesDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    /**
     * 1. api 한개 두고 클릭하면 좋아요누른건지 , 취고한건지 판별
     * 2. 특정 유저가 좋아요 누른 게시물들어가면 게시물에 좋아요 버튼 활성화되어있어야된다.
     *    ->이거는 JWT토큰으로 user 정보 빼서 "like"테이블에서 "post_id", "user정보" 를 가지고 눌렸는지 판별해서 response에 여부 추가해주기
     * 3. api를 "/nickname/postId"로 그냥 post가 낫지않나?!
     */
    //게시물 좋아요 ,삭제
    //좋아요는 로그인 해야지 가능
    @PostMapping("/{nickname}/{postId}/like")
    @ApiOperation(value = "게시물 좋아요 api", notes = "게시물 좋아요 추가, 삭제를 합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<LikeDto> like(@PathVariable("nickname")String nickname, @PathVariable("postId")Long postId,
                                          @RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken ) {

        Long findUserId = jwtProvider.getUserIdToToken(accessToken);
        LikeDto likeDto = likeService.like(postId, findUserId);

        /**
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * 근데 여기서 게시물에 좋아요를 눌렸을때 게시물 좋아요 상태만 리턴하면 되나?!!! 해당 게시물 속성도 다시 리턴해줘야되는거 아니야?!!!!!
         */
        return new DataResponse("200", "좋아요 추가 또는 삭제가 완료되었습니다.", likeDto);
    }


    //게시물 좋아요 리스트 보기
    /**
     * 일단은 좋아요 리스트 보는거는 로그인하지 않아도 가능하게 구현.
     */
    @GetMapping("/{nickname}/{postId}/like")
    public DataResponse likeList(@PathVariable("nickname") String nickname, @PathVariable("postId") Long postId) {
        /**
         * 1. 프록시 초기화 하는거는 쿼리문 2개?!
         * 2. 그냥 바로 sql문으로 쿼리문 1개?!
         */
        List<LikesDto> likes = likeDtoRepository.findLikes(postId);
        return new DataResponse("200", "게시물 좋아요 닉네임 리스트 입니다.", likes);
    }

}

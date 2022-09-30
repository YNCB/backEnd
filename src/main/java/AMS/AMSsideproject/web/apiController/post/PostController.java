package AMS.AMSsideproject.web.apiController.post;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutAllUser;
import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.response.post.PostListResponse;
import AMS.AMSsideproject.web.responseDto.post.PostListDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "게시물 관련 api")
@RequestMapping("/codebox")
public class PostController {

    private final PostService postService;
    private final JwtProvider jwtProvider;

    //메인 페이지 (모든 사용자들의 게시물)
    // - 비 로그인 사용자도 여기는 접속할수 있어야함!!!!
    // - 근데 로그인 사용자랑, 비로그인 사용자는 엑세스 토큰 유무가 차이가 있는데 해당 uri에 인증을 안걸면 비로그인 사용자도 접속이 가능한데
    //   그렇게 되면 로그인 사용자에 대해서 해당 uri에서는 accessToken 기한만료등을 검사할수 없는뎅?...움....
    @GetMapping("/")
    @ApiOperation(value = "서비스 메인페이지 , 모든 회원에 대한 게시물 리스트 조회 api", notes = "모든 회원 게시물에 대해서 필터링 조건에 맞게 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostListResponse> mainPage(@RequestBody SearchFormAboutAllUser form) {
        Slice<Post> result = postService.findAboutAllUserPost(form);

        //Dto 변환
        List<PostListDto> findPostDtos = result.getContent().stream()
                .map(p -> new PostListDto(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, result.getNumberOfElements(), result.hasNext());
        return new DataResponse<>("200", "모든 사용자들의 게시물 리스트 결과입니다.", postListResponse);
    }

    //특정 사용자 메인 페이지
    @GetMapping("/{nickname}")
    @ApiOperation(value = "회원별 메인페이지 api", notes = "해당 회원의 문제 리스트을 보여줍니다. 개발 중...")
    public PostListResponse userPage(@PathVariable("nickname")String nickname ) {

        return null;
    }

    // 게시물 상세조회
    @ApiOperation(value = "회원 게시물 상세조회 api", notes = "개발중 ...")
    @GetMapping("/{nickname}/{postId}")
    public PostListResponse post(@PathVariable("nickname") String nickname, @PathVariable("title") String title) {
        return null;
    }


    //- !게시물 작성 api 호출전에 -> 인터셉터에서 한번더 걸려줘야된다!!! (다른사용자가 다른사람닉네임으로 해당 uri를 호출해서 데이터를 보낼수 있으니)
    //- 추가로 "problem_uri"가 String 으로 될까??!!! -> DB는 TEXT 형인데 TEXT 자료형 크기만큼 어떻게 받게 하지??!
    //- 또한 "context" 부분이 "마크업"으로 되야된다.!!! -> DB는 TEXT 형인데 TEXT 자료형 크기만큼 어떻게 받게 하지??!
    //- "PostSaveForm" validated 적용하기
    //0 "406" error 정의
    @ApiOperation(value = "게시물 작성 api", notes = "게시물을 작성하는 api 입니다.")
    @PostMapping("/{nickname}/write")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public BaseResponse writePost(@RequestHeader(JwtProperties.HEADER_STRING)String accessToken,
                                      @RequestBody PostSaveForm form) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        Post savaPost = postService.registration(userId, form);
        return new BaseResponse("200", "게시물이 저장되었습니다.");
    }

    //게시물 수정

}

package AMS.AMSsideproject.web.apiController.post;

import AMS.AMSsideproject.domain.like.service.LikeService;
import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.query.PostRepositoryQueryDto;
import AMS.AMSsideproject.domain.post.service.PostServiceImplV2;
import AMS.AMSsideproject.web.apiController.post.requestForm.*;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.Auth;
import AMS.AMSsideproject.web.custom.annotation.PostAuthor;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.response.post.PostListResponse;
import AMS.AMSsideproject.web.responseDto.post.*;
import AMS.AMSsideproject.web.swagger.postController.UserPage_200;
import AMS.AMSsideproject.web.swagger.userController.Join_406;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "게시물 관련 api")
@RequestMapping("/codebox")
public class PostControllerV2 {

    private final PostServiceImplV2 postService;

    private final PostRepositoryQueryDto postRepositoryQueryDto; //성능 튜닝한 repository
    private final JwtProvider jwtProvider;
    private final LikeService likeService;

    //메인 페이지
    @PostMapping(value = "/")
    @ApiOperation(value = "서비스 메인페이지 api", notes = "모든 회원 게시물들에 대해서 필터링 조건에 맞게 게시물들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = false)
    })
    public DataResponse<PostListResponse> mainPage(@Validated @RequestBody PostSearchFormAboutAllUser form,
                                                   @RequestHeader(value = JwtProperties.ACCESS_HEADER_STRING, required = false) String accessToken) {

        Slice<Post> result = postService.findPostsAboutAllUser(form);

        //Dto 변환
        List<PostListDtoAboutAllUser> findPostDtos = result.getContent().stream()
                .map(p -> new PostListDtoAboutAllUser(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, result.getNumberOfElements(), result.hasNext());
        return new DataResponse<>("200", "모든 사용자들의 게시물 리스트입니다.", postListResponse);
    }

    //특정 사용자 페이지(게스트 페이지, 마이 페이지)
    @PostMapping(value = "/{nickname}")
    @Auth
    @ApiOperation(value = "사용자 페이지 api", notes = "특정 회원 게시물들에 대해서 필터링 조건에 맞게 게시물들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출", response = UserPage_200.class),
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = false)
    })
    public DataResponse<PostListResponse> userPage(@PathVariable("nickname")String nickname,
                                                   @RequestHeader(value = JwtProperties.ACCESS_HEADER_STRING, required = false)String accessToken,
                                                   @Validated @RequestBody PostSearchFormAboutSpecificUser form){

        Slice<Post> posts = null;
        //게스트페이지
        if(!StringUtils.hasText(accessToken)){
            posts = postService.findPostsAboutGuestPage(nickname, form);
        }else {
            //마이 페이지
            if(myPageCheck(accessToken, nickname))
                posts = postService.findPostsAboutMyPage(nickname, form);
            //게스트 페이지
            else
                posts = postService.findPostsAboutGuestPage(nickname, form);
        }

        //Dto 변환
        List<PostListDtoAboutSpecificUser> postsDto = posts.getContent().stream()
                .map(p -> new PostListDtoAboutSpecificUser(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(postsDto, posts.getNumberOfElements(), posts.hasNext());
        return new DataResponse<>("200", "사용자의 게시물 리스트 입니다.",postListResponse);
    }

    // 게시물 상세페이지
    @GetMapping(value = "/{nickname}/{postId}")
    @ApiOperation(value = "게시물 상세페이지 api", notes = "게시물의 상세 정보를 보여줍니다.")
    @Auth
    @ApiResponses({
            @ApiResponse(code=200, message = "정상 호출.추가로 읽은 게시물 id를 포함시킨 쿠키 발급"),
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = false),
            @ApiImplicitParam(name = "postView", value = "게시물 중복 조회 증가 방지 쿠키", required = false, paramType = "cookie")
    })
    public DataResponse<PostDto> postPage(      @PathVariable("postId") Long postId,
                                                @PathVariable("nickname") String nickname,
                                                @RequestHeader(value = JwtProperties.ACCESS_HEADER_STRING, required = false) String accessToken,
                                                @ApiIgnore @CookieValue(value = "postView" ,required = false) Cookie postViewCookie,
                                                HttpServletResponse response) {

        //게시물 상세 정보 검생
        PostDto findPostDto = postService.findPost(postId);

        //로그인한 사용자 일경우 추가 정보 적용-토큰 검증은 인터셉터에서 진행
        if(StringUtils.hasText(accessToken)) {
            Long findUserId = jwtProvider.getUserId(accessToken);

            //좋아요 활성화 판단
            Boolean existing = likeService.checkExisting(postId, findUserId);
            findPostDto.setLikeExisting(existing);

            //조회순 증가 검증
            Cookie newCookie = postService.readPost(postId, postViewCookie);
            response.addCookie(newCookie);
        }

        return new DataResponse<>("200", "문제 상제 조회 결과입니다.", findPostDto);
    }


    //게시물 작성 -> JWT 토큰에 있는 userId 로 게시물을 저장시킨다.
    /**
     * "context" 부분이 "마크업"으로 되야된다.!!!
     */
    @ApiOperation(value = "게시물 작성 api", notes = "게시물을 작성하는 api 입니다.")
    @PostMapping("/write")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING,value = "엑세스 토큰", required = true)
    public BaseResponse writePost(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken,
                                  @Validated @RequestBody PostSaveForm form) {

        Long userId = jwtProvider.getUserId(accessToken);

        //태그 하나하나 쿼리문이 발생함 존재 유무를 확인하기 위해
        Post savaPost = postService.registration(userId, form);

        return new BaseResponse("200", "게시물이 저장되었습니다.");
    }

    //게시물 수정 form
    //이어지는 고민인 "게시물 PK"를 path 에 두는게 좋은건가!??
    @GetMapping("/{nickname}/{postId}/edit")
    @PostAuthor //추가 권한 검사 대상
    @ApiOperation(value = "게시물 수정 api", notes = "게시물에서 수정가능한 항목들을 보여줍니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    })
    public DataResponse<PostEditDto> edit(@PathVariable("nickname")String nickname,
                                          @PathVariable("postId")Long postId,
                                          @RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken) {

        //1. post 조회 쿼리 1번
        //2. post tags 프록시 초기화 -> Post_tag 테이블 쿼리 한번 + tag 테이블 쿼리 한번씩 나간다.(원래는 각 2개씩 나가야되는데 betch 로 인해 각각 한번씩만일단 나감)
        //3. Dto 변환 시간
        //Post findPost = postRepository.findPostByPostId(postId);
        //PostEditDto findPostEdit = PostEditDto.create(findPost);//Dto 변환

        //-성능 튜닝?!!
        //1. post 조회 쿼리 1번 -> Dto 바로 변환
        //2. Dto 의 tags 를 찾는데 쿼리 1번 만 발생
        PostEditDto findPostEdit = postRepositoryQueryDto.findQueryPostEditDtoByPostId(postId);

        return new DataResponse<>("200", "게시물 수정 폼입니다.", findPostEdit);
    }

    // 실제 게시물 수정
    /**
     * - 전체적으로 쿼리문이 너무 낭비....움... 좋은 방법이 없나
     * - 그리고 수정되면서 태그중에 언급 개수가 0인거는 삭제되어야되는거 아니야?! 그냥 남겨놔도 되나?!
     */
    @PutMapping("/{nickname}/{postId}/edit")
    @PostAuthor //추가 권한 검사 대상
    @ApiOperation(value = "게시물 수정 api", notes = "실제 게시물을 수정하는 api 입니다. 이후 게시물 상세 페이지 api 호출바랍니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)

    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    })
    public BaseResponse edit(@PathVariable("nickname")String nickname,
                             @PathVariable("postId")Long postId,
                             @Validated @RequestBody PostEditForm postEditForm,
                             @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        postService.updatePost(postId, postEditForm);

        return new BaseResponse("200", "게시물 수정이 완료되었습니다.");
    }

    //게시물 삭제
    /**
     * 게시물 삭제시 태그 언급수가 0인 태그는 삭제해야되나?!!!!!!
     */
    @DeleteMapping("/{nickname}/{postId}")
    @PostAuthor //추가 권한 검사 대상
    @ApiOperation(value = "게시물 삭제 api", notes = "게시물을 삭제하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code=200, message= "정상 호출"),
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    })
    public BaseResponse delete(@PathVariable("nickname")String nickname,
                               @PathVariable("postId")Long postId,
                               @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        postService.deletePost(postId);
        return new BaseResponse("200", "게시물 삭제가 완료되었습니다.");
    }


    //마이페이지 요청인지 판단
    private Boolean myPageCheck(String token, String nickname){
        String findNickname = jwtProvider.getNickname(token);
        if(findNickname.equals(nickname))
            return true;
        return false;
    }
}

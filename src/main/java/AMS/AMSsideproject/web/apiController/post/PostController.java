package AMS.AMSsideproject.web.apiController.post;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutAllUser;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutSpecificUser;
import AMS.AMSsideproject.domain.post.repository.query.PostRepositoryQueryDto;
import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.response.post.PostListResponse;
import AMS.AMSsideproject.web.responseDto.post.PostDto;
import AMS.AMSsideproject.web.responseDto.post.PostEditDto;
import AMS.AMSsideproject.web.responseDto.post.PostListDtoAboutAllUser;
import AMS.AMSsideproject.web.responseDto.post.PostListDtoAboutSpecificUser;
import AMS.AMSsideproject.web.swagger.postController.MainPage_200;
import AMS.AMSsideproject.web.swagger.postController.UserPage_200;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "게시물 관련 api")
@RequestMapping("/codebox")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final PostRepositoryQueryDto postRepositoryQueryDto; //성능 튜닝한 repository
    private final JwtProvider jwtProvider;

    //메인 페이지 (모든 사용자들의 게시물)
    // - 비 로그인 사용자도 여기는 접속할수 있어야함!!!!
    // - 근데 로그인 사용자랑, 비로그인 사용자는 엑세스 토큰 유무가 차이가 있는데 해당 uri에 인증을 안걸면 비로그인 사용자도 접속이 가능한데
    //   그렇게 되면 로그인 사용자에 대해서 해당 uri에서는 accessToken 기한만료등을 검사할수 없는뎅?...움....
    @GetMapping("/")
    @ApiOperation(value = "서비스 메인페이지, 모든 회원에 대한 게시물 리스트 조회 api", notes = "모든 회원 게시물들에 대해서 필터링 조건에 맞게 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출", response = MainPage_200.class),
            //@ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            //@ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostListResponse> mainPage(@RequestBody SearchFormAboutAllUser form) {
        Slice<Post> result = postService.findPostsAboutAllUser(form);

        //Dto 변환
        List<PostListDtoAboutAllUser> findPostDtos = result.getContent().stream()
                .map(p -> new PostListDtoAboutAllUser(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, result.getNumberOfElements(), result.hasNext());
        return new DataResponse<>("200", "모든 사용자들의 게시물 리스트 결과입니다.", postListResponse);
    }

    //특정 사용자 페이지
    @GetMapping("/{nickname}")
    @ApiOperation(value = "회원별 메인페이지, 특정 회원에 대한 게시물 리스트 api", notes = "특정 회원 게시물들에 대해서 필터링 조건에 맞게 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출", response = UserPage_200.class),
            //@ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            //@ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostListResponse> userPage(@PathVariable("nickname")String nickname , @RequestBody SearchFormAboutSpecificUser form) {
        Slice<Post> findPosts = postService.findPostsAboutSpecificUser(nickname, form);

        //Dto 변환
        List<PostListDtoAboutSpecificUser> findPostDtos = findPosts.getContent().stream()
                .map(p -> new PostListDtoAboutSpecificUser(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, findPosts.getNumberOfElements(), findPosts.hasNext());
        return new DataResponse<>("200", "특정 사용자의 게시물 리스트 입니다.",postListResponse);
    }

    // 게시물 상세조회
    /**
     * 1. 게시물 조회할때 uri 에 "postId" 로 해서 바로 사용하는 게 좋을까?! : 정보가 노출되면 좋지 않은뎅...
     * 2. api 에는 게시물 제목이 있고 json 으로 추가로 "postId"를 요청 받을까?!움....
     */
    @ApiOperation(value = "게시물 상세조회 api", notes = "게시물의 상세 정보를 보여줍니다.")
    @GetMapping("/{nickname}/{postId}")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            //@ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            //@ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostDto> post(@PathVariable("postId") Long postId) {

        //최적화 Dto 버전 사용
        PostDto findPostDto = postRepositoryQueryDto.findQueryPostDtoByPostId(postId);
        return new DataResponse<>("200", "문제 상제 조회 결과입니다.", findPostDto);
    }

    // uri 를 nickname 를 둘필요가 있나?!???????????????????????????????????????????????
    //- 또한 "context" 부분이 "마크업"으로 되야된다.!!! -> DB는 TEXT 형인데 TEXT 자료형 크기만큼 어떻게 받게 하지??!
    //- "PostSaveForm" validated 적용하기
    //- "406" error 정의
    @ApiOperation(value = "게시물 작성 api", notes = "게시물을 작성하는 api 입니다.")
    @PostMapping("/{nickname}/write")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public BaseResponse writePost(@RequestHeader(JwtProperties.HEADER_STRING)String accessToken, //swagger 에 표시를 위해
                                      @RequestBody PostSaveForm form) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        Post savaPost = postService.registration(userId, form);
        return new BaseResponse("200", "게시물이 저장되었습니다.");
    }

    //게시물 수정 form
    //이것도 추가 권한 체크 해야됌
    //이어지는 고민인 "게시물 PK"를 path 에 두는게 좋은건가!??
    @GetMapping("/{nickname}/{postId}/edit")
    @ApiOperation(value = "게시물 수정 api", notes = "개발중....")
    public DataResponse<PostEditDto> editForm(@PathVariable("postId")Long postId, @RequestHeader(JwtProperties.HEADER_STRING)String accessToken) {

        //1. post 조회 쿼리 1번
        //2. post tags 프록시 초기화 -> Post_tag 테이블 쿼리 한번 + tag 테이블 쿼리 한번씩 나간다.(원래는 각 2개씩 나가야되는데 betch 로 인해 각각 한번씩만일단 나감)
        //3. Dto 변환 시간
        //Post findPost = postRepository.findPostByPostId(postId);
        //PostEditDto postEditDto = PostEditDto.create(findPost);//Dto 변환

        //-성능 튜닝?!!
        //1. post 조회 쿼리 1번 -> Dto 바로 변환
        //2. Dto 의 tags 를 찾는데 쿼리 1번 만 발생
        PostEditDto findPostEdit = postRepositoryQueryDto.findQueryPostEditDtoByPostId(postId);

        return new DataResponse<>("200", "게시물 수정 폼입니다.", findPostEdit);
    }

    //게시물 수정
    //이것도 추가 권한 체크 해야됌
    @PutMapping("/{nickname}/{postId}/edit")
    @ApiOperation(value = "게시물 수정 api", notes = "개발중....")
    public void edit(){

    }


}

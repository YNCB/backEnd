package AMS.AMSsideproject.web.apiController.post;

import AMS.AMSsideproject.domain.like.repository.LikeRepository;
import AMS.AMSsideproject.domain.like.service.LikeService;
import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutAllUser;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutSpecificUser;
import AMS.AMSsideproject.domain.post.repository.query.PostRepositoryQueryDto;
import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostEditForm;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.AddAuthRequired;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.response.post.PostListResponse;
import AMS.AMSsideproject.web.responseDto.post.*;
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
    private final PostRepositoryQueryDto postRepositoryQueryDto; //성능 튜닝한 repository
    private final JwtProvider jwtProvider;
    private final LikeService likeService;

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
     *
     *
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!!!!!!!!!!좋아요 개수는 총 list 개수 리턴하면 되는데
     * !!!!!!! 1) 게시물 조회에서 좋아요 버튼은 "로그인"만 한 사용자가 할수 있는데 로그인 하지 않은 사용자가 게시물을 봤을때
     *             자기가 좋아요를 누른 게시물이면 좋아요 버튼이 활성화 되어있어야 되는데 -> 이럴러면 "JWT"토큰이 필요하게 되는데
     *             그럼 "인증" 처리를 해야되는데.....
     *         2) 그리고 만약에 어떤 사용자가 좋아요 누른 게시물이면 표시를 뜨게 "response"에 담아줘야함
     *         => 아래 똑같은 메서드를 2개 만들고 "매개변수만" 다르게 하면 되는거 아니야!!!!!!!!!????????-> 오버로딩!!!!!!!!!!!!!!!!!!!
     *         => 위에 "JWT" 인증이 필요없는 "api"도 전부다 "오버로딩"으로 하면 매개변수별로 달라지게 함수를 정의해놓기!!!!!!!!!!!!!!!!!!!
     *            그러면 "로그인한 사용자"에 대해서 "JWT"처리 가능?!
     *
     *            !!!!! 그러나 이렇게 되면 "JWT"토큰 기한만료!!를 "spring security filter" 에서 하는데  같은 api이니깐 안타지않나...
     *            그러면 "커스텀 어노테이션"으로 "인터셉터"를 구현해서 "인증처리"??!!!!!!!!
     *            -> 근데 같은 "http method" 여서 안되는데(error)
     *            -> 그럼 어떻게 보여주지....움!!!!!!!!!!!!!!!!!!!!!!!!!!
     *            => 그러면 그냥 "인터셉터"로 "인증처리"기능을 넣어서 로그인 한 사용자 이면 인터셉터에서 컨트롤러로 "파라미터"(토큰)넘겨서
     *               값이 있으면 로그인한 사용자라고 인식해서 처리?!!!!!
     *               파리미터에 "엑세스토큰"은 표시못함(swagger) , 있으면 반드시 줘야되는 파라미터라서 비로그인 사용자는 접근 못함...움...
     *               아니면 비로그인 사용자는 토큰을 "null"??로 ?? 움 이건 이상한데....
     */
    @ApiOperation(value = "비로그인 게시물 상세조회 api", notes = "게시물의 상세 정보를 보여줍니다.")
    @GetMapping("/{nickname}/{postId}")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            //@ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            //@ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostDto> postPage(@PathVariable("postId") Long postId) {

        //최적화 Dto 버전 사용
        PostDto findPostDto = postRepositoryQueryDto.findQueryPostDtoByPostId(postId);
        return new DataResponse<>("200", "문제 상제 조회 결과입니다.", findPostDto);
    }
//    @ApiOperation(value = "로그인 게시물 상세조회 api", notes = "게시물의 상세 정보를 보여줍니다.")
//    @GetMapping("/{nickname}/{postId}")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출"),
//            //@ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
//            //@ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    public DataResponse<LoginPostDto> postPage(@PathVariable("postId") Long postId,
//                                          @RequestHeader(JwtProperties.HEADER_STRING)String accessToken) {
//
//        Long findUserId = jwtProvider.getUserIdToToken(accessToken);
//        Boolean existing = likeService.checkExisting(postId, findUserId);
//
//        //최적화 Dto 버전 사용
//        PostDto findPostDto = postRepositoryQueryDto.findQueryPostDtoByPostId(postId);
//        LoginPostDto loginPostDto = LoginPostDto.create(findPostDto, existing);
//        return new DataResponse<>("200", "문제 상제 조회 결과입니다.", loginPostDto);
//    }



    /**
     * "nickname" 패스는 빼기!! -> 그럼 인터셉터도 필요 x , 토큰만으로 판별
     */
    // uri 를 nickname 를 둘필요가 있나?!???????????????????????????????????????????????
    //- 또한 "context" 부분이 "마크업"으로 되야된다.!!! -> DB는 TEXT 형인데 TEXT 자료형 크기만큼 어떻게 받게 하지??!
    //- "PostSaveForm" validated 적용하기
    //- "406" error 정의
    @ApiOperation(value = "게시물 작성 api", notes = "게시물을 작성하는 api 입니다.")
    @PostMapping("/{nickname}/write")
    @AddAuthRequired //추가 권한 검사 대상
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
    //이어지는 고민인 "게시물 PK"를 path 에 두는게 좋은건가!??
    @GetMapping("/{nickname}/{postId}/edit")
    @AddAuthRequired //추가 권한 검사 대상
    @ApiOperation(value = "게시물 수정 api", notes = "게시물에서 수정가능한 항목들을 보여줍니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostEditDto> edit(@PathVariable("nickname")String nickname,
                                          @PathVariable("postId")Long postId,
                                          @RequestHeader(JwtProperties.HEADER_STRING)String accessToken) { //swagger 에 표시를 위해

        //1. post 조회 쿼리 1번
        //2. post tags 프록시 초기화 -> Post_tag 테이블 쿼리 한번 + tag 테이블 쿼리 한번씩 나간다.(원래는 각 2개씩 나가야되는데 betch 로 인해 각각 한번씩만일단 나감)
        //3. Dto 변환 시간
        //Post findPost = postRepository.findPostByPostId(postId);
        //PostEditDto findPostEdit = PostEditDto.create(findPost);//Dto 변환

        //-성능 튜닝?!!
        //1. post 조회 쿼리 1번 -> Dto 바로 변환
        //2. Dto 의 tags 를 찾는데 쿼리 1번 만 발생
        PostEditDto findPostEdit = postRepositoryQueryDto.findQueryPostEditDtoByPostId(postId);
        return new DataResponse<>("200", "게시물 수정 항목입니다.", findPostEdit);
    }
    /**
     * Hard!!!!!
     */
    //게시물 수정
    // 만약 "Tag" 테이블에서 더이상 사용하지않은 "Tag"는 삭제해야되지않나!???!!!!!!! 자동으로 하게 해야되는거 아니가?!!!!!
    @PutMapping("/{nickname}/{postId}/edit")
    @AddAuthRequired //추가 권한 검사 대상
    @ApiOperation(value = "게시물 수정 api", notes = "실제 게시물을 수정하는 api 입니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public BaseResponse edit(@PathVariable("nickname")String nickname,
                             @PathVariable("postId")Long postId,
                             @RequestBody PostEditForm postEditForm,
                             @RequestHeader(JwtProperties.HEADER_STRING) String accessToken) { //swagger 에 표시를 위해

        postService.updatePost(postId, postEditForm);
        return new BaseResponse("200", "게시물 수정이 완료되었습니다.");
        //redirect 로 "게시물 상세 조회" 로 이동해야됌
    }

    //게시물 삭제
    //"postTag"는 배열에서 remove하면 옵션으로 없어지는데 만약에 tag 테이블에 사용하고 있지 않은 tag 도 삭제??!!
    @DeleteMapping("/{nickname}/{postId}")
    @AddAuthRequired //추가 권한 검사 대상
    @ApiOperation(value = "게시물 삭제 api", notes = "게시물을 삭제하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public BaseResponse delete(@PathVariable("nickname")String nickname,
                               @PathVariable("postId")Long postId,
                               @RequestHeader(JwtProperties.HEADER_STRING) String accessToken) {

        postService.deletePost(postId);
        return new BaseResponse("200", "게시물 삭제가 완료되었습니다.");
        //redirect 로 "유저 페이지"로 이동해야됌
    }

}

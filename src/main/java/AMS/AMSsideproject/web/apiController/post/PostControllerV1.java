//package AMS.AMSsideproject.web.apiController.post;
//
//import AMS.AMSsideproject.domain.like.service.LikeService;
//import AMS.AMSsideproject.domain.post.Post;
//import AMS.AMSsideproject.web.apiController.post.requestForm.*;
//import AMS.AMSsideproject.domain.post.repository.query.PostRepositoryQueryDto;
//import AMS.AMSsideproject.domain.post.service.PostServiceImpl;
//import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
//import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
//import AMS.AMSsideproject.web.custom.annotation.PostAuthor;
//import AMS.AMSsideproject.web.custom.annotation.UserAuthen;
//import AMS.AMSsideproject.web.custom.annotation.UserAuthor;
//import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
//import AMS.AMSsideproject.web.response.BaseResponse;
//import AMS.AMSsideproject.web.response.DataResponse;
//import AMS.AMSsideproject.web.response.post.PostListResponse;
//import AMS.AMSsideproject.web.responseDto.post.*;
//import AMS.AMSsideproject.web.swagger.postController.UserPage_200;
//import AMS.AMSsideproject.web.swagger.userController.Join_406;
//import io.swagger.annotations.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Slice;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import springfox.documentation.annotations.ApiIgnore;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@Api(tags = "게시물 관련 api")
//@RequestMapping("/codebox")
//public class PostControllerV1 {
//
//    private final PostServiceImpl postService;
//    private final PostRepositoryQueryDto postRepositoryQueryDto; //성능 튜닝한 repository
//    private final JwtProvider jwtProvider;
//    private final LikeService likeService;
//
//    //메인 페이지
//    @PostMapping(value = "/")
//    @ApiOperation(value = "서비스 메인페이지-모든 게시물 리스트 조회 api", notes = "모든 회원 게시물들에 대해서 필터링 조건에 맞게 게시물을 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출"),
//            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    public DataResponse<PostListResponse> mainPage(@Validated @RequestBody PostSearchFormAboutAllUser form,
//                                                   @RequestHeader(value = JwtProperties.ACCESS_HEADER_STRING, required = false) String accessToken) {
//
//        Slice<Post> result = postService.findPostsAboutAllUser(form);
//
//        //Dto 변환
//        List<PostListDtoAboutAllUser> findPostDtos = result.getContent().stream()
//                .map(p -> new PostListDtoAboutAllUser(p))
//                .collect(Collectors.toList());
//
//        //response
//        PostListResponse postListResponse = new PostListResponse(findPostDtos, result.getNumberOfElements(), result.hasNext());
//        return new DataResponse<>("200", "모든 사용자들의 게시물 리스트입니다.", postListResponse);
//    }
//
//
//
//    //비로그인 사용자 - 특정 사용자 페이지(내 페이지, 상대방 페이지 -> 같음)
//    @PostMapping(value = "/{nickname}")
//    @ApiOperation(value = "비로그인 회원 - 특정 회원에 대한 게시물 리스트 조회",
//            notes = "특정 회원 게시물들에 대해서 필터링 조건에 맞게 리스트를 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출", response = UserPage_200.class),
//            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true)
//    })
//    public DataResponse<PostListResponse> userPage1(@PathVariable("nickname")String nickname ,
//                                                   @Validated @RequestBody SearchFormAboutOtherUserPost form) {
//
//        Slice<Post> findPosts = postService.findPostsAboutOtherUser(nickname, form);
//
//        //Dto 변환
//        List<PostListDtoAboutSpecificUser> findPostDtos = findPosts.getContent().stream()
//                .map(p -> new PostListDtoAboutSpecificUser(p))
//                .collect(Collectors.toList());
//
//        //response
//        PostListResponse postListResponse = new PostListResponse(findPostDtos, findPosts.getNumberOfElements(), findPosts.hasNext());
//        return new DataResponse<>("200", "특정 사용자의 게시물 리스트 입니다.",postListResponse);
//    }
//
//    //로그인 사용자 - 다른 사용자 페이지 접속하는 경우
//    @PostMapping(value = "/{nickname}", headers = JwtProperties.ACCESS_HEADER_STRING)
//    @UserAuthen
//    @UserAuthor
//    @ApiOperation(value = "로그인 회원 - 다른사용자 페이지에 대한 게시물 리스트 조회",
//            notes = "특정 회원 게시물들에 대해서 필터링 조건에 맞게 리스트를 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출", response = UserPage_200.class),
//            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
//            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
//            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
//            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
//    })
//    public DataResponse<PostListResponse> userPage2(@PathVariable("nickname")String nickname,
//                                                   @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken,
//                                                   @Validated @RequestBody SearchFormAboutOtherUserPost form) {
//
//        Slice<Post> findPosts = findPosts = postService.findPostsAboutOtherUser(nickname, form);
//
//        //Dto 변환
//        List<PostListDtoAboutSpecificUser> findPostDtos = findPosts.getContent().stream()
//                .map(p -> new PostListDtoAboutSpecificUser(p))
//                .collect(Collectors.toList());
//
//        //response
//        PostListResponse postListResponse = new PostListResponse(findPostDtos, findPosts.getNumberOfElements(), findPosts.hasNext());
//        return new DataResponse<>("200", "특정 사용자의 게시물 리스트 입니다.",postListResponse);
//    }
//
//    //로그인 사용자 - 내 페이지 접속하는 경우
//    //"my_session token"은 기본정보 검증안해도 되나???!!(유효기간 등)????
//    @PostMapping(value = "/{nickname}", headers = {JwtProperties.ACCESS_HEADER_STRING, JwtProperties.MYSESSION_HEADER_STRING})
//    @UserAuthen
//    @UserAuthor
//    @ApiOperation(value = "로그인 회원 - 자신의 페이지에 대한 게시물 리스트 조회",
//            notes = "특정 회원 게시물들에 대해서 필터링 조건에 맞게 리스트를 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출", response = UserPage_200.class),
//            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
//            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
//            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
//            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true),
//            @ApiImplicitParam(name = JwtProperties.MYSESSION_HEADER_STRING, value = "마이페이지 엑세스 토큰", required = true)
//    })
//    public DataResponse<PostListResponse> userPage3(@PathVariable("nickname")String nickname,
//                                                   @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken,
//                                                   @RequestHeader(JwtProperties.MYSESSION_HEADER_STRING) String my_sessionToken,
//                                                   @Validated @RequestBody SearchFormAboutSelfUserPost form) {
//
//        Slice<Post> findPosts = findPosts = postService.findPostsAboutOneSelf(nickname, form);
//
//        //Dto 변환
//        List<PostListDtoAboutSpecificUser> findPostDtos = findPosts.getContent().stream()
//                .map(p -> new PostListDtoAboutSpecificUser(p))
//                .collect(Collectors.toList());
//
//        //response
//        PostListResponse postListResponse = new PostListResponse(findPostDtos, findPosts.getNumberOfElements(), findPosts.hasNext());
//        return new DataResponse<>("200", "특정 사용자의 게시물 리스트 입니다.",postListResponse);
//    }
//
//
//    // 게시물 상세조회 - 비로그인 사용자
//    /**
//     * <나중에 더 찾아볼 내용!></나중에>
//     * - "postID" : 내부적으로는 int pk, 외부적으로는 UUID 사용하는 방법은 움..
//     * => "postID" 를 알게되더라도 게시물작성, 수정등은 토큰을 통해 "권한" 체크를 하니깐 유출되어도 상관없움??!!
//     */
//    @GetMapping(value = "/{nickname}/{postId}")
//    @ApiOperation(value = "비로그인 사용자 - 게시물 상세조회 api", notes = "게시물의 상세 정보를 보여줍니다.")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출"),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
//            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true)
//    })
//    public DataResponse<PostDto> postPage1(@PathVariable("postId") Long postId,
//                                          @PathVariable("nickname") String nickname) {
//
//        //최적화 Dto 버전 사용 - post 정보만
//        //PostDto findPostDto = postRepositoryQueryDto.findQueryPostDtoByPostId(postId);
//        /**
//         * - post 정보 포함 : 최적의 Dto 버전사용
//         * - reply 정보 포함
//         */
//        PostDto findPostDto = postService.findPostByPostId(postId);
//
//        return new DataResponse<>("200", "문제 상제 조회 결과입니다.", findPostDto);
//    }
//
//    // 게시물 상세조회 - 로그인 사용자
//    @GetMapping(value = "/{nickname}/{postId}", headers = JwtProperties.ACCESS_HEADER_STRING)
//    @ApiOperation(value = "로그인 사용자 - 게시물 상세조회 api", notes = "게시물의 상세 정보를 보여줍니다. 추가로 게시물 좋아요 누른 유무도 알려줍니다.")
//    @UserAuthen
//    @UserAuthor
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출 - 추가로 Header 에 해당 게시물 id를 포함시킨 쿠키를 포함함"),
//            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
//            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
//            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
//            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true),
//            @ApiImplicitParam(name = "postView", value = "게시물 중복 조회 증가 방지 쿠키", required = false, paramType = "cookie")
//    })
//    public DataResponse<LoginPostDto> postPage2(@PathVariable("postId") Long postId,
//                                               @PathVariable("nickname") String nickname,
//                                               @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken,
//                                               @ApiIgnore @CookieValue(value = "postView" ,required = false) Cookie postViewCookie, //쿠키가 없을수 있음
//                                               HttpServletResponse response) {
//
//        Long findUserId = jwtProvider.getUserIdToToken(accessToken);
//
//        //체크 유무 판단
//        Boolean existing = likeService.checkExisting(postId, findUserId);
//
//        //최적화 Dto 버전 사용
//        //PostDto findPostDto = postRepositoryQueryDto.findQueryPostDtoByPostId(postId);
//        /**
//         * - post 정보 포함 : 최적의 Dto 버전사용
//         * - reply 정보 포함
//         */
//        PostDto findPostDto = postService.findPostByPostId(postId);
//
//        //조회순 증가 검증
//        postService.readPost(postId, postViewCookie, response);
//
//        LoginPostDto loginPostDto = LoginPostDto.create(findPostDto, existing);
//        return new DataResponse<>("200", "문제 상제 조회 결과입니다.", loginPostDto);
//    }
//
//
//    //게시물 작성 -> JWT 토큰에 있는 userId 로 게시물을 저장시킨다.
//    /**
//     * "context" 부분이 "마크업"으로 되야된다.!!!
//     */
//    @ApiOperation(value = "게시물 작성 api", notes = "게시물을 작성하는 api 입니다.")
//    @PostMapping("/write")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출"),
//            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
//            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
//            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING,value = "엑세스 토큰", required = true)
//    public BaseResponse writePost(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken,
//                                  @Validated @RequestBody PostSaveForm form) {
//
//        Long userId = jwtProvider.getUserIdToToken(accessToken);
//
//        //태그 하나하나 쿼리문이 발생함 존재 유무를 확인하기 위해
//        Post savaPost = postService.registration(userId, form);
//
//        return new BaseResponse("200", "게시물이 저장되었습니다.");
//    }
//
//    //게시물 수정 form
//    //이어지는 고민인 "게시물 PK"를 path 에 두는게 좋은건가!??
//    @GetMapping("/{nickname}/{postId}/edit")
//    @PostAuthor //추가 권한 검사 대상
//    @ApiOperation(value = "게시물 수정 api", notes = "게시물에서 수정가능한 항목들을 보여줍니다.")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출"),
//            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
//            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
//            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
//            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
//    })
//    public DataResponse<PostEditDto> edit(@PathVariable("nickname")String nickname,
//                                          @PathVariable("postId")Long postId,
//                                          @RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken) {
//
//        //1. post 조회 쿼리 1번
//        //2. post tags 프록시 초기화 -> Post_tag 테이블 쿼리 한번 + tag 테이블 쿼리 한번씩 나간다.(원래는 각 2개씩 나가야되는데 betch 로 인해 각각 한번씩만일단 나감)
//        //3. Dto 변환 시간
//        //Post findPost = postRepository.findPostByPostId(postId);
//        //PostEditDto findPostEdit = PostEditDto.create(findPost);//Dto 변환
//
//        //-성능 튜닝?!!
//        //1. post 조회 쿼리 1번 -> Dto 바로 변환
//        //2. Dto 의 tags 를 찾는데 쿼리 1번 만 발생
//        PostEditDto findPostEdit = postRepositoryQueryDto.findQueryPostEditDtoByPostId(postId);
//
//        return new DataResponse<>("200", "게시물 수정 폼입니다.", findPostEdit);
//    }
//
//    // 실제 게시물 수정
//    /**
//     * - 전체적으로 쿼리문이 너무 낭비....움... 좋은 방법이 없나
//     * - 그리고 수정되면서 태그중에 언급 개수가 0인거는 삭제되어야되는거 아니야?! 그냥 남겨놔도 되나?!
//     */
//    @PutMapping("/{nickname}/{postId}/edit")
//    @PostAuthor //추가 권한 검사 대상
//    @ApiOperation(value = "게시물 수정 api", notes = "실제 게시물을 수정하는 api 입니다. 이후 게시물 상세 페이지 api 호출바랍니다.")
//    @ApiResponses({
//            @ApiResponse(code=200, message="정상 호출"),
//            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
//            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
//            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
//            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
//            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
//    })
//    public BaseResponse edit(@PathVariable("nickname")String nickname,
//                             @PathVariable("postId")Long postId,
//                             @Validated @RequestBody PostEditForm postEditForm,
//                             @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {
//
//        postService.updatePost(postId, postEditForm);
//
//        return new BaseResponse("200", "게시물 수정이 완료되었습니다.");
//    }
//
//    //게시물 삭제
//    /**
//     * 게시물 삭제시 태그 언급수가 0인 태그는 삭제해야되나?!!!!!!
//     */
//    @DeleteMapping("/{nickname}/{postId}")
//    @PostAuthor //추가 권한 검사 대상
//    @ApiOperation(value = "게시물 삭제 api", notes = "게시물을 삭제하는 api입니다.")
//    @ApiResponses({
//            @ApiResponse(code=200, message= "정상 호출"),
//            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
//            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=403, message = "잘못된 접근입니다. 권한이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
//            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "nickname", value = "회원 닉네임", required = true),
//            @ApiImplicitParam(name = "postId", value = "게시물 아이디", required = true),
//            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
//    })
//    public BaseResponse delete(@PathVariable("nickname")String nickname,
//                               @PathVariable("postId")Long postId,
//                               @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {
//
//        postService.deletePost(postId);
//        return new BaseResponse("200", "게시물 삭제가 완료되었습니다.");
//    }
//
//}

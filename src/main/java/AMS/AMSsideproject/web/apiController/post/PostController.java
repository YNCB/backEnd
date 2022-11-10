package AMS.AMSsideproject.web.apiController.post;

import AMS.AMSsideproject.domain.like.service.LikeService;
import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.query.form.LikeDto;
import AMS.AMSsideproject.web.apiController.post.requestForm.SearchFormAboutAllUserPost;
import AMS.AMSsideproject.web.apiController.post.requestForm.SearchFormAboutSpecificUserByLogin;
import AMS.AMSsideproject.web.apiController.post.requestForm.SearchFormAboutSpecificUserByNonLogin;
import AMS.AMSsideproject.domain.post.repository.query.PostRepositoryQueryDto;
import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.domain.post.service.form.SearchFormOneSelf;
import AMS.AMSsideproject.domain.post.service.form.SearchFormOtherUser;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostEditForm;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostSaveForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.AddAuthRequired;
import AMS.AMSsideproject.web.custom.annotation.LoginAuthRequired;
import AMS.AMSsideproject.web.custom.annotation.VerityUserType;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "게시물 관련 api")
@RequestMapping("/codebox")
public class PostController {

    private final PostService postService;
    private final PostRepositoryQueryDto postRepositoryQueryDto; //성능 튜닝한 repository
    private final JwtProvider jwtProvider;
    private final LikeService likeService;

    //모든 사용자들의 게시물 리스트 조회 (로그인, 비로그인)
    @GetMapping(value = "/")
    @ApiOperation(value = "서비스 메인페이지, 비회원 - 게시물 리스트 조회 api", notes = "모든 회원 게시물들에 대해서 필터링 조건에 맞게 게시물을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출", response = MainPage_200.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostListResponse> mainPage(@RequestBody SearchFormAboutAllUserPost form) {
        Slice<Post> result = postService.findPostsAboutAllUser(form);

        //Dto 변환
        List<PostListDtoAboutAllUser> findPostDtos = result.getContent().stream()
                .map(p -> new PostListDtoAboutAllUser(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, result.getNumberOfElements(), result.hasNext());
        return new DataResponse<>("200", "모든 사용자들의 게시물 리스트 결과입니다.", postListResponse);
    }
    @GetMapping(value = "/", headers = JwtProperties.HEADER_STRING)
    @LoginAuthRequired //로그인 전용 인증 체크
    //권한 체크도 해야되는거 아니야!?????????!!!!!!!!!!(USER.....)
    @ApiOperation(value = "서비스 메인페이지, 로그인 회원 - 게시물 리스트 조회 api", notes = "모든 회원 게시물들에 대해서 필터링 조건에 맞게 게시물을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출", response = MainPage_200.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostListResponse> mainPage(@RequestBody SearchFormAboutAllUserPost form,
                                                   @RequestHeader(JwtProperties.HEADER_STRING) String accessToken) {
        Slice<Post> result = postService.findPostsAboutAllUser(form);

        //Dto 변환
        List<PostListDtoAboutAllUser> findPostDtos = result.getContent().stream()
                .map(p -> new PostListDtoAboutAllUser(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, result.getNumberOfElements(), result.hasNext());
        return new DataResponse<>("200", "모든 사용자들의 게시물 리스트 결과입니다.", postListResponse);
    }

    //비로그인 사용자 - 특정 사용자 페이지(내 페이지, 상대방 페이지 -> 같음)
    @GetMapping(value = "/{nickname}")
    @ApiOperation(value = "회원별 페이지, 비로그인 회원-특정 회원에 대한 게시물 리스트 조회",
            notes = "특정 회원 게시물들에 대해서 필터링 조건에 맞게 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출", response = UserPage_200.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostListResponse> userPage(@PathVariable("nickname")String nickname ,
                                                   @RequestBody SearchFormAboutSpecificUserByNonLogin form) {

        SearchFormOtherUser searchForm = new SearchFormOtherUser(form.getLanguage(), form.getSearchTitle(), form.getOrderKey(), form.getLastPostId(),
                form.getLastReplyNum(), form.getLastLikeNum());

        Slice<Post> findPosts = postService.findPostsAboutOtherUser(nickname, searchForm);

        //Dto 변환
        List<PostListDtoAboutSpecificUser> findPostDtos = findPosts.getContent().stream()
                .map(p -> new PostListDtoAboutSpecificUser(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, findPosts.getNumberOfElements(), findPosts.hasNext());
        return new DataResponse<>("200", "특정 사용자의 게시물 리스트 입니다.",postListResponse);
    }
    //로그인 사용자 - 특정 사용자 페이지(내 페이지, 다른 사용자 페이지 -> 다름)
    @GetMapping(value = "/{nickname}", headers = JwtProperties.HEADER_STRING)
    @LoginAuthRequired
    @VerityUserType //자신이 자신의 페이지에 접속했는지 다른 사용자 페이지에 접속했는지 구분
    @ApiOperation(value = "회원별 페이지, 비로그인 회원-특정 회원(나,다른유저)에 대한 게시물 리스트 조회",
            notes = "특정 회원 게시물들에 대해서 필터링 조건에 맞게 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출", response = UserPage_200.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostListResponse> userPage(@PathVariable("nickname")String nickname,
                                                   @RequestHeader(JwtProperties.HEADER_STRING) String accessToken,
                                                   @RequestBody SearchFormAboutSpecificUserByLogin form,
                                                   HttpServletRequest request) {

        Slice<Post> findPosts = null;
        Boolean verity = (Boolean)request.getAttribute("verity");
        if(verity == true) { //내 페이지에 접속한 경우
            SearchFormOneSelf searchFormOneSelf = new SearchFormOneSelf(form.getTags(), form.getType(), form.getLanguage(), form.getSearchTitle()
                    , form.getOrderKey(), form.getLastPostId(), form.getLastReplyNum(), form.getLastLikeNum());

            findPosts = postService.findPostsAboutOneSelf(nickname, searchFormOneSelf);

        }else { //다른 사용자의 페이지에 접속한 경우
            SearchFormOtherUser searchFormOtherUser = new SearchFormOtherUser(form.getLanguage(), form.getSearchTitle(), form.getOrderKey(),
                    form.getLastPostId(), form.getLastReplyNum(), form.getLastLikeNum());

            findPosts = postService.findPostsAboutOtherUser(nickname, searchFormOtherUser);
        }

        List<PostListDtoAboutSpecificUser> findPostDtos = findPosts.getContent().stream()
                .map(p -> new PostListDtoAboutSpecificUser(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, findPosts.getNumberOfElements(), findPosts.hasNext());
        return new DataResponse<>("200", "특정 사용자의 게시물 리스트 입니다.",postListResponse);
    }


    // 게시물 상세조회(로그인, 비로그인)
    /**
     * - 게시물 조회할때 uri 에 "postId" 로 해서 바로 사용하는 게 좋을까?! : 정보가 노출되면 좋지 않은데.....
     */
    @ApiOperation(value = "비로그인 사용자 - 게시물 상세조회 api", notes = "게시물의 상세 정보를 보여줍니다.")
    @GetMapping(value = "/{nickname}/{postId}")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<PostDto> postPage(@PathVariable("postId") Long postId) {

        //최적화 Dto 버전 사용
        PostDto findPostDto = postRepositoryQueryDto.findQueryPostDtoByPostId(postId);
        return new DataResponse<>("200", "문제 상제 조회 결과입니다.", findPostDto);
    }
    @ApiOperation(value = "로그인 사용자 - 게시물 상세조회 api", notes = "게시물의 상세 정보를 보여줍니다.")
    @GetMapping(value = "/{nickname}/{postId}", headers = JwtProperties.HEADER_STRING)
    @LoginAuthRequired//로그인 전용 인증 체크
    //권한 체크도 해야되는거 아니야!?????????!!!!!!!!!!(USER.....)
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<LoginPostDto> postPage(@PathVariable("postId") Long postId,
                                          @RequestHeader(JwtProperties.HEADER_STRING)String accessToken) {

        Long findUserId = jwtProvider.getUserIdToToken(accessToken);

        //굳이 쿼리문을 하나더 생성할 필요가 없지!!!!!!!!!!!!!
        //Boolean existing = likeService.checkExisting(postId, findUserId);

        //최적화 Dto 버전 사용
        PostDto findPostDto = postRepositoryQueryDto.findQueryPostDtoByPostId(postId);

        Boolean existing = checkLikeExisting(findPostDto, findUserId);
        LoginPostDto loginPostDto = LoginPostDto.create(findPostDto, existing);
        return new DataResponse<>("200", "문제 상제 조회 결과입니다.", loginPostDto);
    }
    //굳이 postService 에 정의할필요없다고 생각해서!
    private Boolean checkLikeExisting(PostDto postDto, Long userId) {
        List<LikeDto> likes = postDto.getLikes();
        for(LikeDto likeDto : likes) {
            if(likeDto.user_id==userId){
                return true;
            }
        }
        return false;
    }




    /**
     * "nickname" 패스는 빼면 -> 그럼 인터셉터도 사용안함 , 토큰으로만 판별(토큰에서 사용자 정보를 추출해서 누가쓰는지 알아냄!)
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
            //스프링 시큐리티 (USER 권한에 대한 에러도 처리해야되는거 아니야?!!!!!!!!!!!!
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

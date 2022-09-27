package AMS.AMSsideproject.web.apiController.post;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutAllUser;
import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.response.post.PostListResponse;
import AMS.AMSsideproject.web.responseDto.post.PostListDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "게시물 관련 api")
@RequestMapping("/ams")
public class PostController {

    private final PostService postService;

    //메인 페이지 (모든 사용자들의 게시물)
    @GetMapping("/")
    @ApiOperation(value = "서비스 메인페이지 , 모든 회원에 대한 게시물 리스트 조회 api", notes = "개발중....")
    public DataResponse<PostListResponse> main(@RequestBody SearchFormAboutAllUser form) {
        Slice<Post> result = postService.findAboutAllUserPost(form);

        //Dto 변환
        List<PostListDto> findPostDtos = result.getContent().stream()
                .map(p -> new PostListDto(p))
                .collect(Collectors.toList());

        //response
        PostListResponse postListResponse = new PostListResponse(findPostDtos, result.getNumberOfElements(), result.hasNext());
        return new DataResponse<>("200", "모든 사용자들의 대한 게시물 리스트입니다.", postListResponse);
    }


    //특정 사용자 메인 페이지
    @GetMapping("/{nickname}")
    @ApiOperation(value = "회원별 메인페이지 api", notes = "해당 회원의 문제 리스트을 보여줍니다. 개발 중...")
    public PostListResponse home(@PathVariable("nickname") String nickname ,
                                 @RequestParam(value = "tag_name", required = false) String tagName,
                                 @RequestParam(value = "problem_type", required = false) String problem_type) {

        return null;
    }

    // 게시물 상세조회
    @ApiOperation(value = "회원 게시물 상세조회 api", notes = "개발중 ...")
    @GetMapping("/{nickname}/{title}")
    public PostListResponse post(@PathVariable("nickname") String nickname, @PathVariable("title") String title) {
        return null;
    }

    //게시물 작성
    @ApiOperation(value = "회원 게시물 작성 api", notes = "개발중 ...")
    @PostMapping("/{nickname}/write")
    public PostListResponse writePost() {
        return null;
    }

    //게시물 수정

}

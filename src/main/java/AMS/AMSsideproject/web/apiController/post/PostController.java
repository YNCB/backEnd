package AMS.AMSsideproject.web.apiController.post;

import AMS.AMSsideproject.web.response.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "게시물 관련 api")
@RequestMapping("/ams")
public class PostController {

    //사용자 메인 페이지
    @GetMapping("/{nickname}")
    @ApiOperation(value = "회원별 메인페이지 api", notes = "해당 회원의 문제 리스트을 보여줍니다. 개발 중...")
    public DefaultResponse home(@PathVariable("nickname") String nickname ,
                                @RequestParam(value = "tag_name", required = false) String tagName,
                                @RequestParam(value = "problem_type", required = false) String problem_type) {

        return null;
    }

    // 게시물 상세조회
    @ApiOperation(value = "게시물 상세조회 api", notes = "개발중 ...")
    @GetMapping("/{nickname}/{title}")
    public DefaultResponse post(@PathVariable("nickname") String nickname, @PathVariable("title") String title) {
        return null;
    }

    //게시물 작성
    @ApiOperation(value = "게시물 작성 api", notes = "개발중 ...")
    @PostMapping("/{nickname}/write")
    public DefaultResponse writePost() {
        return null;
    }

    //게시물 수정

}

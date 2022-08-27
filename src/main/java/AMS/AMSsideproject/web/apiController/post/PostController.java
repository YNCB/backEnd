package AMS.AMSsideproject.web.apiController.post;

import AMS.AMSsideproject.web.response.defaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "게시물 관련 api")
@RequestMapping("/api")
public class PostController {

    //사용자 메인 페이지
    @GetMapping("/{nickname}")
    @ApiOperation(value = "", notes = "")
    public defaultResponse home(@PathVariable("nickname") String nickname) {
        return null;
    }
}

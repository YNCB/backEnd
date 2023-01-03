package AMS.AMSsideproject.web.apiController.follow;

import AMS.AMSsideproject.domain.follow.Follow;
import AMS.AMSsideproject.domain.follow.service.FollowService;
import AMS.AMSsideproject.web.apiController.follow.requestForm.FollowSaveForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.follow.FollowersDto;
import AMS.AMSsideproject.web.responseDto.follow.FollowingsDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/codebox")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final JwtProvider jwtProvider;

    /**
     * - 팔로잉 중복체크 해야함!!!!!
     * - 리스트 조회에서 시큐리티 필터가 타지않음
     * - Bean validation 적용하기
     */

    //팔로우 저장
    @PostMapping("/follow/add")
    public BaseResponse addFollow(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken,
                                 @RequestBody FollowSaveForm form) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        Follow follow = followService.addFollow(userId, form.getUserId());

        return new BaseResponse("200", "팔로우가 저장되었습니다");
    }

    //팔로우 삭제
    @DeleteMapping("/follow/{followId}")
    public BaseResponse deleteFollow(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken,
                                     @PathVariable("followId")Long followId){
        followService.deleteFollow(followId);
        return new BaseResponse("200", "팔로우가 삭제되었습니다");
    }

    //팔로워 리스트 조회
    @GetMapping("/follower/{userId}")
    public DataResponse<List> followers(@PathVariable("userId")Long userId,
                                        @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        List<Follow> followers = followService.findFollowersByUserId(userId);
        List<FollowersDto> result = followers.stream().map(e -> new FollowersDto(e)).collect(Collectors.toList());

        return new DataResponse<>("200", "팔로워 리스트 입니다.", result);
    }

    //팔로잉 리스트 조회
    @GetMapping("/following/{userId}")
    public DataResponse following(@PathVariable("userId")Long userId,
                                  @RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        List<Follow> followings = followService.findFollowingsByUserId(userId);
        List<FollowingsDto> result = followings.stream().map(e -> new FollowingsDto(e)).collect(Collectors.toList());

        return new DataResponse("200", "팔로잉 리스트 입니다.", result);
    }

}

package AMS.AMSsideproject.web.apiController.follow;

import AMS.AMSsideproject.domain.follow.Follow;
import AMS.AMSsideproject.domain.follow.service.FollowService;
import AMS.AMSsideproject.web.apiController.follow.requestForm.FollowSaveForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.follow.FollowersDto;
import AMS.AMSsideproject.web.responseDto.follow.FollowingsDto;
import AMS.AMSsideproject.web.swagger.userController.Join_406;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/codebox")
@RequiredArgsConstructor
@Api(tags = "팔로우 관련 api")
public class FollowController {

    private final FollowService followService;
    private final JwtProvider jwtProvider;

    //팔로우 저장
    @PostMapping("/follow/add")
    @ApiOperation(value = "팔로우 저장 api", notes = "특정 사용자를 팔로우 합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=400, message = "잘못된 요청", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=412, message = "로그아웃 처리된 엑세스 토큰입니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    })
    public BaseResponse addFollow(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken,
                                  @Validated @RequestBody FollowSaveForm form) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        Follow follow = followService.addFollow(userId, form.getUserId());

        return new BaseResponse("200", "팔로우가 저장되었습니다");
    }

    //팔로우 삭제
    @DeleteMapping("/follow/{followId}")
    @ApiOperation(value = "팔로우 삭제 api", notes = "특정 사용자를 언팔로우합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=400, message = "잘못된 요청", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "로그아웃 처리된 엑세스 토큰입니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true),
            @ApiImplicitParam(name = "followId", value = "삭제할 팔로우 Id", required = true)
    })
    public BaseResponse deleteFollow(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken,
                                     @PathVariable("followId")Long followId){
        followService.deleteFollow(followId);

        return new BaseResponse("200", "팔로우가 삭제되었습니다");
    }

    //팔로워 리스트 조회
    @GetMapping("/follow/follower")
    @ApiOperation(value = "팔로워 리스트를 조회합니다.", notes = "내가 팔로우한 사용자 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message= "정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=401, message = "JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "로그아웃 처리된 엑세스 토큰입니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true),
    })
    public DataResponse<List> followers(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        List<Follow> followers = followService.findFollowersByUserId(userId);

        //Dto 변환
        List<FollowersDto> result = followers.stream().map(e -> new FollowersDto(e)).collect(Collectors.toList());
        return new DataResponse<>("200", "팔로워 리스트 입니다.", result);
    }

    //팔로잉 리스트 조회
    @GetMapping("/follow/following")
    @ApiOperation(value = "팔로잉 리스트를 조회합니다.", notes = "나를 팔로우한 사용자 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message= "정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=401, message = "JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "로그아웃 처리된 엑세스 토큰입니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true),
    })
    public DataResponse following(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken) {

        Long userId = jwtProvider.getUserIdToToken(accessToken);
        List<Follow> followings = followService.findFollowingsByUserId(userId);

        //Dto 변환
        List<FollowingsDto> result = followings.stream().map(e -> new FollowingsDto(e)).collect(Collectors.toList());
        return new DataResponse("200", "팔로잉 리스트 입니다.", result);
    }

}

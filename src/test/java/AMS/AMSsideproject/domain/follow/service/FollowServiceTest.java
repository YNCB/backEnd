package AMS.AMSsideproject.domain.follow.service;

import AMS.AMSsideproject.domain.follow.Follow;
import AMS.AMSsideproject.domain.follow.repository.FollowRepository;
import AMS.AMSsideproject.domain.user.Job;
import AMS.AMSsideproject.domain.user.LoginType;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import AMS.AMSsideproject.web.exception.AlreadyExistingFollow;
import AMS.AMSsideproject.web.exception.NotExistingFollow;
import AMS.AMSsideproject.web.exception.NotExistingUser;
import AMS.AMSsideproject.web.responseDto.follow.IsFollowDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class FollowServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    FollowRepository followRepository;

    @Test
    @Transactional
    public void 팔로우저장테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com", "test1","test1", LoginType.BASIC.name(), Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@naver.com", "test2","test2",LoginType.BASIC.name(),Job.학생.name(),"Java");

        User findUser1 = userService.join(userJoinForm1);
        User findUser2 = userService.join(userJoinForm2);

        //when
        Follow follow = followService.addFollow(findUser1.getUser_id(), findUser2.getUser_id());

        //then
        Follow findFollow = followRepository.findFollow(follow.getId());
        Assertions.assertThat(findFollow.getUser().getUser_id()).isEqualTo(findUser1.getUser_id());
        Assertions.assertThat(findFollow.getFollow().getUser_id()).isEqualTo(findUser2.getUser_id());
    }

    @Test
    public void 팔로우중복저장테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com", "test1","test1",LoginType.BASIC.name(),Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@naver.com", "test2","test2",LoginType.BASIC.name(),Job.학생.name(),"Java");

        User findUser1 = userService.join(userJoinForm1);
        User findUser2 = userService.join(userJoinForm2);

        //when
        Follow follow = followService.addFollow(findUser1.getUser_id(), findUser2.getUser_id());

        //then
        Assertions.assertThatThrownBy(() -> followService.addFollow(findUser1.getUser_id(), findUser2.getUser_id()))
                .isInstanceOf(AlreadyExistingFollow.class);
    }

    @Test
    @Transactional
    public void 팔로우저장에러테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com", "test1","test1",LoginType.BASIC.name(), Job.학생.name(), "Java");
        User findUser1 = userService.join(userJoinForm1);

        //when, then
        Assertions.assertThatThrownBy( () -> followService.addFollow(findUser1.getUser_id(), 1000L))
                .isInstanceOf(NotExistingUser.class);

    }

    @Test
    public void 팔로우삭제테스트() throws Exception {
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com", "test1","test1",LoginType.BASIC.name(),Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@naver.com", "test2","test2",LoginType.BASIC.name(),Job.학생.name(),"Java");
        User findUser1 = userService.join(userJoinForm1);
        User findUser2 = userService.join(userJoinForm2);

        Follow follow = followService.addFollow(findUser1.getUser_id(), findUser2.getUser_id());

        //when
        followService.deleteFollow(findUser1.getUser_id(), findUser2.getUser_id());

        //then
        Assertions.assertThat(followRepository.findFollow(follow.getId())).isNull();
    }

    @Test
    public void 팔로우삭제에러테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com", "test1","test1",LoginType.BASIC.name(),Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@naver.com", "test2","test2",LoginType.BASIC.name(),Job.학생.name(),"Java");
        User findUser1 = userService.join(userJoinForm1);
        User findUser2 = userService.join(userJoinForm2);

        //when & then
        Assertions.assertThatThrownBy(() -> followService.deleteFollow(findUser1.getUser_id(), findUser2.getUser_id()))
                .isInstanceOf(NotExistingFollow.class);
    }

    @Test
    public void 팔로워리스트검색테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com", "test1","test1",LoginType.BASIC.name(),Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@naver.com", "test2","test2",LoginType.BASIC.name(),Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm3 = new UserJoinForm2("test3@naver.com", "test3","test3",LoginType.BASIC.name(),Job.학생.name(),"Java");
        User findUser1 = userService.join(userJoinForm1);
        User findUser2 = userService.join(userJoinForm2);
        User findUser3 = userService.join(userJoinForm3);

        Follow follow1 = followService.addFollow(findUser1.getUser_id(), findUser2.getUser_id());
        Follow follow2 = followService.addFollow(findUser1.getUser_id(), findUser3.getUser_id());

        //when
        System.out.println("------------------------");
        List<Follow> findFollows = followService.findFollowersByUserId(findUser1.getUser_id());

        //then
        Assertions.assertThat(findFollows.size()).isEqualTo(2);
    }

    @Test
    public void 팔로잉리스트검색테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com", "test1","test1",LoginType.BASIC.name(),Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@naver.com", "test2","test2",LoginType.BASIC.name(),Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm3 = new UserJoinForm2("test3@naver.com", "test3","test3",LoginType.BASIC.name(),Job.학생.name(),"Java");
        User findUser1 = userService.join(userJoinForm1);
        User findUser2 = userService.join(userJoinForm2);
        User findUser3 = userService.join(userJoinForm3);

        Follow follow1 = followService.addFollow(findUser2.getUser_id(), findUser1.getUser_id());
        Follow follow2 = followService.addFollow(findUser3.getUser_id(), findUser1.getUser_id());

        //when
        System.out.println("-------------------");
        List<Follow> findFollowings = followService.findFollowingsByUserId(findUser1.getUser_id());

        //then
        Assertions.assertThat(findFollowings.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void 팔로우유무검색테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com", "test1","test1",LoginType.BASIC.name(),Job.학생.name(),"Java");
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@naver.com", "test2","test2",LoginType.BASIC.name(),Job.학생.name(),"Java");

        User findUser1 = userService.join(userJoinForm1);
        User findUser2 = userService.join(userJoinForm2);

        Follow follow1 = followService.addFollow(findUser1.getUser_id(), findUser2.getUser_id());

        //when
        Boolean isFollow1 = followService.findIsFollow(findUser1.getUser_id(), findUser2.getUser_id());
        Boolean isFollow2 = followService.findIsFollow(findUser2.getUser_id(), findUser1.getUser_id());

        //then
        Assertions.assertThat(isFollow1).isTrue();
        Assertions.assertThat(isFollow2).isFalse();
    }

}
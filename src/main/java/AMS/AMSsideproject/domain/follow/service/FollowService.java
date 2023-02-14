package AMS.AMSsideproject.domain.follow.service;

import AMS.AMSsideproject.domain.follow.Follow;
import AMS.AMSsideproject.domain.follow.repository.FollowRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepositoryImpl;
import AMS.AMSsideproject.web.exception.AlreadyExistingFollow;
import AMS.AMSsideproject.web.exception.NotExistingFollow;
import AMS.AMSsideproject.web.exception.NotExistingUser;
import AMS.AMSsideproject.web.responseDto.follow.IsFollowDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepositoryImpl userRepository;

    //팔로우 저장
    @Transactional
    public Follow addFollow(Long userId, Long followId) {

        Optional<User> user = userRepository.findByUserId(userId);
        Optional<User> follow = userRepository.findByUserId(followId);

        follow.orElseThrow( () -> new NotExistingUser("존재하지 않은 사용자입니다."));

        Optional<Follow> findFollow = followRepository.findFollowById(userId, followId);
        if(findFollow.isPresent())
            throw new AlreadyExistingFollow("이미 팔로우 하였습니다.");

        Follow createFollow = Follow.createFollow(user.get(), follow.get());
        followRepository.save(createFollow);
        return createFollow;
    }

    //팔로우 유무 검색
    @Transactional(readOnly = true)
    public Boolean findIsFollow(Long userId, Long followId){
        return followRepository.existsFollow(userId, followId);
    }

    //팔로우 삭제
    @Transactional
    public void deleteFollow(Long userId, Long followId) {

        Optional<Follow> follow = followRepository.findFollowById(userId, followId);
        follow.orElseThrow( () -> new NotExistingFollow("존재하지 않은 팔로우 입니다."));

        followRepository.delete(follow.get());
    }

    //팔로워 리스트 검색
    public List<Follow> findFollowersByUserId(Long userId) {

        List<Follow> followers = followRepository.findFollowers(userId);
        return followers;
    }

    //팔로잉 리스트 검색
    public List<Follow> findFollowingsByUserId(Long userId) {

        List<Follow> followings = followRepository.findFollowings(userId);
        return followings;
    }

}

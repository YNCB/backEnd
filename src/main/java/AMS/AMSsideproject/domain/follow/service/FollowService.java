package AMS.AMSsideproject.domain.follow.service;

import AMS.AMSsideproject.domain.follow.Follow;
import AMS.AMSsideproject.domain.follow.repository.FollowRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepositoryImpl;
import AMS.AMSsideproject.web.exception.NotExistingUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        if(follow.isEmpty())
            throw new NotExistingUser("존재하지 않은 사용자입니다.");

        Follow createFollow = Follow.createFollow(user.get(), follow.get());
        followRepository.save(createFollow);
        return createFollow;
    }

    //팔로우 삭제
    @Transactional
    public void deleteFollow(Long followId) {

        Follow follow = followRepository.findFollow(followId);
        followRepository.delete(follow);
    }

    //팔로워 검색
    public List<Follow> findFollowersByUserId(Long userId) {

        List<Follow> followers = followRepository.findFollowers(userId);
        return followers;
    }

    //팔로잉 검색
    public List<Follow> findFollowingsByUserId(Long userId) {

        List<Follow> followings = followRepository.findFollowings(userId);
        return followings;
    }

}

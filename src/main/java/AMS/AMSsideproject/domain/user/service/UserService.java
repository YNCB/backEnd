package AMS.AMSsideproject.domain.user.service;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.web.apiController.user.form.UserEditForm;
import AMS.AMSsideproject.web.apiController.user.form.UserJoinForm;
import AMS.AMSsideproject.web.exception.DuplicationUserNickname;
import AMS.AMSsideproject.web.exception.UserNullException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    //회원 가입 메서드
    @Transactional
    public User join(UserJoinForm joinForm) {

        //사용자 닉네임 중복 검사
        validDuplicateUserNickName(joinForm.getNickname());

        //정상적인 사용자
        User user = User.createUser(joinForm);
        return userRepository.save(user);
    }


    public User findUserByUserId(Long userId) {
        User findUser = userRepository.findByUserId(userId);
        return findUser;
    }
    //회원 조회 메서드 - 닉네임
    public User findUserByNickName(String nickName) {
        Optional<User> findUser = userRepository.findByNickName(nickName);

        if(findUser.isEmpty())
            throw new UserNullException("존재하지 않는 회원입니다.");
        return findUser.get();
    }

    //회원 조회 메서드 - 소셜 고유 id
    public User findUserBySocialId(String socialId) {
        Optional<User> findUser = userRepository.findBySocialId(socialId);

        if(findUser.isEmpty())
            throw new UserNullException("회원가입을 해주시기 바랍니다.");
        return findUser.get();
    }

    //회원 수정 메서드 -> 1차로 닉네임만 변경가능하게 구현
    @Transactional
    public User update(Long user_id, UserEditForm userEditForm) {
        User findUser = userRepository.findByUserId(user_id);

        //닉네임 중복 검사
        validDuplicateUserNickName(userEditForm.getNickname());

        findUser.setNickname(userEditForm.getNickname());
        findUser.setJob(userEditForm.getJob());
        findUser.setMain_lang(userEditForm.getMain_lang());

        return findUser;
    }



    //회원 닉네임을 기준으로 중복 검사
    private void validDuplicateUserNickName(String nickname) {
        userRepository.findByNickName(nickname)
                .ifPresent(e -> {
                    throw new DuplicationUserNickname("이미 존재하는 닉네임입니다.");
                });
    }
}

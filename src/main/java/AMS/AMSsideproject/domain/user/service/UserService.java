package AMS.AMSsideproject.domain.user.service;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserEditForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import AMS.AMSsideproject.web.exception.DuplicationUserNickname;
import AMS.AMSsideproject.web.exception.UserNullException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encodePwd;

    //회원 가입 메서드
    @Transactional
    public User join(UserJoinForm2 joinForm) {

        joinForm.setPassword(encodePwd.encode(joinForm.getPassword()));

        //정상적인 사용자
        User user = User.createUser(joinForm);
        return userRepository.save(user);
    }

    //회원 닉네임 중복 검사 메서드
    public String validDuplicateUserNickName(String nickName) {

        userRepository.findByNickName(nickName)
                .ifPresent(e -> {
                    throw new DuplicationUserNickname("이미 존재하는 닉네임 입니다.", nickName);
                });
        return nickName;
    }

    //회원 조회 메서드 - 고유 id
    public User findUserByUserId(Long userId) {
        Optional<User> findUser = userRepository.findByUserId(userId);

        if(findUser.isEmpty())
            throw new UserNullException("존재하지 않는 회원입니다.");
        return findUser.get();
    }

    //회원 조회 메서드 - 닉네임
    public User findUserByNickName(String nickName) {
        Optional<User> findUser = userRepository.findByNickName(nickName);

        if(findUser.isEmpty())
            throw new UserNullException("존재하지 않는 회원입니다.");
        return findUser.get();
    }

    //회원 조회 메서드 - 회원 id
    public User findUserByEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);

        if(findUser.isEmpty())
            throw new UserNullException("존재하지 않은 회원입니다.");
        return findUser.get();
    }

    //회원 수정 메서드 -> 1차로 닉네임만 변경가능하게 구현
    @Transactional
    public User update(Long user_id, UserEditForm userEditForm) {
        User findUser = userRepository.findByUserId(user_id).get();
        findUser.update(userEditForm);

        return findUser;
    }



}

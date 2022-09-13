package AMS.AMSsideproject.domain.user.repository;


import AMS.AMSsideproject.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    public User save(User user); //회원 저장 메서드
    public Optional<User> findByUserId(Long userId); //회원 고유 id로 찾는 메서드
    public Optional<User> findByEmail(String email); //회원 이메일(id)로 회원을 찾는 메서드
    public Optional<User> findByNickName(String nickName); //회원 닉네임으로 찾는 메서드

}

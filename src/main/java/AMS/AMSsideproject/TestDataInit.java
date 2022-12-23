package AMS.AMSsideproject;

import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public void init() {

    }
}

package AMS.AMSsideproject.domain.post.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.repository.TagRepository;
import AMS.AMSsideproject.domain.tag.Tag.service.TagService;
import AMS.AMSsideproject.domain.tag.postTag.PostTag;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final TagService tagService;
    private final TagRepository tagRepository;

    //게시물 등록
    @Transactional
    public Post registration(Long userId, PostSaveForm postSaveForm) {

        User findUser = userService.findUserByUserId(userId);
        Post post = Post.createPost(findUser, postSaveForm);

        //기존에 없던 tag 는 저장시키기
        tagService.addFromTagList(postSaveForm.getTags());

        //없던 tag는 다 저장시켰기 때문에 전달받은 tagList에 있는 tag들은 전부 tag table에 존재
        for(String tagName : postSaveForm.getTags()) {
            Tag findTagName = tagRepository.findByTagName(tagName).get();
            PostTag postTag = PostTag.createPostTag(findTagName);
            post.addPostTag(postTag);
        }

        postRepository.save(post);
        return post;
    }





}

package AMS.AMSsideproject.domain.post.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutAllUser;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutSpecificUser;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.repository.TagRepository;
import AMS.AMSsideproject.domain.tag.Tag.service.TagService;
import AMS.AMSsideproject.domain.tag.postTag.PostTag;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    //전체 유저 게시물에 대한 게시물 조회
    public Slice<Post> findPostsAboutAllUser(SearchFormAboutAllUser searchForm) {

        BooleanBuilder builder = new BooleanBuilder();
        // 페이징, 정렬 기준 세팅하기
        Pageable pageable = null;
        if(StringUtils.hasText(searchForm.getOrderKey())) {

            List<Sort.Order> orders = new ArrayList<>();
            //동적쿼리 where 문 생성
            if(searchForm.getOrderKey().equals("latest")) { //최신순
                orders.add(Sort.Order.desc("redate"));
                if(searchForm.getLastPostId() != null) {
                    builder.and(QPost.post.post_id.lt(searchForm.getLastPostId()));
                }
            }else if(searchForm.getOrderKey().equals("oldest")){ //오래된순
                orders.add(Sort.Order.asc("redate"));
                if(searchForm.getLastPostId() !=null) {
                    builder.and(QPost.post.post_id.gt(searchForm.getLastPostId()));
                }
            }
            else if(searchForm.getOrderKey().equals("likeNum")) { //좋아요순
                orders.add(Sort.Order.desc(searchForm.getOrderKey()));
                if(searchForm.getLastLikeNum()!=null && searchForm.getLastPostId()!=null) {
                    builder.and(QPost.post.likeNum.eq(searchForm.getLastLikeNum()).and(QPost.post.post_id.gt(searchForm.getLastPostId())));
                    builder.or(QPost.post.likeNum.lt(searchForm.getLastLikeNum()));
                }
            }
            else if(searchForm.getOrderKey().equals("replyNum")) { //댓글순
                orders.add(Sort.Order.desc(searchForm.getOrderKey()));
                if(searchForm.getLastReplyNum()!=null && searchForm.getLastPostId()!=null) {
                    builder.and(QPost.post.replyNum.eq(searchForm.getLastReplyNum()).and(QPost.post.post_id.gt(searchForm.getLastPostId())));
                    builder.or(QPost.post.replyNum.lt(searchForm.getLastReplyNum()));
                }
            }
            pageable = PageRequest.of(0, 3, Sort.by(orders)); //10개 씩
        }else
            pageable = PageRequest.of(0,3);

        return postRepository.findPostsByAllUser(searchForm.getLanguage(), searchForm.getSearchTitle(), pageable , builder);
    }

    //특정 유저에 대한 게시물 조회
    public Slice<Post> findPostsAboutSpecificUser(String nickname, SearchFormAboutSpecificUser searchForm) {

        BooleanBuilder builder = new BooleanBuilder();
        // 페이징, 정렬 기준 세팅하기
        Pageable pageable = null;
        if(StringUtils.hasText(searchForm.getOrderKey())) {

            List<Sort.Order> orders = new ArrayList<>();
            //동적쿼리 where 문 생성
            if(searchForm.getOrderKey().equals("latest")) { //최신순
                orders.add(Sort.Order.desc("redate"));
                if(searchForm.getLastPostId() != null) {
                    builder.and(QPost.post.post_id.lt(searchForm.getLastPostId()));
                }
            }else if(searchForm.getOrderKey().equals("oldest")){ //오래된순
                orders.add(Sort.Order.asc("redate"));
                if(searchForm.getLastPostId() !=null) {
                    builder.and(QPost.post.post_id.gt(searchForm.getLastPostId()));
                }
            }
            else if(searchForm.getOrderKey().equals("likeNum")) { //좋아요순
                orders.add(Sort.Order.desc(searchForm.getOrderKey()));
                if(searchForm.getLastLikeNum()!=null && searchForm.getLastPostId()!=null) {
                    builder.and(QPost.post.likeNum.eq(searchForm.getLastLikeNum()).and(QPost.post.post_id.gt(searchForm.getLastPostId())));
                    builder.or(QPost.post.likeNum.lt(searchForm.getLastLikeNum()));
                }
            }
            else if(searchForm.getOrderKey().equals("replyNum")) { //댓글순
                orders.add(Sort.Order.desc(searchForm.getOrderKey()));
                if(searchForm.getLastReplyNum()!=null && searchForm.getLastPostId()!=null) {
                    builder.and(QPost.post.replyNum.eq(searchForm.getLastReplyNum()).and(QPost.post.post_id.gt(searchForm.getLastPostId())));
                    builder.or(QPost.post.replyNum.lt(searchForm.getLastReplyNum()));
                }
            }
            pageable = PageRequest.of(0, 3, Sort.by(orders)); //10개 씩
        }else //구조상 이경우는 없음.
            pageable = PageRequest.of(0,3);
        return postRepository.findPostsBySpecificUser(nickname, searchForm, pageable, builder);
    }





    //게시물 좋아요 수 증가
    @Transactional
    public Post addPostLikeNum(Long postId , Long num) {
        Post findPost = postRepository.findPostByPostId(postId);
        findPost.addLikeNum(num);
        return findPost;
    }



}

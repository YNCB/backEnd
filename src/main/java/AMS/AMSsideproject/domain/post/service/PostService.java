package AMS.AMSsideproject.domain.post.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutAllUserPost;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutOneSelfPost;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutOtherUser;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.service.TagService;
import AMS.AMSsideproject.domain.tag.postTag.PostTag;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostEditForm;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final TagService tagService;

    //게시물 등록
    @Transactional
    public Post registration(Long userId, PostSaveForm postSaveForm) {

        User findUser = userService.findUserByUserId(userId);
        Post post = Post.createPost(findUser, postSaveForm);

        //기존에 없던 tag 는 저장시키기
        List<Tag> addTags = tagService.addFromTagList(postSaveForm.getTags());

        //없던 tag는 다 저장시켰기 때문에 전달받은 tagList에 있는 tag들은 전부 tag table에 존재
        for(Tag tag : addTags) {
            PostTag postTag = PostTag.createPostTag(tag);
            post.addPostTag(postTag); //양방향 연관관계
        }

        postRepository.save(post);
        return post;
    }

    //전체 유저 게시물에 대한 게시물 조회
    public Slice<Post> findPostsAboutAllUser(SearchFormAboutAllUserPost searchForm) {

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
    public Slice<Post> findPostsAboutOtherUser(String nickname, SearchFormAboutOtherUser searchForm) {

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
                orders.add(Sort.Order.asc("post_id")); //같은 likeNum에 대해서는 post_id로 오름차순 정렬 기준으로 정의
                if(searchForm.getLastLikeNum()!=null && searchForm.getLastPostId()!=null) {
                    builder.and(QPost.post.likeNum.eq(searchForm.getLastLikeNum()).and(QPost.post.post_id.gt(searchForm.getLastPostId())));
                    builder.or(QPost.post.likeNum.lt(searchForm.getLastLikeNum()));
                }
            }
            else if(searchForm.getOrderKey().equals("replyNum")) { //댓글순
                orders.add(Sort.Order.desc(searchForm.getOrderKey()));
                orders.add(Sort.Order.asc("post_id")); //같은 replyNum에 대해서는 post_id로 오름차순 정렬 기준으로 정의
                if(searchForm.getLastReplyNum()!=null && searchForm.getLastPostId()!=null) {
                    builder.and(QPost.post.replyNum.eq(searchForm.getLastReplyNum()).and(QPost.post.post_id.gt(searchForm.getLastPostId())));
                    builder.or(QPost.post.replyNum.lt(searchForm.getLastReplyNum()));
                }
            }
            pageable = PageRequest.of(0, 3, Sort.by(orders)); //10개 씩
        }else //구조상 이경우는 없음.
            pageable = PageRequest.of(0,3);

        return postRepository.findPostsByOtherUser(nickname, searchForm.getLanguage(), searchForm.getSearchTitle(), pageable, builder);
    }

    //내페이지에 대한 게시물 조회
    public Slice<Post> findPostsAboutOneSelf(String nickname, SearchFormAboutOneSelfPost searchForm) {

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
                orders.add(Sort.Order.asc("post_id")); //같은 likeNum에 대해서는 post_id로 오름차순 정렬 기준으로 정의
                if(searchForm.getLastLikeNum()!=null && searchForm.getLastPostId()!=null) {
                    builder.and(QPost.post.likeNum.eq(searchForm.getLastLikeNum()).and(QPost.post.post_id.gt(searchForm.getLastPostId()))); //같은 개수를 가진 게시물 처리
                    builder.or(QPost.post.likeNum.lt(searchForm.getLastLikeNum()));
                }
            }
            else if(searchForm.getOrderKey().equals("replyNum")) { //댓글순
                orders.add(Sort.Order.desc(searchForm.getOrderKey()));
                orders.add(Sort.Order.asc("post_id")); //같은 replyNum에 대해서는 post_id로 오름차순 정렬 기준으로 정의
                if(searchForm.getLastReplyNum()!=null && searchForm.getLastPostId()!=null) {
                    builder.and(QPost.post.replyNum.eq(searchForm.getLastReplyNum()).and(QPost.post.post_id.gt(searchForm.getLastPostId())));
                    builder.or(QPost.post.replyNum.lt(searchForm.getLastReplyNum()));
                }
            }
            pageable = PageRequest.of(0, 3, Sort.by(orders)); //10개 씩
        }else //구조상 이경우는 없음.
            pageable = PageRequest.of(0,3);

        return postRepository.findPostsByOneSelf(nickname, searchForm.getTags(), searchForm.getType(), searchForm.getSearchTitle(), pageable, builder);
    }










    //게시물 수정(업데이트) - "지연감지 사용!" -> 근데 쿼리문이 너무 많이 나가는데.. 이건 어쩔수 없지 않나?!
    @Transactional
    public void updatePost(Long postId, PostEditForm postEditForm) {

        //게시물 찾기
        Post findPost = postRepository.findPostByPostId(postId);

        //기존 게시물 태그중 삭제될 태그들
        List<PostTag> needDeleteTags = checkDeletePostTags(findPost.getPostTagList(), postEditForm.getTags());

        //기존 게시물에 추가될 태그들
        List<Tag> newAddTags = checkAddPostTags(findPost.getPostTagList(), postEditForm.getTags());
        //추가될 태그들 추가
        for(Tag tag : newAddTags) {
            PostTag newPostTag = PostTag.createPostTag(tag);
            findPost.addPostTag(newPostTag);
        }

        //삭제될 태그들 삭제
        needDeleteTags.stream().forEach(p -> findPost.getPostTagList().remove(p));

        //태그를 제외한 나머지 게시물 정보들 업데이트
        findPost.setPost(postEditForm);
    }

    //게시물 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post findPost = postRepository.findPostByPostId(postId);
        postRepository.delete(findPost);
    }




//    //게시물 좋아요 수 증가
//    @Transactional
//    public Post addPostLikeNum(Long postId , Long num) {
//        Post findPost = postRepository.findPostByPostId(postId);
//        findPost.addLikeNum(num);
//        return findPost;
//    }






    //게시물 수정시 기존태그중 삭제될 태그 찾기
    private List<PostTag> checkDeletePostTags(List<PostTag> oldTags, List<String> newTags) {

        List<PostTag> deleteTags = new ArrayList<>();
        for(PostTag postTag : oldTags) {
            //프록시 초기화 되면서 sql문 나감. "batch" 옵션으로 나감
            String oldTagName = postTag.getTag().getName();
            boolean contains = newTags.stream().anyMatch(t -> t.equals(oldTagName));

            if(contains == false)
                deleteTags.add(postTag);
        }
        return deleteTags;
    }

    //게시물 수정시 새롭게 추가될 태그 찾기
    private List<Tag> checkAddPostTags(List<PostTag> oldTags, List<String> newTags) {

        List<String> newAddTags = new ArrayList<>();

        for(String newTag : newTags) {
            //기존 게시물에 태그들이 없을수 있다.
            if(!oldTags.isEmpty()) {
                boolean contains = oldTags.stream().anyMatch(p -> p.getTag().getName().equals(newTag));

                if(contains == false) //새롭게 추가될 태그인 경우
                    newAddTags.add(newTag);

            }else
                newAddTags.add(newTag); //새롭게 태그 추가
        }

        //게시물에 새롭게 추가할 태그들이 "태그테이블"에 있는지 체크. 없으면 생성 및 추가
        List<Tag> tags = tagService.addFromTagList(newAddTags);
        return tags;
    }


}

package AMS.AMSsideproject.domain.post.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.web.apiController.post.requestForm.*;
import AMS.AMSsideproject.domain.post.service.form.NoOffsetPage;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.service.TagService;
import AMS.AMSsideproject.domain.tag.postTag.PostTag;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        //없던 tag는 다 저장시켰기 때문에 전달받은 tagList에 있는 tag들은 전부 tag table에 존재함
        for(Tag tag : addTags) {
            PostTag postTag = PostTag.createPostTag(tag);
            postTag.plusTagNum(); //태그 언급 개수 늘리기
            post.addPostTag(postTag); //양방향 연관관계 -> "cascade" 로 인해 post 가 저장되면서 postTag 테이블에도 자동 저장
        }

        postRepository.save(post);
        return post;
    }

    //전체 유저 게시물에 대한 게시물 조회
    public Slice<Post> findPostsAboutAllUser(SearchFormAboutAllUserPost searchForm) {

        NoOffsetPage noOffsetPage = NoOffsetPageNation(searchForm.getOrderKey(), searchForm.getLastPostId(),
                searchForm.getLastLikeNum(), searchForm.getLastReplyNum(), searchForm.getCountView());

        return postRepository.findPostsByAllUser(searchForm.getLanguage(), searchForm.getSearchTitle(),
                noOffsetPage.getPageable(), noOffsetPage.getBooleanBuilder());
    }

    //다른 유저에 대한 게시물 조회
    public Slice<Post> findPostsAboutOtherUser(String nickname, SearchFormAboutOtherUserPost searchForm) {

        NoOffsetPage noOffsetPage = NoOffsetPageNation(searchForm.getOrderKey(), searchForm.getLastPostId(), searchForm.getLastLikeNum(),
                searchForm.getLastReplyNum(), searchForm.getCountView());

        return postRepository.findPostsByOtherUser(nickname, searchForm.getLanguage(), searchForm.getSearchTitle(),
                noOffsetPage.getPageable(), noOffsetPage.getBooleanBuilder());
    }

    //내페이지에 대한 게시물 조회
    public Slice<Post> findPostsAboutOneSelf(String nickname, SearchFormAboutSelfUserPost searchForm) {

        NoOffsetPage noOffsetPage = NoOffsetPageNation(searchForm.getOrderKey(), searchForm.getLastPostId(), searchForm.getLastLikeNum(),
                searchForm.getLastReplyNum(), searchForm.getCountView());

        return postRepository.findPostsByOneSelf(nickname, searchForm.getTags(), searchForm.getType(), searchForm.getLanguage(),
                searchForm.getSearchTitle(), noOffsetPage.getPageable(), noOffsetPage.getBooleanBuilder());
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
            newPostTag.plusTagNum(); //태그 언급수 늘리기
            findPost.addPostTag(newPostTag);
        }

        //삭제될 태그들 삭제
        needDeleteTags.stream().forEach(p -> {
            p.reduceTagNum(); //태그 언급수 줄이기
            findPost.getPostTagList().remove(p);
        });

        //태그를 제외한 나머지 게시물 정보들 업데이트
        findPost.setPost(postEditForm);
    }

    //게시물 삭제
    @Transactional
    public void deletePost(Long postId) {

        Post findPost = postRepository.findPostByPostId(postId);
        findPost.getPostTagList().stream().forEach(p -> p.reduceTagNum());
        postRepository.delete(findPost);
    }

    //조회순 늘리기 -> "쿠기를 이용한 중복 방지 기능 추가"
    @Transactional
    public void readPost(Long postId ,Cookie postViewCookie, HttpServletResponse response){
        /**
         * - [1]지연 감지 사용
         * 1. 게시물 조회수 업데이트 되는 상황에서 -> 게시물 찾기, update => 쿼리 2번
         * 2. 게시물 조회수 업데이트 되지 않는 상황 -> 게시물 찾기 => 쿼리 1번
         *
         * - [2] 직접 게시물 조회수 update
         * 1. 게시물 조회수 업데이트 되는 상황에서 -> update => 쿼리 1번
         * 2. 게시물 조회수 업데이트 되지 않는 상황 -> 쿼리 0번
         */
        //Post findPost = postRepository.findPostByPostId(postId); //[1]

        if(postViewCookie != null) { //기존에 해당 기능의 쿠키가 존재하는 경우 -> 쿠기내에 해당 "게시물 Id" 가 존재하는지 판별

            if(!postViewCookie.getValue().contains("[" + postId + "]")) { //쿠기내에 해당 게시물 Id가 존재 x -> 추가해줘야함

                //findPost.addCountView(); //[1]
                postRepository.updateViewCount(postId); //[2]

                postViewCookie.setValue(postViewCookie.getValue() + "_[" + postId + "]" ); //기존 쿠기내에 해당 게시물 Id 추가
                postViewCookie.setPath("/");
                postViewCookie.setMaxAge(60 * 60 * 24);  //쿠키의 수명을 24시간 설정하니 기존에 있던 게시물Id 에 대해서도 모두 적용!
                response.addCookie(postViewCookie);
            }

        }else { //해당 기능의 필요한 쿠키가 존재x -> 쿠기에 "게시물 Id"를 추가

            //findPost.addCountView(); //게시물 조회순 늘리기 [1]
            postRepository.updateViewCount(postId); //[2]

            Cookie newCookie = new Cookie("postView", "[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); //24시간 수명 설정
            response.addCookie(newCookie);
        }
    }

    //무한 스크롤를 위한 파라미터 세팅 함수
    private NoOffsetPage NoOffsetPageNation(String orderKey, Long lastPostId, Integer lastLikeNum, Integer lastReplyNum, Long lastCountView) {

        BooleanBuilder builder = new BooleanBuilder();
        Pageable pageable = null;

        // 페이징, 정렬 기준 세팅하기
        if(StringUtils.hasText(orderKey)) {

            List<Sort.Order> orders = new ArrayList<>();
            //동적쿼리 where 문 생성
            if(orderKey.equals("latest")) { //최신순
                orders.add(Sort.Order.desc("redate"));
                if(lastPostId != null) {
                    builder.and(QPost.post.post_id.lt(lastPostId));
                }
            }else if(orderKey.equals("oldest")){ //오래된순
                orders.add(Sort.Order.asc("redate"));
                if(lastPostId !=null) {
                    builder.and(QPost.post.post_id.gt(lastPostId));
                }
            }
            else if(orderKey.equals("likeNum")) { //좋아요순
                orders.add(Sort.Order.desc(orderKey));
                orders.add(Sort.Order.asc("post_id")); //같은 likeNum에 대해서는 post_id로 오름차순 정렬 기준으로 정의
                if(lastLikeNum!=null && lastPostId!=null) {
                    builder.and(QPost.post.likeNum.eq(lastLikeNum).and(QPost.post.post_id.gt(lastPostId))); //같은 개수를 가진 게시물 처리
                    builder.or(QPost.post.likeNum.lt(lastLikeNum));
                }
            }
            else if(orderKey.equals("replyNum")) { //댓글순
                orders.add(Sort.Order.desc(orderKey));
                orders.add(Sort.Order.asc("post_id")); //같은 replyNum에 대해서는 post_id로 오름차순 정렬 기준으로 정의
                if(lastReplyNum!=null && lastPostId!=null) {
                    builder.and(QPost.post.replyNum.eq(lastReplyNum).and(QPost.post.post_id.gt(lastPostId)));
                    builder.or(QPost.post.replyNum.lt(lastReplyNum));
                }
            }
            else if(orderKey.equals("countView")) {
                orders.add(Sort.Order.desc(orderKey)); //내림차순 정렬
                orders.add(Sort.Order.asc("post_id")); //같은 likeNum에 대해서는 post_id로 오름차순 정렬 기준으로 정의
                if(lastCountView != null && lastPostId!=null) {
                    builder.and(QPost.post.countView.eq(lastCountView).and(QPost.post.post_id.gt(lastPostId)));
                    builder.or(QPost.post.countView.lt(lastCountView));
                }
            }

            pageable = PageRequest.of(0, 3, Sort.by(orders)); //10개 씩

        }else //구조상 이경우는 없음.
            pageable = PageRequest.of(0,3);

        return new NoOffsetPage(pageable, builder);
    }

    //게시물 수정시 기존태그중 삭제될 태그 찾기
    private List<PostTag> checkDeletePostTags(List<PostTag> oldTags, List<String> newTags) {

        List<PostTag> deleteTags = new ArrayList<>();
        for(PostTag postTag : oldTags) {

            //프록시 초기화 되면서 sql문 나감. "batch" 옵션으로 나감
            String oldTagName = postTag.getTag().getName();
            boolean contains = newTags.stream().anyMatch(t -> t.equals(oldTagName));

            if(contains == false) {
                deleteTags.add(postTag);
            }
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

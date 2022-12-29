package AMS.AMSsideproject.domain.reply.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.domain.reply.Reply;
import AMS.AMSsideproject.domain.reply.repository.ReplyRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplyEditForm;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplySaveForm;
import AMS.AMSsideproject.web.exception.post.NotExistingPost;
import AMS.AMSsideproject.web.exception.reply.NotExistingReply;
import AMS.AMSsideproject.web.exception.reply.NotUserEq;
import AMS.AMSsideproject.web.responseDto.reply.RepliesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    //댓글 저장
    @Transactional
    public Reply addReply(Long postId, Long userId, ReplySaveForm replySaveForm) {

        //존재하는 게시물인지 확인
        Post findPost = postRepository.findPostByPostId(postId);
        if(findPost==null)
            throw new NotExistingPost("존재하지 않는 게시물 입니다.");

        Optional<User> findUser = userRepository.findByUserId(userId);
        Reply parent = null;
        if(replySaveForm.getParent_id() != null){ //루트 댓글이 아닌 경우 에러 경우 처리
            parent = replyRepository.findReply(replySaveForm.getParent_id());
            if(parent == null)  //루트 댓글이 없어진 경우 대비
                throw new NotExistingReply("상위 댓글이 존재하지 않습니다.");
        }

        Reply newReply = Reply.createReply(replySaveForm.getTitle(), replySaveForm.getContent(), findUser.get(), findPost);
        if(parent != null) { //루트 댓글이 아닌 경우
            newReply.setParent(parent); //parent 댓글과 양방향 연관관계 성정
        }

        replyRepository.save(newReply); //댓글 저장
        findPost.addReplyNum(); //게시물에 달린 댓글수 늘리기 - 댓글, 대댓글 모든 댓글에 대해서 개수 늘림!!!!!!

        return newReply;
    }

    //댓글 리스트 조회
    //서비스 단에서 바로 "Dto"로 변환한 경우임
    public List<RepliesDto> findRepliesByPostId(Long postId) {

        List<Reply> replies = replyRepository.findReplies(postId); //댓글 리스트 전체 조회

        List<RepliesDto> repliesDtoList = new ArrayList<>();
        Map<Long, RepliesDto> map = new HashMap<>(); //상위 부모를 한번에 알기 위해서 임시로 사용하는 변수
        /**
         * 프로그래밍의 기본적인 개념
         * - 하나의 객체를 다른 두배열에 각각 넣고 객체의 값을 변경 시키면 두 배열에 담겨있는 해당 객체의 값이 같이 바뀜
         *   -> 왜냐하면 같은 객체를 사용하는것은 즉 같은 주소값을 사용하니 해당 객체가 복사되서 넣어지는것이 아니라
         *      같은 주소값의 객체가 각각 두배열에 들어가는것이기 때문에!!!
         */
        for(Reply reply : replies) {
            RepliesDto repliesDto = RepliesDto.createRepliesDto(reply);

            map.put(repliesDto.getReply_id(), repliesDto);
            if(reply.getParent() != null) {
                map.get(reply.getParent().getReply_id()).getChildren().add(repliesDto);
            }else{
                repliesDtoList.add(repliesDto);
            }
        }
        return repliesDtoList;
    }

    //댓글 조회(수정폼 줄때만 사용됨)
    public Reply findReplyByReplyId(Long replyId) {

        Reply findReply = replyRepository.findReply(replyId);
        return findReply;
    }

    //댓글 삭제
    @Transactional
    public void deleteReply(Long replyId, Long userId) {

        Reply findReply = replyRepository.findReply(replyId);
        if(findReply.getUser().getUser_id() != userId)  //프록시 초기화
            throw new NotUserEq("권한이 없습니다.");

        replyRepository.delete(findReply); //"orphanRemoval=true" 로 인해 자식 댓글도 자동 삭제!!
    }

    //댓글 수정
    @Transactional
    public void updateReply(Long userId, Long replyId, ReplyEditForm form) {

        Reply findReply = replyRepository.findReply(replyId);
        if(findReply.getUser().getUser_id() != userId)
            throw new NotUserEq("권한이 없습니다.");

        findReply.edit(form.getTitle(), form.getContent());
    }
}

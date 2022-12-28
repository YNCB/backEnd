package AMS.AMSsideproject.domain.reply.service;

import AMS.AMSsideproject.domain.reply.Reply;
import AMS.AMSsideproject.domain.reply.repository.ReplyRepository;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplySaveForm;
import AMS.AMSsideproject.web.responseDto.reply.RepliesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;

    @Transactional
    public Long addReply(Long postId, Long userId, ReplySaveForm replySaveForm) {

        return null;
    }

    public List<RepliesDto> findRepliesByPostId(Long postId) {

        List<Reply> replies = replyRepository.findReplies(postId); //댓글 리스트 전체 조회

        List<RepliesDto> repliesDtoList = new ArrayList<>();
        Map<Long, RepliesDto> map = new HashMap<>();
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

}

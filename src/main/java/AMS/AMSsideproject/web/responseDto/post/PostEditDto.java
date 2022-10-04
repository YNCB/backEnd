package AMS.AMSsideproject.web.responseDto.post;

import AMS.AMSsideproject.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEditDto {

    private String title;
    private String problem_uri;
    private String content;
    private String type;
    private String language;
    private Integer level;
    private List<String> tags;

    static public PostEditDto create(Post post) {
        PostEditDto postEditDto = new PostEditDto();
        postEditDto.setTitle(post.getTitle());
        postEditDto.setProblem_uri(post.getProblem_uri());
        postEditDto.setContent(post.getContext());
        postEditDto.setType(post.getType());
        postEditDto.setLanguage(post.getLanguage());
        postEditDto.setLevel(post.getLevel());

        //프록시 초기화 -> Post_tag 테이블 쿼리 한번 + tag 테이블 쿼리 한번씩 나간다.(원래는 각 2개씩 나가야되는데 betch 로 인해 각각 한번씩만일단 나감)
        List<String> tags = post.getPostTagList().stream()
                .map(p -> new String(p.getTag().getName()))
                .collect(Collectors.toList());
        postEditDto.setTags(tags);
        return postEditDto;
    }
}

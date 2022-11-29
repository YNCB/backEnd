package AMS.AMSsideproject.web.responseDto.post;

import AMS.AMSsideproject.domain.post.Post;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(example = "test")
    private String title;
    @ApiModelProperty(example = "test")
    private String problem_uri;
    @ApiModelProperty(example = "test")
    private String content;
    @ApiModelProperty(example = "alone")
    private String type;
    @ApiModelProperty(example = "Java")
    private String language;
    @ApiModelProperty(example = "3")
    private Integer level;
    @ApiModelProperty(example = "DFS", notes = "문제 태그들 여러개 가능")
    private List<String> tags;






    public PostEditDto(String title, String problem_uri, String content, String type, String language, Integer level) {

        this.title = title;
        this.problem_uri = problem_uri;
        this.content = content;
        this.type = type;
        this.language = language;
        this.level = level;
    }

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

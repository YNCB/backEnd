package AMS.AMSsideproject.domain.post.repository.form;

import lombok.Data;

import java.util.List;

@Data
public class SearchFormAboutSpecificUser {

    private List<String> tagList;
    private String type;

    private String language;
    private String searchTitle;


    private String likeNum;

}

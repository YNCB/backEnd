package AMS.AMSsideproject.domain.post.repository;

import lombok.Data;

import java.util.List;

@Data
public class SearchForm {

    private List<String> tagList;
    private String language;
    private String type;

}

package AMS.AMSsideproject.domain.tag.Tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;
    private String name;

    //생성 메서드
    public static Tag createTag(String tagName) {
        Tag tag = new Tag();
        tag.name = tagName;
        return tag;
    }
}

package AMS.AMSsideproject.domain.tag.Tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;

    private String name;

    private Integer num;

    //생성 메서드
    public static Tag createTag(String tagName) {
        Tag tag = new Tag();
        tag.name = tagName;
        tag.num=0;

        return tag;
    }

    //태그 언급개수를 늘려주는 메소드
    public void upTagNum(){
        this.num++;
    }

    public void downTagNum() {
        this.num--;
    }
}

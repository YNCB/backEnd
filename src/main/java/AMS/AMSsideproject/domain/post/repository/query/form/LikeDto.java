package AMS.AMSsideproject.domain.post.repository.query.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {

    public Long user_id;
    public String nickname;

    @Override
    public boolean equals(Object object) {
        Long userId = (Long)object;

        if(this.user_id == userId)
            return true;
        return false;
    }
}

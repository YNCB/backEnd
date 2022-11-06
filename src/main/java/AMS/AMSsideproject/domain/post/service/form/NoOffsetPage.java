package AMS.AMSsideproject.domain.post.service.form;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoOffsetPage {

    Pageable pageable;
    BooleanBuilder booleanBuilder;
}

package AMS.AMSsideproject.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Birthday {

    private Integer year;
    private Integer month;
    private Integer day;
}

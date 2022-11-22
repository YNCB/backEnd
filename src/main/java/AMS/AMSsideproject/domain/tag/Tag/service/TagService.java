package AMS.AMSsideproject.domain.tag.Tag.service;

import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    /**
     * 저장, 조회 할때 태그 별로 쿼리가 각각 나가는데 너무 성능 이슈인데...움.....
     */

    //tagList 에 있는 tag 를 존재여부 체크 및 추가하는 메서드
    @Transactional
    public List<Tag> addFromTagList(List<String> tagList) {

        List<Tag> result = new ArrayList<>();

        for(String tagName : tagList) {
            Optional<Tag> findTagName = tagRepository.findByTagName(tagName);
            if(findTagName.isEmpty()) { //태그 테이블에 없는경우
                Tag tag = Tag.createTag(tagName);
                Tag saveTag = tagRepository.save(tag);
                result.add(saveTag);
            }else { //태그 테이블에 있는경우
                result.add(findTagName.get());
            }
        }

        return result;
    }

}

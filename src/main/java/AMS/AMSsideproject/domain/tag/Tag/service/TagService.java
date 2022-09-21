package AMS.AMSsideproject.domain.tag.Tag.service;

import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public void addFromTagList(List<String> tagList) {

        for(String tagName : tagList) {
            Optional<Tag> findTagName = tagRepository.findByTagName(tagName);
            if(findTagName.isEmpty()) {
                Tag tag = Tag.createTag(tagName);
                tagRepository.save(tag);
            }
        }
    }




}

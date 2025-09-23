package ProductService.service;

import ProductService.entity.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SequenceGeneratorService {

    private final MongoTemplate mongoTemplate;

    public long generateSequence(String seqName) {
        Counter counter = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Counter.class
        );
        return counter.getSeq();
    }
}

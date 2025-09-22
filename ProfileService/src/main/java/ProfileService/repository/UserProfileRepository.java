package ProfileService.repository;

import java.util.List;
import java.util.Optional;

import ProfileService.entity.UserProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {

}

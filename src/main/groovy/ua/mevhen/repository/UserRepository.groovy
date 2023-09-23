package ua.mevhen.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import ua.mevhen.domain.model.User

interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUsername(String username)

    boolean existsByUsername(String username)

}

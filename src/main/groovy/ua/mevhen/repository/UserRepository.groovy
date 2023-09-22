package ua.mevhen.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ua.mevhen.domain.model.User

interface UserRepository extends MongoRepository<User, String> {

    User updateUsernameById(String id, String username)

}

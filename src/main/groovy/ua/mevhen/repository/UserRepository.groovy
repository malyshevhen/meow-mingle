package ua.mevhen.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.security.core.userdetails.UserDetails
import ua.mevhen.domain.model.User

interface UserRepository extends MongoRepository<User, ObjectId> {
    UserDetails findByEmail(String email)
}

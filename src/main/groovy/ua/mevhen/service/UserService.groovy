package ua.mevhen.service

import ua.mevhen.dto.UserInfo
import ua.mevhen.dto.UserRegistration
import ua.mevhen.domain.model.User

interface UserService {

    User findByUsername(String username)

    UserInfo save(UserRegistration rerForm)

    UserInfo updateUsername(String usernameToUpdate, String username)

    void deleteById(String id)

    void subscribe(String username, String subId)

    void unsubscribe(String username, String subId)

    boolean ifExists(String username)

    void deleteByUsername(String username)
}
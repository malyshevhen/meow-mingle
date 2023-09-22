package ua.mevhen.service

import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.domain.model.User

interface UserService {

    User findById(String id)

    UserInfo save(UserRegistration rerForm)

    UserInfo updateUsername(String id, String username)

    void deleteById(String id)

}
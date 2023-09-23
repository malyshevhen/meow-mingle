package ua.mevhen.service

import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration

interface UserService {

    UserInfo save(UserRegistration rerForm)

    UserInfo updateUsername(String id, String username)

    void deleteById(String id)

    Collection<UserInfo> subscribe(String username, String subId)

    Collection<UserInfo> unsubscribe(String username, String subId)
}
package ua.mevhen.service

import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration

interface UserService {
    UserInfo save(UserRegistration rerForm)
}
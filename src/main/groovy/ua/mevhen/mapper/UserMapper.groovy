package ua.mevhen.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.domain.model.User

@Mapper(componentModel = 'spring')
interface UserMapper {

    @Mapping(target = 'id', ignore = true)
    @Mapping(target = 'subscribers', ignore = true)
    @Mapping(target = 'subscriptions', ignore = true)
    @Mapping(target = 'created', ignore = true)
    @Mapping(target = 'updated', ignore = true)
    User toUser(UserRegistration registration)

    UserInfo toUserInfo(User user)

}
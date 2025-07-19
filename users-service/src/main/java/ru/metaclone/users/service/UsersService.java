package ru.metaclone.users.service;

import org.springframework.stereotype.Service;
import ru.metaclone.users.mappers.UserEntityMapper;
import ru.metaclone.users.models.dto.SaveUserDetailsRequest;
import ru.metaclone.users.models.dto.SaveUserResponse;
import ru.metaclone.users.repository.UsersRepository;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final UserEntityMapper userEntityMapper;

    public UsersService(UsersRepository usersRepository, UserEntityMapper userEntityMapper) {
        this.usersRepository = usersRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public SaveUserResponse saveUser(SaveUserDetailsRequest newUser) {
        var userEntity = userEntityMapper.mapEntityFrom(newUser);
        usersRepository.save(userEntity);

        return new SaveUserResponse(userEntity.getUserId(), userEntity.getLogin());
    }
}

package com.company.ems.user;

import java.util.List;
import java.util.UUID;

public interface UserService {
	UserDto createUser(CreateUserDto dto);
    UserDto updateUser(UUID id, EditUserDto dto);
    UserDto getUserById(UUID id);
    List<UserDto> getAllUsers();
    void deleteUser(UUID id);
    List<UserDto> searchByEmail(String email);
}

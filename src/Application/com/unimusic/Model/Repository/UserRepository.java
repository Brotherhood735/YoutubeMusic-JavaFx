package com.unimusic.Model.Repository;

import com.unimusic.Model.Authentication.User;

public interface UserRepository {
    void saveUser(User user);
    boolean usernameExists(String username);
    User getUser(String username);
}

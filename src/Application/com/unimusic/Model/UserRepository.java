package com.unimusic.Model;

import java.util.Map;

public interface UserRepository {
    void loadUsers();
    void saveUser(User user);
}

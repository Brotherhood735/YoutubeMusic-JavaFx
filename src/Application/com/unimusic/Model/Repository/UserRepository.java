package com.unimusic.Model.Repository;

import com.unimusic.Model.Authentication.UniUserRequest;
import com.unimusic.Model.Authentication.User;

import java.util.ArrayList;

public interface UserRepository {
    void saveUser(User user);
    void addToRegistrationReqList(UniUserRequest user);
    void removeFromRegistrationReqList(UniUserRequest user);

    ArrayList<UniUserRequest> getRegistrationReqList();
    boolean usernameExists(String username);
    User getRegistredUser(String username);

    void removeUser(User user);

    void updatePermissions(User user, boolean isAdmin, boolean isGuest);
}
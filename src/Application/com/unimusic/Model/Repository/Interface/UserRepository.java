package com.unimusic.Model.Repository.Interface;

import com.unimusic.Model.Authentication.UserProperty;
import com.unimusic.Model.Authentication.User;

import java.util.ArrayList;

public interface UserRepository {
    void saveUser(User user);
    void addToRegistrationReqList(UserProperty user);
    void removeFromRegistrationReqList(UserProperty user);

    ArrayList<UserProperty> getRegistrationReqList();
    boolean usernameExists(String username);
    User getRegistredUser(String username);

    void removeUser(User user);

    void replaceUser(User user);

    ArrayList<UserProperty> getAllRegisteredUsers();
}
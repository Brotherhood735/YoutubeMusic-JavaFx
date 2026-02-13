package com.unimusic.Model.Repository.Interface;

import com.unimusic.Model.Authentication.UserProperty;
import java.util.ArrayList;

public interface UserRequestRepository {
    void addRequest(UserProperty request);
    void removeRequest(UserProperty request);
    ArrayList<UserProperty> getAllRequests();
    boolean requestExists(String username);
}
package com.unimusic.Model.Repository.Implementation;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Authentication.UserProperty;
import com.unimusic.Model.Repository.CsvBaseRepository;
import com.unimusic.Model.Repository.Interface.UserRequestRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class UserRequestRepositoryImpl extends CsvBaseRepository<UserProperty> implements UserRequestRepository {

    public UserRequestRepositoryImpl() {
        super(AppConfig.UNI_USER_REGISTRATION_REQ_CSV_PATH, AppConfig.UNI_USER_REGISTRATION_REQ_HEADER);
        loadAll();
    }

    @Override
    protected UserProperty parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 2) {
            try {
                String username = parts[0];
                String password = parts[1];
                return new UserProperty(username, password);
            } catch (Exception e) {
                System.err.println("Error parsing request line: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    protected String serialize(UserProperty request) {
        return request.getUsername() + "," + request.getPassword();
    }

    @Override
    public void addRequest(UserProperty request) {
        if (!requestExists(request.getUsername())) {
            memoryCache.add(request);
            saveAll();
        }
    }

    @Override
    public void removeRequest(UserProperty request) {
        memoryCache.removeIf(r -> r.getUsername().equals(request.getUsername()));
        saveAll();
    }

    @Override
    public ArrayList<UserProperty> getAllRequests() {
        return new ArrayList<>(memoryCache);
    }

    @Override
    public boolean requestExists(String username) {
        return memoryCache.stream()
                .anyMatch(r -> r.getUsername().equals(username));
    }
}
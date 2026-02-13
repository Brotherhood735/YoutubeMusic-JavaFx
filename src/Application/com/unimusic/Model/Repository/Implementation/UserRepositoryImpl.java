package com.unimusic.Model.Repository.Implementation;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Authentication.User;
import com.unimusic.Model.Authentication.UserProperty;
import com.unimusic.Model.Repository.CsvBaseRepository;
import com.unimusic.Model.Repository.Interface.UserRepository;
import com.unimusic.Model.Repository.Interface.UserRequestRepository;
import com.unimusic.Model.Repository.RepositoryFactory;

import java.util.ArrayList;

// Implementazione specifica per User
public class UserRepositoryImpl extends CsvBaseRepository<User> implements UserRepository {

    private final UserRequestRepository requestRepository;
    public UserRepositoryImpl() {
        super(AppConfig.USERS_CSV_PATH, AppConfig.USERS_CSV_HEADER);

        this.requestRepository = new UserRequestRepositoryImpl();
        loadAll();
    }

    @Override
    protected User parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 3) {
            String username = parts[0];
            String password = parts[1];
            boolean isAdmin = "1".equals(parts[2]);
            boolean isGuest = parts.length > 3 && "1".equals(parts[3]);
            return new User(username, password, isAdmin, isGuest);
        }
        return null;
    }

    @Override
    protected String serialize(User user) {
        return user.getUsername() + "," +
                user.getPassword() + "," +
                (user.isAdmin() ? "1" : "0") + "," +
                (user.isGuest() ? "1" : "0");
    }

    @Override
    public void saveUser(User user) {
        memoryCache.removeIf(u -> u.getUsername().equals(user.getUsername()));
        memoryCache.add(user);
        saveAll();
    }

    @Override
    public User getRegistredUser(String username) {
        return memoryCache.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean usernameExists(String username) {
        return getRegistredUser(username) != null;
    }

    @Override
    public void addToRegistrationReqList(UserProperty user) {
        requestRepository.addRequest(user);
    }

    @Override public void removeFromRegistrationReqList(UserProperty user) {
        requestRepository.removeRequest(user);
    }
    @Override public ArrayList<UserProperty> getRegistrationReqList() { return requestRepository.getAllRequests();}
    @Override public void removeUser(User user) {
        memoryCache.removeIf( e -> e.getUsername() == user.getUsername());
        saveAll();
    }
    @Override public void replaceUser(User user) { saveUser(user); }
    @Override public ArrayList<UserProperty> getAllRegisteredUsers()
    {
        ArrayList<UserProperty> usersList = new ArrayList<>();
        for (User user : memoryCache){
            if (user.isAdmin() || user.isGuest()) continue;
            usersList.add(new UserProperty(user.getUsername(), user.getPassword()));
        }
        return usersList;
    }
}
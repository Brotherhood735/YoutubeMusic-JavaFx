package com.unimusic.Model.Authentication;

import com.unimusic.Main;
import com.unimusic.Model.Repository.Interface.UserRepository;
import com.unimusic.Model.Repository.RepositoryFactory;

public class AuthenticationFacade {

    private static AuthenticationFacade instance;
    private final UserRepository userRepo;

    private AuthenticationFacade() {
        this.userRepo = RepositoryFactory.getInstance().getUserRepository();
    }

    public static AuthenticationFacade getInstance() {
        if (instance == null) {
            instance = new AuthenticationFacade();
        }
        return instance;
    }

    /**
     * Gestisce il login.
     * @return true se il login ha successo, false altrimenti.
     */
    public boolean login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User user = userRepo.getRegistredUser(username);
        if (user != null && user.checkPassword(password)) {
            AuthManager.setUser(user);
            return true;
        }
        return false;
    }

    /**
     * Gestisce la registrazione completa.
     * Restituisce una stringa di errore se fallisce, o null se ha successo.
     */
    public String register(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Please fill out all fields";
        }

        if (userRepo.usernameExists(username)) {
            return "Username already exists";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }

        UserProperty newRequest = User.createRequest(username, password);
        userRepo.saveUser(newRequest.getUser());
        userRepo.addToRegistrationReqList(newRequest);

        return null;
    }
}
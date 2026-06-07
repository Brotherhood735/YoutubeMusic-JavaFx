package com.unimusic.Model.Authentication;

public class AuthManager {
    private static User authenticatedUser = null;

    public static User getUser()
    {
        return authenticatedUser;
    }
    public static void setUser(User user)
    {
        authenticatedUser = user;
    }
    public static boolean isAuthenticated()
    {
        return authenticatedUser != null;
    }
    public static void logout(){
        authenticatedUser = null;
    }
}

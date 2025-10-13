package com.unimusic.Model;

public class AuthManager {
    private static String authenticatedUser = null;

    public static String getUser()
    {
        return authenticatedUser;
    }
    public static void setUser(String user)
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

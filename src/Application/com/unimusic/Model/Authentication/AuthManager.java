package com.unimusic.Model.Authentication;

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
    public static String getAuthenticatedUser()
    {
        return authenticatedUser;
    }
}

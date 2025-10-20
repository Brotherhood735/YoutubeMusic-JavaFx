package com.unimusic.Model;

import com.unimusic.Config.AppConfig;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
        private final String COMMENTS_CSV_HEADER = "id_comment,id_media,username,comment";
        private Map<Double, UniComment> comments = new HashMap<>();
        private Map<String, User> users = new HashMap<>();

        public DataManager() {
            ensureUsersCsvExists();
            loadUsers();
        }

        /**
         * Make sure the CSV file exists, create it if needed
         */
        private void ensureUsersCsvExists() {
            ensureCsvExists(AppConfig.USERS_CSV_PATH, "username,password,is_admin\n", "admin,admin,1\n");
        }
        private void ensureCommentsCsvExists() {
            ensureCsvExists(AppConfig.COMMENTS_CSV_PATH, COMMENTS_CSV_HEADER + "\n", "");
        }
        private void ensureCsvExists(String csvPath, String csvHeader, String defaultRow)
        {
            File csvFile = new File(csvPath);
            if (!csvFile.exists()) {
                try {
                    // Create parent directories if they don't exist
                    File parentDir = csvFile.getParentFile();
                    if (parentDir != null) {
                        parentDir.mkdirs();
                    }

                    // Create the file and write headers
                    try (FileWriter writer = new FileWriter(csvFile)) {
                        writer.write(csvHeader);
                        writer.write(defaultRow);
                    }
                } catch (IOException e) {
                    System.err.println("Error creating users CSV file: " + e.getMessage());
                }
            }
        }
        /**
         * Load users from the CSV file
         */
        private void loadUsers() {
            try (BufferedReader reader = new BufferedReader(new FileReader(AppConfig.USERS_CSV_PATH))) {
                String line;
                // Skip header line
                reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String username = parts[0];
                        String password = parts[1];
                        boolean isAdmin = parts[2].equals("1");
                        users.put(username, new User(username, password, isAdmin));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading users from CSV: " + e.getMessage());
            }
        }
        public Map<Double, UniComment> loadComments(double id) {
            Map<Double, UniComment> comments = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(AppConfig.COMMENTS_CSV_PATH))) {
                String line;
                // Skip header line
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        double media_id = Double.parseDouble(parts[1]);
                        if (media_id != id){ continue; }

                        double comment_id = Double.parseDouble(parts[0]);
                        String username = parts[2];
                        String comment = parts[3];
                        comments.put(comment_id, new UniComment(comment_id, media_id, username, comment));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading comments from CSV: " + e.getMessage());
            }
            return comments;
        }
    private void saveAllComments(Map<Double, UniComment> comments) {
        String isAdmin;
        try (FileWriter writer = new FileWriter(AppConfig.COMMENTS_CSV_PATH)) {
            // Write header
            writer.write(COMMENTS_CSV_HEADER + "\n");

            // Write users
            for (UniComment comment : comments.values()) {
                writer.write(comment.getId() + "," + comment.getMediaId() + "," + comment.getUsername() + "," + comment.getComment() +  "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving comments to CSV: " + e.getMessage());
        }
    }
        /**
         * Save all users to the CSV file
         */
        private void saveAllUsers() {
            String isAdmin;
            try (FileWriter writer = new FileWriter(AppConfig.USERS_CSV_PATH)) {
                // Write header
                writer.write("username,password,is_admin\n");

                // Write users
                for (User user : users.values()) {
                    isAdmin = "0";
                    if (user.isAdmin()) {
                        isAdmin = "1";
                    }
                    writer.write(user.getUsername() + "," + user.getPassword() + "," + isAdmin +  "\n");
                }
            } catch (IOException e) {
                System.err.println("Error saving users to CSV: " + e.getMessage());
            }
        }
        /**
         * Save a user to the repository
         */
        public void saveUser(User user) {
            users.put(user.getUsername(), user);
            saveAllUsers();
        }
        public void saveComment(Map<Double, UniComment> comments, UniComment comment) {
            comments.put(comment.getMediaId(), comment);
            saveAllComments(comments);
        }
        public void deleteComment(Map<Double, UniComment> comments, double comment_id)
        {
            if (comments.containsKey(comment_id)) {
                comments.remove(comment_id);
                saveAllComments(comments);
            }
        }
        /**
         * Delete a user from the repository
         */
        public void deleteUser(String username) {
            if (users.containsKey(username)) {
                User user = users.get(username);
                // Never delete admin users as a safety measure
                if (!user.isAdmin()) {
                    users.remove(username);
                    saveAllUsers();
                }
            }
        }
        public UniComment getComment(double comment_id) {return comments.get(comment_id);}
        /**
         * Get a user by username
         */
        public User getUser(String username) {
            return users.get(username);
        }

        /**
         * Check if a username exists
         */
        public boolean usernameExists(String username) {
            return users.containsKey(username);
        }

        public Map<Double, UniComment> getAllComments() { return new HashMap<>(comments); }
        /**
         * Get all users
         */
        public Map<String, User> getAllUsers() {
            return new HashMap<>(users);
        }
}

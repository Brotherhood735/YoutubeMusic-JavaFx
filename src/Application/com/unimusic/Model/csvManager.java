package com.unimusic.Model;

import com.unimusic.Config.AppConfig;

import javax.xml.stream.events.Comment;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class csvManager {

        private Map<String, User> users = new HashMap<>();
        private Map<Double, UniComment> comments = new HashMap<>();

        public csvManager() {
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
            ensureCsvExists(AppConfig.COMMENTS_CSV_PATH, "id_comment,id_media,username,comment\n", "");
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
        private void loadComments() {
            try (BufferedReader reader = new BufferedReader(new FileReader(AppConfig.COMMENTS_CSV_PATH))) {
                String line;
                // Skip header line
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        double media_id = Double.parseDouble(parts[0]);
                        String username = parts[1];
                        String comment = parts[2];
                        comments.put(media_id, new UniComment(media_id, username, comment));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading comments from CSV: " + e.getMessage());
            }
        }
    private void saveAllComments() {
        String isAdmin;
        try (FileWriter writer = new FileWriter(AppConfig.COMMENTS_CSV_PATH)) {
            // Write header
            writer.write("id_comment,id_media,username,comments\n");

            // Write users
            for (UniComment comment : comments.values()) {
                writer.write(comment.getMediaId() + "," + comment.getUsername() + "," + comment.getComment() +  "\n");
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
        public void saveComment(UniComment comment) {
            comments.put(comment.getMediaId(), comment);
            saveAllComments();
        }
        public void deleteComment(double comment_id)
        {

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

        /**
         * Get all users
         */
        public Map<String, User> getAllUsers() {
            return new HashMap<>(users);
        }
}

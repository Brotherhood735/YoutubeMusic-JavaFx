package com.unimusic.Model;

import com.unimusic.Config.AppConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
        private ArrayList<UniComment> comments = new ArrayList<>();
        private final String COMMENTS_CSV_HEADER = "id_comment,id_media,id_parent,username,comment";
        private Map<String, User> users = new HashMap<>();

        public DataManager() {
            ensureUsersCsvExists();
            loadUsers();
            loadAllComments();
        }

    private void loadAllComments()
    {
        int i = 0;
        while (checkCsvExists(AppConfig.COMMENTS_CSV_PATH + i + ".csv"))
        {
            comments.addAll(loadComments(i));
            i++;
        }
    }

    /**
         * Make sure the CSV file exists, create it if needed
         */
        private void ensureUsersCsvExists() {
            ensureCsvExists(AppConfig.USERS_CSV_PATH, "username,password,is_admin\n", "admin,admin,1\n");
        }
        private void ensureCommentsCsvExists(int depthLevel)
        {
            ensureCsvExists(AppConfig.COMMENTS_CSV_PATH + depthLevel + ".csv", COMMENTS_CSV_HEADER + "\n", "");
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
        private boolean checkCsvExists(String csvPath)
        {
            File csvFile = new File(csvPath);
            if (!csvFile.exists()) {
                return false;
            }
            return true;
        }
        private void createCsvFile(String csvPath, String csvHeader, String defaultRow)
        {
            File csvFile = new File(csvPath);
            try
            {
                // Create parent directories if they don't exist
                File parentDir = csvFile.getParentFile();
                if (parentDir != null)
                {
                    parentDir.mkdirs();
                }
                // Create the file and write headers
                try (FileWriter writer = new FileWriter(csvFile))
                {
                    writer.write(csvHeader);
                    writer.write(defaultRow);
                }
            }
            catch (IOException e)
            {
                System.err.println("Error creating users CSV file: " + e.getMessage());
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

        // with id we mean the media id
        public ArrayList<UniComment> loadComments(double id, int depthLevel) {
            ensureCommentsCsvExists(depthLevel);
            ArrayList<UniComment> comments = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(
                    new FileReader(AppConfig.COMMENTS_CSV_PATH + depthLevel + ".csv"))) {
                String line;
                // Skip header line
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        int media_id =  Integer.parseInt(parts[1]);
                        if (media_id != id){ continue; }

                        int comment_id = Integer.parseInt(parts[0]);
                        int parent_id =  Integer.parseInt(parts[2]);
                        String username = parts[3];
                        String comment = parts[4];
                        comments.add(new UniComment(getParentComment(parent_id), comment_id, media_id, username, comment, depthLevel));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading comments from CSV: " + e.getMessage());
            }
            return comments;
        }
    public ArrayList<UniComment> loadComments(double id, int depthLevel, boolean getOnlyParentComments) {
        ensureCommentsCsvExists(depthLevel);
        ArrayList<UniComment> comments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.COMMENTS_CSV_PATH + depthLevel + ".csv"))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int media_id =  Integer.parseInt(parts[1]);
                    int comment_id = Integer.parseInt(parts[0]);
                    int parent_id =  Integer.parseInt(parts[2]);
                    if (getOnlyParentComments)
                    {
                        if (parent_id != id) continue;
                    }
                    else
                    {
                        if (media_id != id){ continue; }
                    }
                    String username = parts[3];
                    String comment = parts[4];
                    comments.add(new UniComment(getParentComment(parent_id), comment_id, media_id, username, comment, depthLevel));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
        return comments;
    }
        public ArrayList<UniComment> loadComments(int depthLevel) {
            ArrayList<UniComment> comments = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(AppConfig.COMMENTS_CSV_PATH + depthLevel + ".csv"))) {
                String line;
                // Skip header line
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        int comment_id =  Integer.parseInt(parts[0]);
                        int media_id =  Integer.parseInt(parts[1]);
                        int parent_id =  Integer.parseInt(parts[2]);
                        String username = parts[3];
                        String comment = parts[4];
                        comments.add(new UniComment(
                                getParentComment(parent_id), comment_id, media_id, username, comment, depthLevel));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading comments from CSV: " + e.getMessage());
            }
            return comments;
        }

    private UniComment getParentComment(int parentId) {
            for (UniComment comment : comments) {
               if (comment.getId() == parentId) {
                   return comment;
               }
            }
            // return null if no parent comment was found meaning this will be a parent comment for others.
            return null;
    }

    private void saveAllComments(List<UniComment> comments, int depthLevel) {
        String isAdmin;
        try (FileWriter writer = new FileWriter(AppConfig.COMMENTS_CSV_PATH + depthLevel + ".csv")) {
            // Write header
            writer.write(COMMENTS_CSV_HEADER + "\n");

            // Write users
            for (UniComment comment : comments) {
                if (comment.getDepth() != depthLevel) continue;
                String parent = comment.getParent() != null ? String.valueOf(comment.getParent().getId()) : "-1";
                writer.write(comment.getId() + "," + comment.getMediaId() + "," + parent + ',' + comment.getUsername() + "," + comment.getComment() +  "\n");
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
        public void saveComment(List<UniComment> controllerComments, UniComment comment, int depthLevel) {
            comments.add(comment);
            controllerComments.add(comment);
            saveAllComments(comments, depthLevel);
        }
        public void deleteComment(List<UniComment> comments, double comment_id, int depthLevel)
        {
            for (UniComment comment : comments)
            {
                if (comment.getId() == comment_id)
                {
                    comments.remove(comment);

                }
            }
            saveAllComments(comments, depthLevel);
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

package com.unimusic.Model.Repository;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Authentication.User;
import com.unimusic.Model.Media.UniMediaMetaInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRepository implements MediaRepository, UserRepository, CommentRepository, MediaMetaInfoRepository{
    private ArrayList<UniComment> comments = new ArrayList<>();
    private ArrayList<UniMedia> media = new ArrayList<>();
    private final String COMMENTS_CSV_HEADER = "id_comment,id_media,id_parent,username,comment";
    private Map<String, User> users = new HashMap<>();
    private ArrayList<UniMediaMetaInfo> metaInfos = new ArrayList<>();

    public DataRepository() {
        ensureUsersCsvExists();
        loadUsers();
        loadAllComments();
        loadAllMedia();
        loadAllMediaMetaInfo();
    }

    private void loadAllMediaMetaInfo() {
        metaInfos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.UNI_MEDIA_META_INFO_CSV_PATH))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    int mediaId = Integer.parseInt(parts[1]);
                    String title = parts[2];
                    String artists = parts[3];
                    String instruments = parts[4];
                    double startTime = Double.parseDouble(parts[5]);
                    double endTime = Double.parseDouble(parts[6]);
                    metaInfos.add(new UniMediaMetaInfo(id, mediaId, title, artists, instruments, startTime, endTime));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
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

    private void ensureUsersCsvExists() {
        ensureCsvExists(AppConfig.USERS_CSV_PATH, AppConfig.USERS_CSV_HEADER, AppConfig.USERS_DEFAULT_ROW);
    }
    private void ensureCommentsCsvExists(int depthLevel) {
        ensureCsvExists(AppConfig.COMMENTS_CSV_PATH + depthLevel + ".csv", AppConfig.COMMENTS_CSV_HEADER, "");
    }
    private void ensureCsvExists(String csvPath, String csvHeader, String defaultRow) {
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
    private boolean checkCsvExists(String csvPath) {
        File csvFile = new File(csvPath);
        if (!csvFile.exists()) {
            return false;
        }
        return true;
    }
    private void createCsvFile(String csvPath, String csvHeader, String defaultRow) {
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
                    comments.add(new UniComment(getParentComment(parent_id, depthLevel - 1), comment_id, media_id, username, comment, depthLevel));
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
                    comments.add(new UniComment(getParentComment(parent_id, depthLevel-1), comment_id, media_id, username, comment, depthLevel));
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
                            getParentComment(parent_id, depthLevel-1), comment_id, media_id, username, comment, depthLevel));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
        return comments;
    }

    private void loadAllMedia() {
        media = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.UNI_MEDIA_CSV_PATH))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    String title       = getNullValue(parts[1]);
                    String videoUrl    = getNullValue(parts[2]);
                    String audioUrl    = getNullValue(parts[3]);
                    String webUrl      = getNullValue(parts[4]);
                    String artist      = getNullValue(parts[5]);
                    String imageUrl    = getNullValue(parts[6]);
                    String type        = getNullValue(parts[7]);
                    String instruments = getNullValue(parts[8]);
                    String date        = getNullValue(parts[9]);
                    String place       = getNullValue(parts[10]);
                    media.add(new UniMedia(id, title, videoUrl, audioUrl, webUrl, artist, imageUrl, type,
                            instruments, date, place));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
    }
    private String getNullValue(String str)
    {
        if(str.equals("null")) return null;
        else return str;
    }
    private String validateStr(String str)
    {
        if(str == null || str.isEmpty()) return "null";
        else return str;
    }
    public void saveMedia(UniMedia uniMedia)
    {
        media.add(uniMedia);
        saveAllMedia();
    }

    private void saveAllMedia() {
        try (FileWriter writer = new FileWriter(AppConfig.UNI_MEDIA_CSV_PATH)) {
            // Write header
            writer.write(AppConfig.UNI_MEDIA_CSV_HEADER);

            // Write users
            for (UniMedia medium : media) {
                writer.write(
                        "%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n".formatted(
                        medium.getId(), validateStr(medium.getTitle()), validateStr(medium.getVideoName()),
                        validateStr(medium.getAudioName()), validateStr(medium.getWebUrl()),
                        validateStr(medium.getArtist()), validateStr(medium.getImageName()),
                        validateStr(medium.getType()), validateStr(medium.getInstruments()),
                        validateStr(medium.getDate()), validateStr(medium.getPlace())));
            }
        } catch (IOException e) {
            System.err.println("Error saving media to CSV: " + e.getMessage());
        }
    }

    public ArrayList<UniMedia> loadMediaWithInterval(int start, int end) {

        ArrayList<UniMedia> subMedia = new ArrayList<>();
        if (start >= media.size() || end > media.size()) {return media;}
        if (media.isEmpty()) {return null;}
        for (int i = start; i < end; i++) {
            subMedia.add(media.get(i));
        }
        return subMedia;
    }

    @Override
    public int generateMediaId() {
        return media.size();
    }

    @Override
    public int getMediaLength() {
        return media.size();
    }

    private UniComment getParentComment(int parentId, int depthLevel) {
            for (UniComment comment : comments) {
               if (comment.getId() == parentId && comment.getDepth() == depthLevel) {
                   return comment;
               }
            }
            // return null if no parent comment was found meaning this will be a parent comment for others.
            return null;
    }

    private void saveAllComments(List<UniComment> comments, int depthLevel) {
        try (FileWriter writer = new FileWriter(AppConfig.COMMENTS_CSV_PATH + depthLevel + ".csv")) {
            // Write header
            writer.write(AppConfig.COMMENTS_CSV_HEADER);

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
            writer.write(AppConfig.USERS_CSV_HEADER);

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
    @Override
    public void saveComment(UniComment comment, int depthLevel) {
        comments.add(comment);
        saveAllComments(comments, depthLevel);
    }

    @Override
    public UniComment loadComment(int id) {
        for (UniComment comment : comments) {
            if (comment.getId() == id) {
                return comment;
            }
        }
        return null;
    }

    public void deleteComment(List<UniComment> comments, double comment_id, int depthLevel) {
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

    @Override
    public void saveMetaInfo(UniMediaMetaInfo metaInfo) {
        metaInfos.add(metaInfo);
        saveAllMetaInfos();
    }

    @Override
    public ArrayList<UniMediaMetaInfo> getMetaInfo(int mediaId)
    {
        if (mediaId < 0) return null;

        ArrayList<UniMediaMetaInfo> metaInfosWithMediaId = new ArrayList<>();

        for (UniMediaMetaInfo metaInfo : metaInfos)
            if (metaInfo.getMediaId() == mediaId)
            {
                metaInfosWithMediaId.add(metaInfo);
            }
        return metaInfosWithMediaId;
    }

    @Override
    public int getMetaInfoLength(int mediaId) {
        int i = 0;
        for (UniMediaMetaInfo metaInfo : metaInfos)
            if (metaInfo.getMediaId() == mediaId) i++;

        return i;
    }

    @Override
    public int generateMetaInfoId() {
        return metaInfos.size();
    }

    private void saveAllMetaInfos() {
        try (FileWriter writer = new FileWriter(AppConfig.UNI_MEDIA_META_INFO_CSV_PATH)) {
            // Write header
            writer.write(AppConfig.UNI_MEDIA_META_INFO_CSV_HEADER);

            // Write users
            for (UniMediaMetaInfo metaInfo : metaInfos) {
                writer.write(
                        "%d,%d,%s,%s,%s,%s,%s\n".formatted(
                                metaInfo.getId(),
                                metaInfo.getMediaId(),
                                validateStr(metaInfo.getTitle()),
                                validateStr(metaInfo.getArtists()),
                                validateStr(metaInfo.getInstruments()),
                                validateStr(String.valueOf(metaInfo.getStartTime())),
                                validateStr(String.valueOf(metaInfo.getEndTime()))
                        ));
            }
        } catch (IOException e) {
            System.err.println("Error saving Meta Info to CSV: " + e.getMessage());
        }
    }
}

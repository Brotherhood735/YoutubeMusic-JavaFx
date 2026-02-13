package com.unimusic.Model.Repository;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Authentication.UniUserRequest;
import com.unimusic.Model.Media.UniDataType;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Authentication.User;
import com.unimusic.Model.Media.UniMediaSegmentInfo;
import com.unimusic.Model.Media.UniMediaUserNote;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataRepository implements MediaRepository, UserRepository, CommentRepository, MediaMetaInfoRepository, MediaNoteRepository {
    private ArrayList<UniComment> comments = new ArrayList<>();
    private ArrayList<UniMedia> media = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private ArrayList<UniMediaSegmentInfo> metaInfos = new ArrayList<>();
    private ArrayList<UniMediaUserNote> mediaNotes = new ArrayList<>();
    private ArrayList<UniUserRequest> registrationRequests = new ArrayList<>();

    public DataRepository() {
        ensureUsersCsvExists();
        loadUsers();
        loadAllComments();
        loadAllMedia();
        loadAllMediaMetaInfo();
        loadAllMediaNotes();
        loadAllRequests();
    }

    private void loadAllRequests() {
        ensureUsersRegistrationRequestCsvExists();
        registrationRequests = new ArrayList<UniUserRequest>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.UNI_USER_REGISTRATION_REQ_CSV_PATH))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String password = parts[1];
                    registrationRequests.add(new UniUserRequest(username, password));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
    }

    private void loadAllMediaMetaInfo() {
        ensureMediaMetaInfoCsvExists();
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
                    String extraInfo = parts[5];
                    double startTime = Double.parseDouble(parts[6]);
                    double endTime = Double.parseDouble(parts[7]);
                    metaInfos.add(new UniMediaSegmentInfo(id, mediaId, title, artists, instruments,
                            extraInfo, startTime, endTime, UniDataType.MetaInfo));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
    }



    private void loadAllMediaNotes() {
        ensureMediaNotesCsvExists();
        mediaNotes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.UNI_MEDIA_NOTES_CSV_PATH))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    int mediaId = Integer.parseInt(parts[1]);
                    String username = parts[2];
                    String title = parts[3];
                    String artists = parts[4];
                    String instruments = parts[5];
                    String extraInfo = parts[6];
                    double startTime = Double.parseDouble(parts[7]);
                    double endTime = Double.parseDouble(parts[8]);
                    mediaNotes.add(new UniMediaUserNote(id, username, mediaId, title, artists, instruments,
                            extraInfo, startTime, endTime, UniDataType.Note));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading notes from CSV: " + e.getMessage());
        }
    }
    private void loadAllComments()
    {
        checkCsvExists(AppConfig.COMMENTS_CSV_PATH);
        comments.addAll(loadComments());
    }

    private void ensureUsersCsvExists() {
        ensureCsvExists(AppConfig.USERS_CSV_PATH, AppConfig.USERS_CSV_HEADER, AppConfig.USERS_DEFAULT_ROW);
    }
    private void ensureCommentsCsvExists() {
        ensureCsvExists(AppConfig.COMMENTS_CSV_PATH, AppConfig.COMMENTS_CSV_HEADER, "");
    }
    private void ensureMediaNotesCsvExists(){
        ensureCsvExists(AppConfig.UNI_MEDIA_NOTES_CSV_PATH, AppConfig.UNI_MEDIA_META_INFO_CSV_HEADER, "");
    }
    private void ensureMediaMetaInfoCsvExists() {
        ensureCsvExists(AppConfig.UNI_MEDIA_META_INFO_CSV_PATH, AppConfig.UNI_MEDIA_META_INFO_CSV_HEADER, "");
    }
    private void ensureUsersRegistrationRequestCsvExists() {
        ensureCsvExists(AppConfig.UNI_USER_REGISTRATION_REQ_CSV_PATH, AppConfig.UNI_USER_REGISTRATION_REQ_CSV_PATH, "");
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
                    boolean isGuest = parts[3].equals("1");
                    users.put(username, new User(username, password, isAdmin, isGuest));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from CSV: " + e.getMessage());
        }
    }
    // with id we mean the media id
    public ArrayList<UniComment> loadComments(double id, int depthLevel) {
        ensureCommentsCsvExists();
        ArrayList<UniComment> comments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.COMMENTS_CSV_PATH))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (depthLevel != Integer.parseInt(parts[5])) continue;
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
        ensureCommentsCsvExists();
        ArrayList<UniComment> comments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.COMMENTS_CSV_PATH))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (depthLevel != Integer.parseInt(parts[5])) continue;
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
    public ArrayList<UniComment> loadComments(int depthLevelToCheck) {
        ensureCommentsCsvExists();
        ArrayList<UniComment> comments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.COMMENTS_CSV_PATH))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (depthLevelToCheck != Integer.parseInt(parts[5])) continue;
                    int comment_id =  Integer.parseInt(parts[0]);
                    int media_id =  Integer.parseInt(parts[1]);
                    int parent_id =  Integer.parseInt(parts[2]);
                    String username = parts[3];
                    String comment = parts[4];
                    int depthLevel =  Integer.parseInt(parts[5]);
                    comments.add(new UniComment(
                            getParentComment(parent_id, depthLevel-1), comment_id, media_id, username, comment, depthLevel));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
        return comments;
    }
    public ArrayList<UniComment> loadComments() {
        ensureCommentsCsvExists();
        comments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.COMMENTS_CSV_PATH))) {
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
                    int depthLevel =  Integer.parseInt(parts[5]);
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
                    String username    = getNullValue(parts[11]);
                    User user = users.get(username);
                    media.add(new UniMedia(id, title, videoUrl, audioUrl, webUrl, artist, imageUrl, type,
                            instruments, date, place, user));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
    }
    private ArrayList<UniMedia> loadAllMedia(String type) {
        ArrayList<UniMedia> medium = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(AppConfig.UNI_MEDIA_CSV_PATH))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (!parts[7].equals(type)) continue;
                    int id = Integer.parseInt(parts[0]);
                    String title       = getNullValue(parts[1]);
                    String videoUrl    = getNullValue(parts[2]);
                    String audioUrl    = getNullValue(parts[3]);
                    String webUrl      = getNullValue(parts[4]);
                    String artist      = getNullValue(parts[5]);
                    String imageUrl    = getNullValue(parts[6]);
                    String loadedType  = getNullValue(parts[7]);
                    String instruments = getNullValue(parts[8]);
                    String date        = getNullValue(parts[9]);
                    String place       = getNullValue(parts[10]);
                    String username    = getNullValue(parts[11]);
                    User user = users.get(username);
                    medium.add(new UniMedia(id, title, videoUrl, audioUrl, webUrl, artist, imageUrl, type,
                            instruments, date, place, user));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading comments from CSV: " + e.getMessage());
        }
        return medium;
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
                        "%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n".formatted(
                        medium.getId(), validateStr(medium.getTitle()), validateStr(medium.getVideoName()),
                        validateStr(medium.getAudioName()), validateStr(medium.getWebUrl()),
                        validateStr(medium.getArtist()), validateStr(medium.getImageName()),
                        validateStr(medium.getType()), validateStr(medium.getInstruments()),
                        validateStr(medium.getDate()), validateStr(medium.getPlace()), validateStr(medium.getUser().getUsername())
                        ));
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
    public ArrayList<UniMedia> loadMediaWithInterval(ArrayList<UniMedia> medium, int start, int end) {
        ArrayList<UniMedia> subMedia = new ArrayList<>();
        if (start >= medium.size() || end > medium.size()) {return medium;}
        if (medium.isEmpty()) {return null;}
        for (int i = start; i < end; i++) {
            subMedia.add(medium.get(i));
        }
        return subMedia;
    }

    @Override
    public int generateMediaId() {
        return media.size();
    }

    @Override
    public ArrayList<UniMedia> getSongs() {
        return loadAllMedia("Song");
    }

    @Override
    public ArrayList<UniMedia> getConcerts() {
        return loadAllMedia("Concert");
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

    private void saveAllComments() {
        try (FileWriter writer = new FileWriter(AppConfig.COMMENTS_CSV_PATH)) {
            // Write header
            writer.write(AppConfig.COMMENTS_CSV_HEADER);

            // Write users
            for (UniComment comment : comments) {
                String parent = comment.getParent() != null ? String.valueOf(comment.getParent().getId()) : "-1";
                writer.write(comment.getId() + "," + comment.getMediaId() + "," + parent + ',' + comment.getUsername() + "," + comment.getComment() + "," +comment.getDepth() +"\n");
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
        String isGuest;
        try (FileWriter writer = new FileWriter(AppConfig.USERS_CSV_PATH)) {
            // Write header
            writer.write(AppConfig.USERS_CSV_HEADER);

            // Write users
            for (User user : users.values()) {
                isAdmin = "0";
                if (user.isAdmin()) {
                    isAdmin = "1";
                }
                isGuest = "0";
                if (user.isGuest()) {
                    isGuest = "1";
                }
                writer.write(user.getUsername() + "," + user.getPassword() + "," + isAdmin + "," + isGuest + "\n");
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
    private void saveAllRegistrationReq()
    {
        try (FileWriter writer = new FileWriter(AppConfig.UNI_USER_REGISTRATION_REQ_CSV_PATH)) {
            // Write header
            writer.write(AppConfig.UNI_USER_REGISTRATION_REQ_HEADER);

            // Write users
            for (UniUserRequest user : registrationRequests) {
                writer.write(user.getUsername() + "," + user.getPassword() +  "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving users to CSV: " + e.getMessage());
        }
    }

    @Override
    public void addToRegistrationReqList(UniUserRequest user) {
        registrationRequests.add(user);
        saveAllRegistrationReq();
    }

    @Override
    public void removeFromRegistrationReqList(UniUserRequest user) {
        registrationRequests.remove(user);
        saveAllRegistrationReq();
    }

    @Override
    public ArrayList<UniUserRequest> getRegistrationReqList() {
        return registrationRequests;
    }


    @Override
    public void saveComment(UniComment comment, int depthLevel) {
        comments.add(comment);
        saveAllComments();
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

    @Override
    public void deleteComment(UniComment comment) {
        deleteCommentCascade(comments, comment.getId());
        saveAllComments();
    }
    public void deleteCommentCascade(List<UniComment> allComments, int idToRemove) {
        Map<Integer, List<UniComment>> mapByParent = allComments.stream()
                .collect(Collectors.groupingBy(c -> {
                    if (c.getParent() == null) return -1;
                    return c.getParent().getId();
                }));

        Set<Integer> idsToDelete = new HashSet<>();
        collectDescendants(idToRemove, mapByParent, idsToDelete);
        allComments.removeIf(c -> idsToDelete.contains(c.getId()));
    }

    private void collectDescendants(int currentId, Map<Integer, List<UniComment>> mapByParent, Set<Integer> idsToDelete) {
        idsToDelete.add(currentId);

        if (mapByParent.containsKey(currentId)) {
            for (UniComment child : mapByParent.get(currentId)) {
                collectDescendants(child.getId(), mapByParent, idsToDelete);
            }
        }
    }
    @Override
    public int getCommentsSize() {
        return comments.size();
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
            }
        }
        saveAllUsers();
    }
    /**
     * Get a user by username
     */
    public User getRegistredUser(String username) {
        return users.get(username);
    }

    @Override
    public void removeUser(User user) {
        deleteUser(user.getUsername());
    }

    @Override
    public void updatePermissions(User user, boolean isAdmin, boolean isGuest) {
        if (users.containsKey(user.getUsername())) {
            User use = users.get(user.getUsername());
            use.setAdmin(isAdmin);
            use.setGuest(isGuest);
        }
        saveAllUsers();
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
    public void saveMetaInfo(UniMediaSegmentInfo metaInfo) {
        metaInfos.add(metaInfo);
        saveAllMetaInfos();
    }

    @Override
    public ArrayList<UniMediaSegmentInfo> getMetaInfo(int mediaId)
    {
        if (mediaId < 0) return null;

        ArrayList<UniMediaSegmentInfo> metaInfosWithMediaId = new ArrayList<>();

        for (UniMediaSegmentInfo metaInfo : metaInfos)
            if (metaInfo.getMediaId() == mediaId)
            {
                metaInfosWithMediaId.add(metaInfo);
            }
        return metaInfosWithMediaId;
    }

    @Override
    public int getMetaInfoLength(int mediaId) {
        int i = 0;
        for (UniMediaSegmentInfo metaInfo : metaInfos)
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
            for (UniMediaSegmentInfo metaInfo : metaInfos) {
                writer.write(
                        "%d,%d,%s,%s,%s,%s,%s,%s\n".formatted(
                                metaInfo.getId(),
                                metaInfo.getMediaId(),
                                validateStr(metaInfo.getTitle()),
                                validateStr(metaInfo.getArtists()),
                                validateStr(metaInfo.getInstruments()),
                                validateStr(metaInfo.getExtraInfo()),
                                validateStr(String.valueOf(metaInfo.getStartTime())),
                                validateStr(String.valueOf(metaInfo.getEndTime()))
                        ));
            }
        } catch (IOException e) {
            System.err.println("Error saving Meta Info to CSV: " + e.getMessage());
        }
    }
    private void saveallMediaNotes() {
        try (FileWriter writer = new FileWriter(AppConfig.UNI_MEDIA_NOTES_CSV_PATH)) {
            // Write header
            writer.write(AppConfig.UNI_MEDIA_META_INFO_CSV_HEADER);

            // Write users
            for (UniMediaUserNote note : mediaNotes) {
                writer.write(
                        "%d,%d,%s,%s,%s,%s,%s,%s,%s\n".formatted(
                                note.getId(),
                                note.getMediaId(),
                                validateStr(note.getUsername()),
                                validateStr(note.getTitle()),
                                validateStr(note.getArtists()),
                                validateStr(note.getInstruments()),
                                validateStr(note.getExtraInfo()),
                                validateStr(String.valueOf(note.getStartTime())),
                                validateStr(String.valueOf(note.getEndTime()))
                        ));
            }
        } catch (IOException e) {
            System.err.println("Error saving Media Notes to CSV: " + e.getMessage());
        }
    }
    @Override
    public void saveNote(UniMediaUserNote note) {
        mediaNotes.add(note);
        saveallMediaNotes();
    }

    @Override
    public ArrayList<UniMediaUserNote> getNotes(int mediaId) {
        if (mediaId < 0) return null;

        ArrayList<UniMediaUserNote> notesWithMediaId = new ArrayList<>();

        for (UniMediaUserNote note : mediaNotes)
            if (note.getMediaId() == mediaId)
            {
                notesWithMediaId.add(note);
            }
        return notesWithMediaId;
    }

    @Override
    public int getNoteLength(int mediaId) {
        int i = 0;
        for (UniMediaSegmentInfo note : mediaNotes)
            if (note.getMediaId() == mediaId) i++;

        return i;
    }

    @Override
    public int generateNoteId() {
        return mediaNotes.size();
    }
}

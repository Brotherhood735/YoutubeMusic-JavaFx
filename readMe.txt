# UniMusic (YouTube Music–style app in JavaFX)

A desktop music and media app written in Java 21 with JavaFX, loosely inspired by YouTube Music. I made it as a university project.

## What it is

You browse a library of songs and concerts, open one, and play it inside the app. Each track page also has comments, notes and sheet music around the player.

- **Media playback.** The app plays local video and audio files through VLC (via VLCJ), MIDI files through Java's built-in synthesizer, and online videos in an embedded web view. All of them share a common `PlayableMedia` interface.
- **Home page.** Songs and concerts appear as cards, with a search bar for title or artist and genre filters (Pop, Rock, Classical, Jazz).
- **Track segments.** A track can be split into timed sections, each with its own title, artists and instruments, so you can jump to a specific part of a song.
- **Notes.** Users can attach personal notes to a time range of a track.
- **Threaded comments.** Users can comment on a track and reply to other comments in nested threads.
- **Documents.** Sheet music, lyrics and chord sheets (PDF) can be attached to a track.
- **Uploading.** Users can add new songs with their media files, cover image and details.
- **Accounts and roles.** New users register and browse as guests until an admin approves them. The admin panel is for approving or rejecting requests, promoting users to admin and removing accounts.

All data is stored in CSV files behind a repository layer, with an interface and an implementation for each kind of data, created through a single factory. The UI is split into FXML views, controllers and CSS stylesheets.

## Why I made it

Beyond being a uni project, I was curious how a real media app is put together. I wanted to see how to play different kinds of media (video, audio, MIDI, web) behind one interface. I also wanted to learn how to structure a larger JavaFX app with FXML, controllers and navigation between screens. The other parts I was interested in were how features like threaded comments, timed notes and user roles work, and how to keep the data layer separate so the storage could be swapped out later.

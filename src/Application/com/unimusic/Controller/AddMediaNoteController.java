package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.Media.UniDataType;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Media.UniMediaSegmentInfo;
import com.unimusic.Model.Repository.MediaNoteRepository;
import com.unimusic.View.Components.PlayableMedia;

public class AddMediaNoteController extends AddMediaMetaInfoController {

    private MediaNoteRepository notesRepo;
    public AddMediaNoteController(UniMedia media, PlayableMedia renderer) {
        super(media, renderer);
    }


    @Override
    public void initialize() {
        notesRepo = Main.getDataRepository();
        setupRangeSlider();
    }

    @Override
    protected void onAddMetaInfo() {
        UniMediaSegmentInfo metaInfo = generateMetaInfoClass();
        notesRepo.saveNote(metaInfo);
    }

    @Override
    protected UniMediaSegmentInfo generateMetaInfoClass() {
        return new UniMediaSegmentInfo(notesRepo.generateNoteId(), super.generateMetaInfoClass(), UniDataType.Note);
    }
}

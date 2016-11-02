package fr.bmartel.youtubetv.listener;

import java.util.List;

import fr.bmartel.youtubetv.model.VideoQuality;

/**
 * Created by akinaru on 02/11/16.
 */
public interface IVideoActivity {

    void displayQualityFragment(List<VideoQuality> availableQualities);

}

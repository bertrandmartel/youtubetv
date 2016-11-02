package fr.bmartel.youtubetv.listener;

import java.util.List;

import fr.bmartel.youtubetv.model.VideoInfo;

/**
 * Created by akinaru on 02/11/16.
 */
public interface IVideoInfoListener {

    void onQualityReceived(VideoInfo videoInfo);
}

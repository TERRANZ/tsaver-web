
package ru.terra.twochsaver.web.engine.dto;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class TwochCatalog {

    private String board;
    private String boardName;
    private String boardBannerImage;
    private String boardBannerLink;
    private Integer enableDices;
    private Integer enableFlags;
    private Integer enableIcons;
    private Integer enableImages;
    private Integer enableNames;
    private Integer enableSage;
    private Integer enableShield;
    private Integer enableSubject;
    private Integer enableTrips;
    private Integer enableVideo;
    private String filter;
    private Integer maxComment;
    private List<TwochThread> threads = new ArrayList<TwochThread>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardBannerImage() {
        return boardBannerImage;
    }

    public void setBoardBannerImage(String boardBannerImage) {
        this.boardBannerImage = boardBannerImage;
    }

    public String getBoardBannerLink() {
        return boardBannerLink;
    }

    public void setBoardBannerLink(String boardBannerLink) {
        this.boardBannerLink = boardBannerLink;
    }

    public Integer getEnableDices() {
        return enableDices;
    }

    public void setEnableDices(Integer enableDices) {
        this.enableDices = enableDices;
    }

    public Integer getEnableFlags() {
        return enableFlags;
    }

    public void setEnableFlags(Integer enableFlags) {
        this.enableFlags = enableFlags;
    }

    public Integer getEnableIcons() {
        return enableIcons;
    }

    public void setEnableIcons(Integer enableIcons) {
        this.enableIcons = enableIcons;
    }

    public Integer getEnableImages() {
        return enableImages;
    }

    public void setEnableImages(Integer enableImages) {
        this.enableImages = enableImages;
    }

    public Integer getEnableNames() {
        return enableNames;
    }

    public void setEnableNames(Integer enableNames) {
        this.enableNames = enableNames;
    }

    public Integer getEnableSage() {
        return enableSage;
    }

    public void setEnableSage(Integer enableSage) {
        this.enableSage = enableSage;
    }

    public Integer getEnableShield() {
        return enableShield;
    }

    public void setEnableShield(Integer enableShield) {
        this.enableShield = enableShield;
    }

    public Integer getEnableSubject() {
        return enableSubject;
    }

    public void setEnableSubject(Integer enableSubject) {
        this.enableSubject = enableSubject;
    }

    public Integer getEnableTrips() {
        return enableTrips;
    }

    public void setEnableTrips(Integer enableTrips) {
        this.enableTrips = enableTrips;
    }

    public Integer getEnableVideo() {
        return enableVideo;
    }

    public void setEnableVideo(Integer enableVideo) {
        this.enableVideo = enableVideo;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getMaxComment() {
        return maxComment;
    }

    public void setMaxComment(Integer maxComment) {
        this.maxComment = maxComment;
    }

    public List<TwochThread> getThreads() {
        return threads;
    }

    public void setThreads(List<TwochThread> threads) {
        this.threads = threads;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

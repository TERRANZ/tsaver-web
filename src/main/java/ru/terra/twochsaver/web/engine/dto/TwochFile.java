package ru.terra.twochsaver.web.engine.dto;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class TwochFile {

    private Integer height;
    private String md5;
    private String name;
    private String path;
    private Integer size;
    private String thumbnail;
    private Integer tnHeight;
    private Integer tnWidth;
    private Integer width;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private Integer tn_height;
    private Integer tn_width;
    private String type;
    private Integer duration;

    public TwochFile() {
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getTnHeight() {
        return tnHeight;
    }

    public void setTnHeight(Integer tnHeight) {
        this.tnHeight = tnHeight;
    }

    public Integer getTnWidth() {
        return tnWidth;
    }

    public void setTnWidth(Integer tnWidth) {
        this.tnWidth = tnWidth;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Integer getTn_height() {
        return tn_height;
    }

    public void setTn_height(Integer tn_height) {
        this.tn_height = tn_height;
    }

    public Integer getTn_width() {
        return tn_width;
    }

    public void setTn_width(Integer tn_width) {
        this.tn_width = tn_width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}

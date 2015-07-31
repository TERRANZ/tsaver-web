/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.terra.twochsaver.web.db.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author terranz
 */
@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Img.findAll", query = "SELECT i FROM Img i"),
        @NamedQuery(name = "Img.countForThr", query = "SELECT count(i) FROM Img i WHERE i.thrId = :thr"),
        @NamedQuery(name = "Img.findByUrl", query = "SELECT i FROM Img i WHERE i.url = :url")})
public class Img implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(nullable = false, length = 150)
    private String url;
    @JoinColumn(name = "thr_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Thr thrId;
    @Basic(optional = false)
    @Column(nullable = false, length = 32)
    private String md5hash;
    @Basic(optional = false)
    @Column(nullable = false, length = 150)
    private String fileName;


    public Img() {
    }

    public Img(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Thr getThrId() {
        return thrId;
    }

    public void setThrId(Thr thrId) {
        this.thrId = thrId;
    }

    public String getMd5hash() {
        return md5hash;
    }

    public void setMd5hash(String md5hash) {
        this.md5hash = md5hash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Img{" +
                "url='" + url + '\'' +
                ", thrId=" + thrId +
                ", md5hash='" + md5hash + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Img img = (Img) o;

        if (fileName != null ? !fileName.equals(img.fileName) : img.fileName != null) return false;
        if (md5hash != null ? !md5hash.equals(img.md5hash) : img.md5hash != null) return false;
        if (thrId != null ? !thrId.equals(img.thrId) : img.thrId != null) return false;
        if (url != null ? !url.equals(img.url) : img.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (thrId != null ? thrId.hashCode() : 0);
        result = 31 * result + (md5hash != null ? md5hash.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.terra.twochsaver.web.db.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author terranz
 */
@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Thr.findAll", query = "SELECT t FROM Thr t"),
        @NamedQuery(name = "Thr.findById", query = "SELECT t FROM Thr t WHERE t.id = :id"),
        @NamedQuery(name = "Thr.findByUrl", query = "SELECT t FROM Thr t WHERE t.url = :url"),
        @NamedQuery(name = "Thr.findByAdded", query = "SELECT t FROM Thr t WHERE t.added = :added"),
        @NamedQuery(name = "Thr.findByUpdated", query = "SELECT t FROM Thr t WHERE t.updated = :updated"),
        @NamedQuery(name = "Thr.findByCount", query = "SELECT t FROM Thr t WHERE t.count = :count"),
        @NamedQuery(name = "Thr.findByChecked", query = "SELECT t FROM Thr t WHERE t.checked = :checked"),
        @NamedQuery(name = "Thr.findUnFinished", query = "SELECT t FROM Thr t WHERE t.finished = 0")})
public class Thr implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String url;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date added;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    private Integer count;
    private Integer checked;
    private Integer finished;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "thrId", fetch = FetchType.LAZY)
    private List<Img> imgList;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String title;

    public Thr() {
    }

    public Thr(Integer id) {
        this.id = id;
    }

    public Thr(Integer id, String url, Date added, Date updated) {
        this.id = id;
        this.url = url;
        this.added = added;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    @XmlTransient
    @JsonIgnore
    public List<Img> getImgList() {
        return imgList;
    }

    public void setImgList(List<Img> imgList) {
        this.imgList = imgList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Thr thr = (Thr) o;

        if (added != null ? !added.equals(thr.added) : thr.added != null) return false;
        if (checked != null ? !checked.equals(thr.checked) : thr.checked != null) return false;
        if (count != null ? !count.equals(thr.count) : thr.count != null) return false;
        if (finished != null ? !finished.equals(thr.finished) : thr.finished != null) return false;
        if (id != null ? !id.equals(thr.id) : thr.id != null) return false;
        if (imgList != null ? !imgList.equals(thr.imgList) : thr.imgList != null) return false;
        if (title != null ? !title.equals(thr.title) : thr.title != null) return false;
        if (updated != null ? !updated.equals(thr.updated) : thr.updated != null) return false;
        if (url != null ? !url.equals(thr.url) : thr.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (added != null ? added.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (checked != null ? checked.hashCode() : 0);
        result = 31 * result + (finished != null ? finished.hashCode() : 0);
        result = 31 * result + (imgList != null ? imgList.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Thr{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", added=" + added +
                ", updated=" + updated +
                ", count=" + count +
                ", checked=" + checked +
                ", finished=" + finished +
                ", imgList=" + imgList +
                ", title='" + title + '\'' +
                '}';
    }
}

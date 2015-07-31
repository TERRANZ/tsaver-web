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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Thr)) {
            return false;
        }
        Thr other = (Thr) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.terra.twochsaver.web.db.Thr[ id=" + id + " ]";
    }

}

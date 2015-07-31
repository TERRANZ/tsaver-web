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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (url != null ? url.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Img)) {
            return false;
        }
        Img other = (Img) object;
        if ((this.url == null && other.url != null) || (this.url != null && !this.url.equals(other.url))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.terra.twochsaver.web.db.Img[ url=" + url + " ]";
    }

}

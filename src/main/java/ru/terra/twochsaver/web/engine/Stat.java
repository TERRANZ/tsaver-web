package ru.terra.twochsaver.web.engine;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Date: 21.01.15
 * Time: 20:18
 */
@XmlRootElement
public class Stat {
    public String bt;
    public Integer count;
    public Integer remaining;
    public Integer finished;
    public Integer checked;
    public String added;
    public String updated;
    public String title;


    public Stat(String bt, Integer count, Integer remaining) {
        this.bt = bt;
        this.count = count;
        this.remaining = remaining;
    }

    public Stat(String bt, Integer count, Integer remaining, Integer finished, Integer checked, String added, String updated, String title) {
        this.bt = bt;
        this.count = count;
        this.remaining = remaining;
        this.finished = finished;
        this.checked = checked;
        this.added = added;
        this.updated = updated;
        this.title = title;
    }

    public Stat() {
    }
}

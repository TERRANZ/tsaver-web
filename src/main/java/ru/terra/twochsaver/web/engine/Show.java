package ru.terra.twochsaver.web.engine;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by terranz on 21.01.16.
 */
@XmlRootElement
public class Show extends Stat {
    public List<String> images;

    public Show(String bt, Integer count, Integer remaining, Integer finished, Integer checked, String added, String updated, List<String> images) {
        super(bt, count, remaining, finished, checked, added, updated);
        this.images = images;
    }


}

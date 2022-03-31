package hello.itemservice.domain.item;

import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "Album")
@Getter
@NoArgsConstructor
public class Album extends Item{

    private String artist;
    private String etc;

    public Album(String artist, String etc) {
        this.artist = artist;
        this.etc = etc;
    }
}

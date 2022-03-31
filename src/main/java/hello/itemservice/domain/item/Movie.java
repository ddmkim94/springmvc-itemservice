package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "Movie")
@Getter
@NoArgsConstructor
public class Movie extends Item{

    private String director;
    private String actor;

    public Movie(String director, String actor) {
        this.director = director;
        this.actor = actor;
    }
}

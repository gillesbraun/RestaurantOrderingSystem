package lu.btsi.bragi.ros.models;

import java.time.LocalDateTime;

/**
 * Created by gillesbraun on 15/02/2017.
 */
public class Table {
    private final int id;

    private int places;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public int getId() {
        return id;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public Table(int id, int places, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.places = places;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}

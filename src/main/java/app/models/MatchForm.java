package app.models;

import javax.validation.constraints.NotNull;

public class MatchForm {

    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

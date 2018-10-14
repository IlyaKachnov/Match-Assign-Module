package app.models;

import javax.validation.constraints.NotNull;

public class MatchForm {

    @NotNull
    private Long id;

    private String urlToRedirect;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlToRedirect() {
        return urlToRedirect;
    }

    public void setUrlToRedirect(String urlToRedirect) {
        this.urlToRedirect = urlToRedirect;
    }
}

package app.services;

import app.models.MatchMessage;

import java.util.List;

public interface SlotMessageService {

    MatchMessage findById(Long id);

    List<MatchMessage> findAll();

    void save(MatchMessage matchMessage);

    void store(MatchMessage matchMessage);

    void delete(Long id);
}

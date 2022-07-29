package ru.sergio.kanji.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sergio.kanji.dictionary.model.KanjiRus;

import java.util.List;
import java.util.Optional;

@Repository
public interface KanjiRusRepository extends JpaRepository<KanjiRus, Long> {

    Optional<KanjiRus> findBySymbol(String symbol);

}

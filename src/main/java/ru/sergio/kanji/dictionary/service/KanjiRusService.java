package ru.sergio.kanji.dictionary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sergio.kanji.dictionary.mapper.KanjiRusMapper;
import ru.sergio.kanji.dictionary.model.dto.KanjiRusDto;
import ru.sergio.kanji.dictionary.repository.KanjiRusRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KanjiRusService {

    private final KanjiRusRepository kanjiRusRepository;

    private final KanjiRusMapper kanjiRusMapper;

    public KanjiRusDto getKanji(String kanji) {
        log.info("Searching kanji {}", kanji);
        return kanjiRusRepository.findBySymbol(kanji)
                .map(kanjiRusMapper::toKanjiRusDto)
                .orElseThrow(() -> new IllegalArgumentException("No such kanji found"));
    }

    public List<KanjiRusDto> getKanjiList(int size, int page) {
        log.info("Getting kanji page {} with size {}", page, size);
        return kanjiRusRepository.findAll(Pageable.ofSize(size).withPage(page))
                .stream()
                .map(kanjiRusMapper::toKanjiRusDto)
                .collect(Collectors.toList());
    }

}

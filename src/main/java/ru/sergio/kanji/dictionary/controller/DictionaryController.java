package ru.sergio.kanji.dictionary.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sergio.kanji.dictionary.model.dto.KanjiPrediction;
import ru.sergio.kanji.dictionary.model.dto.KanjiRusDto;
import ru.sergio.kanji.dictionary.service.KanjiRusService;
import ru.sergio.kanji.dictionary.service.RecognitionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final RecognitionService recognitionService;

    private final KanjiRusService kanjiRusService;


    @PostMapping("recognize")
    public List<KanjiPrediction> recognize(@RequestBody String kanjiImage, @RequestParam int size) {
        return recognitionService.recognize(kanjiImage, size);
    }

    @GetMapping("kanji")
    public KanjiRusDto getKanji(@RequestParam String kanjiSymbol) {
        return kanjiRusService.getKanji(kanjiSymbol);
    }

    @GetMapping("kanji-list/{pageNumber}")
    public List<KanjiRusDto> getKanjiList(@PathVariable int pageNumber, @RequestParam int size) {
        return kanjiRusService.getKanjiList(size, pageNumber);
    }

}

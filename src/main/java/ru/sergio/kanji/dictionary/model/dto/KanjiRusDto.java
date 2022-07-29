package ru.sergio.kanji.dictionary.model.dto;

import lombok.Data;

@Data
public class KanjiRusDto {

    private Long id;

    private String symbol;

    private String yomi;

    private String onYomiEng;

    private String kunYomiEng;

    private String onYomiTranslationRus;

    private String kunYomiTranslationRus;

}

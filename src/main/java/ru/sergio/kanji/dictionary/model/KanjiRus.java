package ru.sergio.kanji.dictionary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "kanji_rus")
@AllArgsConstructor
@NoArgsConstructor
public class KanjiRus {

    @Id
    private Long id;

    private String symbol;

    private String yomi;

    private String onYomiEng;

    private String kunYomiEng;

    private String onYomiTranslationRus;

    private String kunYomiTranslationRus;

}


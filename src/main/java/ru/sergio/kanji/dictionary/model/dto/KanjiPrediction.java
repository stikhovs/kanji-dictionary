package ru.sergio.kanji.dictionary.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KanjiPrediction {

    private String className;

    private BigDecimal probability;

}

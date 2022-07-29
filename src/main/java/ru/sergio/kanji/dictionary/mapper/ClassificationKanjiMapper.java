package ru.sergio.kanji.dictionary.mapper;

import ai.djl.modality.Classifications;
import org.mapstruct.Mapper;
import ru.sergio.kanji.dictionary.model.dto.KanjiPrediction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassificationKanjiMapper {

    List<KanjiPrediction> toKanjiPredictionList(List<Classifications.Classification> classifications);

    /*default BigDecimal toBigDecimal(double num) {
        return BigDecimal.valueOf(num).setScale(10, RoundingMode.HALF_UP);
    }*/

}

package ru.sergio.kanji.dictionary.mapper;

import org.mapstruct.Mapper;
import ru.sergio.kanji.dictionary.model.KanjiRus;
import ru.sergio.kanji.dictionary.model.dto.KanjiRusDto;

@Mapper(componentModel = "spring")
public interface KanjiRusMapper {

    KanjiRusDto toKanjiRusDto(KanjiRus kanjiRus);

}

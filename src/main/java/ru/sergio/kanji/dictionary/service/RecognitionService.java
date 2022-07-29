package ru.sergio.kanji.dictionary.service;

import ai.djl.modality.Classifications;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import ru.sergio.kanji.dictionary.cnn.KanjiCnn;
import ru.sergio.kanji.dictionary.mapper.ClassificationKanjiMapper;
import ru.sergio.kanji.dictionary.model.dto.KanjiPrediction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecognitionService {

    private final static String ENCODING_PREFIX = "base64,";
    private final static int IMAGE_SIZE_IN_PIXELS = 64;
    private final static String MODEL_NAME = "test_kanji_model_2";

    private final KanjiCnn kanjiCnn;

    private final ClassificationKanjiMapper classificationKanjiMapper;


    public List<KanjiPrediction> recognize(String kanjiImage, int size) {
        BufferedImage image = getAndPrepareImage(kanjiImage);

        log.info("Start recognition");
        Classifications classifications = kanjiCnn.predict(MODEL_NAME, image);
        log.info("End Recognition");

        return classificationKanjiMapper.toKanjiPredictionList(classifications.topK(size));
    }

    private BufferedImage getAndPrepareImage(String kanjiImage) {
        BufferedImage bufferedImage = toBufferedImage(kanjiImage);
        BufferedImage resizedImage = resizeImage(bufferedImage, IMAGE_SIZE_IN_PIXELS);
        inverseColor(resizedImage);
        return resizedImage;
    }

    @SneakyThrows
    private BufferedImage toBufferedImage(String kanjiImage) {
        int contentStartIndex = kanjiImage.indexOf(ENCODING_PREFIX) + ENCODING_PREFIX.length();
        byte[] decodedBytes = Base64.getDecoder().decode(kanjiImage.substring(contentStartIndex));
        return ImageIO.read(new ByteArrayInputStream(decodedBytes));
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int size) {
        return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, size);
    }

    private void inverseColor(BufferedImage bufferedImage) {
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int rgba = bufferedImage.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                bufferedImage.setRGB(x, y, col.getRGB());
            }
        }
    }
}

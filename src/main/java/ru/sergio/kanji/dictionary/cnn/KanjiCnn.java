package ru.sergio.kanji.dictionary.cnn;

import ai.djl.Application;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Activation;
import ai.djl.nn.Block;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.convolutional.Conv2d;
import ai.djl.nn.core.Linear;
import ai.djl.nn.norm.Dropout;
import ai.djl.nn.pooling.Pool;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Translator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KanjiCnn {

    private final KanjiTranslator kanjiTranslator;


    @SneakyThrows
    public Classifications predict(String modelName, BufferedImage bufferedImage) {
        List<String> classNames = List.of("一", "丁", "七", "万", "三", "上", "下", "不", "世", "両", "中", "主", "久", "乗", "九", "予", "争", "事", "二", "五", "交", "京", "人", "仁", "今", "仏", "仕", "他", "付", "代", "令", "以", "仮", "件", "任", "休", "会", "伝", "似", "位", "低", "住", "体", "何", "余", "作", "使", "例", "供", "依", "価", "便", "係", "俗", "保", "信", "修", "俵", "倉", "個", "倍", "候", "借", "停", "健", "側", "備", "働", "像", "億", "元", "兄", "先", "光", "児", "党", "入", "全", "八", "公", "六", "共", "兵", "具", "典", "兼", "内", "円", "再", "写", "冬", "冷", "処", "出", "刀", "分", "切", "刊", "列", "初", "判", "別", "利", "制", "刷", "券", "則", "前", "創", "力", "功", "加", "助", "努", "労", "効", "勇", "勉", "動", "務", "勝", "勢", "勤", "勧", "包", "化", "北", "区", "医", "十", "千", "午", "半", "卒", "協", "南", "単", "博", "印", "厚", "原", "厳", "去", "参", "友", "反", "収", "取", "受", "口", "古", "句", "可", "台", "史", "右", "号", "司", "各", "合", "同", "名", "后", "向", "君", "否", "告", "周", "味", "命", "和", "品", "員", "唱", "商", "問", "善", "喜", "営", "器", "四", "回", "因", "団", "囲", "図", "固", "国", "園", "土", "圧", "在", "地", "坂", "均", "型", "基", "堂", "報", "場", "塩", "境", "墓", "増", "士", "声", "壱", "売", "変", "夏", "夕", "外", "多", "夜", "大", "天", "太", "夫", "央", "失", "奮", "女", "妹", "妻", "姉", "始", "委", "婦", "子", "字", "存", "孝", "季", "学", "孫", "守", "安", "完", "宗", "官", "定", "実", "客", "宣", "室", "宮", "害", "家", "容", "宿", "寄", "富", "寒", "察", "寺", "対", "専", "尊", "導", "小", "少", "就", "局", "居", "届", "屋", "展", "属", "山", "岩", "岸", "島", "川", "州", "工", "左", "差", "己", "市", "布", "希", "師", "席", "帯", "帰", "帳", "常", "幅", "平", "年", "幸", "幹", "広", "序", "底", "店", "府", "度", "庫", "庭", "康", "延", "建", "弁", "式", "弐", "引", "弟", "弱", "張", "強", "当", "形", "役", "往", "待", "律", "後", "徒", "従", "得", "復", "徳", "心", "必", "志", "応", "忠", "快", "念", "思", "急", "性", "恩", "息", "悪", "悲", "情", "想", "意", "愛", "感", "態", "慣", "憲", "成", "我", "戦", "戸", "所", "手", "才", "打", "承", "技", "投", "折", "招", "拝", "拡", "拾", "持", "指", "挙", "授", "採", "接", "推", "提", "損", "支", "改", "放", "政", "故", "救", "敗", "教", "散", "敬", "数", "整", "敵", "文", "料", "断", "新", "方", "旅", "族", "旗", "日", "旧", "早", "明", "易", "星", "春", "昨", "昭", "是", "昼", "時", "景", "晴", "暑", "暗", "暴", "曜", "曲", "書", "最", "月", "有", "服", "望", "朝", "期", "木", "未", "末", "本", "材", "村", "条", "来", "東", "板", "林", "果", "柱", "査", "栄", "校", "株", "根", "格", "案", "械", "森", "植", "検", "業", "極", "楽", "構", "様", "標", "権", "横", "橋", "機", "欠", "次", "欲", "歌", "歓", "止", "正", "武", "歩", "歯", "歴", "死", "残", "殺", "母", "毎", "毒", "比", "毛", "氏", "民", "気", "水", "氷", "永", "求", "池", "決", "汽", "河", "油", "治", "法", "波", "注", "泳", "洋", "活", "派", "流", "浅", "浴", "海", "消", "液", "深", "混", "清", "済", "減", "温", "測", "港", "湖", "湯", "満", "準", "漁", "演", "漢", "潔", "火", "災", "炭", "点", "無", "然", "焼", "照", "熱", "燃", "父", "版", "牛", "牧", "物", "特", "犬", "犯", "状", "独", "率", "玉", "王", "現", "球", "理", "生", "産", "用", "田", "由", "申", "男", "町", "画", "界", "畑", "留", "略", "番", "異", "疑", "病", "発", "登", "白", "百", "的", "皇", "皮", "益", "盟", "目", "直", "相", "省", "県", "真", "眼", "着", "知", "短", "石", "研", "破", "確", "示", "礼", "社", "祖", "祝", "神", "票", "祭", "禁", "福", "私", "秋", "科", "秒", "称", "移", "程", "税", "種", "穀", "積", "究", "空", "立", "章", "童", "競", "竹", "第", "筆", "等", "答", "策", "算", "管", "節", "築", "米", "粉", "精", "糸", "系", "紀", "約", "納", "純", "紙", "級", "素", "細", "終", "組", "経", "結", "給", "統", "絵", "絶", "絹", "続", "綿", "総", "緑", "線", "編", "練", "績", "織", "罪", "置", "美", "群", "義", "習", "老", "考", "者", "耕", "耳", "聖", "聞", "職", "肉", "肥", "育", "胃", "能", "脈", "腸", "臣", "臨", "自", "至", "興", "舌", "舎", "航", "船", "良", "色", "花", "芸", "芽", "苦", "英", "茶", "草", "荷", "菜", "落", "葉", "著", "蔵", "薬", "虫", "蚕", "血", "衆", "行", "術", "衛", "表", "補", "製", "複", "西", "要", "見", "規", "視", "覚", "親", "観", "角", "解", "言", "計", "討", "訓", "記", "設", "許", "訳", "証", "評", "詞", "試", "詩", "話", "認", "語", "誠", "誤", "説", "読", "課", "調", "談", "論", "諸", "講", "謝", "識", "議", "護", "谷", "豊", "象", "貝", "負", "財", "貧", "貨", "責", "貯", "貴", "買", "貸", "費", "貿", "賀", "賃", "資", "賛", "賞", "質", "赤", "走", "起", "足", "路", "身", "車", "軍", "転", "軽", "輪", "輸", "辞", "農", "辺", "近", "返", "述", "迷", "追", "退", "送", "逆", "通", "速", "造", "連", "週", "進", "遊", "運", "過", "道", "達", "遠", "適", "選", "遺", "郡", "部", "都", "配", "酒", "酸", "釈", "里", "重", "野", "量", "金", "鉄", "鉱", "銀", "銅", "銭", "録", "鏡", "長", "門", "開", "間", "関", "防", "限", "陛", "院", "除", "陸", "険", "陽", "隊", "階", "際", "集", "雑", "難", "雨", "雪", "雲", "電", "需", "青", "静", "非", "面", "革", "音", "順", "預", "領", "頭", "題", "額", "顔", "願", "類", "風", "飛", "食", "飯", "飲", "養", "館", "首", "馬", "駅", "験", "高", "魚", "鳥", "鳴", "麦", "黄", "黒", "鼻");
        Translator<Image, Classifications> translator =
                ImageClassificationTranslator.builder()
                        .addTransform(new ToTensor())
                        .optSynset(classNames)
                        .build();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("models/test_kanji_model_2-0000.params");
        File file = new File("models/test_kanji_model_2-0000.params");
        FileUtils.copyInputStreamToFile(inputStream, file);

        Criteria<Image, Classifications> criteria = Criteria.builder()
                .optApplication(Application.CV.OBJECT_DETECTION)
                .setTypes(Image.class, Classifications.class)
                .optBlock(getCnn())
                .optModelPath(file.getParentFile().toPath())
                .optModelName(modelName)
                .optTranslator(translator)
                .build();

        ZooModel<Image, Classifications> model = criteria.loadModel();

        Image image = ImageFactory.getInstance().fromImage(bufferedImage);
        Classifications predict = model.newPredictor(kanjiTranslator).predict(image);
        System.out.println(predict);
        return predict;
    }

    private Block getCnn() {
        return new SequentialBlock()
                .add(Conv2d.builder()
                        .setKernelShape(new Shape(3, 3))
                        .optStride(new Shape(1, 1))
                        .setFilters(64)
                        .optBias(false)
                        .build())
                .add(Activation::relu)
                .add(Pool.maxPool2dBlock(new Shape(2, 2), new Shape(2, 2)))
                .add(Conv2d.builder()
                        .setKernelShape(new Shape(3, 3))
                        .optStride(new Shape(1, 1))
                        .setFilters(64)
                        .optBias(false)
                        .build())
                .add(Activation::relu)
                .add(Pool.maxPool2dBlock(new Shape(2, 2), new Shape(2, 2)))
                .add(Blocks.batchFlattenBlock())
                .add(Dropout.builder().optRate(0.5f).build())
                .add(Linear
                        .builder()
                        .setUnits(2048)
                        .build())
                .add(Activation::relu)
                .add(Linear
                        .builder()
                        .setUnits(880)
                        .build());
    }

}

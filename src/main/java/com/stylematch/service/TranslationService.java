package com.stylematch.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class TranslationService {

    private final Map<String, Map<String, String>> translations = new HashMap<>();

    public TranslationService() {
        // Simple internal translation map for core system text
        // English (en)
        Map<String, String> en = new HashMap<>();
        en.put("BODY_SHAPE_EXPLANATION_HOURGLASS", "Balanced shoulders and hips with a defined waist.");
        en.put("BODY_SHAPE_EXPLANATION_RECTANGLE", "Shoulders, bust, and hips are fairly uniform with little waist definition.");
        en.put("BODY_SHAPE_EXPLANATION_TRIANGLE", "Hips are wider than the shoulders and bust (Pear shape).");
        en.put("BODY_SHAPE_EXPLANATION_INVERTED_TRIANGLE", "Shoulders or bust are wider than the hips.");
        en.put("BODY_SHAPE_EXPLANATION_OVAL", "Bust and waist are wider than the hips (Apple shape).");
        
        en.put("BODY_SHAPE_DISPLAY_NAME_HOURGLASS", "Hourglass");
        en.put("BODY_SHAPE_DISPLAY_NAME_RECTANGLE", "Rectangle");
        en.put("BODY_SHAPE_DISPLAY_NAME_TRIANGLE", "Triangle");
        en.put("BODY_SHAPE_DISPLAY_NAME_INVERTED_TRIANGLE", "Inverted Triangle");
        en.put("BODY_SHAPE_DISPLAY_NAME_OVAL", "Oval");
        
        en.put("REC_HOURGLASS_1", "Highlight your waist with belts and wrap dresses.");
        en.put("REC_HOURGLASS_2", "V-necks and scoop necks work great.");
        en.put("REC_RECTANGLE_1", "Create curves with peplum tops and ruffled details.");
        en.put("REC_RECTANGLE_2", "Use layers and textures to add dimension.");
        en.put("REC_TRIANGLE_1", "Add volume to your upper body with boat necks.");
        en.put("REC_TRIANGLE_2", "Darker colors on the bottom help balance your silhouette.");
        en.put("REC_INVERTED_TRIANGLE_1", "Fuller skirts and wide-leg pants add volume to the bottom.");
        en.put("REC_INVERTED_TRIANGLE_2", "V-necks help narrow your upper body visually.");
        en.put("REC_OVAL_1", "Empire waist dresses and tunics are very flattering.");
        en.put("REC_OVAL_2", "Monochromatic outfits create a longer, leaner look.");
        
        en.put("COLOR_RESULT_PREFIX", "Color Analysis Result");
        en.put("COLOR_RESULT_METADATA", "Personalized style profile based on seasonal color theory.");
        en.put("COLOR_ANALYSIS_COMPLETED", "Your personalized color analysis is ready!");

        en.put("STYLE_ADVICE_TEMPLATE_WINTER", "Hello {name}! As a {age}-year-old Winter type, jewel tones and high contrast are your power move. Deep sapphire and emerald look stunning on you. ✨");
        en.put("STYLE_ADVICE_TEMPLATE_AUTUMN", "Welcome {name}! Your {age}-year-old glow is perfectly complemented by rich earthy tones like terracotta and olive. Muted gold jewelry flatters you perfectly. 🍂");
        en.put("STYLE_ADVICE_TEMPLATE_SPRING", "Hey {name}! At {age}, light coral and peach make you look vibrant. Delicate gold jewelry and soft blush are your best friends. 🌸");
        en.put("STYLE_ADVICE_TEMPLATE_SUMMER", "Hi {name}! Soft muted blues and lavender look beautiful with your {age}-year-old complexion. Silver jewelry enhances your cool undertone. 💙");
        en.put("STYLE_ADVICE_TEMPLATE_DEFAULT", "Your style at {age} is uniquely beautiful, {name}. Experiment with color and trust your instincts! 💜");
        
        translations.put("en", en);

        // Ukrainian (uk)
        Map<String, String> uk = new HashMap<>();
        uk.put("BODY_SHAPE_EXPLANATION_HOURGLASS", "Збалансовані плечі та стегна з вираженою талією.");
        uk.put("BODY_SHAPE_EXPLANATION_RECTANGLE", "Плечі, бюст і стегна досить рівномірні, талія маловиражена.");
        uk.put("BODY_SHAPE_EXPLANATION_TRIANGLE", "Стегна ширші за плечі та бюст (тип Груша).");
        uk.put("BODY_SHAPE_EXPLANATION_INVERTED_TRIANGLE", "Плечі або бюст ширші за стегна (Перевернутий трикутник).");
        uk.put("BODY_SHAPE_EXPLANATION_OVAL", "Бюст і талія ширші за стегна (тип Яблуко).");
        
        uk.put("BODY_SHAPE_DISPLAY_NAME_HOURGLASS", "Пісочний годинник");
        uk.put("BODY_SHAPE_DISPLAY_NAME_RECTANGLE", "Прямокутник");
        uk.put("BODY_SHAPE_DISPLAY_NAME_TRIANGLE", "Трикутник");
        uk.put("BODY_SHAPE_DISPLAY_NAME_INVERTED_TRIANGLE", "Перевернутий трикутник");
        uk.put("BODY_SHAPE_DISPLAY_NAME_OVAL", "Овал");
        
        uk.put("COLOR_RESULT_PREFIX", "Результат аналізу кольору");
        uk.put("COLOR_RESULT_METADATA", "Персоналізований профіль стилю на основі теорії сезонного кольору.");
        uk.put("COLOR_ANALYSIS_COMPLETED", "Ваш персональний аналіз кольору готовий!");
        
        uk.put("REC_HOURGLASS_1", "Підкреслюйте талію поясами та сукнями на запах.");
        uk.put("REC_HOURGLASS_2", "V-подібні та овальні вирізи чудово пасують.");
        uk.put("REC_RECTANGLE_1", "Створюйте вигини за допомогою баски та рюшів.");
        uk.put("REC_RECTANGLE_2", "Використовуйте шаруватість та текстури для об'єму.");
        uk.put("REC_TRIANGLE_1", "Додайте об'єму верхній частині тіла за допомогою вирізу човник.");
        uk.put("REC_TRIANGLE_2", "Темні кольори низу допоможуть збалансувати силует.");
        uk.put("REC_INVERTED_TRIANGLE_1", "Пишні спідниці та широкі штани додають об'єму низу.");
        uk.put("REC_INVERTED_TRIANGLE_2", "V-подібні вирізи візуально звужують верхню частину.");
        uk.put("REC_OVAL_1", "Сукні з завищеною талією (ампір) та туніки дуже личать.");
        uk.put("REC_OVAL_2", "Монохромні образи створюють більш стрункий вигляд.");

        uk.put("STYLE_ADVICE_TEMPLATE_WINTER", "Привіт, {name}! Для вашого віку ({age} років) та типу Зима ідеально пасують насичені дорогоцінні відтінки. ✨");
        uk.put("STYLE_ADVICE_TEMPLATE_AUTUMN", "Вітаємо, {name}! У ваші {age} років земні відтінки, такі як теракотовий та оливковий, підкреслять вашу природну красу. 🍂");
        uk.put("STYLE_ADVICE_TEMPLATE_SPRING", "Привіт, {name}! У {age} років світлий корал і персик змушують вас виглядати яскраво. 🌸");
        uk.put("STYLE_ADVICE_TEMPLATE_SUMMER", "Привіт, {name}! Ніжні приглушені сині та лавандові кольори чудово пасують до вашого кольору обличчя в {age} років. 💙");
        uk.put("STYLE_ADVICE_TEMPLATE_DEFAULT", "Ваш стиль у {age} років унікальний, {name}. Довіртеся інтуїції! 💜");
        
        translations.put("uk", uk);

        // German (de)
        Map<String, String> de = new HashMap<>();
        de.put("BODY_SHAPE_EXPLANATION_HOURGLASS", "Ausgewogene Schultern und Hüften mit einer definierten Taille.");
        de.put("BODY_SHAPE_EXPLANATION_RECTANGLE", "Schultern, Brust und Hüften sind ziemlich einheitlich mit wenig Taillendefinition.");
        de.put("BODY_SHAPE_EXPLANATION_TRIANGLE", "Die Hüften sind breiter als Schultern und Brust (Birnenform).");
        de.put("BODY_SHAPE_EXPLANATION_INVERTED_TRIANGLE", "Schultern oder Brust sind breiter als die Hüften.");
        de.put("BODY_SHAPE_EXPLANATION_OVAL", "Brust und Taille sind breiter als die Hüften (Apfelform).");
        
        de.put("BODY_SHAPE_DISPLAY_NAME_HOURGLASS", "Sanduhr");
        de.put("BODY_SHAPE_DISPLAY_NAME_RECTANGLE", "Rechteck");
        de.put("BODY_SHAPE_DISPLAY_NAME_TRIANGLE", "Dreieck");
        de.put("BODY_SHAPE_DISPLAY_NAME_INVERTED_TRIANGLE", "Umgekehrtes Dreieck");
        de.put("BODY_SHAPE_DISPLAY_NAME_OVAL", "Oval");

        de.put("COLOR_RESULT_PREFIX", "Farbanalyse Ergebnis");
        de.put("COLOR_RESULT_METADATA", "Personalisiertes Stilprofil basierend auf der saisonalen Farbentheorie.");
        de.put("COLOR_ANALYSIS_COMPLETED", "Ihre personalisierte Farbanalyse ist fertig!");
        
        de.put("REC_HOURGLASS_1", "Betonen Sie Ihre Taille mit Gürteln und Wickelkleidern.");
        de.put("REC_HOURGLASS_2", "V-Ausschnitte und Rundhalsausschnitte funktionieren hervorragend.");
        de.put("REC_RECTANGLE_1", "Erzeugen Sie Kurven mit Peplum-Oberteilen und Rüschendetails.");
        de.put("REC_RECTANGLE_2", "Nutzen Sie Lagen-Looks und Texturen für mehr Dimension.");
        de.put("REC_TRIANGLE_1", "Bringen Sie Volumen in Ihren Oberkörper mit U-Boot-Ausschnitten.");
        de.put("REC_TRIANGLE_2", "Dunklere Farben unten helfen, die Silhouette auszugleichen.");
        de.put("REC_INVERTED_TRIANGLE_1", "Ausgestellte Röcke und weite Hosen bringen Volumen nach unten.");
        de.put("REC_INVERTED_TRIANGLE_2", "V-Ausschnitte lassen den Oberkörper optisch schmaler wirken.");
        de.put("REC_OVAL_1", "Kleider mit Empire-Taille und Tuniken stehen Ihnen sehr gut.");
        de.put("REC_OVAL_2", "Monochrome Outfits strecken die Silhouette optisch.");

        de.put("STYLE_ADVICE_TEMPLATE_WINTER", "Hallo {name}! Als {age}-jähriger Winter-Typ sind kräftige Farben und hoher Kontrast Ihre Stärke. ✨");
        de.put("STYLE_ADVICE_TEMPLATE_AUTUMN", "Willkommen {name}! Ihr {age}-jähriges Strahlen wird perfekt durch erdige Töne wie Terrakotta ergänzt. 🍂");
        de.put("STYLE_ADVICE_TEMPLATE_SPRING", "Hallo {name}! Mit {age} lassen dich helles Korallenrot und Pfirsich lebendig wirken. 🌸");
        de.put("STYLE_ADVICE_TEMPLATE_SUMMER", "Hallo {name}! Zarte gedämpfte Blau- und Lavendeltöne sehen mit {age} wunderschön aus. 💙");
        de.put("STYLE_ADVICE_TEMPLATE_DEFAULT", "Dein Stil mit {age} ist einzigartig schön, {name}. Vertraue deinem Instinkt! 💜");
        
        translations.put("de", de);

        // Spanish (es)
        Map<String, String> es = new HashMap<>();
        es.put("BODY_SHAPE_EXPLANATION_HOURGLASS", "Hombros y caderas equilibrados con una cintura definida.");
        es.put("BODY_SHAPE_EXPLANATION_RECTANGLE", "Hombros, busto y caderas son bastante uniformes con poca definición de la cintura.");
        es.put("BODY_SHAPE_EXPLANATION_TRIANGLE", "Las caderas son más anchas que los hombros y el busto (forma de pera).");
        es.put("BODY_SHAPE_EXPLANATION_INVERTED_TRIANGLE", "Los hombros o el busto son más anchos que las caderas.");
        es.put("BODY_SHAPE_EXPLANATION_OVAL", "El busto y la cintura son más anchos que las caderas (forma de manzana).");
        
        es.put("BODY_SHAPE_DISPLAY_NAME_HOURGLASS", "Reloj de Arena");
        es.put("BODY_SHAPE_DISPLAY_NAME_RECTANGLE", "Rectángulo");
        es.put("BODY_SHAPE_DISPLAY_NAME_TRIANGLE", "Triángulo");
        es.put("BODY_SHAPE_DISPLAY_NAME_INVERTED_TRIANGLE", "Triángulo Invertido");
        es.put("BODY_SHAPE_DISPLAY_NAME_OVAL", "Ovalado");

        es.put("COLOR_RESULT_PREFIX", "Resultado del análisis de color");
        es.put("COLOR_RESULT_METADATA", "Perfil de estilo personalizado basado en la teoría del color estacional.");
        es.put("COLOR_ANALYSIS_COMPLETED", "¡Tu análisis de color personalizado está listo!");
        
        es.put("REC_HOURGLASS_1", "Resalta tu cintura con cinturones y vestidos cruzados.");
        es.put("REC_HOURGLASS_2", "Los escotes en V y redondos funcionan de maravilla.");
        es.put("REC_RECTANGLE_1", "Crea curvas con tops peplum y detalles con volantes.");
        es.put("REC_RECTANGLE_2", "Usa capas y texturas para añadir dimensión a tu look.");
        es.put("REC_TRIANGLE_1", "Añade volumen a tu parte superior con cuellos barco.");
        es.put("REC_TRIANGLE_2", "Colores más oscuros abajo ayudan a equilibrar tu silueta.");
        es.put("REC_INVERTED_TRIANGLE_1", "Faldas con vuelo y pantalones anchos añaden volumen abajo.");
        es.put("REC_INVERTED_TRIANGLE_2", "Los escotes en V ayudan a estrechar visualmente el torso.");
        es.put("REC_OVAL_1", "Los vestidos de cintura imperio y las túnicas son muy favorecedores.");
        es.put("REC_OVAL_2", "Los conjuntos monocromáticos crean un efecto más estilizado.");

        es.put("STYLE_ADVICE_TEMPLATE_WINTER", "¡Hola {name}! A los {age} años, como tipo Invierno, los tonos joya son tu mejor aliado. ✨");
        es.put("STYLE_ADVICE_TEMPLATE_AUTUMN", "¡Bienvenida {name}! Tu brillo a los {age} años se complementa con tonos tierra como el terracota. 🍂");
        es.put("STYLE_ADVICE_TEMPLATE_SPRING", "¡Hola {name}! A los {age}, el coral claro y el melocotón te hacen lucir vibrante. 🌸");
        es.put("STYLE_ADVICE_TEMPLATE_SUMMER", "¡Hola {name}! Los azules suaves y lavanda se ven hermosos a los {age} años. 💙");
        es.put("STYLE_ADVICE_TEMPLATE_DEFAULT", "Tu estilo a los {age} es único, {name}. ¡Confía en tu instinto! 💜");
        
        translations.put("es", es);

        // French (fr)
        Map<String, String> fr = new HashMap<>();
        fr.put("BODY_SHAPE_EXPLANATION_HOURGLASS", "Épaules et hanches équilibrées avec une taille définie.");
        fr.put("BODY_SHAPE_EXPLANATION_RECTANGLE", "Épaules, buste et hanches sont assez uniformes avec peu de définition de la taille.");
        fr.put("BODY_SHAPE_EXPLANATION_TRIANGLE", "Les hanches sont plus larges que les épaules et le buste (forme de poire).");
        fr.put("BODY_SHAPE_EXPLANATION_INVERTED_TRIANGLE", "Les épaules ou le buste sont plus larges que les hanches.");
        fr.put("BODY_SHAPE_EXPLANATION_OVAL", "Le buste et la taille sont plus larges que les hanches (forme de pomme).");
        
        fr.put("BODY_SHAPE_DISPLAY_NAME_HOURGLASS", "Sablier");
        fr.put("BODY_SHAPE_DISPLAY_NAME_RECTANGLE", "Rectangle");
        fr.put("BODY_SHAPE_DISPLAY_NAME_TRIANGLE", "Triangle");
        fr.put("BODY_SHAPE_DISPLAY_NAME_INVERTED_TRIANGLE", "Triangle Inversé");
        fr.put("BODY_SHAPE_DISPLAY_NAME_OVAL", "Ovale");

        fr.put("COLOR_RESULT_PREFIX", "Résultat de l'analyse des couleurs");
        fr.put("COLOR_RESULT_METADATA", "Profil de style personnalisé basé sur la théorie des couleurs saisonnières.");
        fr.put("COLOR_ANALYSIS_COMPLETED", "Votre analyse de couleurs personnalisée est prête !");
        
        fr.put("REC_HOURGLASS_1", "Mettez en valeur votre taille avec des ceintures et des robes portefeuille.");
        fr.put("REC_HOURGLASS_2", "Les cols en V et les cols ronds fonctionnent très bien.");
        fr.put("REC_RECTANGLE_1", "Créez des courbes avec des hauts à basque et des détails à volants.");
        fr.put("REC_RECTANGLE_2", "Utilisez les superpositions et les textures pour donner du relief.");
        fr.put("REC_TRIANGLE_1", "Ajoutez du volume au haut de votre corps avec des cols bateau.");
        fr.put("REC_TRIANGLE_2", "Des couleurs plus sombres en bas aident à équilibrer la silhouette.");
        fr.put("REC_INVERTED_TRIANGLE_1", "Des jupes évasées et des pantalons larges ajoutent du volume en bas.");
        fr.put("REC_INVERTED_TRIANGLE_2", "Les cols en V aident à affiner visuellement le haut du corps.");
        fr.put("REC_OVAL_1", "Les robes taille empire et les tuniques sont très flatteuses.");
        fr.put("REC_OVAL_2", "Les tenues monochromes créent une silhouette plus allongée.");

        fr.put("STYLE_ADVICE_TEMPLATE_WINTER", "Bonjour {name} ! À {age} ans, en tant que type Hiver, les tons bijoux sont votre atout majeur. ✨");
        fr.put("STYLE_ADVICE_TEMPLATE_AUTUMN", "Bienvenue {name} ! Votre éclat à {age} ans est complété par des tons terreux comme le terracotta. 🍂");
        fr.put("STYLE_ADVICE_TEMPLATE_SPRING", "Salut {name} ! À {age} ans, le corail clair et la pêche vous rendent vibrante. 🌸");
        fr.put("STYLE_ADVICE_TEMPLATE_SUMMER", "Salut {name} ! Les bleus doux et la lavande sont magnifiques à {age} ans. 💙");
        fr.put("STYLE_ADVICE_TEMPLATE_DEFAULT", "Votre style à {age} ans est unique, {name}. Faites-vous confiance ! 💜");
        
        translations.put("fr", fr);

        // Korean (ko)
        Map<String, String> ko = new HashMap<>();
        ko.put("BODY_SHAPE_EXPLANATION_HOURGLASS", "정의된 허리 라인과 균형 잡힌 어깨와 엉덩이.");
        ko.put("BODY_SHAPE_EXPLANATION_RECTANGLE", "어깨, 가슴, 엉덩이가 거의 일정하며 허리 라인이 뚜렷하지 않음.");
        ko.put("BODY_SHAPE_EXPLANATION_TRIANGLE", "엉덩이가 어깨와 가슴보다 넓음 (배 모양).");
        ko.put("BODY_SHAPE_EXPLANATION_INVERTED_TRIANGLE", "어깨나 가슴이 엉덩이보다 넓음.");
        ko.put("BODY_SHAPE_EXPLANATION_OVAL", "가슴과 허리가 엉덩이보다 넓음 (사과 모양).");
        
        ko.put("BODY_SHAPE_DISPLAY_NAME_HOURGLASS", "모래시계형");
        ko.put("BODY_SHAPE_DISPLAY_NAME_RECTANGLE", "직사각형형");
        ko.put("BODY_SHAPE_DISPLAY_NAME_TRIANGLE", "삼각형형");
        ko.put("BODY_SHAPE_DISPLAY_NAME_INVERTED_TRIANGLE", "역삼각형형");
        ko.put("BODY_SHAPE_DISPLAY_NAME_OVAL", "타원형");

        ko.put("COLOR_RESULT_PREFIX", "퍼스널 컬러 진단 결과");
        ko.put("COLOR_RESULT_METADATA", "계절별 컬러 이론을 바탕으로 한 맞춤형 스타일 프로필.");
        ko.put("COLOR_ANALYSIS_COMPLETED", "당신의 맞춤형 컬러 분석이 완료되었습니다!");
        
        ko.put("REC_HOURGLASS_1", "벨트와 랩 드레스로 허리 라인을 강조하세요.");
        ko.put("REC_HOURGLASS_2", "V넥과 스쿱넥이 아주 잘 어울립니다.");
        ko.put("REC_RECTANGLE_1", "페플럼 상의와 러플 디테일로 곡선을 만드세요.");
        ko.put("REC_RECTANGLE_2", "레이어드와 텍스처를 활용해 입체감을 더하세요.");
        ko.put("REC_TRIANGLE_1", "보트넥으로 상체에 볼륨을 더하세요.");
        ko.put("REC_TRIANGLE_2", "하의에 어두운 색을 입으면 실루엣의 균형이 맞습니다.");
        ko.put("REC_INVERTED_TRIANGLE_1", "플레어 스커트와 와이드 팬츠로 하체에 볼륨을 더하세요.");
        ko.put("REC_INVERTED_TRIANGLE_2", "V넥은 상체를 시각적으로 좁아 보이게 합니다.");
        ko.put("REC_OVAL_1", "엠파이어 웨이스트 드레스와 튜닉이 매우 잘 어울립니다.");
        ko.put("REC_OVAL_2", "모노크롬 룩은 더 길고 날씬해 보이는 효과를 줍니다.");

        ko.put("STYLE_ADVICE_TEMPLATE_WINTER", "안녕하세요 {name}님! {age}세의 겨울 타입으로서 보석처럼 빛나는 톤들이 완벽합니다. ✨");
        ko.put("STYLE_ADVICE_TEMPLATE_AUTUMN", "환영합니다 {name}님! {age}세의 생기는 테라코타와 같은 대지의 색조로 완성됩니다. 🍂");
        ko.put("STYLE_ADVICE_TEMPLATE_SPRING", "안녕하세요 {name}님! {age}세에는 밝은 코랄과 피치 색상이 당신을 더욱 화사하게 만듭니다. 🌸");
        ko.put("STYLE_ADVICE_TEMPLATE_SUMMER", "안녕하세요 {name}님! 부드러운 블루와 라벤더 색조가 {age}세의 안색에 아주 잘 어울립니다. 💙");
        ko.put("STYLE_ADVICE_TEMPLATE_DEFAULT", "{age}세의 당신의 스타일은 유니크하고 아름답습니다, {name}님. 본인의 직감을 믿으세요! 💜");
        
        translations.put("ko", ko);
    }

    public String translate(String key, String lang) {
        if (lang == null || !translations.containsKey(lang)) {
            lang = "en";
        }
        return translations.get(lang).getOrDefault(key, translations.get("en").get(key));
    }

    public String translate(String key, String lang, Map<String, String> placeholders) {
        String template = translate(key, lang);
        if (placeholders == null || template == null) return template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return template;
    }
}

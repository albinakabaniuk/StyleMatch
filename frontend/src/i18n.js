import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

import en from './locales/en/translation.json';
import uk from './locales/uk/translation.json';
import de from './locales/de/translation.json';
import fr from './locales/fr/translation.json';
import es from './locales/es/translation.json';
import ko from './locales/ko/translation.json';

i18n
    .use(LanguageDetector)
    .use(initReactI18next)
    .init({
        resources: {
            en: { translation: en },
            uk: { translation: uk },
            de: { translation: de },
            fr: { translation: fr },
            es: { translation: es },
            ko: { translation: ko },
        },
        fallbackLng: 'en',
        detection: {
            order: ['localStorage', 'navigator'],
            caches: ['localStorage'],
        },
        interpolation: { escapeValue: false },
    });

export default i18n;

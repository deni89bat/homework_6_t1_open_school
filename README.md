# Домашняя работа 6 к лекции 7 Actions и специфические проверки

## Описание задания
Все практические задания выполняются на открытом стенде [the-internet.herokuapp.com](https://the-internet.herokuapp.com/).

### Задания:
1. **Drag and Drop**
    - Перейти на страницу **Drag and Drop**.
    - Перетащить элемент A на элемент B.
    - **Задача на 10 баллов** – сделать это, не прибегая к методу `DragAndDrop()`.
    - Проверить, что элементы поменялись местами.

2. **Context Menu**
    - Перейти на страницу **Context Menu**.
    - Нажать правой кнопкой мыши на отмеченной области и проверить, что JS Alert имеет ожидаемый текст.

3. **Infinite Scroll**
    - Перейти на страницу **Infinite Scroll**.
    - Проскролить страницу до текста «Eius», проверить, что текст в поле зрения.

4. **Key Presses**
    - Перейти на страницу **Key Presses**.
    - Нажать по 10 латинских символов, клавиши Enter, Ctrl, Alt, Tab.
    - Проверить, что после нажатия отображается всплывающий текст снизу, соответствующий конкретной клавише.

## Стек технологий
- **Java**
- **Selenide** 
- **JUnit** 
- **Allure** 

## Установка
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/ваш-логин/ваш-репозиторий.git


## Структура проекта 

ваш_репозиторий/
│
├── build.gradle
├── settings.gradle
├── src/
│   └── test/
│       └── java/
│           └── tests/
│               └── HomeWorkActionsTests.java
│
└── target/
└── screenshots/

## Запуск тестов
Для запуска тестов и генерации отчетов выполните следующие команды:
1. Запустите тесты из модуля `ui-tests`:
   ./gradlew :ui-tests:test --tests "tests.HomeWorkActionsTests"
2. Сгенерируйте отчет Allure:
   ./gradlew allureReport
3. Просмотрите отчет Allure в веб-браузере:
   ./gradlew allureServe
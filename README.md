# Документация

* [План автоматизации](https://github.com/Algusb/TravelShop/blob/master/Documents/Plan.md)

* [Отчет по итогам тестированию]()

* [Отчет по итогам автоматизации](https://github.com/Algusb/TravelShop/blob/master/Documents/Summary.md)

## Запуск приложения

### Запустить контейнеры с симулятором банковских сервисов из корневой директории проекта командой:

```docker-compose up```

### Запустить приложение командой

* для MySQL:

```java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar.```

* для Postgres:

```java -Dspring.datasource.url=jdbc:postgresql://localhost::5432/app -jar artifacts/aqa-shop.jar```

### Запустить тесты командой

```gradlew clean test```

## Отчёт Allure

* Для генерации отчёта по тестам нужно выполнить команду

```gradlew allureReport allureServe```

### Окончание тестов и остановка контейнеров

* Прервать выполнение SUT сочетанием клавиш Ctrl+C
* Остановить контейнеры командой
 ```docker-compose down```

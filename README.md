
# Инструкцию по запуску проекта
### Предусловия

На ПК необходимо установить:
`IntelliJ IDEA`, `Google Chrome`, `Git`, `Docker Desktop`.

### Установка и запуск

1. Запустить `Docker Desktop`.
2. Склонировать код репозитория проекта и открыть в `IntelliJ IDEA`.
3. Выполнить команду в терминале для запуска контейнеров из файла `docker-compose.yml`:
   
```
docker-compose up

```

4. Проверить статус контейнеров командой:

```
docker-compose ps

```
Убеждаемся, что статус контейнеров `UP`.

5. После развертывания контейнера для запуска `SUT` в зависимости от выбранной для работы `СУБД` выполнить команду в консоли:

- Для PostgreSQL:

```
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar ./artifacts/aqa-shop.jar

```

- Для MySQL:

```
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./artifacts/aqa-shop.jar

```

Сервис будет доступен по адресу: _http://localhost:8080/_

6. Запустить тесты командой в терминале:

- Для PostgreSQL:

```
./gradlew clean test "-Ddatasource.url=jdbc:postgresql://localhost:5432/app"

```

- Для MySQL:

```
./gradlew clean test "-Ddatasource.url=jdbc:mysql://localhost:3306/app"

```
___

### Дополнительные сведения:

Для просмотра отчета `Allure Report` после выполнения тестов ввести в терминале:
```
./gradlew allureServe

```
___
#### После окончания работы:
1. Завершить работу `SUT` сочетанием клавиш `CTRL + C` с подтверждением действия в терминале вводом `Y`.
2. Завершить работу контейнеров командой в консоли:
```
docker-compose down

```
___

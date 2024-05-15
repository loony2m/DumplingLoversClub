# Dumpling Lovers Club

## О приложении

Это приложение использует Maven 4.0.0 в качестве инструмента сборки и Java 17 версии. Проект использует следующие зависимости:

- Spring Web
- Spring Security
- Spring Data JPA
- MySQL Database
- Freemarker

## Запуск приложения

Чтобы запустить это приложение из своей IDE выполните следующую команду:

```bash
mvn spring-boot:run
```

### Railway

Сервис для запуска приложения: [Railway](https://railway.app/)

Создайте новый пустой проект в Railway и начните с создания базы данных MySQL. Затем вы можете создать новый проект с GitHub. Вы можете использовать следующие переменные среды на основе только что созданной базы данных.

```properties
spring_profiles_active=prod
PROD_DB_HOST=roundhouse.proxy.rlwy.net
PROD_DB_PORT=34042
PROD_DB_NAME=railway
PROD_DB_PASSWORD=qXnUvOPCyYBWwZKRSRHGPPvnGMKyWkmu
PROD_DB_USERNAME=root
```

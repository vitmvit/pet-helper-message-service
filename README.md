# pet-helper-message-service

[Точка входа в приложение](https://github.com/vitmvit/pet-helper-api-gateway-service)

Этот микросервис предназначен для управления системой чатов и мгновенных сообщений между пользователями и службой
поддержки в режиме реального времени. Реализованный на реактивном стеке Spring WebFlux, он обеспечивает высокую
производительность, неблокирующую обработку запросов и эффективное взаимодействие с базой данных через R2DBC.

## Технический стек

- Java 17+
- Spring Boot 3.2.1
- Spring WebFlux (реактивный REST API)
- Spring Security
- Spring Cloud Netflix Eureka Client
- Project Reactor (Mono/Flux)
- Lombok
- SpringDoc OpenAPI
- MapStruct

## Доступ

Роли:

- SUPPORT
- USER

## Swagger

http://localhost:8084/api/doc/swagger-ui/index.html#/

## Порт

```text
8084
```

#### Аутентификация и авторизация

С каждым запросом необходимо передавать Bearer токен.

## Валидация DTO моделей на ChatController и MessageController

### Таблица валидации MessageCreateDto

| Поле       | Тип    | Обязательно | Правила валидации                 | Сообщение об ошибке                                             |
|------------|--------|-------------|-----------------------------------|-----------------------------------------------------------------|
| chatId     | Long   | Да          | Не может быть null                | "ID чата обязателен"                                            |
| senderName | String | Да          | Не пустое (без пробелов по краям) | "Имя отправителя не может быть пустым"                          |
| content    | String | Да          | Не пустое, максимум 2000 символов | "Сообщение не может быть пустым"<br>"Сообщение слишком длинное" |
| uuidPhoto  | String | Нет         | Опционально                       | -                                                               |

### Таблица валидации ChatCreateDto

| Поле        | Тип      | Обязательно | Правила валидации                 | Сообщение об ошибке            |
|-------------|----------|-------------|-----------------------------------|--------------------------------|
| supportName | String   | Нет         | Опционально                       | -                              |
| userName    | String   | Да          | Не пустое (без пробелов по краям) | "Имя пользователя обязательно" |
| type        | ChatType | Да          | Не может быть null                | "Тип чата обязателен"          |
| isConstant  | boolean  | Нет         | Опционально, по умолчанию false   | -                              |

## Доступные роли

ПУБЛИЧНЫЕ РОУТЫ (доступ без аутентификации):
--------------------------------------------------------
• /swagger-ui/**        - Swagger UI интерфейс
• /api/doc/**           - Документация API
• /v3/api-docs/**       - OpenAPI спецификация

АВТОРИЗОВАННЫЕ РОУТЫ (требуется аутентификация):
--------------------------------------------------------
Для работы с чатами (ROLE_USER или ROLE_SUPPORT):
• GET /api/v1/chats/**    - Просмотр чатов
• POST /api/v1/chats/**    - Создание чатов
• PUT /api/v1/chats/**    - Обновление чатов  
• DELETE /api/v1/chats/**    - Удаление чатов

ВСЕ ОСТАЛЬНЫЕ РОУТЫ:
--------------------------------------------------------
• Любой метод, любой путь - Требуется любая аутентификация

## Интеграция с другими сервисами

### Зависимости

- **User Service** (порт 8081) - для создания пользователей и обновления даты последнего визита
- **Auth Service** (порт 8084) - для проверки токена

### Конфигурация WebClient

Сервис использует реактивный WebClient для взаимодействия с User Service:

```yaml
user-service:
  url: http://localhost:8081
```

Сервис использует реактивный WebClient для взаимодействия с Auth Service:

```yaml
auth-service:
  url: http://localhost:8084
```

## ChatController (8084/api/v1/chats)

Контроллер поддерживает следующие операции:

- поиск чата по id
- поиск чатов по логину тех.поддержки
- поиск чатов без указанной тех.поддержки
- поиск чатов по статусу
- поиск чатов по логину пользователя
- поиск чатов по фрагменту логина пользователя и поддержке
- поиск чатов по логину тех.поддержки и пользователя
- поиск чатов по типу и статусу
- поиск чатов по типу
- создание чата
- вывод всех чатов
- обновление статуса чата
- присвоение чату пользователя тех.поддержки
- удаление чата

### GET-запросы:

#### ChatDto findChatById(@PathVariable("id") Long id)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/3
```

Response:

```json
{
  "id": 3,
  "supportName": "support1@mail.com",
  "userName": "user3@mail.com",
  "messageList": [
    {
      "id": 5,
      "chatId": 3,
      "senderName": "user3@mail.com",
      "content": "content",
      "createDate": "2024-04-02T22:33:17.920154"
    },
    {
      "id": 6,
      "chatId": 3,
      "senderName": "user3@mail.com",
      "content": "content",
      "createDate": "2024-04-02T22:33:18.722023"
    },
    ...
  ],
  "status": "CLOSED",
  "createDate": "2024-04-01T12:38:46.047585",
  "updateDate": "2024-04-03T22:11:20.661897"
}
```

##### Чат не найден

Request:

```http request
http://localhost:8084/api/v1/chats/3
```

Response:

```json
{
  "errorMessage": "Resource not found!",
  "errorCode": 404
}
```

#### List<ChatDto> findChatsBySupportName(@PathVariable("name") String name)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/supportName/support1@mail.com
```

Response:

```json
[
  {
    "id": 12,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 39,
        "chatId": 12,
        "senderName": "user1@mail.com",
        "content": "ornare mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-05T22:02:00.800183"
      },
      {
        "id": 42,
        "chatId": 12,
        "senderName": "support1@mail.com",
        "content": "Duis et lectus dignissim",
        "uuidPhoto": null,
        "createDate": "2024-04-06T00:56:07.246225"
      },
      {
        "id": 43,
        "chatId": 12,
        "senderName": "support1@mail.com",
        "content": "efficitur velit ac, accumsan elit",
        "uuidPhoto": null,
        "createDate": "2024-04-06T00:56:11.033316"
      },
      {
        "id": 60,
        "chatId": 12,
        "senderName": "user1@mail.com",
        "content": "Quisque mi nisl, blandit nec mauris eget, scelerisque tempus nunc.",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:10:15.911013"
      }
    ],
    "status": "OPEN",
    "type": "SUPPORT",
    "createDate": "2024-05-25T16:00:46.692917",
    "updateDate": "2024-05-25T16:01:34.054444"
  },
  {
    "id": 16,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 65,
        "chatId": 16,
        "senderName": "user1@mail.com",
        "content": "Nam sollicitudin in elit vel lacinia",
        "uuidPhoto": null,
        "createDate": "2024-04-10T23:59:59.446409"
      }
    ],
    "status": "OPEN",
    "type": "SUPPORT",
    "createDate": "2024-05-29T09:06:09.015739",
    "updateDate": "2024-06-02T18:07:38.124932"
  },
  {
    "id": 20,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 74,
        "chatId": 20,
        "senderName": "user1@mail.com",
        "content": "Donec id pellentesque purus.",
        "uuidPhoto": null,
        "createDate": "2024-04-12T15:54:56.204855"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-06-08T20:10:02.533238",
    "updateDate": "2024-06-08T20:10:32.867069"
  },
  {
    "id": 3,
    "supportName": "support1@mail.com",
    "userName": "user3@mail.com",
    "messageList": [
      {
        "id": 50,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "In auctor at mi nec imperdiet",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:30:03.566122"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac dictum enim",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:27.078565"
      },
      {
        "id": 51,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque gravida nec arcu maximus tempus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:10:56.868287"
      },
      {
        "id": 52,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Quisque semper sapien mauris, ut pharetra ligula euismod a",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:11:54.166143"
      },
      {
        "id": 53,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vivamus auctor fermentum vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:27:00.961773"
      },
      {
        "id": 54,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin purus erat, porttitor et pretium eu, pretium nec est.",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:28:36.600161"
      },
      {
        "id": 44,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Ut tellus dolor, mollis id suscipit ut",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:04.421661"
      },
      {
        "id": 45,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "accumsan id dolor",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:09.573314"
      },
      {
        "id": 46,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque eget leo a arcu porta vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:08:36.103681"
      },
      {
        "id": 47,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin mi nisl, vestibulum id leo vel, viverra euismod mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:13.098805"
      },
      {
        "id": 48,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Duis ultrices dui a tellus porttitor cursus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:40.898474"
      },
      {
        "id": 49,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Donec convallis fermentum lorem quis rhoncus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:45.021734"
      },
      {
        "id": 5,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Lorem ipsum dolor sit amet,",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:17.920154"
      },
      {
        "id": 6,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "consectetur adipiscing elit",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:18.722023"
      },
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Suspendisse in vulputate ligula",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac molestie eros",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Phasellus ac eros ut nunc finibus laoreet",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "In aliquam rhoncus felis",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "eget accumsan mauris convallis ut",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vestibulum lobortis massa sed faucibus varius",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Maecenas quis cursus magna",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:21.688996"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-04-11T00:08:02.955369",
    "updateDate": "2024-05-04T18:42:07.33653"
  }
]
```

##### Пустой список

Request:

```http request
http://localhost:8084/api/v1/chats/supportName/support2@mail.com
```

Response:

```json
[]
```

#### List<ChatDto> findChatsByEmptySupportName()

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/free
```

Response:

```json
[
  {
    "id": 6,
    "supportName": "",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 37,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Donec vel enim ornare",
        "uuidPhoto": null,
        "createDate": "2024-04-04T20:30:33.27718"
      },
      {
        "id": 38,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "placerat urna ac",
        "uuidPhoto": null,
        "createDate": "2024-04-04T20:30:36.961346"
      },
      {
        "id": 57,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Fusce faucibus, nunc vitae fringilla aliquet, nulla ligula varius leo, vitae tempus lectus velit at diam",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:10.295722"
      },
      {
        "id": 58,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Phasellus arcu arcu, imperdiet ac congue non, faucibus quis neque",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:12.340459"
      },
      {
        "id": 59,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Aenean egestas rhoncus pellentesque",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:14.336505"
      },
      {
        "id": 61,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Proin venenatis tincidunt mollis",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:10:52.842496"
      },
      {
        "id": 62,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "In a mauris et orci molestie dapibus eu sit amet tellus",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:11:21.114371"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-05-06T11:56:56.775259",
    "updateDate": "2024-05-13T12:05:33.84568"
  }
]
```

#### List<ChatDto> findChatsByStatus(@PathVariable("status") ChatStatus status)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/status/OPEN
```

Response:

```json
[
  {
    "id": 12,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 39,
        "chatId": 12,
        "senderName": "user1@mail.com",
        "content": "ornare mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-05T22:02:00.800183"
      },
      {
        "id": 42,
        "chatId": 12,
        "senderName": "support1@mail.com",
        "content": "Duis et lectus dignissim",
        "uuidPhoto": null,
        "createDate": "2024-04-06T00:56:07.246225"
      },
      {
        "id": 43,
        "chatId": 12,
        "senderName": "support1@mail.com",
        "content": "efficitur velit ac, accumsan elit",
        "uuidPhoto": null,
        "createDate": "2024-04-06T00:56:11.033316"
      },
      {
        "id": 60,
        "chatId": 12,
        "senderName": "user1@mail.com",
        "content": "Quisque mi nisl, blandit nec mauris eget, scelerisque tempus nunc.",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:10:15.911013"
      }
    ],
    "status": "OPEN",
    "type": "SUPPORT",
    "createDate": "2024-05-25T16:00:46.692917",
    "updateDate": "2024-05-25T16:01:34.054444"
  },
  {
    "id": 16,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 65,
        "chatId": 16,
        "senderName": "user1@mail.com",
        "content": "Nam sollicitudin in elit vel lacinia",
        "uuidPhoto": null,
        "createDate": "2024-04-10T23:59:59.446409"
      }
    ],
    "status": "OPEN",
    "type": "SUPPORT",
    "createDate": "2024-05-29T09:06:09.015739",
    "updateDate": "2024-06-02T18:07:38.124932"
  }
]
```

#### List<ChatDto> findChatsByUserName(@PathVariable("name") String name)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/userName/user3@mail.com
```

Response:

```json
[
  {
    "id": 3,
    "supportName": "support1@mail.com",
    "userName": "user3@mail.com",
    "messageList": [
      {
        "id": 50,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "In auctor at mi nec imperdiet",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:30:03.566122"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac dictum enim",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:27.078565"
      },
      {
        "id": 51,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque gravida nec arcu maximus tempus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:10:56.868287"
      },
      {
        "id": 52,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Quisque semper sapien mauris, ut pharetra ligula euismod a",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:11:54.166143"
      },
      {
        "id": 53,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vivamus auctor fermentum vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:27:00.961773"
      },
      {
        "id": 54,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin purus erat, porttitor et pretium eu, pretium nec est.",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:28:36.600161"
      },
      {
        "id": 44,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Ut tellus dolor, mollis id suscipit ut",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:04.421661"
      },
      {
        "id": 45,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "accumsan id dolor",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:09.573314"
      },
      {
        "id": 46,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque eget leo a arcu porta vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:08:36.103681"
      },
      {
        "id": 47,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin mi nisl, vestibulum id leo vel, viverra euismod mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:13.098805"
      },
      {
        "id": 48,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Duis ultrices dui a tellus porttitor cursus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:40.898474"
      },
      {
        "id": 49,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Donec convallis fermentum lorem quis rhoncus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:45.021734"
      },
      {
        "id": 5,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Lorem ipsum dolor sit amet,",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:17.920154"
      },
      {
        "id": 6,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "consectetur adipiscing elit",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:18.722023"
      },
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Suspendisse in vulputate ligula",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac molestie eros",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Phasellus ac eros ut nunc finibus laoreet",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "In aliquam rhoncus felis",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "eget accumsan mauris convallis ut",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vestibulum lobortis massa sed faucibus varius",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Maecenas quis cursus magna",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:21.688996"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-04-11T00:08:02.955369",
    "updateDate": "2024-05-04T18:42:07.33653"
  }
]
```

#### List<ChatDto> findChatsByUserNameContains(@PathVariable("name") String name, @PathVariable("supportName") String supportName)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/userName/like/user3@mail.com/support1@mail.com
```

Response:

```json
[
  {
    "id": 3,
    "supportName": "support1@mail.com",
    "userName": "user3@mail.com",
    "messageList": [
      {
        "id": 50,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "In auctor at mi nec imperdiet",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:30:03.566122"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac dictum enim",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:27.078565"
      },
      {
        "id": 51,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque gravida nec arcu maximus tempus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:10:56.868287"
      },
      {
        "id": 52,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Quisque semper sapien mauris, ut pharetra ligula euismod a",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:11:54.166143"
      },
      {
        "id": 53,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vivamus auctor fermentum vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:27:00.961773"
      },
      {
        "id": 54,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin purus erat, porttitor et pretium eu, pretium nec est.",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:28:36.600161"
      },
      {
        "id": 44,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Ut tellus dolor, mollis id suscipit ut",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:04.421661"
      },
      {
        "id": 45,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "accumsan id dolor",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:09.573314"
      },
      {
        "id": 46,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque eget leo a arcu porta vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:08:36.103681"
      },
      {
        "id": 47,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin mi nisl, vestibulum id leo vel, viverra euismod mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:13.098805"
      },
      {
        "id": 48,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Duis ultrices dui a tellus porttitor cursus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:40.898474"
      },
      {
        "id": 49,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Donec convallis fermentum lorem quis rhoncus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:45.021734"
      },
      {
        "id": 5,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Lorem ipsum dolor sit amet,",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:17.920154"
      },
      {
        "id": 6,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "consectetur adipiscing elit",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:18.722023"
      },
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Suspendisse in vulputate ligula",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac molestie eros",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Phasellus ac eros ut nunc finibus laoreet",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "In aliquam rhoncus felis",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "eget accumsan mauris convallis ut",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vestibulum lobortis massa sed faucibus varius",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Maecenas quis cursus magna",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:21.688996"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-04-11T00:08:02.955369",
    "updateDate": "2024-05-04T18:42:07.33653"
  }
]
```

#### List<ChatDto> findChatsBySupportNameAndUserName(@PathVariable("supportName") String supportName, @PathVariable("userName") String userName)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/support1@mail.com/user3@mail.com
```

Response:

```json
[
  {
    "id": 3,
    "supportName": "support1@mail.com",
    "userName": "user3@mail.com",
    "messageList": [
      {
        "id": 50,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "In auctor at mi nec imperdiet",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:30:03.566122"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac dictum enim",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:27.078565"
      },
      {
        "id": 51,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque gravida nec arcu maximus tempus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:10:56.868287"
      },
      {
        "id": 52,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Quisque semper sapien mauris, ut pharetra ligula euismod a",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:11:54.166143"
      },
      {
        "id": 53,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vivamus auctor fermentum vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:27:00.961773"
      },
      {
        "id": 54,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin purus erat, porttitor et pretium eu, pretium nec est.",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:28:36.600161"
      },
      {
        "id": 44,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Ut tellus dolor, mollis id suscipit ut",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:04.421661"
      },
      {
        "id": 45,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "accumsan id dolor",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:09.573314"
      },
      {
        "id": 46,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque eget leo a arcu porta vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:08:36.103681"
      },
      {
        "id": 47,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin mi nisl, vestibulum id leo vel, viverra euismod mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:13.098805"
      },
      {
        "id": 48,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Duis ultrices dui a tellus porttitor cursus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:40.898474"
      },
      {
        "id": 49,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Donec convallis fermentum lorem quis rhoncus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:45.021734"
      },
      {
        "id": 5,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Lorem ipsum dolor sit amet,",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:17.920154"
      },
      {
        "id": 6,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "consectetur adipiscing elit",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:18.722023"
      },
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Suspendisse in vulputate ligula",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac molestie eros",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Phasellus ac eros ut nunc finibus laoreet",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "In aliquam rhoncus felis",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "eget accumsan mauris convallis ut",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vestibulum lobortis massa sed faucibus varius",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Maecenas quis cursus magna",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:21.688996"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-04-11T00:08:02.955369",
    "updateDate": "2024-05-04T18:42:07.33653"
  }
]
```

#### List<ChatDto> findAllChats()

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats
```

Response:

```json
[
  {
    "id": 12,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 39,
        "chatId": 12,
        "senderName": "user1@mail.com",
        "content": "ornare mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-05T22:02:00.800183"
      },
      {
        "id": 42,
        "chatId": 12,
        "senderName": "support1@mail.com",
        "content": "Duis et lectus dignissim",
        "uuidPhoto": null,
        "createDate": "2024-04-06T00:56:07.246225"
      },
      {
        "id": 43,
        "chatId": 12,
        "senderName": "support1@mail.com",
        "content": "efficitur velit ac, accumsan elit",
        "uuidPhoto": null,
        "createDate": "2024-04-06T00:56:11.033316"
      },
      {
        "id": 60,
        "chatId": 12,
        "senderName": "user1@mail.com",
        "content": "Quisque mi nisl, blandit nec mauris eget, scelerisque tempus nunc.",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:10:15.911013"
      }
    ],
    "status": "OPEN",
    "type": "SUPPORT",
    "createDate": "2024-05-25T16:00:46.692917",
    "updateDate": "2024-05-25T16:01:34.054444"
  },
  {
    "id": 16,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 65,
        "chatId": 16,
        "senderName": "user1@mail.com",
        "content": "Nam sollicitudin in elit vel lacinia",
        "uuidPhoto": null,
        "createDate": "2024-04-10T23:59:59.446409"
      }
    ],
    "status": "OPEN",
    "type": "SUPPORT",
    "createDate": "2024-05-29T09:06:09.015739",
    "updateDate": "2024-06-02T18:07:38.124932"
  },
  {
    "id": 20,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 74,
        "chatId": 20,
        "senderName": "user1@mail.com",
        "content": "Donec id pellentesque purus.",
        "uuidPhoto": null,
        "createDate": "2024-04-12T15:54:56.204855"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-06-08T20:10:02.533238",
    "updateDate": "2024-06-08T20:10:32.867069"
  },
  {
    "id": 6,
    "supportName": "",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 37,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Donec vel enim ornare",
        "uuidPhoto": null,
        "createDate": "2024-04-04T20:30:33.27718"
      },
      {
        "id": 38,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "placerat urna ac",
        "uuidPhoto": null,
        "createDate": "2024-04-04T20:30:36.961346"
      },
      {
        "id": 57,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Fusce faucibus, nunc vitae fringilla aliquet, nulla ligula varius leo, vitae tempus lectus velit at diam",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:10.295722"
      },
      {
        "id": 58,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Phasellus arcu arcu, imperdiet ac congue non, faucibus quis neque",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:12.340459"
      },
      {
        "id": 59,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Aenean egestas rhoncus pellentesque",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:14.336505"
      },
      {
        "id": 61,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Proin venenatis tincidunt mollis",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:10:52.842496"
      },
      {
        "id": 62,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "In a mauris et orci molestie dapibus eu sit amet tellus",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:11:21.114371"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-05-06T11:56:56.775259",
    "updateDate": "2024-05-13T12:05:33.84568"
  },
  {
    "id": 3,
    "supportName": "support1@mail.com",
    "userName": "user3@mail.com",
    "messageList": [
      {
        "id": 50,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "In auctor at mi nec imperdiet",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:30:03.566122"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac dictum enim",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:27.078565"
      },
      {
        "id": 51,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque gravida nec arcu maximus tempus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:10:56.868287"
      },
      {
        "id": 52,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Quisque semper sapien mauris, ut pharetra ligula euismod a",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:11:54.166143"
      },
      {
        "id": 53,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vivamus auctor fermentum vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:27:00.961773"
      },
      {
        "id": 54,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin purus erat, porttitor et pretium eu, pretium nec est.",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:28:36.600161"
      },
      {
        "id": 44,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Ut tellus dolor, mollis id suscipit ut",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:04.421661"
      },
      {
        "id": 45,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "accumsan id dolor",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:09.573314"
      },
      {
        "id": 46,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque eget leo a arcu porta vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:08:36.103681"
      },
      {
        "id": 47,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin mi nisl, vestibulum id leo vel, viverra euismod mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:13.098805"
      },
      {
        "id": 48,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Duis ultrices dui a tellus porttitor cursus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:40.898474"
      },
      {
        "id": 49,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Donec convallis fermentum lorem quis rhoncus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:45.021734"
      },
      {
        "id": 5,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Lorem ipsum dolor sit amet,",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:17.920154"
      },
      {
        "id": 6,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "consectetur adipiscing elit",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:18.722023"
      },
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Suspendisse in vulputate ligula",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac molestie eros",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Phasellus ac eros ut nunc finibus laoreet",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "In aliquam rhoncus felis",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "eget accumsan mauris convallis ut",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vestibulum lobortis massa sed faucibus varius",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Maecenas quis cursus magna",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:21.688996"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-04-11T00:08:02.955369",
    "updateDate": "2024-05-04T18:42:07.33653"
  }
]
```

#### Flux<ChatDto> findChatsByType(ChatType type)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/type/SUPPORT
```

Response:

```json
[
  {
    "id": 12,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 39,
        "chatId": 12,
        "senderName": "user1@mail.com",
        "content": "ornare mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-05T22:02:00.800183"
      },
      {
        "id": 42,
        "chatId": 12,
        "senderName": "support1@mail.com",
        "content": "Duis et lectus dignissim",
        "uuidPhoto": null,
        "createDate": "2024-04-06T00:56:07.246225"
      },
      {
        "id": 43,
        "chatId": 12,
        "senderName": "support1@mail.com",
        "content": "efficitur velit ac, accumsan elit",
        "uuidPhoto": null,
        "createDate": "2024-04-06T00:56:11.033316"
      },
      {
        "id": 60,
        "chatId": 12,
        "senderName": "user1@mail.com",
        "content": "Quisque mi nisl, blandit nec mauris eget, scelerisque tempus nunc.",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:10:15.911013"
      }
    ],
    "status": "OPEN",
    "type": "SUPPORT",
    "createDate": "2024-05-25T16:00:46.692917",
    "updateDate": "2024-05-25T16:01:34.054444"
  },
  {
    "id": 16,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 65,
        "chatId": 16,
        "senderName": "user1@mail.com",
        "content": "Nam sollicitudin in elit vel lacinia",
        "uuidPhoto": null,
        "createDate": "2024-04-10T23:59:59.446409"
      }
    ],
    "status": "OPEN",
    "type": "SUPPORT",
    "createDate": "2024-05-29T09:06:09.015739",
    "updateDate": "2024-06-02T18:07:38.124932"
  },
  {
    "id": 20,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 74,
        "chatId": 20,
        "senderName": "user1@mail.com",
        "content": "Donec id pellentesque purus.",
        "uuidPhoto": null,
        "createDate": "2024-04-12T15:54:56.204855"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-06-08T20:10:02.533238",
    "updateDate": "2024-06-08T20:10:32.867069"
  },
  {
    "id": 6,
    "supportName": "",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 37,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Donec vel enim ornare",
        "uuidPhoto": null,
        "createDate": "2024-04-04T20:30:33.27718"
      },
      {
        "id": 38,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "placerat urna ac",
        "uuidPhoto": null,
        "createDate": "2024-04-04T20:30:36.961346"
      },
      {
        "id": 57,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Fusce faucibus, nunc vitae fringilla aliquet, nulla ligula varius leo, vitae tempus lectus velit at diam",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:10.295722"
      },
      {
        "id": 58,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Phasellus arcu arcu, imperdiet ac congue non, faucibus quis neque",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:12.340459"
      },
      {
        "id": 59,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Aenean egestas rhoncus pellentesque",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:14.336505"
      },
      {
        "id": 61,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Proin venenatis tincidunt mollis",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:10:52.842496"
      },
      {
        "id": 62,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "In a mauris et orci molestie dapibus eu sit amet tellus",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:11:21.114371"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-05-06T11:56:56.775259",
    "updateDate": "2024-05-13T12:05:33.84568"
  },
  {
    "id": 3,
    "supportName": "support1@mail.com",
    "userName": "user3@mail.com",
    "messageList": [
      {
        "id": 50,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "In auctor at mi nec imperdiet",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:30:03.566122"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac dictum enim",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:27.078565"
      },
      {
        "id": 51,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque gravida nec arcu maximus tempus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:10:56.868287"
      },
      {
        "id": 52,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Quisque semper sapien mauris, ut pharetra ligula euismod a",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:11:54.166143"
      },
      {
        "id": 53,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vivamus auctor fermentum vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:27:00.961773"
      },
      {
        "id": 54,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin purus erat, porttitor et pretium eu, pretium nec est.",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:28:36.600161"
      },
      {
        "id": 44,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Ut tellus dolor, mollis id suscipit ut",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:04.421661"
      },
      {
        "id": 45,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "accumsan id dolor",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:09.573314"
      },
      {
        "id": 46,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque eget leo a arcu porta vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:08:36.103681"
      },
      {
        "id": 47,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin mi nisl, vestibulum id leo vel, viverra euismod mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:13.098805"
      },
      {
        "id": 48,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Duis ultrices dui a tellus porttitor cursus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:40.898474"
      },
      {
        "id": 49,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Donec convallis fermentum lorem quis rhoncus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:45.021734"
      },
      {
        "id": 5,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Lorem ipsum dolor sit amet,",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:17.920154"
      },
      {
        "id": 6,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "consectetur adipiscing elit",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:18.722023"
      },
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Suspendisse in vulputate ligula",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac molestie eros",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Phasellus ac eros ut nunc finibus laoreet",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "In aliquam rhoncus felis",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "eget accumsan mauris convallis ut",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vestibulum lobortis massa sed faucibus varius",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Maecenas quis cursus magna",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:21.688996"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-04-11T00:08:02.955369",
    "updateDate": "2024-05-04T18:42:07.33653"
  }
]
```

#### Flux<ChatDto> findChatsByTypeAndStatus(ChatType type, ChatStatus status)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/chats/status/SUPPORT/FREE
```

Response:

```json
[
  {
    "id": 20,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 74,
        "chatId": 20,
        "senderName": "user1@mail.com",
        "content": "Donec id pellentesque purus.",
        "uuidPhoto": null,
        "createDate": "2024-04-12T15:54:56.204855"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-06-08T20:10:02.533238",
    "updateDate": "2024-06-08T20:10:32.867069"
  },
  {
    "id": 6,
    "supportName": "",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 37,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Donec vel enim ornare",
        "uuidPhoto": null,
        "createDate": "2024-04-04T20:30:33.27718"
      },
      {
        "id": 38,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "placerat urna ac",
        "uuidPhoto": null,
        "createDate": "2024-04-04T20:30:36.961346"
      },
      {
        "id": 57,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Fusce faucibus, nunc vitae fringilla aliquet, nulla ligula varius leo, vitae tempus lectus velit at diam",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:10.295722"
      },
      {
        "id": 58,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Phasellus arcu arcu, imperdiet ac congue non, faucibus quis neque",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:12.340459"
      },
      {
        "id": 59,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Aenean egestas rhoncus pellentesque",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:07:14.336505"
      },
      {
        "id": 61,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "Proin venenatis tincidunt mollis",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:10:52.842496"
      },
      {
        "id": 62,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "In a mauris et orci molestie dapibus eu sit amet tellus",
        "uuidPhoto": null,
        "createDate": "2024-04-09T01:11:21.114371"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-05-06T11:56:56.775259",
    "updateDate": "2024-05-13T12:05:33.84568"
  },
  {
    "id": 3,
    "supportName": "support1@mail.com",
    "userName": "user3@mail.com",
    "messageList": [
      {
        "id": 50,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "In auctor at mi nec imperdiet",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:30:03.566122"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac dictum enim",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:27.078565"
      },
      {
        "id": 51,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque gravida nec arcu maximus tempus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:10:56.868287"
      },
      {
        "id": 52,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Quisque semper sapien mauris, ut pharetra ligula euismod a",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:11:54.166143"
      },
      {
        "id": 53,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vivamus auctor fermentum vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:27:00.961773"
      },
      {
        "id": 54,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin purus erat, porttitor et pretium eu, pretium nec est.",
        "uuidPhoto": null,
        "createDate": "2024-04-08T22:28:36.600161"
      },
      {
        "id": 44,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Ut tellus dolor, mollis id suscipit ut",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:04.421661"
      },
      {
        "id": 45,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "accumsan id dolor",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:07:09.573314"
      },
      {
        "id": 46,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Pellentesque eget leo a arcu porta vulputate",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:08:36.103681"
      },
      {
        "id": 47,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Proin mi nisl, vestibulum id leo vel, viverra euismod mauris",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:13.098805"
      },
      {
        "id": 48,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Duis ultrices dui a tellus porttitor cursus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:40.898474"
      },
      {
        "id": 49,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Donec convallis fermentum lorem quis rhoncus",
        "uuidPhoto": null,
        "createDate": "2024-04-08T21:16:45.021734"
      },
      {
        "id": 5,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Lorem ipsum dolor sit amet,",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:17.920154"
      },
      {
        "id": 6,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "consectetur adipiscing elit",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:18.722023"
      },
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "Curabitur pellentesque faucibus vestibulum",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Suspendisse in vulputate ligula",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "ac molestie eros",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Phasellus ac eros ut nunc finibus laoreet",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "In aliquam rhoncus felis",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "eget accumsan mauris convallis ut",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Vestibulum lobortis massa sed faucibus varius",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "Maecenas quis cursus magna",
        "uuidPhoto": null,
        "createDate": "2024-04-03T21:29:21.688996"
      }
    ],
    "status": "FREE",
    "type": "SUPPORT",
    "createDate": "2024-04-11T00:08:02.955369",
    "updateDate": "2024-05-04T18:42:07.33653"
  }
]
```

### POST-запросы:

#### ChatDto createChat(@RequestBody ChatCreateDto chatCreateDto)

##### Успешное сохранение

Request:

```http request
http://localhost:8084/api/v1/chats
```

Body:

```json
{
  "supportName": "support1@mail.com",
  "userName": "user1@mail.com"
}
```

Response:

```json
{
  "id": 38,
  "supportName": "support1@mail.com",
  "userName": "user1@mail.com",
  "messageList": null,
  "status": "FREE",
  "type": "SUPPORT",
  "createDate": "2025-12-22T12:03:38.059837",
  "updateDate": "2025-12-22T12:03:38.059837"
}
```

##### Отсутствует пользователь

Request:

```http request
http://localhost:8084/api/v1/chats
```

Body:

```json
{
  "supportName": "support111@mail.com",
  "userName": "user1@mail.com"
}
```

Response:

```json
{
  "errorMessage": "Support not found: support111@mail.com",
  "errorCode": 404
}
```

#### ChatDto updateStatusChat(@PathVariable("id") Long id, @PathVariable("status") ChatStatus status)

##### Успешное обновление

Request:

```http request
http://localhost:8084/api/v1/chats/status/3/OPEN
```

Response:

```json
{
  "id": 12,
  "supportName": "support1@mail.com",
  "userName": "user1@mail.com",
  "messageList": [],
  "status": "OPEN",
  "createDate": "2024-04-05T21:55:42.548966",
  "updateDate": "2024-04-05T21:58:59.223235"
}
```

##### Чат не найден

Request:

```http request
http://localhost:8084/api/v1/chats/status/33/OPEN
```

Response:

```json
{
  "errorMessage": "Resource not found!",
  "errorCode": 404
}
```

#### ChatDto updateSupportChat(@PathVariable("id") Long id, @PathVariable("login") String login)

##### Успешное обновление

Request:

```http request
http://localhost:8084/api/v1/chats/support/12/support1@mail.com
```

Response:

```json
{
  "id": 12,
  "supportName": "support1@mail.com",
  "userName": "user1@mail.com",
  "messageList": [
    {
      "id": 39,
      "chatId": 12,
      "senderName": "user1@mail.com",
      "content": "ornare mauris",
      "uuidPhoto": null,
      "createDate": "2024-04-05T22:02:00.800183"
    },
    {
      "id": 42,
      "chatId": 12,
      "senderName": "support1@mail.com",
      "content": "Duis et lectus dignissim",
      "uuidPhoto": null,
      "createDate": "2024-04-06T00:56:07.246225"
    },
    {
      "id": 43,
      "chatId": 12,
      "senderName": "support1@mail.com",
      "content": "efficitur velit ac, accumsan elit",
      "uuidPhoto": null,
      "createDate": "2024-04-06T00:56:11.033316"
    },
    {
      "id": 60,
      "chatId": 12,
      "senderName": "user1@mail.com",
      "content": "Quisque mi nisl, blandit nec mauris eget, scelerisque tempus nunc.",
      "uuidPhoto": null,
      "createDate": "2024-04-09T01:10:15.911013"
    }
  ],
  "status": "OPEN",
  "type": "SUPPORT",
  "createDate": "2024-05-25T16:00:46.692917",
  "updateDate": "2025-12-22T12:17:05.519035"
}
```

##### Не существует пользователь

Request:

```http request
http://localhost:8084/api/v1/chats/support/12/support1333@mail.com
```

Response:

```json
{
  "errorMessage": "Support user not found: support1333@mail.com",
  "errorCode": 404
}
```

##### Не существует чат

Request:

```http request
http://localhost:8084/api/v1/chats/support/122/support1@mail.com
```

Response:

```json
{
  "errorMessage": "Resource not found!",
  "errorCode": 404
}
```

### DELETE-запросы:

Не возвращают ничего:

- deleteChat(@PathVariable("id") Long id)

## MessageController (8084/api/v1/messages)

Контроллер поддерживает следующие операции:

- поиск сообщений по id чата
- создание сообщения
- удаление сообщения

### GET-запросы:

#### List<MessageDto> findAllMessageByChatId(@PathVariable("id") Long id)

##### Успешный поиск

Request:

```http request
http://localhost:8084/api/v1/messages/3
```

Response:

```json
[
  {
    "id": 50,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "In auctor at mi nec imperdiet",
    "uuidPhoto": null,
    "createDate": "2024-04-08T21:30:03.566122"
  },
  {
    "id": 16,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "ac dictum enim",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:29:27.078565"
  },
  {
    "id": 51,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Pellentesque gravida nec arcu maximus tempus",
    "uuidPhoto": null,
    "createDate": "2024-04-08T22:10:56.868287"
  },
  {
    "id": 52,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Quisque semper sapien mauris, ut pharetra ligula euismod a",
    "uuidPhoto": null,
    "createDate": "2024-04-08T22:11:54.166143"
  },
  {
    "id": 53,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "Vivamus auctor fermentum vulputate",
    "uuidPhoto": null,
    "createDate": "2024-04-08T22:27:00.961773"
  },
  {
    "id": 54,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Proin purus erat, porttitor et pretium eu, pretium nec est.",
    "uuidPhoto": null,
    "createDate": "2024-04-08T22:28:36.600161"
  },
  {
    "id": 44,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "Ut tellus dolor, mollis id suscipit ut",
    "uuidPhoto": null,
    "createDate": "2024-04-08T21:07:04.421661"
  },
  {
    "id": 45,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "accumsan id dolor",
    "uuidPhoto": null,
    "createDate": "2024-04-08T21:07:09.573314"
  },
  {
    "id": 46,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Pellentesque eget leo a arcu porta vulputate",
    "uuidPhoto": null,
    "createDate": "2024-04-08T21:08:36.103681"
  },
  {
    "id": 47,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Proin mi nisl, vestibulum id leo vel, viverra euismod mauris",
    "uuidPhoto": null,
    "createDate": "2024-04-08T21:16:13.098805"
  },
  {
    "id": 48,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Duis ultrices dui a tellus porttitor cursus",
    "uuidPhoto": null,
    "createDate": "2024-04-08T21:16:40.898474"
  },
  {
    "id": 49,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Donec convallis fermentum lorem quis rhoncus",
    "uuidPhoto": null,
    "createDate": "2024-04-08T21:16:45.021734"
  },
  {
    "id": 5,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Lorem ipsum dolor sit amet,",
    "uuidPhoto": null,
    "createDate": "2024-04-02T22:33:17.920154"
  },
  {
    "id": 6,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "consectetur adipiscing elit",
    "uuidPhoto": null,
    "createDate": "2024-04-02T22:33:18.722023"
  },
  {
    "id": 7,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Curabitur pellentesque faucibus vestibulum",
    "uuidPhoto": null,
    "createDate": "2024-04-02T22:33:19.430088"
  },
  {
    "id": 8,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "Curabitur pellentesque faucibus vestibulum",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:08:39.622999"
  },
  {
    "id": 9,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "Suspendisse in vulputate ligula",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:08:57.749837"
  },
  {
    "id": 10,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "ac molestie eros",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:19:44.174408"
  },
  {
    "id": 11,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "Phasellus ac eros ut nunc finibus laoreet",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:24:24.215284"
  },
  {
    "id": 12,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "In aliquam rhoncus felis",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:24:29.457451"
  },
  {
    "id": 13,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "eget accumsan mauris convallis ut",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:29:08.742179"
  },
  {
    "id": 14,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "Vestibulum lobortis massa sed faucibus varius",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:29:17.659692"
  },
  {
    "id": 15,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "Maecenas quis cursus magna",
    "uuidPhoto": null,
    "createDate": "2024-04-03T21:29:21.688996"
  }
]
```

### POST-запросы:

#### MessageDto createMessage(@RequestBody MessageCreateDto messageCreateDto)

##### Успешное создание

Request:

```http request
http://localhost:8084/api/v1/messages
```

Body:

```json
{
  "chatId": "12",
  "senderName": "user1@mail.com",
  "content": "content"
}
```

Response:

```json
{
  "id": 182,
  "chatId": 12,
  "senderName": "user1@mail.com",
  "content": "content",
  "uuidPhoto": null,
  "createDate": "2025-12-22T13:03:19.615605"
}
```

##### Не найден чат

Request:

```http request
http://localhost:8084/api/v1/messages
```

Body:

```json
{
  "chatId": "128",
  "senderName": "user1@mail.com",
  "content": "content"
}
```

Response:

```json
{
  "errorMessage": "Resource not found!",
  "errorCode": 404
}
```

##### Не найден пользователь

Request:

```http request
http://localhost:8084/api/v1/messages
```

Body:

```json
{
  "chatId": "12",
  "senderName": "user1@mail.com",
  "content": "content"
}
```

Response:

```json
{
  "errorMessage": "Sender not found: user123@mail.com",
  "errorCode": 404
}
```

### DELETE-запросы:

- deleteMessage(@PathVariable("id") Long id)
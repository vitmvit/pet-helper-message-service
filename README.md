# pet-helper-message-service

[Точка входа в приложение](https://github.com/vitmvit/pet-helper-api-gateway-service)

Данный микросервис предоставляет функционал для работы с сообщениями и чатами.

## ChatController (8084/api/v1/chats)

Контроллер поддерживает следующие операции:

- поиск чата по id
- поиск чатов по логину тех.поддержки
- поиск чатов по логину пользователя
- поиск чатов по фрагменту логина пользователя
- поиск чатов по логину тех.поддержки и пользователя
- поиск чатов по статусу
- поиск чатов без указанной тех.поддержки
- поиск сообщений по id чата
- вывод всех чатов
- создание чата
- обновление статуса чата
- присвоение чату пользователя тех.поддержки
- создание сообщения
- удаление чата
- удаление сообщения

### GET-запросы:

#### ChatDto findChatById(@PathVariable("id") Long id)

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
    {
      "id": 7,
      "chatId": 3,
      "senderName": "user3@mail.com",
      "content": "content",
      "createDate": "2024-04-02T22:33:19.430088"
    },
    {
      "id": 8,
      "chatId": 3,
      "senderName": "user3@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:08:39.622999"
    },
    {
      "id": 9,
      "chatId": 3,
      "senderName": "support1@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:08:57.749837"
    },
    {
      "id": 10,
      "chatId": 3,
      "senderName": "support1@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:19:44.174408"
    },
    {
      "id": 11,
      "chatId": 3,
      "senderName": "support1@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:24:24.215284"
    },
    {
      "id": 12,
      "chatId": 3,
      "senderName": "support1@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:24:29.457451"
    },
    {
      "id": 13,
      "chatId": 3,
      "senderName": "support1@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:29:08.742179"
    },
    {
      "id": 14,
      "chatId": 3,
      "senderName": "support1@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:29:17.659692"
    },
    {
      "id": 15,
      "chatId": 3,
      "senderName": "support1@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:29:21.688996"
    },
    {
      "id": 16,
      "chatId": 3,
      "senderName": "support1@mail.com",
      "content": "content",
      "createDate": "2024-04-03T21:29:27.078565"
    }
  ],
  "status": "CLOSED",
  "createDate": "2024-04-01T12:38:46.047585",
  "updateDate": "2024-04-03T22:11:20.661897"
}
```

Error:

```json
{
  "errorMessage": "Resource not found!",
  "errorCode": 500
}
```

#### List<ChatDto> findChatsBySupportName(@PathVariable("name") String name)

Request:

```http request
http://localhost:8084/api/v1/chats/supportName/support2@mail.com
```

Response:

```json
[
  {
    "id": 7,
    "supportName": "support2@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "OPEN",
    "createDate": "2024-04-01T12:39:51.063604",
    "updateDate": "2024-04-01T14:47:52.539101"
  },
  {
    "id": 4,
    "supportName": "support2@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "OPEN",
    "createDate": "2024-04-01T12:38:46.849295",
    "updateDate": "2024-04-03T00:21:45.15783"
  },
  {
    "id": 2,
    "supportName": "support2@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "CLOSED",
    "createDate": "2024-04-01T12:38:44.227123",
    "updateDate": "2024-04-03T22:19:57.490089"
  }
]
```

#### List<ChatDto> findChatsByEmptySupportName()

Request:

```http request
http://localhost:8084/api/v1/chats/free
```

Response:

```json
[
  {
    "id": 9,
    "supportName": "",
    "userName": "user2@mail.com",
    "messageList": [],
    "status": "FREE",
    "createDate": "2024-04-02T22:32:24.410807",
    "updateDate": "2024-04-05T01:42:36.374878"
  }
]
```

#### List<ChatDto> findChatsByStatus(@PathVariable("status") ChatStatus status)

Request:

```http request
http://localhost:8084/api/v1/chats/status/OPEN
```

Response:

```json
[
  {
    "id": 5,
    "supportName": "support1@mail.com",
    "userName": "user2@mail.com",
    "messageList": [],
    "status": "OPEN",
    "createDate": "2024-04-01T12:39:49.523781",
    "updateDate": "2024-04-05T09:54:10.006558"
  },
  {
    "id": 6,
    "supportName": "support1@mail.com",
    "userName": "user2@mail.com",
    "messageList": [
      {
        "id": 37,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "fefre",
        "createDate": "2024-04-04T20:30:33.27718"
      },
      {
        "id": 38,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "eeferf",
        "createDate": "2024-04-04T20:30:36.961346"
      }
    ],
    "status": "OPEN",
    "createDate": "2024-04-01T12:39:50.366266",
    "updateDate": "2024-04-05T09:56:44.146907"
  },
  {
    "id": 7,
    "supportName": "support2@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "OPEN",
    "createDate": "2024-04-01T12:39:51.063604",
    "updateDate": "2024-04-01T14:47:52.539101"
  },
  {
    "id": 4,
    "supportName": "support2@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "OPEN",
    "createDate": "2024-04-01T12:38:46.849295",
    "updateDate": "2024-04-03T00:21:45.15783"
  }
]
```

#### List<ChatDto> findChatsByUserName(@PathVariable("name") String name)

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
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "content",
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:21.688996"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:27.078565"
      }
    ],
    "status": "CLOSED",
    "createDate": "2024-04-01T12:38:46.047585",
    "updateDate": "2024-04-03T22:11:20.661897"
  }
]
```

#### List<ChatDto> findChatsByUserNameContains(@PathVariable("name") String name, @PathVariable("supportName") String supportName)

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
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "content",
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:21.688996"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:27.078565"
      }
    ],
    "status": "CLOSED",
    "createDate": "2024-04-01T12:38:46.047585",
    "updateDate": "2024-04-03T22:11:20.661897"
  }
]
```

#### List<ChatDto> findChatsBySupportNameAndUserName(@PathVariable("supportName") String supportName, @PathVariable("userName") String userName)

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
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "content",
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:21.688996"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:27.078565"
      }
    ],
    "status": "CLOSED",
    "createDate": "2024-04-01T12:38:46.047585",
    "updateDate": "2024-04-03T22:11:20.661897"
  }
]
```

#### List<MessageDto> findAllMessageByChatId(@PathVariable("id") Long id)

Request:

```http request
http://localhost:8084/api/v1/chats/messages/3
```

Response:

```json
[
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
  {
    "id": 7,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "content",
    "createDate": "2024-04-02T22:33:19.430088"
  },
  {
    "id": 8,
    "chatId": 3,
    "senderName": "user3@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:08:39.622999"
  },
  {
    "id": 9,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:08:57.749837"
  },
  {
    "id": 10,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:19:44.174408"
  },
  {
    "id": 11,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:24:24.215284"
  },
  {
    "id": 12,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:24:29.457451"
  },
  {
    "id": 13,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:29:08.742179"
  },
  {
    "id": 14,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:29:17.659692"
  },
  {
    "id": 15,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:29:21.688996"
  },
  {
    "id": 16,
    "chatId": 3,
    "senderName": "support1@mail.com",
    "content": "content",
    "createDate": "2024-04-03T21:29:27.078565"
  }
]
```

#### List<ChatDto> findAllChats()

Request:

```http request
http://localhost:8084/api/v1/chats
```

Response:

```json
[
  {
    "id": 11,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "CLOSED",
    "createDate": "2024-04-01T12:39:51.063604",
    "updateDate": "2024-04-05T09:51:39.634095"
  },
  {
    "id": 5,
    "supportName": "support1@mail.com",
    "userName": "user2@mail.com",
    "messageList": [],
    "status": "OPEN",
    "createDate": "2024-04-01T12:39:49.523781",
    "updateDate": "2024-04-05T09:54:10.006558"
  },
  {
    "id": 6,
    "supportName": "support1@mail.com",
    "userName": "user2@mail.com",
    "messageList": [
      {
        "id": 37,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "fefre",
        "createDate": "2024-04-04T20:30:33.27718"
      },
      {
        "id": 38,
        "chatId": 6,
        "senderName": "support1@mail.com",
        "content": "eeferf",
        "createDate": "2024-04-04T20:30:36.961346"
      }
    ],
    "status": "OPEN",
    "createDate": "2024-04-01T12:39:50.366266",
    "updateDate": "2024-04-05T09:56:44.146907"
  },
  {
    "id": 7,
    "supportName": "support2@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "OPEN",
    "createDate": "2024-04-01T12:39:51.063604",
    "updateDate": "2024-04-01T14:47:52.539101"
  },
  {
    "id": 4,
    "supportName": "support2@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "OPEN",
    "createDate": "2024-04-01T12:38:46.849295",
    "updateDate": "2024-04-03T00:21:45.15783"
  },
  {
    "id": 2,
    "supportName": "support2@mail.com",
    "userName": "user1@mail.com",
    "messageList": [],
    "status": "CLOSED",
    "createDate": "2024-04-01T12:38:44.227123",
    "updateDate": "2024-04-03T22:19:57.490089"
  },
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
      {
        "id": 7,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "content",
        "createDate": "2024-04-02T22:33:19.430088"
      },
      {
        "id": 8,
        "chatId": 3,
        "senderName": "user3@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:08:39.622999"
      },
      {
        "id": 9,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:08:57.749837"
      },
      {
        "id": 10,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:19:44.174408"
      },
      {
        "id": 11,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:24:24.215284"
      },
      {
        "id": 12,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:24:29.457451"
      },
      {
        "id": 13,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:08.742179"
      },
      {
        "id": 14,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:17.659692"
      },
      {
        "id": 15,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:21.688996"
      },
      {
        "id": 16,
        "chatId": 3,
        "senderName": "support1@mail.com",
        "content": "content",
        "createDate": "2024-04-03T21:29:27.078565"
      }
    ],
    "status": "CLOSED",
    "createDate": "2024-04-01T12:38:46.047585",
    "updateDate": "2024-04-03T22:11:20.661897"
  },
  {
    "id": 9,
    "supportName": "",
    "userName": "user2@mail.com",
    "messageList": [],
    "status": "FREE",
    "createDate": "2024-04-02T22:32:24.410807",
    "updateDate": "2024-04-05T01:42:36.374878"
  },
  {
    "id": 1,
    "supportName": "support1@mail.com",
    "userName": "user1@mail.com",
    "messageList": [
      {
        "id": 1,
        "chatId": 1,
        "senderName": "user1@mail.com",
        "content": "content",
        "createDate": "2024-04-02T22:33:10.886923"
      },
      {
        "id": 2,
        "chatId": 1,
        "senderName": "user1@mail.com",
        "content": "content",
        "createDate": "2024-04-02T22:33:12.848673"
      },
      {
        "id": 3,
        "chatId": 1,
        "senderName": "user1@mail.com",
        "content": "content",
        "createDate": "2024-04-02T22:33:13.60161"
      },
      {
        "id": 4,
        "chatId": 1,
        "senderName": "user1@mail.com",
        "content": "content",
        "createDate": "2024-04-02T22:33:14.488072"
      },
      {
        "id": 35,
        "chatId": 1,
        "senderName": "support1@mail.com",
        "content": "орпарпарпа",
        "createDate": "2024-04-04T17:40:11.473201"
      },
      {
        "id": 36,
        "chatId": 1,
        "senderName": "support1@mail.com",
        "content": "рпарпарпа",
        "createDate": "2024-04-04T17:40:17.065939"
      }
    ],
    "status": "CLOSED",
    "createDate": "2024-04-01T12:38:37.788389",
    "updateDate": "2024-04-05T01:44:34.043569"
  }
]
```

### POST-запросы:

#### ChatDto createChat(@RequestBody ChatCreateDto chatCreateDto)

Request:

```http request
http://localhost:8084/api/v1/chats
```

Body:

```json
{
  "supportName": "support10@mail.com",
  "userName": "user1@mail.com"
}
```

Response:

```json
{
  "id": 12,
  "supportName": "support10@mail.com",
  "userName": "user1@mail.com",
  "messageList": null,
  "status": "FREE",
  "createDate": "2024-04-05T21:55:42.548966",
  "updateDate": "2024-04-05T21:55:42.55246"
}
```

#### ChatDto updateStatusChat(@PathVariable("id") Long id, @PathVariable("status") ChatStatus status)

Request:

```http request
http://localhost:8084/api/v1/chats/status/12/OPEN
```

Response:

```json
{
  "id": 12,
  "supportName": "support10@mail.com",
  "userName": "user1@mail.com",
  "messageList": [],
  "status": "OPEN",
  "createDate": "2024-04-05T21:55:42.548966",
  "updateDate": "2024-04-05T21:58:59.223235"
}
```

#### ChatDto updateSupportChat(@PathVariable("id") Long id, @PathVariable("login") String login)

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
  "messageList": [],
  "status": "OPEN",
  "createDate": "2024-04-05T21:55:42.548966",
  "updateDate": "2024-04-05T22:00:38.168626"
}
```

#### MessageDto createMessage(@RequestBody MessageCreateDto messageCreateDto)

Request:

```http request
http://localhost:8084/api/v1/chats/messages
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
  "id": 39,
  "chatId": 12,
  "senderName": "user1@mail.com",
  "content": "content",
  "createDate": "2024-04-05T22:02:00.800183"
}
```

Error:

```json
{
  "errorMessage": "Resource not found!",
  "errorCode": 500
}
```

### DELETE-запросы:

Не возвращают ничего:

- deleteChat(@PathVariable("id") Long id)
- deleteMessage(@PathVariable("id") Long id) 
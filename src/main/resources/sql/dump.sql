-- Adminer 4.8.1 PostgreSQL 16.2 (Debian 16.2-1.pgdg120+2) dump

\connect
"messages";

DROP TABLE IF EXISTS "chat";
DROP SEQUENCE IF EXISTS chat_id_seq;
CREATE SEQUENCE chat_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."chat"
(
    "create_date"  timestamp(6),
    "id"           bigint DEFAULT nextval('chat_id_seq') NOT NULL,
    "update_date"  timestamp(6),
    "status"       character varying(255),
    "support_name" character varying(255),
    "user_name"    character varying(255),
    CONSTRAINT "chat_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

INSERT INTO "chat" ("create_date", "id", "update_date", "status", "support_name", "user_name")
VALUES ('2024-04-01 12:39:51.063604', 11, '2024-04-05 09:51:39.634095', 'CLOSED', 'support1@mail.com',
        'user1@mail.com'),
       ('2024-04-01 12:39:50.366266', 6, '2024-04-05 09:56:44.146907', 'OPEN', 'support1@mail.com', 'user2@mail.com'),
       ('2024-04-05 21:55:42.548966', 12, '2024-04-05 22:00:38.168626', 'OPEN', 'support1@mail.com', 'user1@mail.com'),
       ('2024-04-01 12:38:46.047585', 3, '2024-04-08 22:28:41.41466', 'CLOSED', 'support1@mail.com', 'user3@mail.com'),
       ('2024-04-08 22:42:27.543635', 14, '2024-04-08 22:42:59.001436', 'OPEN', 'support1@mail.com', 'user3@mail.com'),
       ('2024-04-01 12:39:49.523781', 5, '2024-04-08 23:58:26.133137', 'CLOSED', 'support1@mail.com', 'user2@mail.com'),
       ('2024-04-02 22:32:24.410807', 9, '2024-04-09 01:11:12.636676', 'OPEN', 'support1@mail.com', 'user2@mail.com'),
       ('2024-04-11 00:00:05.720069', 17, '2024-04-11 00:00:05.720084', 'FREE', '', 'user1@mail.com'),
       ('2024-04-11 00:00:11.035334', 18, '2024-04-11 00:00:11.035347', 'FREE', '', 'user1@mail.com'),
       ('2024-04-11 00:08:02.955369', 19, '2024-04-11 00:08:02.955434', 'FREE', '', 'user1@mail.com'),
       ('2024-04-01 12:39:51.063604', 7, '2024-04-01 14:47:52.539101', 'OPEN', 'support2@mail.com', 'user1@mail.com'),
       ('2024-04-10 23:58:50.798098', 15, '2024-04-11 01:58:19.792332', 'OPEN', 'support1@mail.com', 'user1@mail.com'),
       ('2024-04-10 23:59:55.302054', 16, '2024-04-11 01:58:23.153118', 'OPEN', 'support1@mail.com', 'user1@mail.com'),
       ('2024-04-12 11:59:56.771508', 20, '2024-04-12 11:59:56.77153', 'FREE', '', 'user1@mail.com'),
       ('2024-04-01 12:38:46.849295', 4, '2024-04-03 00:21:45.15783', 'OPEN', 'support2@mail.com', 'user1@mail.com'),
       ('2024-04-01 12:38:44.227123', 2, '2024-04-03 22:19:57.490089', 'CLOSED', 'support2@mail.com', 'user1@mail.com'),
       ('2024-04-01 12:38:37.788389', 1, '2024-04-05 01:44:34.043569', 'CLOSED', 'support1@mail.com', 'user1@mail.com');

DROP TABLE IF EXISTS "message";
DROP SEQUENCE IF EXISTS message_id_seq;
CREATE SEQUENCE message_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE "public"."message"
(
    "chat_id"     bigint,
    "create_date" timestamp(6),
    "id"          bigint DEFAULT nextval('message_id_seq') NOT NULL,
    "content"     character varying(255),
    "sender_name" character varying(255),
    CONSTRAINT "message_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

INSERT INTO "message" ("chat_id", "create_date", "id", "content", "sender_name")
VALUES (3, '2024-04-08 21:30:03.566122', 50, 'In auctor at mi nec imperdiet', 'user3@mail.com'),
       (3, '2024-04-03 21:29:27.078565', 16, 'ac dictum enim', 'support1@mail.com'),
       (1, '2024-04-02 22:33:10.886923', 1, 'Cras id molestie dui. Fusce non mauris euismod', 'user1@mail.com'),
       (1, '2024-04-02 22:33:12.848673', 2, 'sodales justo ac, convallis erat. Maecenas molestie mollis facilisis',
        'user1@mail.com'),
       (1, '2024-04-02 22:33:13.60161', 3, 'In hac habitasse platea dictumst', 'user1@mail.com'),
       (1, '2024-04-02 22:33:14.488072', 4, 'n posuere lorem scelerisque augue placerat placerat', 'user1@mail.com'),
       (1, '2024-04-04 17:40:11.473201', 35, 'Donec consectetur id leo vitae luctus', 'support1@mail.com'),
       (1, '2024-04-04 17:40:17.065939', 36, 'Aenean fermentum odio ante', 'support1@mail.com'),
       (6, '2024-04-04 20:30:33.27718', 37, 'Donec vel enim ornare', 'support1@mail.com'),
       (6, '2024-04-04 20:30:36.961346', 38, 'placerat urna ac', 'support1@mail.com'),
       (12, '2024-04-05 22:02:00.800183', 39, 'ornare mauris', 'user1@mail.com'),
       (12, '2024-04-06 00:56:07.246225', 42, 'Duis et lectus dignissim', 'support1@mail.com'),
       (12, '2024-04-06 00:56:11.033316', 43, 'efficitur velit ac, accumsan elit', 'support1@mail.com'),
       (3, '2024-04-08 22:10:56.868287', 51, 'Pellentesque gravida nec arcu maximus tempus', 'user3@mail.com'),
       (3, '2024-04-08 22:11:54.166143', 52, 'Quisque semper sapien mauris, ut pharetra ligula euismod a',
        'user3@mail.com'),
       (16, '2024-04-10 23:59:59.446409', 65, 'Nam sollicitudin in elit vel lacinia', 'user1@mail.com'),
       (3, '2024-04-08 22:27:00.961773', 53, 'Vivamus auctor fermentum vulputate', 'support1@mail.com'),
       (3, '2024-04-08 22:28:36.600161', 54, 'Proin purus erat, porttitor et pretium eu, pretium nec est.',
        'user3@mail.com'),
       (12, '2024-04-09 15:20:51.584546', 63, 'jhytgjhghg', 'support1@mail.com'),
       (12, '2024-04-09 15:21:11.951695', 64, 'efrefrefe', 'support1@mail.com'),
       (3, '2024-04-08 21:07:04.421661', 44, 'Ut tellus dolor, mollis id suscipit ut', 'support1@mail.com'),
       (3, '2024-04-08 21:07:09.573314', 45, 'accumsan id dolor', 'support1@mail.com'),
       (20, '2024-04-12 15:54:56.204855', 74, 'Donec id pellentesque purus.', 'user1@mail.com'),
       (3, '2024-04-08 21:08:36.103681', 46, 'Pellentesque eget leo a arcu porta vulputate', 'user3@mail.com'),
       (3, '2024-04-08 21:16:13.098805', 47, 'Proin mi nisl, vestibulum id leo vel, viverra euismod mauris',
        'user3@mail.com'),
       (3, '2024-04-08 21:16:40.898474', 48, 'Duis ultrices dui a tellus porttitor cursus', 'user3@mail.com'),
       (3, '2024-04-08 21:16:45.021734', 49, 'Donec convallis fermentum lorem quis rhoncus', 'user3@mail.com'),
       (14, '2024-04-08 22:42:29.923696', 55, 'Donec sed massa sed nunc luctus aliquam sit amet nec felis',
        'user3@mail.com'),
       (5, '2024-04-08 23:58:23.180019', 56, 'Sed id enim dolor.', 'support1@mail.com'),
       (6, '2024-04-09 01:07:10.295722', 57,
        'Fusce faucibus, nunc vitae fringilla aliquet, nulla ligula varius leo, vitae tempus lectus velit at diam',
        'support1@mail.com'),
       (6, '2024-04-09 01:07:12.340459', 58, 'Phasellus arcu arcu, imperdiet ac congue non, faucibus quis neque',
        'support1@mail.com'),
       (6, '2024-04-09 01:07:14.336505', 59, 'Aenean egestas rhoncus pellentesque', 'support1@mail.com'),
       (12, '2024-04-09 01:10:15.911013', 60, 'Quisque mi nisl, blandit nec mauris eget, scelerisque tempus nunc.',
        'user1@mail.com'),
       (6, '2024-04-09 01:10:52.842496', 61, 'Proin venenatis tincidunt mollis', 'support1@mail.com'),
       (6, '2024-04-09 01:11:21.114371', 62, 'In a mauris et orci molestie dapibus eu sit amet tellus',
        'support1@mail.com'),
       (17, '2024-04-11 01:56:45.513039', 66, 'Maecenas ultricies ultrices lacus in volutpat', 'user1@mail.com'),
       (3, '2024-04-02 22:33:17.920154', 5, 'Lorem ipsum dolor sit amet,', 'user3@mail.com'),
       (3, '2024-04-02 22:33:18.722023', 6, 'consectetur adipiscing elit', 'user3@mail.com'),
       (3, '2024-04-02 22:33:19.430088', 7, 'Curabitur pellentesque faucibus vestibulum', 'user3@mail.com'),
       (3, '2024-04-03 21:08:39.622999', 8, 'Curabitur pellentesque faucibus vestibulum', 'user3@mail.com'),
       (3, '2024-04-03 21:08:57.749837', 9, 'Suspendisse in vulputate ligula', 'support1@mail.com'),
       (3, '2024-04-03 21:19:44.174408', 10, 'ac molestie eros', 'support1@mail.com'),
       (3, '2024-04-03 21:24:24.215284', 11, 'Phasellus ac eros ut nunc finibus laoreet', 'support1@mail.com'),
       (3, '2024-04-03 21:24:29.457451', 12, 'In aliquam rhoncus felis', 'support1@mail.com'),
       (3, '2024-04-03 21:29:08.742179', 13, 'eget accumsan mauris convallis ut', 'support1@mail.com'),
       (3, '2024-04-03 21:29:17.659692', 14, 'Vestibulum lobortis massa sed faucibus varius', 'support1@mail.com'),
       (3, '2024-04-03 21:29:21.688996', 15, 'Maecenas quis cursus magna', 'support1@mail.com'),
       (17, '2024-04-11 01:56:48.677999', 67, 'Nunc erat mi, pharetra quis diam non, pharetra fermentum eros',
        'user1@mail.com'),
       (17, '2024-04-11 01:56:50.6445', 68, ' Sed convallis nibh at sapien vehicula iaculis', 'user1@mail.com'),
       (15, '2024-04-12 11:51:13.497422', 69, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit',
        'user1@mail.com'),
       (15, '2024-04-12 11:54:15.767297', 70, 'Etiam imperdiet luctus sapien non egestas', 'user1@mail.com'),
       (15, '2024-04-12 11:55:57.16858', 71, 'Quisque quis pretium nulla', 'user1@mail.com'),
       (15, '2024-04-12 11:55:59.768014', 72, 'Etiam interdum facilisis ultrices.', 'user1@mail.com'),
       (15, '2024-04-12 11:56:02.010097', 73, 'Nam maximus mi nisi, viverra volutpat velit maximus sed',
        'user1@mail.com');

ALTER TABLE ONLY "public"."message" ADD CONSTRAINT "fk_chat_message_id_to_id" FOREIGN KEY (chat_id) REFERENCES chat(id) NOT DEFERRABLE;

-- 2024-04-12 21:35:43.537498+00
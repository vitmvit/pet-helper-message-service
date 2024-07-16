-- Adminer 4.8.1 PostgreSQL 16.2 (Debian 16.2-1.pgdg120+2) dump

\connect
"messages";

DROP TABLE IF EXISTS "message";
DROP SEQUENCE IF EXISTS message_id_seq;
CREATE SEQUENCE message_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

DROP TABLE IF EXISTS "chat";
DROP SEQUENCE IF EXISTS chat_id_seq;
CREATE SEQUENCE chat_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 34 CACHE 1;

CREATE TABLE "public"."chat"
(
    "create_date"  timestamp(6),
    "id"           bigint DEFAULT nextval('chat_id_seq') NOT NULL,
    "update_date"  timestamp(6),
    "status"       character varying(255),
    "support_name" character varying(255),
    "user_name"    character varying(255),
    "type"         character varying(255),
    CONSTRAINT "chat_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "message";
DROP SEQUENCE IF EXISTS message_id_seq;
CREATE SEQUENCE message_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 182 CACHE 1;

CREATE TABLE "public"."message"
(
    "chat_id"     bigint,
    "create_date" timestamp(6),
    "id"          bigint DEFAULT nextval('message_id_seq') NOT NULL,
    "content"     character varying(255),
    "sender_name" character varying(255),
    "uuid_photo"  character varying,
    CONSTRAINT "message_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

TRUNCATE "chat";
INSERT INTO "chat" ("create_date", "id", "update_date", "status", "support_name", "user_name", "type")
VALUES ('2024-04-11 00:08:02.955369', 3, '2024-05-04 18:42:07.33653', 'OPEN', 'support1@mail.com', 'user3@mail.com',
        'SUPPORT'),
       ('2024-05-05 23:51:37.900243', 1, '2024-05-05 23:54:56.244971', 'OPEN', 'support1@mail.com', 'user1@mail.com',
        'MEDICAL'),
       ('2024-05-06 11:56:56.775259', 6, '2024-05-13 12:05:33.84568', 'OPEN', 'support1@mail.com', 'user1@mail.com',
        'SUPPORT'),
       ('2024-05-25 16:00:46.692917', 12, '2024-05-25 16:01:34.054444', 'OPEN', 'support1@mail.com', 'user1@mail.com',
        'SUPPORT'),
       ('2024-05-29 09:06:45.334501', 17, '2024-05-29 10:31:40.941423', 'OPEN', 'support1@mail.com', 'user1@mail.com',
        'MEDICAL'),
       ('2024-05-29 09:06:09.015739', 16, '2024-06-02 18:07:38.124932', 'OPEN', 'support1@mail.com', 'user1@mail.com',
        'SUPPORT'),
       ('2024-06-08 20:10:02.533238', 20, '2024-06-08 20:10:32.867069', 'OPEN', 'support1@mail.com', 'user1@mail.com',
        'SUPPORT'),
       ('2024-05-13 12:03:42.238714', 14, '2024-06-10 14:12:47.142519', 'OPEN', null, 'user3@mail.com',
        'MEDICAL'),
       ('2024-06-10 14:11:58.119249', 15, '2024-06-10 14:13:59.115577', 'OPEN', 'vet1@mail.com', 'user1@mail.com',
        'MEDICAL');

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
       (17, '2024-04-13 01:02:30.526222', 79,
        'Pellentesque lacinia erat sit amet sapien eleifend, sed placerat ipsum scelerisque. Fusce id augue nisi. Maecenas ac auctor ipsum, eu elementum dui. Pellentesque dolor tortor, eleifend quis tellus ac, faucibus vestibulum risus. ',
        'user1@mail.com'),
       (17, '2024-04-13 01:02:55.129097', 80,
        'Pellentesque lacinia erat sit amet sapien eleifend, sed placerat ipsum scelerisque. Fusce id augue nisi. Maecenas ac auctor ipsum, eu elementum dui. Pellentesque dolor tortor, eleifend quis tellus ac, faucibus vestibulum risus. ',
        'support1@mail.com'),
       (12, '2024-04-06 00:56:07.246225', 42, 'Duis et lectus dignissim', 'support1@mail.com'),
       (12, '2024-04-06 00:56:11.033316', 43, 'efficitur velit ac, accumsan elit', 'support1@mail.com'),
       (3, '2024-04-08 22:10:56.868287', 51, 'Pellentesque gravida nec arcu maximus tempus', 'user3@mail.com'),
       (3, '2024-04-08 22:11:54.166143', 52, 'Quisque semper sapien mauris, ut pharetra ligula euismod a',
        'user3@mail.com'),
       (16, '2024-04-10 23:59:59.446409', 65, 'Nam sollicitudin in elit vel lacinia', 'user1@mail.com'),
       (3, '2024-04-08 22:27:00.961773', 53, 'Vivamus auctor fermentum vulputate', 'support1@mail.com'),
       (3, '2024-04-08 22:28:36.600161', 54, 'Proin purus erat, porttitor et pretium eu, pretium nec est.',
        'user3@mail.com'),
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
        'user1@mail.com'),
       (17, '2024-04-13 01:06:21.987384', 82, 'Morbi est quam, ultrices aliquet lobortis at, tristique et nibh.',
        'support1@mail.com');

ALTER TABLE ONLY "public"."message" ADD CONSTRAINT "fk_chat_message_id_to_id" FOREIGN KEY (chat_id) REFERENCES chat(id) NOT DEFERRABLE;

-- 2024-04-12 22:06:51.503546+00
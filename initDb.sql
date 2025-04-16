CREATE SCHEMA `rsc_ts`;
USE `rsc_ts`;

INSERT INTO `rsc_ts`.`rsc_ts_users` (`id`, `added_at`, `is_active`, `name`, `password`, `root_user`, `username`) VALUES ('9999', NOW(), b'1', 'admin', '$2a$10$XZWGvC2ZPYr0XInjXJM/ae8SwaqO4Kg/iBLys8o.ULQuwgwJoRqzG', b'1', 'admin');
INSERT INTO `rsc_ts`.`rsc_ts_roles` (`id`, `role`) VALUES ('1', 'Admin');
INSERT INTO `rsc_ts`.`rsc_ts_roles` (`id`, `role`) VALUES ('2', 'User');
INSERT INTO `rsc_ts`.`rsc_ts_user_roles` (`user_id`, `role_id`) VALUES ('9999', '1');

INSERT INTO `rsc_ts`.`rsc_ts_application_constant` (`id`, `data`, `type`) VALUES (1, '29987', 'TICKET_SERIAL');

INSERT INTO
	`rsc_ts`.`rsc_ts_application_constant` (`id`, `data`, `type`)
VALUES
	(2, '{"title": "ticket_frame", "height": 9.5, "width": 12.7}', "TICKET_PRINT_COORDINATE"),
	(3, '{"title": "ticket_serial", "top": 3.23, "left": 5, "text": "29987"}', "TICKET_PRINT_COORDINATE"),
	(4, '{"title": "ticket_category", "top": 4.44, "left": 5, "text": "Others"}', "TICKET_PRINT_COORDINATE"),
	(5, '{"title": "ticket_person_count", "top": 5.25, "left": 5.92, "text": "1"}', "TICKET_PRINT_COORDINATE"),
	(6, '{"title": "ticket_per_person", "top": 5.92, "left": 5, "text": "Rs. 0"}', "TICKET_PRINT_COORDINATE"),
	(7, '{"title": "ticket_total_amount", "top": 6.55, "left": 5, "text": "Rs. 0"}', "TICKET_PRINT_COORDINATE"),
	(8, '{"title": "ticket_date", "top": 3.2, "left": 8.8, "text": "19, Jul 24"}', "TICKET_PRINT_COORDINATE"),
	(9, '{"title": "ticket_time", "top": 3.91, "left": 8.8, "text": "09:14 AM"}', "TICKET_PRINT_COORDINATE");

INSERT INTO `dev_composite_folder` (`id`, `del`, `creator`, `create_time`, `editor`, `edit_time`, `name`, `type`, `belong`, `parent_id`) VALUES
(10001, 0, ' ', '2021-10-11 16:16:00', ' ', '2021-10-11 16:16:00', '数仓设计', 'FUNCTION', 'DESIGN', 0),
(10002, 0, ' ', '2021-10-11 16:16:00', ' ', '2021-10-11 16:16:00', 'DAG', 'FUNCTION', 'DAG', 0),
(10003, 0, ' ', '2021-10-11 16:16:00', ' ', '2021-10-11 16:16:00', '数据集成', 'FUNCTION', 'DI', 0),
(10004, 0, ' ', '2021-10-11 16:16:00', ' ', '2021-10-11 16:16:00', '数据开发', 'FUNCTION', 'DEV', 0),
(10005, 0, ' ', '2021-10-11 16:16:00', ' ', '2021-10-11 16:16:27', '表', 'FUNCTION', 'DESIGN.TABLE', 10001),
(10006, 0, ' ', '2021-10-11 16:16:00', ' ', '2021-10-11 16:16:27', '标签', 'FUNCTION', 'DESIGN.LABEL', 10001),
(10007, 0, ' ', '2021-10-11 16:16:00', ' ', '2021-10-11 16:16:27', '枚举', 'FUNCTION', 'DESIGN.ENUM', 10001),
(10008, 0, ' ', '2021-10-11 16:16:00', ' ', '2021-10-11 16:16:27', '作业', 'FUNCTION', 'DEV.JOB', 10004);
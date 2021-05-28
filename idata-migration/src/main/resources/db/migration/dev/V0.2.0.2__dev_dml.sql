-- ### 新表预置数据：
-- dev_folder
-- 系统文件夹
--
-- dev_label_define
-- 表：数据库名称、是否分区表、安全等级、数仓分层、表中文名称
-- 字段：主键、字段类型、安全等级、字段中文名称、是否分区字段
--
-- dev_enum
-- 数据域、业务过程、数仓分层、安全等级、ER图关系类型、hive字段类型、聚合方式
--
-- dev_enum_value
-- 同dev_enum
--
-- ### 现有表迁移方案：
--
-- table_folder -> dev_folder
-- table_info -> table_info, dev_label_define 具体字段需确认
-- column_info -> column_info, dev_label_define 具体字段需确认
-- foreign_key -> dev_foreign_key
--
-- data_domain -> dev_enum, dev_enum_value
-- business_process -> dev_enum, dev_enum_value
-- modifier_type -> dev_label_define
-- modifier -> dev_enum, dev_enum_value
-- metric -> dev_label_define
-- dimension -> dev_label_define
-- column_role -> dev_label
-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720194768336, '-1', '/patient/sysMessage/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '系统消息表管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720194768337,1720194768336, 'patient_sysMessage_view', '1', null, '1',  '0', null, '0', null, '系统消息表查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720194768338,1720194768336, 'patient_sysMessage_add', '1', null, '1',  '0', null, '1', null, '系统消息表新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720194768339,1720194768336, 'patient_sysMessage_edit', '1', null, '1',  '0', null, '2', null, '系统消息表修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720194768340,1720194768336, 'patient_sysMessage_del', '1', null, '1',  '0', null, '3', null, '系统消息表删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720194768341,1720194768336, 'patient_sysMessage_export', '1', null, '1',  '0', null, '3', null, '导入导出');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720194768343,1720194768336, 'patient_send_view', '1', null, '1',  '0', null, '3', null, '医生发送消息');
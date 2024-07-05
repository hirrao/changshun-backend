-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720169436911, '-1', '/patient/gptMessage/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , 'GPT对话表管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720169436912,1720169436911, 'patient_gptMessage_view', '1', null, '1',  '0', null, '0', null, 'GPT对话表查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720169436913,1720169436911, 'patient_gptMessage_add', '1', null, '1',  '0', null, '1', null, 'GPT对话表新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720169436914,1720169436911, 'patient_gptMessage_edit', '1', null, '1',  '0', null, '2', null, 'GPT对话表修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720169436915,1720169436911, 'patient_gptMessage_del', '1', null, '1',  '0', null, '3', null, 'GPT对话表删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720169436916,1720169436911, 'patient_gptMessage_export', '1', null, '1',  '0', null, '3', null, '导入导出');
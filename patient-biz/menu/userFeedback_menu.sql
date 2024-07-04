-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720079964427, '-1', '/patient/userFeedback/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '用户反馈信息管理管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720079964428,1720079964427, 'patient_userFeedback_view', '1', null, '1',  '0', null, '0', null, '用户反馈信息管理查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720079964429,1720079964427, 'patient_userFeedback_add', '1', null, '1',  '0', null, '1', null, '用户反馈信息管理新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720079964430,1720079964427, 'patient_userFeedback_edit', '1', null, '1',  '0', null, '2', null, '用户反馈信息管理修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720079964431,1720079964427, 'patient_userFeedback_del', '1', null, '1',  '0', null, '3', null, '用户反馈信息管理删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720079964432,1720079964427, 'patient_userFeedback_export', '1', null, '1',  '0', null, '3', null, '导入导出');
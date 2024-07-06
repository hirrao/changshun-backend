-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720268513243, '-1', '/patient/commonQuestion/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720268513244,1720268513243, 'patient_commonQuestion_view', '1', null, '1',  '0', null, '0', null, '查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720268513245,1720268513243, 'patient_commonQuestion_add', '1', null, '1',  '0', null, '1', null, '新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720268513246,1720268513243, 'patient_commonQuestion_edit', '1', null, '1',  '0', null, '2', null, '修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720268513247,1720268513243, 'patient_commonQuestion_del', '1', null, '1',  '0', null, '3', null, '删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720268513248,1720268513243, 'patient_commonQuestion_export', '1', null, '1',  '0', null, '3', null, '导入导出');
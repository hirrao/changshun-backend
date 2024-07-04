-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720115445646, '-1', '/patient/eatDrugAlert/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '用药管理表管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720115445647,1720115445646, 'patient_eatDrugAlert_view', '1', null, '1',  '0', null, '0', null, '用药管理表查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720115445648,1720115445646, 'patient_eatDrugAlert_add', '1', null, '1',  '0', null, '1', null, '用药管理表新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720115445649,1720115445646, 'patient_eatDrugAlert_edit', '1', null, '1',  '0', null, '2', null, '用药管理表修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720115445650,1720115445646, 'patient_eatDrugAlert_del', '1', null, '1',  '0', null, '3', null, '用药管理表删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720115445651,1720115445646, 'patient_eatDrugAlert_export', '1', null, '1',  '0', null, '3', null, '导入导出');
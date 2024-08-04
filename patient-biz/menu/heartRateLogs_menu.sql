-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1722743138359, '-1', '/patient/heartRateLogs/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '患者心率数据管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1722743138360,1722743138359, 'patient_heartRateLogs_view', '1', null, '1',  '0', null, '0', null, '患者心率数据查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1722743138361,1722743138359, 'patient_heartRateLogs_add', '1', null, '1',  '0', null, '1', null, '患者心率数据新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1722743138362,1722743138359, 'patient_heartRateLogs_edit', '1', null, '1',  '0', null, '2', null, '患者心率数据修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1722743138363,1722743138359, 'patient_heartRateLogs_del', '1', null, '1',  '0', null, '3', null, '患者心率数据删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1722743138364,1722743138359, 'patient_heartRateLogs_export', '1', null, '1',  '0', null, '3', null, '导入导出');
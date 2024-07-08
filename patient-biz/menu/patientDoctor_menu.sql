-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720454190780, '-1', '/patient/patientDoctor/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '医患绑定表管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720454190781,1720454190780, 'patient_patientDoctor_view', '1', null, '1',  '0', null, '0', null, '医患绑定表查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720454190782,1720454190780, 'patient_patientDoctor_add', '1', null, '1',  '0', null, '1', null, '医患绑定表新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720454190783,1720454190780, 'patient_patientDoctor_edit', '1', null, '1',  '0', null, '2', null, '医患绑定表修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720454190784,1720454190780, 'patient_patientDoctor_del', '1', null, '1',  '0', null, '3', null, '医患绑定表删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720454190785,1720454190780, 'patient_patientDoctor_export', '1', null, '1',  '0', null, '3', null, '导入导出');
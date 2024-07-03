-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1719994068214, '-1', '/patient/patientBase/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '患者基本信息管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1719994068215,1719994068214, 'patient_patientBase_view', '1', null, '1',  '0', null, '0', null, '患者基本信息查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1719994068216,1719994068214, 'patient_patientBase_add', '1', null, '1',  '0', null, '1', null, '患者基本信息新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1719994068217,1719994068214, 'patient_patientBase_edit', '1', null, '1',  '0', null, '2', null, '患者基本信息修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1719994068218,1719994068214, 'patient_patientBase_del', '1', null, '1',  '0', null, '3', null, '患者基本信息删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1719994068219,1719994068214, 'patient_patientBase_export', '1', null, '1',  '0', null, '3', null, '导入导出');
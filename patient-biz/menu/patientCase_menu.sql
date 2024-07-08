-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720335214953, '-1', '/patient/patientCase/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '患者病历管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720335214954,1720335214953, 'patient_patientCase_view', '1', null, '1',  '0', null, '0', null, '患者病历查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720335214955,1720335214953, 'patient_patientCase_add', '1', null, '1',  '0', null, '1', null, '患者病历新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720335214956,1720335214953, 'patient_patientCase_edit', '1', null, '1',  '0', null, '2', null, '患者病历修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720335214957,1720335214953, 'patient_patientCase_del', '1', null, '1',  '0', null, '3', null, '患者病历删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720335214958,1720335214953, 'patient_patientCase_export', '1', null, '1',  '0', null, '3', null, '导入导出');
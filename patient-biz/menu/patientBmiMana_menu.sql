-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720113351991, '-1', '/patient/patientBmiMana/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '患者身高体重管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720113351992,1720113351991, 'patient_patientBmiMana_view', '1', null, '1',  '0', null, '0', null, '患者身高体重查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720113351993,1720113351991, 'patient_patientBmiMana_add', '1', null, '1',  '0', null, '1', null, '患者身高体重新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720113351994,1720113351991, 'patient_patientBmiMana_edit', '1', null, '1',  '0', null, '2', null, '患者身高体重修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720113351995,1720113351991, 'patient_patientBmiMana_del', '1', null, '1',  '0', null, '3', null, '患者身高体重删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720113351996,1720113351991, 'patient_patientBmiMana_export', '1', null, '1',  '0', null, '3', null, '导入导出');
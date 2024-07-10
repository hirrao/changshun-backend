-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510231, '-1', '/patient/aiPreDiagnosis/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , 'AI预问诊管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510232,1720324510231, 'patient_aiPreDiagnosis_view', '1', null, '1',  '0', null, '0', null, 'AI预问诊查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510233,1720324510231, 'patient_aiPreDiagnosis_add', '1', null, '1',  '0', null, '1', null, 'AI预问诊新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720324510234,1720324510231, 'patient_aiPreDiagnosis_edit', '1', null, '1',  '0', null, '2', null, 'AI预问诊修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510235,1720324510231, 'patient_aiPreDiagnosis_del', '1', null, '1',  '0', null, '3', null, 'AI预问诊删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510236,1720324510231, 'patient_aiPreDiagnosis_export', '1', null, '1',  '0', null, '3', null, '导入导出');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510237,1720324510231, 'patient_aiPreDiagnosis_export', '1', null, '1',  '0', null, '3', null, '得到伴随疾病');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510238,1720324510231, 'patient_aiPreDiagnosis_carecount', '1', null, '1',  '0', null, '3', null, '得到特别关心伴随疾病');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510239,1720324510231, 'patient_aiPreDiagnosis_hiscount', '1', null, '1',  '0', null, '3', null, '得到客观病史');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510240,1720324510231, 'patient_aiPreDiagnosis_hisccount', '1', null, '1',  '0', null, '3', null, '得到特别关心客观病史');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720324510242,1720324510231, 'patient_aiPreDiagnosis_count', '1', null, '1',  '0', null, '3', null, '得到伴随疾病1');
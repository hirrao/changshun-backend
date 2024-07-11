-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542462, '-1', '/patient/persureHeartRate/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '血压心率展示管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542463,1720094542462, 'patient_persureHeartRate_view', '1', null, '1',  '0', null, '0', null, '血压心率展示查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542464,1720094542462, 'patient_persureHeartRate_add', '1', null, '1',  '0', null, '1', null, '血压心率展示新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720094542465,1720094542462, 'patient_persureHeartRate_edit', '1', null, '1',  '0', null, '2', null, '血压心率展示修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542466,1720094542462, 'patient_persureHeartRate_del', '1', null, '1',  '0', null, '3', null, '血压心率展示删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542467,1720094542462, 'patient_persureHeartRate_export', '1', null, '1',  '0', null, '3', null, '导入导出');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542468,1720094542462, 'patient_persureHeartRate_export', '1', null, '1',  '0', null, '3', null, '最高心率');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542469,1720094542462, 'patient_persureHeartRate_export', '1', null, '1',  '0', null, '3', null, '最低心率');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542470,1720094542462, 'patient_persureHeartRate_view', '1', null, '1',  '0', null, '3', null, '查询心率类型人数');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542471,1720094542462, 'patient_persureHeartCare_view', '1', null, '1',  '0', null, '3', null, '查询特别关系心率类型人数');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542472,1720094542462, 'patient_persureCareRate_view', '1', null, '1',  '0', null, '3', null, '查询特别关心高血压病情');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720094542473,1720094542462, 'patient_persureRate_view', '1', null, '1',  '0', null, '3', null, '查询高血压病情');
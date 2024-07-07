-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720345460581, '-1', '/patient/pressureAnomalyLogs/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '血压异常信息统计管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720345460582,1720345460581, 'patient_pressureAnomalyLogs_view', '1', null, '1',  '0', null, '0', null, '血压异常信息统计查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720345460583,1720345460581, 'patient_pressureAnomalyLogs_add', '1', null, '1',  '0', null, '1', null, '血压异常信息统计新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720345460584,1720345460581, 'patient_pressureAnomalyLogs_edit', '1', null, '1',  '0', null, '2', null, '血压异常信息统计修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720345460585,1720345460581, 'patient_pressureAnomalyLogs_del', '1', null, '1',  '0', null, '3', null, '血压异常信息统计删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720345460586,1720345460581, 'patient_pressureAnomalyLogs_export', '1', null, '1',  '0', null, '3', null, '导入导出');
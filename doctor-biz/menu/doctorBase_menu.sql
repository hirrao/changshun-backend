-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1719998224080, '-1', '/doctor/doctorBase/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '医生基础信息管理管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1719998224081,1719998224080, 'doctor_doctorBase_view', '1', null, '1',  '0', null, '0', null, '医生基础信息管理查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1719998224082,1719998224080, 'doctor_doctorBase_add', '1', null, '1',  '0', null, '1', null, '医生基础信息管理新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1719998224083,1719998224080, 'doctor_doctorBase_edit', '1', null, '1',  '0', null, '2', null, '医生基础信息管理修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1719998224084,1719998224080, 'doctor_doctorBase_del', '1', null, '1',  '0', null, '3', null, '医生基础信息管理删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1719998224085,1719998224080, 'doctor_doctorBase_export', '1', null, '1',  '0', null, '3', null, '导入导出');
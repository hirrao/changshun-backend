-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720171999615, '-1', '/doctor/doctorBase/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '医生信息表管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720171999616,1720171999615, 'doctor_doctorBase_view', '1', null, '1',  '0', null, '0', null, '医生信息表查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720171999617,1720171999615, 'doctor_doctorBase_add', '1', null, '1',  '0', null, '1', null, '医生信息表新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720171999618,1720171999615, 'doctor_doctorBase_edit', '1', null, '1',  '0', null, '2', null, '医生信息表修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720171999619,1720171999615, 'doctor_doctorBase_del', '1', null, '1',  '0', null, '3', null, '医生信息表删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720171999620,1720171999615, 'doctor_doctorBase_export', '1', null, '1',  '0', null, '3', null, '导入导出');
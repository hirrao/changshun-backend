-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)
values (1720144627672, '-1', '/patient/drugEatTime/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '用药时间对照表（因为一款药可能需要多个时间）管理');

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720144627673,1720144627672, 'patient_drugEatTime_view', '1', null, '1',  '0', null, '0', null, '用药时间对照表（因为一款药可能需要多个时间）查看');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720144627674,1720144627672, 'patient_drugEatTime_add', '1', null, '1',  '0', null, '1', null, '用药时间对照表（因为一款药可能需要多个时间）新增');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)
values (1720144627675,1720144627672, 'patient_drugEatTime_edit', '1', null, '1',  '0', null, '2', null, '用药时间对照表（因为一款药可能需要多个时间）修改');

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720144627676,1720144627672, 'patient_drugEatTime_del', '1', null, '1',  '0', null, '3', null, '用药时间对照表（因为一款药可能需要多个时间）删除');

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)
values (1720144627677,1720144627672, 'patient_drugEatTime_export', '1', null, '1',  '0', null, '3', null, '导入导出');
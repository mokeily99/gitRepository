<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
 String webpath = request.getContextPath();
%>
 <div class="modal-dialog" role="document">
     <div class="modal-content">
         <div class="modal-header">
             <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
             <h4 class="modal-title" id="add_modal_type">新增</h4>
         </div>
         <form id="add_menu_form" action="<%=webpath%>/menu/addMenu.action" method="post">
         	<input type="hidden" name="add_parent_code" id="add_parent_code">
         	<div class="modal-body">
	             <div class="form-group">
	                 <label for="txt_departmentname">菜单名称</label>
	                 <input type="text" name="add_menu_name" class="form-control" id="add_menu_name" placeholder="菜单名称" autocomplete="off">
	             </div>
	             <div class="form-group">
	                 <label for="txt_parentdepartment">菜单地址</label>
	                 <input type="text" name="add_menu_address" class="form-control" id="add_menu_address" placeholder="菜单地址" autocomplete="off">
	             </div>
	         </div>
	         <div class="modal-footer">
	             <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
	             <button id="add_btn_submit" class="btn btn-primary" onclick="add_menu()"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
	         </div>
         </form>
     </div>
 </div>

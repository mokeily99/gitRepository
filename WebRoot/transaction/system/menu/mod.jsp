<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
 String webpath = request.getContextPath();
%>
 <div class="modal-dialog" role="document">
     <div class="modal-content">
         <div class="modal-header">
             <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
             <h4 class="modal-title" id="myModalLabel">修改</h4>
         </div>
         <form id="mod_menu_form" action="<%=webpath%>/menu/modMenu.action" method="post">
         	<input type="hidden" id="mod_maxaccept" name="mod_maxaccept"/>
         	<div class="modal-body">
	             <div class="form-group">
	                 <label for="txt_departmentname">菜单名称</label>
	                 <input type="text" name="mod_menu_name" class="form-control" id="mod_menu_name" placeholder="菜单名称" autocomplete="off">
	             </div>
	             <div class="form-group">
	                 <label for="txt_parentdepartment">菜单地址</label>
	                 <input type="text" name="mod_menu_address" class="form-control" id="mod_menu_address" placeholder="菜单地址" autocomplete="off">
	             </div>
	         </div>
	         <div class="modal-footer">
	             <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
	             <button id="mod_btn_submit" class="btn btn-primary" onclick="mod_menu_sub()"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
	         </div>
         </form>
     </div>
 </div>

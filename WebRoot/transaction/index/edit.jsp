<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
 String webpath = request.getContextPath();
%>
 <div class="modal-dialog" role="document" style="z-index:2041;width:260px;margin-top:80px;">
     <div class="modal-content">
         <div class="modal-header">
             <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
             <h4 class="modal-title" id="myModalLabel">修改密码</h4>
         </div>
         <form id="edit_pwd_form" autocomplete="off"><%-- action="<%=webpath%>/login/editPWD.action" method="post"  --%>
         	<div style="height:230px;">
	         	<div class="modal-body" style="float:left; padding-bottom:0px;">
		            <div class="form-group">
		                <label for="txt_departmentname">原始密码</label>
		                <input type="password" name="old_pwd" id="old_pwd" class="form-control" placeholder="原始密码" autocomplete="off">
		            </div>
		             
	            	<div class="form-group">
		                <label for="txt_departmentname">新密码</label>
		                <input type="password" name="new_pwd" id="new_pwd" class="form-control" placeholder="新密码" autocomplete="off">
		            </div>
		            <div class="form-group">
		                <label for="txt_departmentname">确认新密码</label>
		                <input type="password" name="new_pwd_ok" id="new_pwd_ok" class="form-control" placeholder="确认新密码" autocomplete="off">
		            </div>
		         </div>
	        </div>
	        <div class="modal-footer">
	             <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
	             <button id="edit_pwd_submit" class="btn btn-primary" onclick="editPwd()"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
	        </div>
         </form>
     </div>
 </div>

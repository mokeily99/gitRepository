<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.yl.common.user.pojo.UserView"%>
<%
String webpath = request.getContextPath();

UserView user = (UserView) session.getAttribute("userInfo");
String userame = user.getUserName();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>北湖湿地公园公众号后台管理系统</title>
<meta name="keywords" content="">
<meta name="description" content="">

<!-- 引入jquery -->
<jsp:include page="../../common/jsp/commonHeader.jsp" flush="true" />
<script type="text/javascript" src="<%=webpath%>/common/ui/bootstrapvalidator-master/dist/js/bootstrapValidator.min.js"></script>
<link href="<%=webpath%>/common/ui/bootstrapvalidator-master/dist/css/bootstrapValidator.min.css" rel="stylesheet">
<!-- 引入easyui js -->
<script type="text/javascript" src="<%=webpath%>/common/js/jquery-easyui-1.5.5.1/jquery.easyui.min.js"></script>

<script src="<%=webpath%>/transaction/index/index.js"></script>
<script type="text/javascript">
	var webpath = '<%=webpath%>';
</script>
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                    <span class="block m-t-xs" style="font-size:20px;text-align: center;">
                                    	<div style="float:left"><strong style="color:#23b7e5">友<br/>联</strong></div>
                                        <img src="<%=webpath%>/common/ui/hAdmin/img/tip_logo.png" style="height:50px;width:100px;"/>
                                        <div style="float:right"><strong style="color:#23b7e5">科<br/>技</strong></div>
                                    </span>
                                </span>
                            </a>
                        </div>
                        <div class="logo-element">友联科技
                        </div>
                    </li>
                    <li class="hidden-folded padder m-t m-b-sm text-muted text-xs">
                        <span class="ng-scope">菜单</span>
                    </li>
                    <li class="line dk"></li>
                    <li>
                        <a class="J_menuItem" href="<%=webpath%>/transaction/analysis/analysis.jsp">
                            <i class="fa fa-home"></i>
                            <span class="nav-label">主页</span>
                        </a>
                    </li>
                    <li class="line dk"></li>
                    
                    <!-- 菜单补充区域 -->
                    
                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                    <div class="navbar-header"><a class="navbar-minimalize minimalize-styl-2 btn btn-info " href="#"><i class="fa fa-bars"></i> </a>
                        <form role="search" class="navbar-form-custom" method="post" action="<%=webpath%>/common/ui/hAdmin/search_results.html">
                            <div class="form-group">
                                <input type="text" placeholder="请输入您需要查找的内容 …" class="form-control" name="top-search" id="top-search">
                            </div>
                        </form>
                    </div>
                    <ul class="nav navbar-top-links navbar-right">
                    	
                        <li class="dropdown">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                                <i class="fa fa-envelope"></i> <span class="label label-warning"></span>
                            </a>
                            <%-- <ul class="dropdown-menu dropdown-messages">
                                <li class="m-t-xs">
                                    <div class="dropdown-messages-box">
                                        <a href="<%=webpath%>/common/ui/hAdmin/profile.html" class="pull-left">
                                            <img alt="image" class="img-circle" src="<%=webpath%>/common/ui/hAdmin/img/a7.jpg">
                                        </a>
                                        <div class="media-body">
                                            <small class="pull-right">46小时前</small>
                                            <strong>小四</strong> 是不是只有我死了,你们才不骂爵迹
                                            <br>
                                            <small class="text-muted">3天前 2014.11.8</small>
                                        </div>
                                    </div>
                                </li>
                                <li class="divider"></li>
                                <li>
                                    <div class="dropdown-messages-box">
                                        <a href="<%=webpath%>/common/ui/hAdmin/profile.html" class="pull-left">
                                            <img alt="image" class="img-circle" src="<%=webpath%>/common/ui/hAdmin/img/a4.jpg">
                                        </a>
                                        <div class="media-body ">
                                            <small class="pull-right text-navy">25小时前</small>
                                            <strong>二愣子</strong> 呵呵
                                            <br>
                                            <small class="text-muted">昨天</small>
                                        </div>
                                    </div>
                                </li>
                                <li class="divider"></li>
                                <li>
                                    <div class="text-center link-block">
                                        <a class="J_menuItem" href="<%=webpath%>/common/ui/hAdmin/mailbox.html">
                                            <i class="fa fa-envelope"></i> <strong> 查看所有消息</strong>
                                        </a>
                                    </div>
                                </li>
                            </ul> --%>
                        </li>
                        <li class="dropdown">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                                <i class="fa fa-bell"></i> <span class="label label-primary"></span>
                            </a>
                            <%-- <ul class="dropdown-menu dropdown-alerts">
                                <li>
                                    <a href="<%=webpath%>/common/ui/hAdmin/mailbox.html">
                                        <div>
                                            <i class="fa fa-envelope fa-fw"></i> 您有16条未读消息
                                            <span class="pull-right text-muted small">4分钟前</span>
                                        </div>
                                    </a>
                                </li>
                                <li class="divider"></li>
                                <li>
                                    <a href="<%=webpath%>/common/ui/hAdmin/profile.html">
                                        <div>
                                            <i class="fa fa-qq fa-fw"></i> 3条新回复
                                            <span class="pull-right text-muted small">12分钟钱</span>
                                        </div>
                                    </a>
                                </li>
                                <li class="divider"></li>
                                <li>
                                    <div class="text-center link-block">
                                        <a class="J_menuItem" href="<%=webpath%>/common/ui/hAdmin/notifications.html">
                                            <strong>查看所有 </strong>
                                            <i class="fa fa-angle-right"></i>
                                        </a>
                                    </div>
                                </li>
                            </ul> --%>
                        </li>
                        
                        <li class="dropdown">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                                <i class="fa fa-user"></i>
                            </a>
                            <ul class="dropdown-menu dropdown-messages">
                                <li>
                                    <a href="javascript:void(0)">
                                        <div>
                                            <i class="fa fa-user fa-fw"></i> 尊敬的用户：
                                            <strong><%=userame%></strong>
                                        </div>
                                    </a>
                                </li>
                                <li class="divider"></li>
                                <li>
                                    <div class="text-center link-block">
                                        <a href="javascript:edit();">
                                            <i class="fa fa-pencil fa-fw"></i> <strong>修改密码</strong>
                                        </a>
                                    </div>
                                </li>
                                <li class="divider"></li>
                                <li>
                                    <div class="text-center link-block">
                                        <a href="javascript:cancel();">
                                            <i class="fa fa-power-off"></i> <strong>退出</strong>
                                        </a>
                                    </div>
                                </li>
                            </ul>
                        </li>
                        
                    </ul>
                </nav>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe id="J_iframe" width="100%" height="105%" src="<%=webpath%>/transaction/idRead/idRead.jsp" frameborder="0" data-id="<%=webpath%>/transaction/analysis/analysis.jsp" seamless></iframe>
            </div>
        </div>
        <!--右侧部分结束-->
    </div>

    <!-- 自定义js -->
    <script src="<%=webpath%>/common/ui/hAdmin/js/hAdmin.js?v=4.1.0"></script>
    
</body>
</html>
<div class="modal fade" id="edit_pwd_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<jsp:include page="edit.jsp"></jsp:include>
</div>
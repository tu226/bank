<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>资源类型</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<script src="${pageContext.request.contextPath}/miniui/boot.js" type="text/javascript"></script>

</head>
    <style type="text/css">
    html, body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }    
    </style>
<body >   
    <div class="mini-toolbar" style="padding:2px;border-bottom:0;">
        <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                        <a class="mini-button" iconCls="icon-add" onclick="addRow()" plain="true">增加</a>
                        <a class="mini-button" iconCls="icon-remove" onclick="removeRow()" plain="true">删除</a>
                        <span class="separator"></span>
                        <a class="mini-button" iconCls="icon-save" onclick="saveData()" plain="true">保存</a>            
                    </td>
                    <td style="white-space:nowrap;">
                        <input id="key" class="mini-textbox" emptyText="请输入" style="width:150px;" onenter="onKeyEnter"/>   
                        <a class="mini-button" onclick="search()">查询</a>
                    </td>
                </tr>
            </table>   
    </div>
    <!--撑满页面-->
    <div class="mini-fit" >
        
         <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;" 
		        url="${pageContext.request.contextPath}/privilege/loadPrivileges.do" idField="id"
		        allowResize="true" pageSize="20" 
		        allowCellEdit="true" allowCellSelect="true" multiSelect="true" 
		        editNextOnEnterKey="true" 
		        
		    >
            <div property="columns">
                <div type="indexcolumn" ></div>
                <div type="checkcolumn"></div>
                <div field="privilegeId" allowResize="false" width="120" headerAlign="center" allowSort="true">资源编号
	                <input property="editor" class="mini-textbox" style="width:100%;" required="true" requiredErrorText="不能为空"/>
	            </div>
                <div field="privilegeName" allowResize="false" width="120" headerAlign="center" allowSort="true">资源类型
	                <input property="editor" class="mini-textbox" style="width:100%;" required="true" requiredErrorText="不能为空"/>
	            </div>
            </div>
        </div> 

    </div>
    
    <script type="text/javascript">
        mini.parse();
        var grid = mini.get("datagrid1");
        grid.load();

        function search() {       
            var key = mini.get("key").getValue();
            grid.load({ key: key });
        }

        function onKeyEnter(e) {
            search();
        }
        
        function addRow() {          
            var newRow = { name: "New Row" };
            grid.addRow(newRow, 0);
        }
        
        function removeRow() {
            var rows = grid.getSelecteds();
            if (rows.length > 0) {
                grid.removeRows(rows, true);
            }
        }
        
        function saveData() {
            var data = grid.getChanges();
            var json = mini.encode(data);
            alert(json);
            grid.loading("保存中，请稍后......");
            $.ajax({
                url: "${pageContext.request.contextPath}/privilege/savePrivileges.do",
                data: { data: json },
                type: "post",
                success: function (text) {
                    grid.reload();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(jqXHR.responseText);
                }
            });
        }
    </script>

</body>
</html>

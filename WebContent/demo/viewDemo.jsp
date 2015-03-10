<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>View 视图</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="${pageContext.request.contextPath}/miniui/boot.js" type="text/javascript"></script>
</head>
<body >
    <h1>view视图</h1>  

    <div style="padding-bottom:5px;">
        
        <span>员工姓名：</span><input type="text" id="key"  />
        <input type="button" value="查找" onclick="search()"/>
        
        
    </div>
<div id="datagrid1" class="mini-datagrid" style="width:700px;height:250px;" 
    url=""  idField="id" allowResize="true"
    sizeList="[20,30,50,100]" pageSize="20"
>
    <div property="columns">
        <div type="indexcolumn" ></div>
        <div field="loginname" width="120" headerAlign="center" allowSort="true">员工帐号</div>    
        <div field="name" width="120" headerAlign="center" allowSort="true">姓名</div>                            
        <div field="gender" width="100" renderer="onGenderRenderer" align="center" headerAlign="center">性别</div>
        <div field="salary" numberFormat="¥#,0.00" align="right" width="100" allowSort="true">薪资</div>                                
        <div field="age" width="100" allowSort="true" decimalPlaces="2" dataType="float">年龄</div>
        <div field="createtime" width="100" headerAlign="center" dateFormat="yyyy-MM-dd" allowSort="true">创建日期</div>                
    </div>
</div>   
   
    <script type="text/javascript">

        mini.parse();

        var grid = mini.get("datagrid1");
        
        grid.load();
        
        function search() {
            var key = document.getElementById("key").value;
            grid.load({ key: key });
        }
        $("#key").bind("keydown", function (e) {
            if (e.keyCode == 13) {
                search();
            }
        });
        ///////////////////////////////////////////////////////
        var Genders = [{ id: 1, text: '男' }, { id: 2, text: '女'}];
        function onGenderRenderer(e) {
            for (var i = 0, l = Genders.length; i < l; i++) {
                var g = Genders[i];
                if (g.id == e.value) return g.text;
            }
            return "";
        }
    </script>

    <div class="description">
        <h3>Description</h3>
        
    </div>

</body>
</html>
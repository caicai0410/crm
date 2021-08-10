layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 营销机会列表展示
     */
    var  tableIns = table.render({
        elem: '#saleChanceList', // 表格绑定的ID
        url : ctx + '/sale_chance/list', // 访问数据的地址
        cellMinWidth : 95,
        page : true, // 开启分页
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "saleChanceListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'uname', title: '指派人', align:'center'},
            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet:function(d){
                    return formatterState(d.state);
                }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    /**
     * 格式化分配状态
     *  0 - 未分配
     *  1 - 已分配
     *  其他 - 未知
     * @param state
     * @returns {string}
     * templet函数颜色
     */
    function formatterState(state){
        if(state==0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if(state==1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     *  0 - 未开发
     *  1 - 开发中
     *  2 - 开发成功
     *  3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value){
        if(value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if(value==1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if(value==2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if(value==3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    /*选择元素绑定事件*/
    $(".search_btn").click(function(){
        //上述方法等价于
        table.reload('saleChanceListTable', {
            where: { //设定异步数据接口的额外参数，任意设
                /*
                * 绑定三个搜索事件
                * 选择器绑定表单框
                * 最后绑定复选框
                */
                "customerName":$("input[name='customerName']").val(),
                "createMan": $("input[name='createMan']").val(),
                "state":$("#state").val()
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        }); //只重载数据
    });

    /*绑定头部工具栏*/
    //头工具栏事件
    table.on('toolbar(saleChances)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        //console.log(checkStatus.data)
        switch(obj.event){
            case 'add':
                //alert("添加OK");
                openAddOrUpdateSaleChanceDialog();
                break;
            case 'del':
                //删除
                deleteSaleChance(checkStatus.data);
                break;

        };
    });

    /**
     * 删除
     */
    function deleteSaleChance(data){
        if(data.length==0){
            layer.msg("请选择要删除的数据");
            return ;
        }
        //发送ajax删除
        layer.confirm("你确定要删除这些数据吗？",{
            btn:["确定","取消"],
        },function(index){
            //关闭弹出框
            layer.close(index);
            //收集数据
            var ids=[];
            //循环
            for (var i = 0; i <data.length ; i++) {
                ids.push(data[i].id);
            }
            //ids=1&ids=2
            console.log(ids.toString()+"<<<")
            //发送ajax删除
            $.ajax({
                type:"post",
                url:ctx+"/sale_chance/delete",
                data:{"ids":ids.toString()},
                dataType:"json",
                success:function (data){
                    if(data.code==200){
                        //重新加载列表
                        tableIns.reload();
                    }else{
                        //删除失败的提醒
                        layer.msg(data.msg);
                    }
                }
            });
        });
    }

    /**
     * 添加，更新的函数
     * @param saleChanceId
     */
    function openAddOrUpdateSaleChanceDialog(saleChanceId){
        var title="<h3>营销模块-添加</h3>";
        //添加修改 修改根据id修改
        var url=ctx+"/sale_chance/addOrUpdateSaleChancePage";
        //判断
        //非空即为真
        if(saleChanceId){
            title="<h3>营销模块-更新</h3>";
            url+="?saleChanceId="+saleChanceId;
        }

        /*弹出层*/
        layui.layer.open({
            title:title,
            type:2,
            content:url,
            area:["500px","620px"],
            maxmin:true
        })
    }

    /*绑定行内工具栏*/
    //监听行工具事件
    table.on('tool(saleChances)', function(obj){
        var data = obj.data;
        //console.log(obj)
        if(obj.event === 'del'){
            layer.confirm('真的删除行么', function(index){
                //关闭弹出层
                layer.close(index);
                //发送ajax删除
                $.ajax({
                    type:"post",
                    url:ctx+"/sale_chance/delete",
                    data:{"ids":data.id},
                    success:function (data){
                        if(data.code==200){
                            layer.msg("删除OK");
                            tableIns.reload();
                        }else{
                            layer.msg(data.msg);
                        }
                    }
                });
            });
        } else if(obj.event === 'edit'){
            //传入当前对象的id
            openAddOrUpdateSaleChanceDialog(data.id);
        }
    });


});
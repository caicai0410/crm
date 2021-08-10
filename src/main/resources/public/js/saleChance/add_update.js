layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /*添加表单添加信息*/
    form.on("submit(addOrUpdateSaleChance)",function(data){
        console.log(data.field);
        //提交的加载层
        var index=layer.msg("数据提交中，请等哈...",{
            icon:16,
            time:false,
            shade:0.8
        });
        //提交数据url
        var url=ctx+"/sale_chance/save";
        //判断，当前页面的隐藏域有数据，说明做修改操作
        if($("input[name='id']").val()){
            url=ctx+"/sale_chance/update";
        }
        //发送ajax添加
       $.post(url,data.field,function(data){
           if(data.code==200){
                //添加成功了
               layer.msg("添加成功");
               //关闭加载层
               layer.close(index);
               //iframe
               layer.closeAll("iframe");
               //重新加载
               parent.location.reload();
           }else{
               //失败了
               layer.msg(data.msg);
           }
       },"json");
        //阻止跳转
        return false;
    });

    /*下拉框 data装得查询到的销售信息 json格式*/
    /*发送ajax添加下拉框*/
    $(function(){
        /*发送ajax追加下拉框*/
        /*当前营销机会分配给具体人的ID*/
        var assignMan= $("input[name='man']").val();
        $.get(ctx+"/user/sales",function(data){
            //遍历
            for (var x in data) {
                if(assignMan==data[x].id){
                    $("#assignMan").append("<option value='"+data[x].id+"' selected>"+data[x].uname+"</option>");
                }else{
                    //点击修改返回显示
                    $("#assignMan").append("<option value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }
            }
            //重新渲染select
            layui.form.render("select");
        },"json");
    });
    /*取消*/
    $("#closeBtn").click(function (){
        //假设这是iframe页
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });
});
layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 修改用户 隐藏域
     * 选择元素绑定事件
     */

    //触发表单
    form.on("submit(saveBtn)",function(obj){
        console.log(obj.field+"<<<")
        $.ajax({
            type:"post",
            url:ctx+"/user/update",
            data:obj.field,
            dataType:"json",
            success:function (obj){
                if(obj.code ==200){
                    layer.msg("保存成功了",{icon:6});
                    //页面分发
                    parent.location.href=ctx+"/main";
                }else{
                    layer.msg(obj.msg,{icon:5 });
                }
            }
        });
        return false;
    });

});
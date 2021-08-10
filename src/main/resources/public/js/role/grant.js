var ztreeObj;
$(function(){
    loadModuleInfo();
});
function loadModuleInfo(){
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllModules",
        data:{"roleId":$("#roleId").val()},
        dataType:"json",
        success:function(data){
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                view:{
                    showLine: false
                    // showIcon: false
                },
                check: {
                    enable: true,
                    chkboxType: { "Y": "ps", "N": "ps" }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };

            var zNodes =data;
            ztreeObj= $.fn.zTree.init($("#test1"), setting, zNodes);
            //console.log(data);
        }
    });
}

function zTreeOnCheck(event, treeId, treeNode) {
    //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
    //获取角色的Id
    var roleId=$("#roleId").val();
    //获取所有的选中的节点对象
    var nodes = ztreeObj.getCheckedNodes(true);
    //所有模块的id
    var mids="mids=";
    //遍历nodes
    for (var i = 0; i < nodes.length; i++) {
        if(i<nodes.length-1){
            mids=mids+nodes[i].id+"&mids=";
        }else{
            mids=mids+nodes[i].id;
        }
    }
    //发送ajax添加，批量添加，授权
    $.ajax({
        type:"post",
        url:ctx+"/role/addGrand",
        data:mids+"&roleId="+roleId,
        dataType:"json",
        success:function(data){
            if(data.code==200){
                alert("授权成功了");
            }else{
                alert("授权失败了");
            }
        }
    });
};



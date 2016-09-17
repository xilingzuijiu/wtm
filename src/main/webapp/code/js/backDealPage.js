var approveHead = ' <tr class="list">' +
    '<td><label><input class="all" type="checkbox" value="全选"/>全选</label></td>' +
    '<td>提现方式</td>' +
    '<td>用户账号</td>' +
    '<td>提现时间</td>' +
    '<td>提现金额</td>' +
    '<td>处理状态</td>' +
    '<td>审批</td>' +
    '</tr>'
function getApproveList() {
    $.ajax({
        type: 'get',
        dataType: 'json',
        url: '/weitaomi/pc/admin/member/getInvitedCode',
        success: function (params) {
            var json = eval(params); //数组
            console.log("json数据为：" + params)
            if (json != null && json.errorCode == 0) {
                var data = json.data
                $("table:first").empty()
                var trs=dealGetApproveData(data)
                $("table:first").append(trs)
            } else {
                alert("获取列表失败")
            }
        }
    })
}

function dealGetApproveData(data){
    data.forEach(function (child) {

    })
}

function getApproveListTr(account,name,time,amount,isCheck) {
    var checkState='';
    if (isCheck==1){
        checkState='已审核'
    }else if(isCheck==0) {
        checkState='未审核'
    }
    var approveHead = '<tr class="list">' +
        '<td>' +
        '<label><input class="selectchild" type="checkbox" value=""/></label>' +
        '</td>' +
        '<td>'+account+'</td>' +
        '<td>'+name+'</td>' +
        '<td>'+time+'</td>' +
        '<td>'+amount+'</td>' +
        '<td>'+checkState+'</td>' +
        '<td class="audit">'+checkState+'</td>' +
        '</tr>'
    return approveHead
}
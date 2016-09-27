var approveHead = ' <tr class="list">' +
    '<td><label><input class="all" type="checkbox" value="全选"/>全选</label></td>' +
    '<td>提现方式</td>' +
    '<td>用户账号</td>' +
    '<td>用户ID</td>' +
    '<td>提现时间</td>' +
    '<td>提现金额</td>' +
    '<td>处理状态</td>' +
    '<td>审批</td>' +
    '</tr>'

var total=0;
function getApproveList(pageIndex,pageSize) {
    if (typeof pageIndex =='undefine' || pageIndex ==null){
        pageIndex=0
    }
    if (typeof pageSize =='undefine' || pageSize ==null){
        pageSize=0
    }
    $.ajax({
        type: 'get',
        dataType: 'json',
        url: '/pc/admin/paymemberCallBack/getApproveList',
        data: {pageIndex:pageIndex,pageSize:pageSize},
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
    total=data.total
    var elements=approveHead
    data.list.forEach(function (child) {
        elements  = elements + getApproveListTr(child.payType,child.accountName,child.createTime,child.amount,child.isPaid,child.memberId)
    })
    return elements
}

function getApproveListTr(account,name,time,amount,isCheck,memberId) {
    var checkState='';
    var payType='微信'
    if (account==1){
        payType='支付宝'
    }
    if (isCheck==1){
        checkState='已审核'
    }else if(isCheck==0) {
        checkState='未审核'
    }
    var approvecontent = '<tr class="list">' +
        '<td>' +
        '<label><input class="selectchild" type="checkbox" value=""/></label>' +
        '</td>' +
        '<td>'+payType+'</td>' +
        '<td>'+name+'</td>' +
        '<td>'+memberId+'</td>' +
        '<td>'+getLocalTime(time)+'</td>' +
        '<td>'+amount+'</td>' +
        '<td>'+checkState+'</td>' +
        '<td class="audit">'+checkState+'</td>' +
        '</tr>'
    return approvecontent
}



function getLocalTime(nS) {
    return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
}
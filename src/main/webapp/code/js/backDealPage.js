

var approveHead = ' <tr class="list" id="total">' +
    '<td>提现方式</td>' +
    '<td>用户账号</td>' +
    '<td>用户ID</td>' +
    '<td>提现时间</td>' +
    '<td>提现金额</td>' +
    '<td>处理意见</td>' +
    '<td>审批</td>' +
    '</tr>'

function getApproveList(pageIndex,pageSize,checkNum) {
    if (typeof pageIndex =='undefined' || pageIndex ==null){
        pageIndex=0
    }
    if (typeof pageSize =='undefined' || pageSize ==null){
        pageSize=0
    }
    //patchWechatCustomers
    $.ajax({
        type: 'get',
        dataType: 'json',
        async:false,
        url: '/pc/admin/paymemberCallBack/getApproveList',
        data: {pageIndex:pageIndex,pageSize:pageSize},
        success: function (params){
            var json = eval(params); //数组
            console.log("json数据为：" + params)
            if (json != null && json.errorCode == 0){
                var data = json.data;
                $("table:first").empty();
                var trs=dealGetApproveData(data);
                $("table:first").append(trs);
                console.log("总数是"+total);
                checkTotal=data.total
            } else {
                alert("获取列表失败")
            }
        }
    })
}


function dealGetApproveData(data){
    total=data.total;
    var elements=approveHead;
    data.list.forEach(function (child) {
        var id=child.id;
        elements  = elements + getApproveListTr(child.payType,child.accountName,child.createTime,child.amount,child.isPaid,child.memberId,child.id);
    });
    return elements
}

function getApproveListTr(account,name,time,amount,isCheck,memberId,listid) {
    var checkState='';
    var payType='微信';
    if (account==1){
        payType='支付宝'
    }
    if (isCheck==1){
        checkState='已审核'
    }else if(isCheck==0){
        checkState='拒绝'
    }
    var approvecontent = '<tr class="list">' +
    '<td>'+payType+'</td>' +
    '<td>'+name+'</td>' +
    '<td id="one">'+memberId+'</td>' +
    '<td>'+getLocalTime(time)+'</td>' +
    '<td>'+amount+'</td>' +
    '<td>'+name+'</td>' +
    '<td class="audit">'+
        '<select onchange="selectSubmit(this)" id="'+listid+'">'+
        '<option>'+'待审核'+'</option>'+
        '<option>'+'已审核'+'</option>'+
        '<option>'+'拒绝'+'</option>'+
        '</select>'+
        '</td>' +
    '</tr>'
    return approvecontent;
}
var selecount=0;
var selectSubmit=function(obj){
    selecount++;
    var id=obj.getAttribute("id");
    console.log("主体id是"+id);
    //var index=$("select").get(0).selectedIndex;
    var index=obj.selectedIndex;
    index=parseInt(index);
    console.log("index是"+index);
    console.log("selecount是"+selecount);
        if(index!=0){
            selecount--;
            console.log("点击次数减过后"+selecount);
            var view=prompt("请输入审核意见","通过");
            //$("this").parent().prev().empty();
            //$(".audit").prev().text(view);
            var a=obj.parentNode.previousElementSibling;
            a.innerHTML ="";
            a.innerHTML =view;
            if(view){
                var isCheck=0;
                switch (index){
                    case 1:
                        isCheck=1;
                        break;
                    case 2:
                        isCheck=0;
                }
                console.log("索引号是"+index);
                $.ajax({
                    type: 'post',
                    dataType: 'json',
                    url: '/pc/admin/paymemberCallBack/patchWechatCustomers',
                    data: {approveId:id,isApprove:isCheck,remark:view},
                    success: function (params){
                        var json = eval(params); //数组
                        console.log("json数据为：" + params)
                        if (json != null && json.errorCode == 0){
                            var data = json.data;
                            obj.style.color="green";
                        } else {
                            //$("this").parent().prev().empty();
                            var a=obj.parentNode.previousElementSibling;
                            a.innerHTML ="";
                            alert("获取数据失败");
                            //$(".audit select option:first").prop("selected", 'selected');
                            obj.options[0].selected = true;
                        }
                    },error:function(){
                        //$("this").parent().prev().empty();
                        var a=obj.parentNode.previousElementSibling;
                        a.innerHTML ="";
                        alert("审核失败，请重新审核");
                        //$(".audit select option:first").prop("selected", 'selected');
                        obj.options[0].selected = true;
                    }
                })
        }else{
                //$(".audit select option:first").prop("selected", 'selected');
                obj.options[0].selected = true;
            }
        }
};
function getLocalTime(nS) {
    return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
}
/**
 * Created by Administrator on 2016/10/14 0014.
 */
var total=0;
var pageSize=11;
var pageIndex=1;
$(function(){
    loadrecord();

})
console.log("count"+pageIndex);
$(window).scroll(function(){
    var scrollTop = $(this).scrollTop();               //滚动条距离顶部的高度
    var scrollHeight = $(document).height();                   //当前页面的总高度
    var windowHeight = $(this).height();                   //当前可视的页面高度
    if(scrollTop + windowHeight >= scrollHeight){        //距离顶部+当前高度 >=文档总高度 即代表滑动到底部
        console.log(Math.ceil(total/pageSize)+"shi");
        pageIndex++;
        if(pageIndex<=Math.ceil(total/pageSize)){
            loadrecord();
        }
        if(pageIndex==Math.ceil(total/pageSize)){
            var p = document.createElement('p');
            p.className="loadmore"
            p.innerHTML = "没有更多";
            $("body").append(p);
        }
    }else if(scrollTop<=0){         //滚动条距离顶部的高度小于等于0
//                    location.reload();
    }
});
function loadrecord(){
    var memberId= $.cookie("memberId");
    console.log("countsize"+pageSize);
    console.log("现在的页数是"+pageIndex);
    $.ajax({
        type: 'post',
        dataType: 'json',
        url: '/pc/admin/memberTask/getMemberTaskInfo',
        data:{memberId:memberId,pageIndex:pageIndex,pageSize:pageSize,type:1},
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success:function(params){
            var json=eval(params);
            if(json!=null&&json.errorCode==0){
                total=json.data.total;
                json.data.list.forEach(function (record) {
                    var li=document.createElement('li');
                    li.id=record.memberTaskHistoryDetailList[0].taskHistoryId;
                    li.setAttribute("onClick", "recordSubmit(this)");
                    var h6=document.createElement('h6');
                    h6.innerHTML=record.taskName;
                    var p3=document.createElement('p');
                    p3.innerHTML=record.pointCount;
                    li.appendChild(h6);
                    li.appendChild(p3);
                    document.body.appendChild(li);
                    document.getElementsByTagName('ul')[0].appendChild(li);
                })
            } else
            {
                alert("获取数据失败")
            }
        },error:function(data){
            alert("页面加载错误，请重试");
        }
    })
}
var recordSubmit=function(obj){
    var taskHistoryId=parseInt(obj.getAttribute("id"));
    console.log(taskHistoryId);
    $.cookie("taskHistoryId", taskHistoryId, {expires: 0.01, path: "/frontPage/wap/taskdetails.html"})
    location.href="/frontPage/wap/taskdetails.html";
}
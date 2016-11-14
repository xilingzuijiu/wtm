/**
 * Created by Administrator on 2016/10/14 0014.
 */
var total=0;
var pageSize=16;
var count=1;
$(function() {
    loadinviterecord();
})
$(window).scroll(function(){
    var scrollTop = $(this).scrollTop();
    var scrollHeight = $(document).height();
    var windowHeight = $(this).height();
    if(scrollTop + windowHeight >= scrollHeight){
        console.log(Math.ceil(total/pageSize)+"shi");
        if(count<Math.ceil(total/pageSize)){
            $(".loadmore").css("visibility","visible");
            $(".loadmore").click(function(){
                count++;
                $(this).css("visibility","hidden");
                loadinviterecord();
            })
        }
        if(count>=Math.ceil(total/pageSize)){
            $(".loadmore").css("display","none");
        }
    }else if(scrollTop<=0){
    }
});
function loadinviterecord(){
    console.log("count是"+count);
    console.log("pageSize是"+pageSize);
    $.ajax({
        type:'post',
        dataType:'json',
        url:'/pc/admin/memberInvited/getInvitedRecordList',
        data:{pageIndex:count,pageSize:pageSize},
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success:function(params) {
            var json = eval(params);
            if (json.data!= null && json.errorCode == 0) {
                total=json.data.length;
                console.log("总数就是"+total);
                json.data.forEach(function(invitelist) {
                    var li = document.createElement("li");
                    var img = document.createElement('img');
                    img.setAttribute("src",invitelist.imageUrl);
                    var h5 = document.createElement('h5');
                    h5.innerHTML = invitelist.memberName;
                    var p = document.createElement('p');
                    var timestamp = invitelist.invitedTime;
                    p.innerHTML = getLocalTime(timestamp);
                    li.appendChild(img);
                    li.appendChild(h5);
                    li.appendChild(p);
                    document.body.appendChild(li);
                    document.getElementsByTagName('ul')[0].appendChild(li);
                    $(".nullp").css("display","none");
                })
            } else {
                $(".nullp").css("display","block");
            }
        },
        error:function (data) {
            alert("页面加载错误，请重试");
        }
    })
}
function getLocalTime(nS) {
    return new Date(+new Date(parseInt(nS) * 1000)+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'')
}
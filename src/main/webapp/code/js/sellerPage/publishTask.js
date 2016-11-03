/**
 * Created by Administrator on 2016/9/30.
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};



function chooseProvince(value){
    var cityArea='<option value="">--全省--</option>';
//            var value = $('#prov').options[$('#prov').selectedIndex].;
    addressArr.forEach(function(province){
        if (province.province==value){
            var cityList=province.cityList;
            cityList.forEach(function(city){
                cityArea += '<option value="'+city.city+'">'+city.city+'</option>';
            })
        }
    })
    $('#city').empty();
    $('#city').append(cityArea);
    var rate =1.0
    if (value!=""){
        rate=1.2
    }
    var sex=$('input:radio:checked').val();
    if (typeof(sex)!='undefined' && sex!=0){
        rate=rate*1.2
    }
    var  singleScore = singleScoreTemp * rate;
    $("#singleScore").empty()
    var singleEle = ' <input type="text" class="form-control" onchange="changeScore(this.value)" name="singleScore" id="input1" readonly="true" value="' + singleScore.toFixed(2) + '"/>' +
        '<div class="input-group-addon">米币</div>'
    $("#singleScore").append(singleEle)
};


function chooseCity(value){
    var rate =1.2
    if (value!=""){
        rate =1.5
    }
    var sex=$('input:radio:checked').val();
    if (typeof(sex)!='undefined'&& sex!=0){
        rate=rate*1.2
    }
    var  singleScore = singleScoreTemp * rate;
    $("#singleScore").empty()
    var singleEle = ' <input type="text" class="form-control" onchange="changeScore(this.value)" name="singleScore" id="input1" readonly="true" value="' + singleScore.toFixed(2) + '"/>' +
        '<div class="input-group-addon">米币</div>'
    $("#singleScore").append(singleEle)

};


function changeScore(value){
    $("#div1").empty()
    $("#div1").append(value)
    var total = $("#amount").val();
    var number=$("#input2").val()
    if(eval(total-number*value)<0){
        $("#div1").empty()
        $("#input1").val(0)
        Showbo.Msg.alert("可用米币额度不足")
    }
};



function checkAmount(value){
    var total = $("#amount").val();
    var number=$("#input1").val()
    if(eval(total-number*value)<0){
        Showbo.Msg.alert("可用米币额度不足，请充值")
        $("#input2").val(0)
    }
    var i=1
    var eles='';
    if(self.location.pathname=='/frontPage/sellerPage/publishAccounts.html') {
        i=parseInt(value/1000)+1
        eles= '<option value="'+i+'">'+i+'</option>';
    }
    if(self.location.pathname=='/frontPage/sellerPage/publishArticle.html') {
        if (value<30000) {
            i=1
        }
        if (value>=30000&&value<50000){
            i=2
        }
        eles= '<option value="'+i+'">'+i+'</option>';
        if (value>=50000){
            i=3;
            var val=i+"+";
            eles= '<option value="'+i+'">'+val+'</option>';
        }
    }
    $("#select2").empty();
    $("#select2").append(eles)

};
var amount=0.00;
var singleScoreTemp=0.00
function deal(obj){
    var accountList=obj.officialAccountList;
    var elements='';
    accountList.forEach(function(account){
        amount=account.memberScore;
        singleScoreTemp=account.singleScore
        var element='<option value="'+account.id+'">'+account.ofiicialAccountName+'</option>';
        elements += element;
    })
    $("#select1").empty();
    $("#select1").append(elements)
    var eleTotal='  <input type="text" class="form-control" id="amount" readonly="true" value="'+amount+'"/>'+
        '<div class="input-group-addon">米币</div>'
    $("#total").empty()
    $("#total").append(eleTotal)
    if(self.location.pathname=='/frontPage/sellerPage/publishAccounts.html'){
        var provinces='<option value="">--全国--</option>';
        addressArr=obj.addressList;
        addressArr.forEach(function(province){
            provinces +='<option value="'+province.province+'">'+province.province+'</option>';
        })
        $("#prov").empty();
        $("#prov").append(provinces)
    }
    $("#singleScore").empty()
    var singleEle=' <input type="text" class="form-control" onchange="changeScore(this.value)" name="singleScore" id="input1" readonly="true" value="'+singleScoreTemp.toFixed(2)+'"/>'+
        '<div class="input-group-addon">米币</div>'
    $("#singleScore").append(singleEle)
};

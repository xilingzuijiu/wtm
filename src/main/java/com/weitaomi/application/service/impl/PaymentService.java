package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.MemberScoreFlowDto;
import com.weitaomi.application.model.enums.PayType;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.*;
import com.weitaomi.systemconfig.alipay.AlipayConfig;
import com.weitaomi.systemconfig.alipay.AlipayNotify;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import com.weitaomi.systemconfig.wechat.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by supumall on 2016/7/22.
 */
@Service
public class PaymentService implements IPaymentService {
    private Logger logger = LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private PaymentHistoryMapper paymentHistoryMapper;
    @Autowired
    private IPayStrategyContext payStrategyContext;
    @Autowired
    private MemberScoreMapper memberScoreMapper;
    @Autowired
    private MemberScoreFlowMapper memberScoreFlowMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private IMemberTaskHistoryService taskHistoryService;
    @Autowired
    private PaymentApproveMapper approveMapper;
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;
    @Autowired
    private MemberPayAccountsMapper memberPayAccountsMapper;
    @Autowired
    private MemberMapper memberMapper;
    private Lock lock = new ReentrantLock();
    private String certFilePath0;
    private String certFilePath1;

    /**
     * 初始化参数
     */
    @PostConstruct
    public void init() {
        String classPath = this.getClass().getResource("/").getPath();
        certFilePath0 = classPath + "properties/wechat/apiclient_cert.p12";
        certFilePath1 = classPath + "properties/wechat/apiclient_certwx.p12";
    }

    @Override
    @Transactional
    public String getPaymentParams(Map<String, Object> params) {
        String payCode = this.getTradeNo();
        if ((Integer) params.get("payType") == (PayType.WECHAT_APP.getValue())) {
            params.put("out_trade_no", AlipayConfig.payCode_prefix + payCode);
            params.put("sourceType", 0);
        }
        if ((Integer) params.get("payType") == (PayType.WECHAT_WEB.getValue())) {
            ThirdLogin thirdLogin = thirdLoginMapper.getThirdlogInDtoMemberId((Long) params.get("memberId"), 1);
            if (thirdLogin != null) {
                params.put("openId", thirdLogin.getOpenId());
            }
            params.put("out_trade_no", AlipayConfig.payCode_prefix + payCode);
            params.put("sourceType", 1);
        }
        if ((Integer) params.get("payType") == (PayType.WECHAT_PC.getValue())) {
            ThirdLogin thirdLogin = thirdLoginMapper.getThirdlogInDtoMemberId((Long) params.get("memberId"), 1);
            if (thirdLogin != null) {
                params.put("openId", thirdLogin.getOpenId());
            }
            params.put("out_trade_no", AlipayConfig.payCode_prefix + payCode);
            params.put("sourceType", 2);
        }
        if ((Integer) params.get("payType") == (PayType.ALIPAY_APP.getValue())) {
            params.put("trade_no", AlipayConfig.payCode_prefix + payCode);
        }
        String requestParam = payStrategyContext.getPaymentParams(params);
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setMemberId((Long) params.get("memberId"));
        paymentHistory.setPayCode(payCode);
        paymentHistory.setPlatform(PayType.getValue((Integer) params.get("payType")).getDesc());
        paymentHistory.setParams(requestParam);
        paymentHistory.setIsPaySuccess(0);
        paymentHistory.setPayType(0);
        paymentHistory.setCreateTime(DateUtils.getUnixTimestamp());
        paymentHistoryMapper.insertSelective(paymentHistory);
        if (!StringUtils.isEmpty(requestParam)) {
            return requestParam;
        }
        return null;
    }

    @Override
    public String verifyAlipayNotify(Map<String, String[]> requestParams) {
        logger.info("支付宝回调开始，参数为{}", JSON.toJSONString(requestParams));
        //商户订单号
        String out_trade_no = requestParams.get("out_trade_no")[0].toString();
        //支付宝交易号
        String total_amount = requestParams.get("total_fee")[0].toString();
        //交易状态
        String trade_status = requestParams.get("trade_status")[0].toString();
//        try {
//            logger.info("youwenti0");
//            out_trade_no = new String(requestParams.get("out_trade_no").get(0).toString().getBytes("ISO-8859-1"), "UTF-8");
//            logger.info("youwenti1");
//            total_amount = new String(requestParams.get("total_fee").get(0).toString().getBytes("ISO-8859-1"), "UTF-8");
//            logger.info("youwenti2");
//            trade_status = new String(requestParams.get("trade_status").get(0).toString().getBytes("ISO-8859-1"), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        logger.info("out_trade_no:{},total_amount:{},trade_status:{}", out_trade_no, total_amount, trade_status);
        if (trade_status.equals("TRADE_SUCCESS")) {
            logger.info("支付宝回调验证成功");
            PaymentHistory paymentHistory = paymentHistoryMapper.getPaymentHistory(out_trade_no.substring(3));
            if (paymentHistory != null) {
                paymentHistory.setIsPaySuccess(1);
                paymentHistory.setCreateTime(DateUtils.getUnixTimestamp());
                paymentHistoryMapper.updateByPrimaryKeySelective(paymentHistory);
                memberScoreService.addMemberScore(paymentHistory.getMemberId(), 1L, 1, Double.valueOf(total_amount) * 100, UUIDGenerator.generate());
                JpushUtils.buildRequest("米币充值到账", paymentHistory.getMemberId());
            }
            return "success";
        }
        return "fail";
    }

    @Override
    public String verifyWechatNotify(WechatNotifyParams wechatNotifyParams) {
        logger.info("微信支付回调开始{}", JSON.toJSONString(wechatNotifyParams));
        if (wechatNotifyParams != null) {
            if (wechatNotifyParams.getResult_code().equals(WechatConfig.SUCCESS) && wechatNotifyParams.getReturn_code().equals(WechatConfig.SUCCESS)) {
                Long amount = Long.valueOf(wechatNotifyParams.getTotal_fee());
                String trade_no = wechatNotifyParams.getOut_trade_no();
                PaymentHistory paymentHistory = paymentHistoryMapper.getPaymentHistory(trade_no.substring(3));
                if (paymentHistory == null) {
                    return this.getXMLString("FAIL", "查无此单");
                }
                paymentHistory.setIsPaySuccess(1);
                paymentHistory.setCreateTime(DateUtils.getUnixTimestamp());
                paymentHistoryMapper.updateByPrimaryKeySelective(paymentHistory);
                memberScoreService.addMemberScore(paymentHistory.getMemberId(), 1L, 1, Double.valueOf(amount), UUIDGenerator.generate());
                JpushUtils.buildRequest("米币充值到账", paymentHistory.getMemberId());
                return "SUCCESS";
            }
        }
        return "";
    }

    @Override
    public String verifyBatchPayNotify(Map requestParams) {
        String patchTradeNo = (String) requestParams.get("batch_no");
        String flag = cacheService.getCacheByKey(patchTradeNo, String.class);
        if (patchTradeNo.equals(flag)) {
            String fail_details = (String) requestParams.get("fail_details");
            if (StringUtils.isEmpty(fail_details)) {
                paymentHistoryMapper.batchUpdatePayHistory(patchTradeNo, DateUtils.getUnixTimestamp());
                return "success";
            } else {
                String success = (String) requestParams.get("success_details");
                String[] success_details = success.split("\\|");
                List<String> payCodeList = new ArrayList<String>();
                for (int i = 0; i < success_details.length; i++) {
                    payCodeList.add(success.substring(0, success.indexOf("^")));
                }
                paymentHistoryMapper.batchUpdatePayHistoryByTradeNo(payCodeList, DateUtils.getUnixTimestamp());
                return "success";
            }
        }
        return "fail";
    }

    @Override
    @Transactional
    public void patchAliPayCustomers(List<PaymentApprove> approveList) {
        if (approveList.isEmpty()) {
            throw new BusinessException("付款账户列表为空");
        }
        Map<String, String> params = new HashMap<String, String>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        StringBuffer detail_data = new StringBuffer();
        List<PaymentHistory> paymentHistoryList = new ArrayList<PaymentHistory>();
        for (PaymentApprove approve : approveList) {
            totalAmount = totalAmount.add(approve.getAmount());
            String trade_no = this.getTradeNo();
            detail_data.append(AlipayConfig.payCode_prefix + trade_no);
            if (!StringUtils.isEmpty(approve.getAccountNumber()) && !StringUtils.isEmpty(approve.getAccountName()) && approve.getAmount() != null) {
                detail_data.append("^").append(approve.getAccountNumber()).append("^").append(approve.getAccountName()).append("^").append(approve.getAmount());
            }
            if (!StringUtils.isEmpty(approve.getRemark())) {
                detail_data.append("^").append(approve.getRemark()).append("|");
            }
            PaymentHistory paymentHistory = new PaymentHistory();
            paymentHistory.setIsPaySuccess(0);
            paymentHistory.setPayType(1);
            paymentHistory.setMemberId(approve.getMemberId());
            paymentHistory.setPlatform(PayType.ALIPAY_WEB.getDesc());
            paymentHistory.setPayCode(trade_no);
            paymentHistory.setCreateTime(DateUtils.getUnixTimestamp());
            paymentHistoryList.add(paymentHistory);
        }
        params.put("batch_num", String.valueOf(approveList.size()));
        params.put("batch_fee", totalAmount.toString());
        params.put("pay_date", DateUtils.formatYYYY());
        params.put("account_name", AlipayConfig.seller_id);
        String batch_no = DateUtils.formatYYYYMMddHHmmssSSS();
        params.put("batch_no", AlipayConfig.payCode_prefix + batch_no);
        String detail = detail_data.substring(0, detail_data.length() - 1);
        params.put("detail_data", detail_data.toString());
        String result = payStrategyContext.getBatchPayParams(params, PayType.ALIPAY_APP.getValue());
        //TODO  处理result
        if (paymentHistoryList != null) {
            for (PaymentHistory paymentHistory : paymentHistoryList) {
                paymentHistory.setBatchPayNo(batch_no);
                paymentHistory.setParams(detail);
            }
            int num = paymentHistoryMapper.batchInsertPayHistory(paymentHistoryList);
            if (num > 0) {
                logger.info("支付请求成功");
            }
        }
        cacheService.setCacheByKey(batch_no, batch_no, 24 * 60 * 60);
    }

    @Override
    @Transactional
    public MemberScore generatorPayParams(Long memberId, PaymentApprove approve, Integer sourceType) {
        if (approve != null) {
            String minutesKey = "deposit:count:minutes:" + memberId;
            String dayKey = "deposit:count:day:" + memberId;
            Integer minuteNumber = cacheService.getCacheByKey(minutesKey, Integer.class);
            if (minuteNumber != null && minuteNumber == 1) {
                throw new InfoException("提现操作过于频繁，24小时内可提现三次，每次间隔需大于十分钟，请合并金额选择大额提现~");
            } else {
                cacheService.setCacheByKey(minutesKey, 1, 10 * 60);
            }
            Integer dayNumber = cacheService.getCacheByKey(dayKey, Integer.class);
            if (dayNumber != null && dayNumber > 0) {
                if (dayNumber >= 3) {
                    throw new InfoException("提现操作过于频繁，十分钟内可提现一次，24小时内可提现三次，请合并金额选择大额提现~");
                } else {
                    cacheService.increCacheBykey(dayKey, 1L);
                }
            } else {
                cacheService.setCacheByKey(dayKey, 1, 10 * 60);
            }
            MemberScore memberScore = memberScoreMapper.getMemberScoreByMemberId(memberId);
            if (memberScore == null) {
                throw new InfoException("没有可用提现金额");
            }
            if (memberScore.getAvaliableScore().subtract(memberScore.getRechargeCurrentScore()).doubleValue() < approve.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) {
                throw new InfoException("提现金额大于可提现金额");
            }
            if (StringUtil.isEmpty(approve.getAccountNumber()) || StringUtil.isEmpty(approve.getAccountName())) {
                ThirdLogin thirdLogin = thirdLoginMapper.getThirdlogInDtoMemberId(memberId, sourceType);
                if (thirdLogin == null) {
                    throw new InfoException("亲，抱歉没有获取到您的微信信息，请绑定微信或者联系客服人员吧");
                }
                approve.setAccountNumber(thirdLogin.getOpenId());
                approve.setAccountName(thirdLogin.getNickname());
            }
            approve.setMemberId(memberId);
            approve.setCreateTime(DateUtils.getUnixTimestamp());
            int number = approveMapper.insertSelective(approve);
            boolean flag = number > 0 ? true : false;
            if (flag) {
                memberScore.setInValidScore(approve.getAmount().multiply(BigDecimal.valueOf(100)).add(memberScore.getInValidScore()));
                memberScoreMapper.updateByPrimaryKeySelective(memberScore);
                return memberScoreService.addMemberScore(memberId, 2L, 0, -approve.getAmount().multiply(BigDecimal.valueOf(100)).doubleValue(), UUIDGenerator.generate());
            }
        }
        return null;
    }

    @Override
    public Page<MemberScoreFlowDto> getMemberWalletInfo(Long memberId, Integer paygeSize, Integer pageIndex) {
        List<MemberScoreFlowDto> myWalletDto = memberScoreMapper.getMyWalletDtoByMemberId(memberId, new RowBounds(pageIndex, paygeSize));
        if (myWalletDto == null) {
            return null;
        }
        PageInfo<MemberScoreFlowDto> showDtoPage = new PageInfo<MemberScoreFlowDto>(myWalletDto);
        return Page.trans(showDtoPage);
    }

    @Override
    public MemberPayAccounts savePayAccounts(Long memberId, Integer payType, String payAccount, String realName) {
        if (payAccount == null) {
            throw new InfoException("支付账号信息为空");
        }
        MemberPayAccounts payAccounts = new MemberPayAccounts();
        payAccounts.setMemberId(memberId);
        payAccounts.setPayType(payType);
        MemberPayAccounts memberPayAccounts = new MemberPayAccounts();
        memberPayAccounts.setMemberId(memberId);
        memberPayAccounts.setPayAccount(payAccount);
        memberPayAccounts.setPayType(payType);
        memberPayAccounts.setRealName(realName);
        memberPayAccounts.setCreateTime(DateUtils.getUnixTimestamp());
        List<MemberPayAccounts> memberPayAccountsList = memberPayAccountsMapper.select(payAccounts);
        if (memberPayAccountsList.isEmpty()) {
            int num = memberPayAccountsMapper.insertSelective(memberPayAccounts);
            return num > 0 ? memberPayAccounts : null;
        }
        if (memberPayAccountsList.size() > 1) {
            throw new InfoException("绑定微信或支付宝账户过多");
        }
        MemberPayAccounts pay = memberPayAccountsList.get(0);
        pay.setPayAccount(memberPayAccounts.getPayAccount());
        pay.setRealName(memberPayAccounts.getRealName());
        pay.setCreateTime(DateUtils.getUnixTimestamp());
        int number = memberPayAccountsMapper.updateByPrimaryKeySelective(pay);
        return number > 0 ? pay : null;
    }

    @Override
    public Page<PaymentApprove> getPaymentApproveList(Integer pageIndex, Integer pageSize) {
        List<PaymentApprove> paymentApproveList = approveMapper.getPaymentApproveList(new RowBounds(pageIndex, pageSize));
        PageInfo<PaymentApprove> pageInfo = new PageInfo<PaymentApprove>(paymentApproveList);
        return Page.trans(pageInfo);
    }

    private String getTradeNo() {
        try {
            lock.lock();
            String key = "wtm:orderCode:max";
            String payCode = cacheService.getCacheByKey(key, String.class);
            if (StringUtils.isEmpty(payCode)) {
                payCode = paymentHistoryMapper.getMaxPayCode();
                if (StringUtils.isEmpty(payCode)) {
                    payCode = "100000000000";
                    Long orderNumer = Long.valueOf(payCode) + 1;
                    cacheService.setCacheByKey(key, orderNumer.toString(), null);
                } else {
                    Long orderNumer = Long.valueOf(payCode) + 1;
                    cacheService.setCacheByKey(key, orderNumer.toString(), null);
                }
            } else {
                Long orderNumer = Long.valueOf(payCode) + 1;
                cacheService.setCacheByKey(key, orderNumer.toString(), null);
            }
            return payCode;
        } catch (Exception e) {
            throw new BusinessException("业务错误");
        } finally {
            lock.unlock();
        }
    }

    private String getXMLString(String code, String msg) {
        String value = "<xml>\n" +
                "  <return_code><![CDATA[" + code + "]]></return_code>\n" +
                "  <return_msg><![CDATA[" + msg + "]]></return_msg>\n" +
                "</xml>";
        return value;
    }
}

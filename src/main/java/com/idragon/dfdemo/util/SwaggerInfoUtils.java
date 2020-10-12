package com.idragon.dfdemo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * swagger 接口信息收集工具
 * @author chenxinjun
 */
public class SwaggerInfoUtils {

    private String url;

    private String modelName;

    private String[] ignoreTags=new String[]{"basic-error-controller","operation-handler","web-mvc-links-handler","server-controller"};


    public SwaggerInfoUtils(String url,String modelName) {
        this.url = url;
        this.modelName=modelName;
    }
    private String getTag(JSONObject data){
        return data.getJSONArray("tags").getString(0);
    }

    /**
     * 判断check是否合法
     * @param tag
     * @return
     */
    private boolean checkTag(String tag){
        if(StringUtils.isBlank(tag)){
            return false;
        }
        for(int i=0;i<ignoreTags.length;i++){
            if(ignoreTags[i].equalsIgnoreCase(tag)){
                return false;
            }
        }
        return true;
    }


    public JSONArray getMethods(){
        JSONArray ja=new JSONArray();
        JSONObject resutlJson= HttpRequestUtils.get(url);
        if(resutlJson!=null){
            JSONObject paths=resutlJson.getJSONObject("paths");
            Set<String> keys=paths.keySet();
            for(String key:keys){
                if(getIgnoreSet().contains(key)){
                    System.out.println("接口被忽略掉："+key);
                    continue;
                }
                JSONObject methodJson=new JSONObject();
                JSONObject data=paths.getJSONObject(key);

                if(data.containsKey("get")){
                    data=data.getJSONObject("get");
                }else if(data.containsKey("post")){
                    data=data.getJSONObject("post");
                }else{
                    continue;
                }
                String tag=getTag(data);
                if(checkTag(tag)){
                    // 添加接口信息
                    methodJson.put("url",key);
                    methodJson.put("name",data.getString("summary").replaceAll(",","||"));
                    methodJson.put("tag",tag);
                    methodJson.put("modelName",modelName);
                    ja.add(methodJson);
                }
            }
        }
        return ja;
    }

    public static String getToExcelStr(JSONArray methodList){
        StringBuffer sb=new StringBuffer();
        if(methodList!=null&&methodList.size()>0){
            for(int i=0;i<methodList.size();i++){
                sb.append(getMethodLine(methodList.getJSONObject(i))).append("\n");
            }
        }
        return sb.toString();
    }
    private static String getMethodLine(JSONObject item){
        StringBuffer sb=new StringBuffer(item.getString("modelName"));
        sb.append(",").append(item.getString("tag"));
        sb.append(",").append(item.getString("name"));
        sb.append(",").append(item.getString("url"));
        sb.append(",").append("http://fcm-uat.ocj.com.cn/api/newMedia/").append(item.getString("modelName")).append("/swagger-ui.html#").append(item.getString("tag"));
        sb.append(",").append("http://fcm-uat.ocj.com.cn").append(item.getString("url"));
        return sb.toString();
    }

    public static JSONArray getMethodByModelName(String modelName,String groupName){
        String path="http://fcm-uat.ocj.com.cn/api/newMedia/"+modelName+"/v2/api-docs";
        if(!StringUtils.isBlank(groupName)){
            path+="?group="+groupName;
        }
        SwaggerInfoUtils tools=new SwaggerInfoUtils(path,modelName);
        return tools.getMethods();
    }
    public static JSONArray getMethodByModelName(String modelName){
        return getMethodByModelName(modelName,null);
    }


    public static Set<String> ingoreMethodSet=null;

    /**
     * 获取过滤的方法
     * @return
     */
    public Set<String> getIgnoreSet(){
        if(ingoreMethodSet==null){
            ingoreMethodSet=new HashSet<>();
            String[] ignoreList = ignoreMethodAddress.split("\n");
            for(String item:ignoreList){
                if(!StringUtils.isBlank(item)){
                    ingoreMethodSet.add(item.trim());
                }
            }
        }
        return ingoreMethodSet;
    }
    public static void main(String[] args) throws IOException {
        JSONArray methodList=new JSONArray();
        methodList.addAll(getMethodByModelName("cms"));
        methodList.addAll(getMethodByModelName("login"));
        methodList.addAll(getMethodByModelName("trade"));
        methodList.addAll(getMethodByModelName("member"));
        methodList.addAll(getMethodByModelName("search"));
        methodList.addAll(getMethodByModelName("item"));
        methodList.addAll(getMethodByModelName("marketing"));
        FileUtils.writeFileContent(new File("/Users/chenxinjun/Downloads/fcmInterface.csv"),getToExcelStr(methodList),"GBK");
        System.out.println("=================================");
    }


    /**
     * 忽略的接口方法可以从表格中直接复制出来即可
     */
    private static  String ignoreMethodAddress="接口地址\n" +
            "/api/newMedia/cms/corp/member/show/nLogin\n" +
            "/api/newMedia/cms/adverty/posterParse\n" +
            "/api/newMedia/cms/bigData/lookAround\n" +
            "/api/newMedia/cms/activePage/queryChannelInfo\n" +
            "/api/newMedia/cms/activePage/queryGroupBuyingInfo\n" +
            "/commodity/category\n" +
            "/api/newMedia/cms/activePage/querySupplierPageInfo\n" +
            "/api/newMedia/cms/navigation/category\n" +
            "/api/newMedia/cms/search/bottomPictureApp\n" +
            "/api/newMedia/cms/urlConvert/targetUrlQuery\n" +
            "/api/newMedia/cms/queryTv/queryTvProgrammingNumber\n" +
            "/api/newMedia/cms/fixedPage/pageAllInfo\n" +
            "/api/newMedia/cms/link/genPosterQrCode\n" +
            "/api/newMedia/cms/personCenter/memberSpecial\n" +
            "/api/newMedia/cms/tvProgramming/employeeList\n" +
            "/api/newMedia/cms/navigation/categorybycms\n" +
            "/api/newMedia/cms/schedule/queryItemSchedule\n" +
            "/commodity/category1\n" +
            "/api/newMedia/cms/activePage/queryFootMenuInfo\n" +
            "/api/newMedia/cms/navigation/brandById\n" +
            "/api/newMedia/cms/queryTv/queryTvProgrammingByEmpolyeeNo\n" +
            "/api/newMedia/cms/fixedPage/pageAllInfoByPackage\n" +
            "/api/newMedia/cms/bigData/banItem\n" +
            "/api/newMedia/cms/urlConvert/shortUrl\n" +
            "/api/newMedia/cms/activePage/queryLiveTvInfo\n" +
            "/api/newMedia/cms/search/initItemSearch\n" +
            "/api/newMedia/cms/bigData/suggertForYou\n" +
            "/d/{shortUrlCode}\n" +
            "/api/newMedia/cms/activePage/querySupplierInfo\n" +
            "/api/newMedia/search/cmsConfigItemQuery\n" +
            "/admin/se_ads/parseandforward\n" +
            "/api/newMedia/cms/queryTv/queryTvProgrammingByDate\n" +
            "/api/newMedia/cms/fixedPage/navBarPageInfo\n" +
            "/commodity/brand\n" +
            "/api/newMedia/cms/memberSpecial/goods\n" +
            "/api/newMedia/cms/navigation/miniProgram\n" +
            "/api/newMedia/cms/fixedPage/queryPageComponent\n" +
            "/api/newMedia/cms/personCenter/newPeople\n" +
            "/api/newMedia/cms/buy/in/mall/nLogin\n" +
            "/api/newMedia/cms/search/initStartPageApp\n" +
            "/api/newMedia/cms/personCenter/staffShopping\n" +
            "/api/newMedia/cms/switch/getActivitySwitch\n" +
            "/api/newMedia/cms/bigData/SuggertForShoppingCart\n" +
            "/api/newMedia/cms/tvInfo/queryTvProgramming\n" +
            "/api/newMedia/cms/queryTv/queryHotTvProgramming\n" +
            "/api/newMedia/cms/queryTv/queryTvProgrammingForThree\n" +
            "/api/newMedia/cms/search/keywordsPageApp\n" +
            "/d/{shortCode}\n" +
            "/api/newMedia/cms/activePage/queryGlobalTradeInfo\n" +
            "/api/newMedia/cms/commodity/brand\n" +
            "/api/newMedia/cms/commodity/getOneCategory\n" +
            "/api/newMedia/login/login/bindPhone\n" +
            "/api/newMedia/login/login/refreshToken/nLogin\n" +
            "/api/newMedia/login/whiteList/getWhiteList\n" +
            "/api/newMedia/login/pwd/sendMailCode\n" +
            "/api/newMedia/login/login/thirdAccounts/alipay_login\n" +
            "/api/newMedia/login/login/thirdAccounts/weibo_login\n" +
            "/api/newMedia/login/pwd/reSetPasswordMailCheck\n" +
            "/api/newMedia/login/gmall/gmallBindOne\n" +
            "/api/newMedia/login/mini/login/securityCode\n" +
            "/api/newMedia/login/switch/getSwitch\n" +
            "/api/newMedia/login/checking/token\n" +
            "/api/newMedia/login/logout/logout\n" +
            "/api/newMedia/login/accountCheck/validUid\n" +
            "/api/newMedia/login/customers/fieldCheck\n" +
            "/api/newMedia/login/login/bindAndLogin\n" +
            "/api/newMedia/login/pwd/reSetPasswordCheck\n" +
            "/api/newMedia/login/gmall/gmallBindThree\n" +
            "/api/newMedia/login/visitor/getToken\n" +
            "/api/newMedia/login/gmall/gmallBindTwo\n" +
            "/api/newMedia/login/login/securityCode\n" +
            "/api/newMedia/login/login/thirdAccounts/weixin_login\n" +
            "/login/testToken\n" +
            "/api/newMedia/login/qrcode/weixin\n" +
            "/api/newMedia/login/login/thirdAccounts/appaliLogin\n" +
            "/api/newMedia/login/ocjCaptchaImg/valid\n" +
            "/api/newMedia/login/pwd/reSetPassword\n" +
            "/api/newMedia/login/qrcode/alipay\n" +
            "/api/newMedia/login/gmall/login\n" +
            "/api/newMedia/login/login/thirdAccounts/apple_login\n" +
            "/api/newMedia/login/login/getOpenId/nLogin\n" +
            "/api/newMedia/login/login/bindPhoneValidSmsCode\n" +
            "/api/newMedia/login/miniProgram/getOpenId\n" +
            "/api/newMedia/login/login/thirdAccounts/qq_login\n" +
            "/api/newMedia/login/accountCheck/validTel\n" +
            "/api/newMedia/login/login\n" +
            "/api/newMedia/login/accountCheck/validAccount\n" +
            "/api/newMedia/login/qrcode/qq\n" +
            "/api/newMedia/login/register/register\n" +
            "/api/newMedia/login/app/accountCheck/validAccount\n" +
            "/api/newMedia/login/ocjCaptchaImg/cal\n" +
            "/api/newMedia/login/qrcode/weibo\n" +
            "/api/newMedia/login/login/silence/login\n" +
            "/api/newMedia/trade/order/createOrder/nLogin\n" +
            "/api/newMedia/trade/pay/app/toPayApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryLogistics/nLogin\n" +
            "/api/newMedia/trade/shopCart/deleteCommodity/nLogin\n" +
            "/api/newMedia/trade/orders/invoices/trade/list/state/nLogin\n" +
            "/api/newMedia/trade/shopCart/receiveCoupon/nLogin\n" +
            "/api/newMedia/trade/shopCart/addCartRestCommand/nLogin\n" +
            "/api/newMedia/trade/orderCenter/goShopListPage/nLogin\n" +
            "/api/newMedia/trade/shopCart/clearCart/nLogin\n" +
            "/api/newMedia/trade/shopCart/cartRestQuery/nLogin\n" +
            "/api/newMedia/trade/app/orderCenter/deleteOrder/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryOrderDetails/nLogin\n" +
            "/api/newMedia/trade/orderCenter/reversalExpressList/nLogin\n" +
            "/api/newMedia/trade/orders/invoices/trade/invoice/list/pc/nLogin\n" +
            "/api/newMedia/trade/order/cashierShow/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryReversalOrderList/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryOrderQuantity/nLogin\n" +
            "/api/newMedia/trade/orders/logistic/detail/pc/nLogin\n" +
            "/api/newMedia/trade/orderCenter/cancelOrderPayBefore/nLogin\n" +
            "/api/newMedia/trade/orders/invoices/state/nLogin\n" +
            "/api/newMedia/trade/shopCart/getItemSkuById/nLogin\n" +
            "/api/newMedia/trade/shopCart/queryWarehouseForItem\n" +
            "/api/newMedia/trade/shopCart/updateCart/nLogin\n" +
            "/api/newMedia/trade/order/app/confirmOrderInfoApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryAppLogistics/nLogin\n" +
            "/api/newMedia/trade/order/app/createOrderApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/evaluationSubmission/nLogin\n" +
            "/api/newMedia/trade/orderCenter/initReturnReversal/nLogin\n" +
            "/api/newMedia/trade/order/goAddressPage/nLogin\n" +
            "/api/newMedia/trade/order/confirmOrderInfo/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryReversalReasonApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/createAPPReturnReversalApp/nLogin\n" +
            "/api/newMedia/trade/shopCart/queryCartItemQuantity\n" +
            "/api/newMedia/trade/shopCart/addFavorites/nLogin\n" +
            "/api/newMedia/trade/orders/invoices/trade/state/nLogin\n" +
            "/api/newMedia/trade/orderCenter/createExchangeReversal/nLogin\n" +
            "/api/newMedia/trade/orderCenter/createReturnReversal/nLogin\n" +
            "/api/newMedia/trade/orderCenter/orderListQuery/nLogin\n" +
            "/api/newMedia/trade/order/uploadPictureFilesApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/orderListOldQuery/nLogin\n" +
            "/api/newMedia/trade/orderCenter/cancelOrderPayAfter/nLogin\n" +
            "/api/newMedia/trade/order/app/confirmReservationOrderInfoApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/AppInvoiceIssueApp\n" +
            "/api/newMedia/trade/orderCenter/batchQueryOrderList/nLogin\n" +
            "/api/newMedia/trade/pay/payMethodsQuery/nLogin\n" +
            "/api/newMedia/trade/orderCenter/reversalDefaultAddress/nLogin\n" +
            "/api/newMedia/trade/orderCenter/InvoiceIssue\n" +
            "/api/newMedia/trade/orderCenter/cashOnDelivery\n" +
            "/api/newMedia/trade/shopCart/settlementCart/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryReversalOrderDetails/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryCouponStatusApp/nLogin\n" +
            "/api/newMedia/trade/order/getNewPriceByUserCouponApp/nLogin\n" +
            "/api/newMedia/trade/orders/validate/invoices/nLogin\n" +
            "/api/newMedia/trade/orderCenter/submitCommend/nLogin\n" +
            "/api/newMedia/trade/pay/checkorderpaystatusApp/nLogin\n" +
            "/api/newMedia/trade/pay/toPay/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryOrderDetailsOld/nLogin\n" +
            "/api/newMedia/trade/orders/invoices/apply/nLogin\n" +
            "/api/newMedia/trade/order/app/cashierShowApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/cancelOrderCause/nLogin\n" +
            "/api/newMedia/trade/orderCenter/cancelOrderOld/nLogin\n" +
            "/api/newMedia/trade/orderCenter/goCommentPage/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryInvoiceListByOrder\n" +
            "/api/newMedia/trade/shopCart/getItemSkuStock/nLogin\n" +
            "/api/newMedia/trade/orders/logistic/detail/nLogin\n" +
            "/api/newMedia/trade/pay/loopPayQuery/nLogin\n" +
            "/api/newMedia/trade/shopCart/calculatePrice/nLogin\n" +
            "/api/newMedia/trade/orderCenter/app/recentOrdersLogistics/nLogin\n" +
            "/api/newMedia/trade/shopCart/forURecommend\n" +
            "/api/newMedia/trade/orderCenter/updateReversalExpressNo/nLogin\n" +
            "/api/newMedia/trade/orderCenter/couponRealizeApp/nLogin\n" +
            "/api/newMedia/trade/order/uploadIDCardPictureFileApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/app/appointmentToOrder/nLogin\n" +
            "/api/newMedia/trade/orderCenter/cancelReversalAppointment/nLogin\n" +
            "/api/newMedia/trade/orderCenter/evaluateListQuery/nLogin\n" +
            "/api/newMedia/trade/order/uploadPictureFile/nLogin\n" +
            "/api/newMedia/trade/orderCenter/deleteOrder/nLogin\n" +
            "/api/newMedia/trade/orderCenter/initExchangeReversal/nLogin\n" +
            "/api/newMedia/trade/orderCenter/popUpStyle\n" +
            "/api/newMedia/trade/orderCenter/appCancelReversalAppointment/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryAppReversalOrderDetailsApp/nLogin\n" +
            "/api/newMedia/trade/orderCenter/appointmentToOrder/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryAppInvoiceListByOrderApp\n" +
            "/api/newMedia/trade/orders/invoices/trade/list/state/app/nLogin\n" +
            "/api/newMedia/trade/orderCenter/queryAppReversalOrderListApp/nLogin\n" +
            "/api/newMedia/member/customer/tosCredit\n" +
            "/api/newMedia/member/message/funds/nLogin\n" +
            "/api/newMedia/member/setting/weChatGift\n" +
            "/api/newMedia/member/corp/member/nLogin\n" +
            "/api/newMedia/member/invoice/previewInvoice\n" +
            "/api/newMedia/member/message/notices/nLogin\n" +
            "/api/newMedia/member/signIn/opointsCheckIn/nLogin\n" +
            "/api/newMedia/member/advance/depositsStrategy\n" +
            "/api/newMedia/member/customer/qualityRules\n" +
            "/api/newMedia/member/message/winPrice/nLogin\n" +
            "/api/newMedia/member/personal/giftRule\n" +
            "/api/newMedia/member/setting/report\n" +
            "/api/newMedia/member/signIn/otteryRuleUrl/nLogin\n" +
            "/api/newMedia/member/setting/tosRules\n" +
            "/api/newMedia/member/personal/footprintY\n" +
            "/api/newMedia/member/setting/website\n" +
            "/api/newMedia/member/personal/welfares\n" +
            "/api/newMedia/member/collect/productFavorites/nLogin\n" +
            "/api/newMedia/member/customer/enrollactivity/nLogin\n" +
            "/api/newMedia/member/message/updateStatus/nLogin\n" +
            "/api/newMedia/member/signIn/signDetails\n" +
            "/api/newMedia/member/personal/birthday/nLogin\n" +
            "/api/newMedia/member/message/notice\n" +
            "/api/newMedia/member/opoints/opointsDetailPark\n" +
            "/api/newMedia/member/setting/deleteReceiverY/nLogin\n" +
            "/api/newMedia/member/invoice/invoiceInformation\n" +
            "/api/newMedia/member/opoints/details/nLogin\n" +
            "/api/newMedia/member/welfare/activityCustomer\n" +
            "/api/newMedia/member/customer/joinAddingView\n" +
            "/api/newMedia/member/vouchers/couponsDetailsY/nLogin\n" +
            "/api/newMedia/member/personal/oclubNewMember\n" +
            "/api/newMedia/member/integral/orderRolling\n" +
            "/api/newMedia/member/advance/leftdeposit\n" +
            "/api/newMedia/member/message/singleActivityNotice/nLogin\n" +
            "/invoiceConfirmation\n" +
            "/api/newMedia/member/setting/changePasswd\n" +
            "/api/newMedia/member/vouchers/leftCustCoupon\n" +
            "/api/newMedia/member/customer/shoppingGuide\n" +
            "/api/newMedia/member/vouchers/couponsDetails/nLogin\n" +
            "/api/newMedia/member/customer/deliveryGuide\n" +
            "/api/newMedia/member/message/activityNotices/nLogin\n" +
            "/api/newMedia/member/customer/usedreportwrite/nLogin\n" +
            "/api/newMedia/member/giftBig/leftExchangeAmt\n" +
            "/api/newMedia/member/setting/receiverMgrListY/nLogin\n" +
            "/api/newMedia/member/collect/productFavoritesY/nLogin\n" +
            "/api/newMedia/member/opoints/opointsDetailsPark\n" +
            "/api/newMedia/member/giftBig/itemsCategory\n" +
            "/api/newMedia/member/integral/leftsaveamt/nLogin\n" +
            "/api/newMedia/member/vouchers/taocouponsexchange/nLogin\n" +
            "/api/newMedia/member/customer/searchCondition\n" +
            "/api/newMedia/member/mock/update\n" +
            "/api/newMedia/member/feedback/feedback/nLogin\n" +
            "/api/newMedia/member/integrate/getIntegrateSwitch\n" +
            "/api/newMedia/member/invoice/specialInvoiceMerge\n" +
            "/api/newMedia/member/setting/returnReceiverMgrList/nLogin\n" +
            "/api/newMedia/member/welfare/tryoutItemCode\n" +
            "/api/newMedia/member/customer/tosRules\n" +
            "/api/newMedia/member/opoints/exchange/nLogin\n" +
            "/api/newMedia/member/invoice/editInvoiceTitle/nLogin\n" +
            "/api/newMedia/member/collect/favorites/nLogin\n" +
            "/api/newMedia/member/discount/activityProducts\n" +
            "/api/newMedia/member/customer/showCancel\n" +
            "/api/newMedia/member/message/messagesCount\n" +
            "/api/newMedia/member/bill/mybillCategoryAnalysis/nLogin\n" +
            "/api/newMedia/member/personal/orderWallet\n" +
            "/api/newMedia/member/accounts/exchangecouponsPC/nLogin\n" +
            "/api/newMedia/member/personal/footprint\n" +
            "/api/newMedia/member/personal/myPrivilege\n" +
            "/api/newMedia/member/personal/orderWalletY\n" +
            "/api/newMedia/member/welfare/activityCustomerDetail\n" +
            "/api/newMedia/member/message/messages\n" +
            "/api/newMedia/member/personal/password/nLogin\n" +
            "/api/newMedia/member/setting/tosPrivacy\n" +
            "/api/newMedia/member/welfare/activityQuestionCommit\n" +
            "/api/newMedia/member/collect/favoritesY/nLogin\n" +
            "/api/newMedia/member/welfare/reportSubmit\n" +
            "/api/newMedia/member/integral/listSaveamtDetails/nLogin\n" +
            "/api/newMedia/member/personal/personalEvaluationY\n" +
            "/api/newMedia/member/discount/activityOrders\n" +
            "/api/newMedia/member/setting/receiverMgrList/nLogin\n" +
            "/api/newMedia/member/welfare/activityCustomerQuestion\n" +
            "/api/newMedia/member/giftBig/giftcardsDetails/nLogin\n" +
            "/api/newMedia/member/setting/changePhone/nLogin\n" +
            "/api/newMedia/member/personal/profileY/nLogin\n" +
            "/api/newMedia/member/setting/deleteReceiver/nLogin\n" +
            "/api/newMedia/member/setting/editReceiverY/nLogin\n" +
            "/api/newMedia/member/activity/activityResult/nLogin\n" +
            "/api/newMedia/member/customer/messages\n" +
            "/api/newMedia/member/setting/settingDefault/nLogin\n" +
            "/api/newMedia/member/onlineService/popIccUrl/nLogin\n" +
            "/api/newMedia/member/opoints/opointsDetails\n" +
            "/api/newMedia/member/giftBig/giftcardsStrategy\n" +
            "/api/newMedia/member/bill/mybillDetails/nLogin\n" +
            "/api/newMedia/member/discount/schedule\n" +
            "/api/newMedia/member/invoice/invoiceTitle/nLogin\n" +
            "/api/newMedia/member/personal/changeEmail\n" +
            "/api/newMedia/member/onlineService/popIccUrlY/nLogin\n" +
            "/api/newMedia/member/setting/certificate\n" +
            "/api/newMedia/member/message/news\n" +
            "/api/newMedia/member/personalPC/nickName/nLogin\n" +
            "/api/newMedia/member/activity/nearlyz_activity\n" +
            "/api/newMedia/member/integral/saveamtStrategy\n" +
            "/api/newMedia/member/accounts/prepaylistPC/nLogin\n" +
            "/api/newMedia/member/signIn/anchorList/nLogin\n" +
            "/api/newMedia/member/personal/customerHome/nLogin\n" +
            "/api/newMedia/member/setting/addReceiver/nLogin\n" +
            "/api/newMedia/member/vouchers/taocouponsDetails\n" +
            "/api/newMedia/member/personal/profile/nLogin\n" +
            "/api/newMedia/member/customer/payments\n" +
            "/api/newMedia/member/personal/email/nLogin\n" +
            "/api/newMedia/member/setting/editReceiver/nLogin\n" +
            "/api/newMedia/member/welfare/tryoutSubmitOnly\n" +
            "/api/newMedia/member/signIn/fullSign\n" +
            "/api/newMedia/member/message/clearUnread/nLogin\n" +
            "/api/newMedia/member/giftBig/giftcardsExchange\n" +
            "/api/newMedia/member/giftBig/leftgiftcard/nLogin\n" +
            "/api/newMedia/member/setting/addReceiverY/nLogin\n" +
            "/api/newMedia/member/vouchers/drawcoupon\n" +
            "/api/newMedia/member/questionnaire/questionnaireList/nLogin\n" +
            "/api/newMedia/member/personal/party\n" +
            "/api/newMedia/member/personal/defaultAddress/nLogin\n" +
            "/api/newMedia/member/questionnaire/questionnaireCommit/nLogin\n" +
            "/api/newMedia/member/accounts/couponslistPC/nLogin\n" +
            "/api/newMedia/member/customer/tosPolicy\n" +
            "/api/newMedia/member/feedback/feedbacklist/nLogin\n" +
            "/api/newMedia/member/personal/nickName/nLogin\n" +
            "/api/newMedia/member/bill/mybillCategoryList/nLogin\n" +
            "/api/newMedia/member/advance/depositsDetails/nLogin\n" +
            "/api/newMedia/member/invoice/addInvoiceTitle/nLogin\n" +
            "/api/newMedia/member/opoints/leftsaveamt/nLogin\n" +
            "/api/newMedia/member/personal/privilege\n" +
            "/api/newMedia/member/customer/igroup\n" +
            "/api/newMedia/member/personal/personalEvaluation\n" +
            "/api/newMedia/member/personal/moblieOclubFamilyDayList\n" +
            "/api/newMedia/member/welfare/tryoutList\n" +
            "/api/newMedia/member/customer/showReturn\n" +
            "/api/newMedia/member/personalPC/customerHomePC/nLogin\n" +
            "/api/newMedia/member/message/fundMessageType/nLogin\n" +
            "/api/newMedia/member/questionnaire/questionnaire/nLogin\n" +
            "/api/newMedia/member/vouchers/couponsStrategy\n" +
            "/api/newMedia/member/customer/showService\n" +
            "/api/newMedia/member/invoice/deleteInvoiceTitle/nLogin\n" +
            "/api/newMedia/member/signIn/signMcontent/nLogin\n" +
            "/api/newMedia/member/vouchers/giftticketsExchange/nLogin\n" +
            "/api/newMedia/member/customer/tosPrivacy\n" +
            "/api/newMedia/member/personal/userfootprintsDel\n" +
            "/api/newMedia/search/fixedPage/pageAllInfoTest\n" +
            "/api/newMedia/search/searchGroup\n" +
            "/api/newMedia/search/searchQueryByCategoryId\n" +
            "/api/newMedia/search/searchGroupWithPage\n" +
            "/api/newMedia/search/suggestByKeyword\n" +
            "/api/newMedia/search/cmsConfigItemQuery\n" +
            "/api/newMedia/search/searchQuery\n" +
            "/api/newMedia/item/queryCallCfgList\n" +
            "/api/newMedia/item/itemStoreByStoreIds\n" +
            "/api/newMedia/item/item/queryMoreItem\n" +
            "/api/newMedia/item/arrivalReminder/deleteArrivalReminderApp/nLogin\n" +
            "/api/newMedia/item/itemDetailQuery\n" +
            "/api/newMedia/item/comments/getCommentList\n" +
            "/api/newMedia/item/item/obtainCoupon\n" +
            "/api/newMedia/item/getNewItemRank\n" +
            "/api/newMedia/item/favorites/deleteFavoriteApp/nLogin\n" +
            "/api/newMedia/item/activitys/getItemEventsApp\n" +
            "/api/newMedia/item/comments/commentTotalApp\n" +
            "/api/newMedia/item/userfootprints/addUserFootprints\n" +
            "/api/newMedia/item/activitys/getItemCouponsApp\n" +
            "/api/newMedia/item/getHotItemRank\n" +
            "/api/newMedia/item/selectSku/itemDiscounts\n" +
            "/api/newMedia/item/selectSku/goldPriceChange\n" +
            "/api/newMedia/item/favorites/addFavorite/nLogin\n" +
            "/api/newMedia/item/initcolorsize\n" +
            "/api/newMedia/item/popIcc/getSameItemList\n" +
            "/api/newMedia/item/favorites/addShopFavoriteApp/nLogin\n" +
            "/api/newMedia/item/stock/getItemSalableStockApp\n" +
            "/api/newMedia/item/stock/getStockLogisticsInfoApp\n" +
            "/api/newMedia/item/arrivalReminder/addArrivalReminder/nLogin\n" +
            "/api/newMedia/item/getReturnInformationApp\n" +
            "/api/newMedia/item/arrivalReminder/addArrivalReminderApp/nLogin\n" +
            "/api/newMedia/item/queryMoreItem\n" +
            "/api/newMedia/item/insuranceRegister\n" +
            "/api/newMedia/item/item/queryAttributesByCode\n" +
            "/api/newMedia/item/comments/getCommentListApp\n" +
            "/api/newMedia/item/stock/getItemSkuStock\n" +
            "/api/newMedia/item/favorites/deleteFavorite/nLogin\n" +
            "/api/newMedia/item/detailImageContentApp\n" +
            "/api/newMedia/item/popIcc/getPopIccUrl\n" +
            "/api/newMedia/item/staff/queryIsStaff\n" +
            "/api/newMedia/item/stock/getStockLogisticsInfo\n" +
            "/api/newMedia/item/getSalesItemRank\n" +
            "/api/newMedia/item/comments/commentTotal\n" +
            "/api/newMedia/item/county\n" +
            "/api/newMedia/item/favorites/addFavoriteApp/nLogin\n" +
            "/api/newMedia/item/userfootprints/addUserFootprintsApp/nLogin\n" +
            "/api/newMedia/item/otherAndSimilarApp\n" +
            "/api/newMedia/item/barterSkuColorSize\n" +
            "/api/newMedia/item/stock/getItemSalableStock\n" +
            "/api/newMedia/item/getBrandItemRank\n" +
            "/api/newMedia/item/favorites/deleteShopFavoriteApp/nLogin\n" +
            "/api/newMedia/item/arrivalReminder/deleteArrivalReminder/nLogin\n" +
            "/api/newMedia/item/areaGroup/regionQueryById\n" +
            "/api/newMedia/item/item/newQueryMoreItem\n" +
            "/api/newMedia/item/itemDetailQueryApp\n" +
            "/api/newMedia/item/areaGroup/getAreaGroup\n" +
            "/api/newMedia/item/detailInstructionApp\n" +
            "/api/newMedia/item/city\n" +
            "/api/newMedia/marketing/giftFullPiece/activityRemind/nLogin\n" +
            "/api/newMedia/marketing/giftFullPiece/giftConfirm/nLogin\n" +
            "/api/newMedia/marketing/giftcards/custSignGiftSearchApp/nLogin\n" +
            "/api/newMedia/marketing/giftFullPiece/queryGiftifProgress/nLogin\n" +
            "/api/newMedia/marketing/giftFullPiece/queryGiftifProgressHistory/nLogin\n" +
            "/api/newMedia/marketing/giftFullPiece/queryGiftifIndex/nLogin\n" +
            "/api/newMedia/marketing/giftcards/downOrderInfo/nLogin\n" +
            "/api/newMedia/marketing/giftcards/queryCardBalance/nLogin\n" +
            "/api/newMedia/marketing/giftcards/custSignGiftSearch/nLogin\n" +
            "/api/newMedia/marketing/giftcards/custSignGiftGetApp/nLogin\n" +
            "/api/newMedia/marketing/giftcards/custSignGiftGet/nLogin\n" +
            "/api/newMedia/marketing/giftFullPiece/queryOrderList/nLogin\n" +
            "/api/newMedia/marketing/giftcards/queryOrderList/nLogin\n" +
            "/api/newMedia/marketing/giftFullPiece/queryGiftifCampaignItem/nLogin\n" +
            "/api/newMedia/marketing/giftcards/recharge/nLogin\n" +
            "/api/newMedia/member/message/newsForPc\n" +
            "/api/newMedia/member/message/singleActivityNoticeForPc/nLogin\n" +
            "/api/newMedia/member/message/imitateSelect\n" +
            "/api/newMedia/member/message/newsForPc\n" +
            "/api/newMedia/member/personalPC/groupName/nLogin\n" +
            "/api/newMedia/member/personalPC/groupQueryName/nLogin\n" +
            "/api/newMedia/marketing/giftFullPiece/queryGiftifRanking/nLogin\n" +
            "/api/newMedia/member/gift/newGiftPage/nLogin\n" +
            "/api/newMedia/cms/activity/activityOpen";
}

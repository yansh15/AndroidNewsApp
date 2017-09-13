package com.java.group19.helper;

import android.content.Context;

import com.java.group19.R;
import com.java.group19.data.News;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Lzl on 2017/9/13.
 */

public class ShareHelper {
    private static OnekeyShare oks = new OnekeyShare();

    public static void showShare(Context context, News news) {
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(news.getTitle());
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(news.getUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我刚刚看了这条新闻，很有意思，你也来看看吧！");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        if (news.getPictures() != null && news.getPictures().size() > 0)
            oks.setImageUrl(news.getPictures().get(0));
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(news.getUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我刚刚看了这条新闻，很有意思，你也来看看吧！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(context);

    }
}

package com.droidrtc.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.droidrtc.R;

public class EmoticonParser {
    private static EmoticonParser sInstance = null;
    public static EmoticonParser getInstance() { return sInstance; }
    public static void init(Context context) {
    	if (sInstance == null)
    		sInstance = new EmoticonParser(context);
    }
	private String TAG = "EmoticonParser";
    public static void destroyInstance() {
        if (sInstance != null) {
            sInstance = null;
        }
    }

    private final Context mContext;
    private final String[] mEmoticonTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mEmoticonToRes;

    private EmoticonParser(Context context) {
        mContext = context;
        mEmoticonTexts = mContext.getResources().getStringArray(DEFAULT_EMOTICON_TEXTS);
        mEmoticonToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }

    static class Emoticons {
        private static final int[] sIconIds = {
            R.drawable.emo_im_happy,
            R.drawable.emo_im_sad,
            R.drawable.emo_im_winking,
            R.drawable.emo_im_tongue_sticking_out,
            R.drawable.emo_im_surprised,
            R.drawable.emo_im_kissing,
            R.drawable.emo_im_yelling,
            R.drawable.emo_im_cool,
            R.drawable.emo_im_money_mouth,
            R.drawable.emo_im_foot_in_mouth,
            R.drawable.emo_im_embarrassed,
            R.drawable.emo_im_angel,
            R.drawable.emo_im_undecided,
            R.drawable.emo_im_crying,
            R.drawable.emo_im_lips_are_sealed,
            R.drawable.emo_im_laughing,
            R.drawable.emo_im_wtf
        };

        public static int HAPPY = 0;
        public static int SAD = 1;
        public static int WINKING = 2;
        public static int TONGUE_STICKING_OUT = 3;
        public static int SURPRISED = 4;
        public static int KISSING = 5;
        public static int YELLING = 6;
        public static int COOL = 7;
        public static int MONEY_MOUTH = 8;
        public static int FOOT_IN_MOUTH = 9;
        public static int EMBARRASSED = 10;
        public static int ANGEL = 11;
        public static int UNDECIDED = 12;
        public static int CRYING = 13;
        public static int LIPS_ARE_SEALED = 14;
        public static int LAUGHING = 15;
        public static int WTF = 16;

        public static int getEmoticonResource(int which) {
            return sIconIds[which];
        }
    }

    public static final int[] DEFAULT_EMOTICON_RES_IDS = {
        Emoticons.getEmoticonResource(Emoticons.HAPPY),                //  0
        Emoticons.getEmoticonResource(Emoticons.SAD),                  //  1
        Emoticons.getEmoticonResource(Emoticons.WINKING),              //  2
        Emoticons.getEmoticonResource(Emoticons.TONGUE_STICKING_OUT),  //  3
        Emoticons.getEmoticonResource(Emoticons.SURPRISED),            //  4
        Emoticons.getEmoticonResource(Emoticons.KISSING),              //  5
        Emoticons.getEmoticonResource(Emoticons.YELLING),              //  6
        Emoticons.getEmoticonResource(Emoticons.COOL),                 //  7
        Emoticons.getEmoticonResource(Emoticons.MONEY_MOUTH),          //  8
        Emoticons.getEmoticonResource(Emoticons.FOOT_IN_MOUTH),        //  9
        Emoticons.getEmoticonResource(Emoticons.EMBARRASSED),          //  10
        Emoticons.getEmoticonResource(Emoticons.ANGEL),                //  11
        Emoticons.getEmoticonResource(Emoticons.UNDECIDED),            //  12
        Emoticons.getEmoticonResource(Emoticons.CRYING),               //  13
        Emoticons.getEmoticonResource(Emoticons.LIPS_ARE_SEALED),      //  14
        Emoticons.getEmoticonResource(Emoticons.LAUGHING),             //  15
        Emoticons.getEmoticonResource(Emoticons.WTF),                   //  16
    };

    public static final int DEFAULT_EMOTICON_TEXTS = R.array.default_smiley_texts;
    public static final int DEFAULT_EMOTICON_NAMES = R.array.default_smiley_names;
    
    private HashMap<String, Integer> buildSmileyToRes() {
        if (DEFAULT_EMOTICON_RES_IDS.length != mEmoticonTexts.length) {
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }

        HashMap<String, Integer> emoticonToRes =
                            new HashMap<String, Integer>(mEmoticonTexts.length);
        for (int index = 0; index < mEmoticonTexts.length; index++) {
            emoticonToRes.put(mEmoticonTexts[index], DEFAULT_EMOTICON_RES_IDS[index]);
        }

        return emoticonToRes;
    }

    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(mEmoticonTexts.length * 3);
        patternString.append('(');
        for (String s : mEmoticonTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        return Pattern.compile(patternString.toString());
    }

    public CharSequence addSmileySpans(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mEmoticonToRes.get(matcher.group());
            RtcLogs.e(TAG, " resId ["+ resId +"] matcher.start[" + matcher.start() + "] matcher.end() [" + matcher.end() + "]");
            ImageSpan span = new ImageSpan(mContext, resId);
            builder.setSpan(span,matcher.start(), matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }
}
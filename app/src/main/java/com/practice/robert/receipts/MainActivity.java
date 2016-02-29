package com.practice.robert.receipts;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;


//http://blog.csdn.net/ChaoY1116/article/details/45224467   <<解析詳解
public class MainActivity extends AppCompatActivity {
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnCheck, btnClear;
//找到UI工人的經紀人，這樣才能派遣工作  (找到顯示畫面的UI Thread上的Handler)

    private Handler mUI_Handler = new Handler();

    //宣告特約工人的經紀人

    private Handler mThreadHandler;

    //宣告特約工人

    private HandlerThread mThread;
    TextView showTextView, inputTextView;
    ArrayList<Prize>list1;
    ArrayList<Prize>list2;
    ArrayList<Prize>list3;
    String allPrize1="";
    String allPrize2="";
    String allPrize3="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();


        //聘請一個特約工人，有其經紀人派遣其工人做事 (另起一個有Handler的Thread)

        mThread = new HandlerThread("name");

        //讓Worker待命，等待其工作 (開啟Thread)

        mThread.start();

        //找到特約工人的經紀人，這樣才能派遣工作 (找到Thread上的Handler)

        mThreadHandler = new Handler(mThread.getLooper());


        //請經紀人指派工作名稱 r，給工人做

        mThreadHandler.post(r1);


    }

    private Runnable r1 = new Runnable() {

        public void run() {

            // TODO Auto-generated method stub


            //.............................
            getXML();

            test();
        }

    };
    private void test(){

        Log.i("list1", list1.toString());
        Log.i("list2",list2.toString());
        Log.i("list3",list3.toString());
        Log.i("allPrize1", allPrize1.toString());
        Log.i("allPrize2",allPrize2.toString());
        Log.i("allPrize3", allPrize3.toString());

    }
    private void setView() {
        list1=new ArrayList<Prize>();
        list2=new ArrayList<Prize>();
        list3=new ArrayList<Prize>();

        btn0 = (Button) findViewById(R.id.button00);
        btn1 = (Button) findViewById(R.id.button01);
        btn2 = (Button) findViewById(R.id.button02);
        btn3 = (Button) findViewById(R.id.button03);
        btn4 = (Button) findViewById(R.id.button04);
        btn5 = (Button) findViewById(R.id.button05);
        btn6 = (Button) findViewById(R.id.button06);
        btn7 = (Button) findViewById(R.id.button07);
        btn8 = (Button) findViewById(R.id.button08);
        btn9 = (Button) findViewById(R.id.button09);


        showTextView = (TextView) findViewById(R.id.showView);
        inputTextView = (TextView) findViewById(R.id.inputView);
    }

    private String getXML() {
        try {
            URL text = new URL("http://invoice.etax.nat.gov.tw/invoice.xml");

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(text.openStream(), null);

            //


            int parserEvent = parser.getEventType();
            String tag=null;
            boolean inTitle = false;
            int cnt=0;//不要撈太多資料
            //xml结束时，就报告该事件
            while (parserEvent != XmlPullParser.END_DOCUMENT&&cnt<3) {

                switch (parserEvent) {
        /* TEXT
                 读到内容时，就报告该事件。如：读到<div>内容</div>，标签间的内容时
                  getName();
                 该api只有在当前的事件为START_TAG（返回标签名），END_TAG（返回标签名）和ENTITY_REF（返回实体引用名）。其它的事件都返回null。*/
                    case XmlPullParser.TEXT:



                        if (tag!=null&&tag.compareTo("title") == 0) {
                            int indext= parser.getText().indexOf("統一");
                            Log.i("title", "title = " + parser.getText()+":"+indext);
                            if(indext<0){

                                if(list1.size()==0){

                                    list1.add(new Prize(parser.getText()));

                                }
                                else if(list2.size()==0){
                                    list2.add(new Prize(parser.getText()));
                                    Log.e("list2.size()", "" + list2.size());
                                }
                                else {
                                    list3.add(new Prize(parser.getText()));
                                    Log.e("list3.size()", "" + list3.size());
                                }

                            };

                        }else if(tag!=null&&tag.compareTo("description") == 0){

                            String str=parser.getText().replaceAll("<[^>]+>", "");//把XML檔裡面 的<TAG>都消除
                            Log.i("descriptionB", "description = " +str);
                            String[]tempAry=str.split("\\D");
                            int indext= str.indexOf("統一");
                            if(indext<0){
                                if(allPrize1.equals("")){
                                    allPrize1=str;
                                    Log.e("list1.size()", "" + list1.size());
                                    putInArray(list1,tempAry);

                                }
                                else  if(allPrize2.equals("")){
                                    putInArray(list2,tempAry);
                                    allPrize2=str;
                                }
                                else  if(allPrize3.equals("")){
                                    putInArray(list3,tempAry);
                                    allPrize3=str;
                                }
                                cnt++;
                            }


                        }



                        break;
                    /*END_TAG读到结束标签时，就报告该事件。如：读到</div>这种时
                                            getText();
                                            该api只有在当前的事件为TEXT（返回文本内容），ENTITY_REF（返回实体引用引用的内容），
                                            CDSECT（返回其内部的内容），COMMENT（返回注释内容）。其它的事件一般都返回null。

                     */
                    case XmlPullParser.END_TAG:
                        tag = null;
                        // Log.i("tag", "tag = " + tag );
                        /*if (tag.compareTo("title") == 0) {
                            inTitle = false;

                        }*/
                        break;
                    //读到开始标签时，就报告该事件。如：读到<div>这种时
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        //  Log.i("tag", "tag = " + tag );

                        /*if (tag.compareTo("title") == 0) {
                            inTitle = true;

                        }*/
                        break;
                }
                //  next();                主要是用于返回比较高层的事件的。其中包括：START_TAG, TEXT, END_TAG, END_DOCUMENT
                parserEvent = parser.next();
            }
            //status.setText("파싱 끝");
        } catch (Exception e) {
            Log.e("dd", "Error in network call", e);
        }
        return "";
    }


    private  void putInArray(ArrayList<Prize>list,String[]tempAry){
        for(int i=0;i<tempAry.length;i++) {
            if (!("".equals(tempAry[i]))) {
                switch (list.size()) {
                    case 1:
                        list.add(new Prize("特別獎", tempAry[i]));
                        break;
                    case 2:
                        list.add(new Prize("特獎", tempAry[i]));
                        break;
                    case 3:
                        list.add(new Prize("頭獎", tempAry[i]));
                        break;
                    case 4:
                        list.add(new Prize("頭獎", tempAry[i]));
                        break;
                    case 5:
                        list.add(new Prize("頭獎", tempAry[i]));
                        break;
                    case 6:
                        list.add(new Prize("六獎", tempAry[i]));
                        break;
                    case 7:
                        list.add(new Prize("六獎", tempAry[i]));
                        break;
                    case 8:
                        list.add(new Prize("六獎", tempAry[i]));
                        break;
                    default:
                        Log.e("wrong", "獎項數目錯誤"+list.size());


                }
            }
        }

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();


        //移除工人上的工作

        if (mThreadHandler != null) {

            mThreadHandler.removeCallbacks(r1);

        }

        //解聘工人 (關閉Thread)

        if (mThread != null) {

            mThread.quit();

        }

    }
}

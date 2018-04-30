package com.zmj.weibomonitor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmj.po.Weibo;
import com.zmj.weibomonitor.analyze.WeiboPicUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZMJ on 2018/4/29.
 */

public class DisplayFragment extends Fragment {
    private Weibo weibo=null;

    private RecyclerView weiboContentRecyclerView=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        weibo=(Weibo)getActivity().getIntent().getExtras().getSerializable("weibo");

        View v=inflater.inflate(R.layout.fragment_display,container,false);
        weiboContentRecyclerView=(RecyclerView)v.findViewById(R.id.weibo_content_recycler_view);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);//设置为垂直滚动
        weiboContentRecyclerView.setLayoutManager(llm);

        weiboContentRecyclerView.setAdapter(new WeiboContentAdapter());

        return v;
    }


    private class WeiboContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private int windowWidth;
        private List<Bitmap> pics=new ArrayList<Bitmap>();

        /*对windowWidth和pics初始化*/
        public WeiboContentAdapter(){
            WindowManager windowManager=getActivity().getWindowManager();
            Point outSize=new Point();
            windowManager.getDefaultDisplay().getSize(outSize);
            this.windowWidth=outSize.x;

            if(weibo.getType()==Weibo.TYPE_WEIBO_WITH_PIC) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        for (String picUrl : weibo.getPicsUrl()) {
                            try {
                                byte[] picByteArr = new WeiboPicUrl(picUrl).toByteArray();
                                Bitmap pic = BitmapFactory.decodeByteArray(picByteArr, 0, picByteArr.length);
                                pics.add(pic);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FrameLayout frameLayout=new FrameLayout(getContext());
            FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20,20,20,20);
            frameLayout.setPadding(20,20,20,20);
            frameLayout.setLayoutParams(layoutParams);

            return new RecyclerView.ViewHolder(frameLayout) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final FrameLayout frameLayout=(FrameLayout) holder.itemView;

            if(position==0){

                TextView tv=new TextView(getContext());
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextColor(getResources().getColor(R.color.textColor));
                String t=weibo.getText();
                if(weibo.getType()==Weibo.TYPE_FORWORDING_WEIBO){
                    t+="\n\n"+weibo.getOriginAuthorName()+"："+weibo.getOriginWeiBoText();
                }
                tv.setText(t);

                frameLayout.removeAllViews();//这一句不要忘了！
                frameLayout.addView(tv);
            }else{

                final Bitmap pic=pics.get(position-1);

                ViewGroup.LayoutParams layoutParams=frameLayout.getLayoutParams();
                int ivWidth=0;
                if(layoutParams instanceof FrameLayout.LayoutParams) {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
                    ivWidth = windowWidth - lp.leftMargin - lp.rightMargin - frameLayout.getPaddingLeft() - frameLayout.getPaddingRight();
                }else if(layoutParams instanceof RecyclerView.LayoutParams){
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) frameLayout.getLayoutParams();
                    ivWidth = windowWidth - lp.leftMargin - lp.rightMargin - frameLayout.getPaddingLeft() - frameLayout.getPaddingRight();
                }
                int ivHeight = (int) (pic.getHeight() * 1.0 / pic.getWidth() * ivWidth);

                final ImageView iv = new ImageView(getContext());
                iv.setLayoutParams(new FrameLayout.LayoutParams(ivWidth, ivHeight));
                iv.setImageBitmap(pic);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);

                frameLayout.removeAllViews();//这一句不要忘了！
                frameLayout.addView(iv);

                /*
                ViewTreeObserver observer=iv.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {//监听，当图片绘制完的时候再绘制一遍，放大图片

                        double rate=pic.getHeight()*1.0/pic.getWidth();
                        ViewGroup.LayoutParams lp=iv.getLayoutParams();
                        lp.height=(int)(iv.getWidth()*rate);
                        iv.setLayoutParams(lp);//这一句不能少！如果没有这一句，iv.getLayoutParams().height虽然变了，但是不能将这个变化再重绘出来
                        iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });
                */
            }
        }

        @Override
        public int getItemCount() {
            if(weibo.getType()==Weibo.TYPE_WEIBO_WITH_PIC) {
                return weibo.getPicsUrl().size()+1;
            }else{
                return 1;
            }
        }
    }
}

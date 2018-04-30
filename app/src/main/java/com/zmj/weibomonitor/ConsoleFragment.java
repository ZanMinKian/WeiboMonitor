package com.zmj.weibomonitor;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zmj.sqlite.po.WeiboId;
import com.zmj.tools.ToolsFactory;
import com.zmj.tools.Utils;

import java.util.List;

/**
 * Created by ZMJ on 2018/4/21.
 */

public class ConsoleFragment extends Fragment{
    private RecyclerView weiboIdsRecylerView=null;
    private EditText inputWeiboEdt=null;
    private Button addWeiboIdBtn=null;
    private Button runBtn=null;

    private WeiboIdsAdapter adapter=null;
    private List<WeiboId> weiboIds=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_console,container,false);

        weiboIdsRecylerView=(RecyclerView) v.findViewById(R.id.weibo_ids_recycler_view);
        inputWeiboEdt= (EditText) v.findViewById(R.id.input_weibo_id_edt);
        addWeiboIdBtn= (Button) v.findViewById(R.id.add_weibo_id_btn);
        runBtn= (Button) v.findViewById(R.id.run_btn);

        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);//设置为垂直滚动
        weiboIdsRecylerView.setLayoutManager(llm);
        updateUI();

        addWeiboIdBtn.setOnClickListener(new AddWeiboIdBtnListener());
        runBtn.setOnClickListener(new RunBtnListener());

        inputWeiboEdt.requestFocus();

        return v;
    }

    private class AddWeiboIdBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String weiboIdStr=inputWeiboEdt.getText().toString().trim();
            if(weiboIdStr.length()>0) {
                try {
                    ToolsFactory.addWeiboId(weiboIdStr, getContext());
                    inputWeiboEdt.setText("");
                    updateUI();
                }catch (SQLException sqle){
                    Toast.makeText(getActivity(),"该微博已经在列表中！",Toast.LENGTH_SHORT).show();
                    sqle.printStackTrace();
                }

            }
        }
    }

    private class RunBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.d("ConsoleFragment","runBtn clicked!");

            if(!Utils.isProessRunning(getContext(),ConnectService.PROCESS_NAME))
                ConnectService.startService(getActivity());//只需要启动这一个进程即可，如果发现另一个进程没有在运行它会自动叫醒它
        }
    }

    //省略了ViewHolder，直接在Adapter中创建ViewHolder，更加简洁
    //Adapter用于设置RecyclerView，该Adapter更新依据是weiboIds
    private class WeiboIdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{//每一个viewHolder填充的是view_holder_weibo.xml
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//parent指的是ReyclerView
            LinearLayout linearLayout= (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.view_holder_weibo,parent,false);
            return new RecyclerView.ViewHolder(linearLayout){};//因为ViewHolder(View)是抽象构造方法
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){//holder.itemView指的是填充到holder中的子view，即上面的LinearLayout
            final TextView weiboTxv=(TextView)holder.itemView.findViewById(R.id.weibo_id_txv);
            ImageButton deleteBtn=(ImageButton)holder.itemView.findViewById(R.id.delete_btn);

            weiboTxv.setText(weiboIds.get(position).getWeiboId());

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ToolsFactory.deleteWeiboId(weiboTxv.getText().toString(),getContext());
                    updateUI();
                }
            });
        }

        @Override
        public int getItemCount() {
            return weiboIds.size();
        }

    }

    private void updateUI(){//更新UI需要查询数据库中微博ID
        weiboIds = ToolsFactory.getWeiboIds(getContext());
        if(adapter==null) {
            adapter = new WeiboIdsAdapter();
            weiboIdsRecylerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();//当调用这个方法的时候，adapter会调用onBindViewHolder和getItemCount()更新视图
        }
    }
}

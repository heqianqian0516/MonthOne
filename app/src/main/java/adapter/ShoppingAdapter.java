package adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bwei.monthone.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import bean.ShoppingBean;

public class ShoppingAdapter extends BaseExpandableListAdapter {
    private List<ShoppingBean.DataBean> datas;
    private Context context;

    public ShoppingAdapter(List<ShoppingBean.DataBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return datas.get(i).getList().size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder=null;
        if (view==null){
            groupViewHolder=new GroupViewHolder();
            view=View.inflate(context,R.layout.group_layout,null);
             groupViewHolder.groupCheckbox = view.findViewById(R.id.group_checkbox);
             groupViewHolder.groupName= view.findViewById(R.id.group_name);
             view.setTag(groupViewHolder);
        }else{
            groupViewHolder= (GroupViewHolder) view.getTag();
        }
          groupViewHolder.groupName.setText(datas.get(i).getSellerName());
          boolean myChecked=isChilderCheck(i);
          groupViewHolder.groupCheckbox.setChecked(myChecked);
          groupViewHolder.groupCheckbox.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (httpListenerCallBack!=null){
                      httpListenerCallBack.setGroupCheck(i);
                  }
              }
          });
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        GroupChildViewHolder groupChildViewHolder=null;
        if (view==null){
            groupChildViewHolder=new GroupChildViewHolder();
            view=View.inflate(context,R.layout.shoppingcar_item,null);
            groupChildViewHolder.childSdv=view.findViewById(R.id.child_sdv);
            groupChildViewHolder.childTitle=view.findViewById(R.id.child_title);
            groupChildViewHolder.childPrice=view.findViewById(R.id.child_price);
            groupChildViewHolder.childCheckbox=view.findViewById(R.id.child_checkbox);
            view.setTag(groupChildViewHolder);
        }else{
            groupChildViewHolder= (GroupChildViewHolder) view.getTag();
        }
        ShoppingBean.DataBean.ListBean listBean=datas.get(i).getList().get(i1);
        String url = listBean.getImages();
        String[] split = url.split("\\|");
        if (split != null) {
            for (int j = 0; j < split.length; j++) {
                String replace = split[j].replace("https", "http");
                Uri parse = Uri.parse(replace);
                groupChildViewHolder.childSdv.setImageURI(parse);
            }
        } else {
            String replace = url.replace("https", "http");
            Uri parse = Uri.parse(replace);
            groupChildViewHolder.childSdv.setImageURI(parse);
        }

        groupChildViewHolder.childTitle.setText(listBean.getTitle()+"");
        groupChildViewHolder.childPrice.setText(listBean.getPrice()+"");
        groupChildViewHolder.childCheckbox.setChecked(listBean.isChildChecked());
        groupChildViewHolder.childCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (httpListenerCallBack!=null){
                httpListenerCallBack.setChildCheck(i,i1);
                }
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    class GroupViewHolder{
        private CheckBox groupCheckbox;
        private TextView groupName;
    }
    class GroupChildViewHolder{
      private CheckBox childCheckbox;
      private SimpleDraweeView childSdv;
      private TextView childTitle;
      private TextView childPrice;
    }
    // //点击小组的checkbox,进行判断
    public void isChecked(int groupPosition,boolean isCheck){
        ShoppingBean.DataBean dataBean=datas.get(groupPosition);
        List<ShoppingBean.DataBean.ListBean> list=dataBean.getList();
        for (int i = 0; i <list.size() ; i++) {
            ShoppingBean.DataBean.ListBean listBean=list.get(i);
            listBean.setChildChecked(isCheck);
        }

    }
    public boolean isChilderCheck(int groupPosition){
        boolean boo=true;
        ShoppingBean.DataBean dataBean=datas.get(groupPosition);
        List<ShoppingBean.DataBean.ListBean> list=dataBean.getList();
        for (int i = 0; i <list.size() ; i++) {
             ShoppingBean.DataBean.ListBean listBean=list.get(i);
             if (!listBean.isChildChecked()){
                 return false;
             }
        }
        return boo;
    }
    //点击child给他进行赋值    给最下级的列表进行赋值
    public void setChildChecked(int groupPosition,int childPosition,boolean isCheckBox){
        ShoppingBean.DataBean.ListBean listBean=datas.get(groupPosition).getList().get(childPosition);
        listBean.setChildChecked(isCheckBox);
    }
    //查看当前这个商品有没有被选中
    public boolean myChildCheck(int groupPosition,int childPosition){
        ShoppingBean.DataBean.ListBean listBean=datas.get(groupPosition).getList().get(childPosition);
        if (listBean.isChildChecked()){
            return true;
        }
        return false;
    }
 //给商品数量进行赋值
    public void setShoppingNumb(int groupPosition,int childPosition,int number){
        ShoppingBean.DataBean.ListBean listBean=datas.get(groupPosition).getList().get(childPosition);
        listBean.setNum(number);
    }
    //因为底部视图有一个全选反选所以进行监听
    public boolean isAllGoods(){
        boolean boo=true;
        for (int i = 0; i <datas.size() ; i++) {
            ShoppingBean.DataBean dataBean=datas.get(i);
            for (int j = 0; j <dataBean.getList().size() ; j++) {
                ShoppingBean.DataBean.ListBean listBean=dataBean.getList().get(j);
                if (!listBean.isChildChecked()){
                    boo=false;
                }
            }
        }
        return boo;
    }
    //实现全选反选的功能
    public void isAllGoodsChecked(boolean isAllChecked){
        for (int i = 0; i <datas.size() ; i++) {
            ShoppingBean.DataBean dataBean=datas.get(i);
            for (int j = 0; j <dataBean.getList().size() ; j++) {
                ShoppingBean.DataBean.ListBean listBean=dataBean.getList().get(j);
                listBean.setChildChecked(isAllChecked);
            }
        }
    }
   // 计算所有商品的价格
    public float isAllPrice(){
        float allPrice=0;
        for (int i = 0; i <datas.size() ; i++) {
            ShoppingBean.DataBean dataBean=datas.get(i);
            for (int j = 0; j <dataBean.getList().size() ; j++) {
                ShoppingBean.DataBean.ListBean listBean=dataBean.getList().get(j);
                if (listBean.isChildChecked()){
                    allPrice+=listBean.getNum()*Float.parseFloat(listBean.getPrice());
                }
            }
        }
        return allPrice;
    }
    //计算所有商品的总数
    public int isAllGoodsNumber(){
        int number=0;
        for (int i = 0; i <datas.size() ; i++) {
            ShoppingBean.DataBean dataBean=datas.get(i);
            for (int j = 0; j < dataBean.getList().size(); j++) {
                ShoppingBean.DataBean.ListBean listBean=dataBean.getList().get(j);
                if (listBean.isChildChecked()){
                    number+=listBean.getNum();
                }
            }
        }
        return number;
    }
    // //因为点击Group和Child第CheckBox在主页面都需要刷新值所以做成接口回调
    private HttpListenerCallBack httpListenerCallBack;
    public void setCallBack(HttpListenerCallBack listenerCallBack){
        this.httpListenerCallBack=listenerCallBack;
    }
    public interface HttpListenerCallBack{
        void setGroupCheck(int groupPosition);
        void setChildCheck(int groupPosition,int childPosition);
        void setNumber(int groupPosition,int childPosition,int number);
    }
}

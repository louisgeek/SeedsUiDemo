package com.louisgeek.seedsuidemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by louisgeek on 2016/5/10.
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private List<Period> periodList;
    private Context context;
    LayoutInflater layoutInflater;
   // ChildViewHolder childViewHolder;
    LinearLayout id_ll_view_layout;
    Map<String,Object> childViewMap=new HashMap<>();
    List<String> groupKeyStringList=new ArrayList<>();
    public MyBaseExpandableListAdapter(List<Period> periodList, Context context) {
        this.periodList = periodList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return periodList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return periodList.get(groupPosition).getColumsMapList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return periodList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return periodList.get(groupPosition).getColumsMapList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        GroupViewHolder groupViewHolder=null;
        if (convertView==null) {
            groupViewHolder=new GroupViewHolder();
            convertView=layoutInflater.inflate(R.layout.group_item, null, false);
            groupViewHolder.id_tv_g=ButterKnife.findById(convertView,R.id.id_tv_g);

            convertView.setTag(groupViewHolder);
        }else
        {
            groupViewHolder= (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.id_tv_g.setText(periodList.get(groupPosition).getPeriod_name());
        if (!groupKeyStringList.contains(groupPosition+"_")){
            groupKeyStringList.add(groupPosition+"_");
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String type=periodList.get(groupPosition).getColumsMapList().get(childPosition).get("type").toString();
        String showname=periodList.get(groupPosition).getColumsMapList().get(childPosition).get("showname").toString();
        String isshow=periodList.get(groupPosition).getColumsMapList().get(childPosition).get("isshow").toString();
        String columsName_key=periodList.get(groupPosition).getColumsMapList().get(childPosition).get("columsName_key").toString();
        List<Property> propertyList= (List<Property>) periodList.get(groupPosition).getColumsMapList().get(childPosition).get("columsPropertyList");

       // Log.i("XXX","GGG"+periodList.get(groupPosition).getColumsMapList().get(childPosition));
        if (childViewMap.containsKey(groupPosition + "_" + childPosition)){
         return  (View)childViewMap.get(groupPosition + "_" + childPosition);
        }
       //if (convertView==null) {
            //childViewHolder=new ChildViewHolder();
            convertView=layoutInflater.inflate(R.layout.child_item, null, false);
            id_ll_view_layout = ButterKnife.findById(convertView, R.id.id_ll_view_layout);
            Log.i("XXX", "convertView" + type);
           if ("text".equals(type)){
               EditText et = new EditText(context);
               ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
               et.setLayoutParams(vlp);
               et.setHint("请输入" + showname);
               //et.setText(showname + "1");
               et.setTag(columsName_key);

               childViewMap.put(groupPosition + "_" + childPosition,et);
               id_ll_view_layout.addView(et);
           }else if ("int".equals(type)){
               EditText et = new EditText(context);
               ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
               et.setLayoutParams(vlp);
               et.setHint("请输入" + showname);
               //   et.setText(showname + "2");
               et.setTag(columsName_key);

               childViewMap.put(groupPosition + "_" + childPosition, et);
               id_ll_view_layout.addView(et);
           }else if ("radio".equals(type)){


               ViewGroup.LayoutParams vlp_rg_rb = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
               RadioGroup rg = new RadioGroup(context);
               rg.setTag(columsName_key);
               //rg.setOrientation();
               if (propertyList!=null&&propertyList.size()>0){
                   for (int i = 0; i < propertyList.size(); i++) {
                       String showname4p=propertyList.get(i).getShowname();

                       RadioButton rb = new RadioButton(context);
                       rg.setLayoutParams(vlp_rg_rb);
                       rb.setText(showname4p);
                       rg.addView(rb);

                   }
               }
           /* RadioButton rb2 = new RadioButton(context);
            rb2.setLayoutParams(vlp_rg_rb);
            rb2.setText("rb2");
            rg.addView(rb2);
           */
               childViewMap.put(groupPosition + "_" + childPosition, rg);
               id_ll_view_layout.addView(rg);
           }else if ("check".equals(type)){
               ViewGroup.LayoutParams vlp_rg_group = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
               LinearLayout ll = new LinearLayout(context);
               ll.setLayoutParams(vlp_rg_group);
               ll.setOrientation(LinearLayout.VERTICAL);
               ll.setTag(columsName_key);
               if (propertyList!=null&&propertyList.size()>0){
                   for (int i = 0; i < propertyList.size(); i++) {
                       String showname4p=propertyList.get(i).getShowname();
                       CheckBox cb = new CheckBox(context);
                       ViewGroup.LayoutParams vlp_cb = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                       cb.setLayoutParams(vlp_cb);
                       cb.setTag(columsName_key);
                       cb.setText(showname4p);
                       ll.addView(cb);

                   }
               }
               childViewMap.put(groupPosition + "_" + childPosition, ll);
               id_ll_view_layout.addView(ll);
           }else if ("drop".equals(type)){
          /* String[] strs = new String[5];
            for (int k = 0; k < strs.length; k++) {
                strs[k] = "选项" + k;
            }*/
               String[] strs =null;
               if (propertyList!=null&&propertyList.size()>0){
                   strs = new String[propertyList.size()];
                   for (int i = 0; i < propertyList.size(); i++) {
                       strs[i] =propertyList.get(i).getShowname();
                   }
               }
               Spinner spi = new Spinner(context);
               ViewGroup.LayoutParams vlp_spi = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
               spi.setLayoutParams(vlp_spi);
               spi.setPadding(20, 5, 5, 20);
               spi.setTag(columsName_key);
               ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, strs);
               spi.setAdapter(arrayAdapter);


               childViewMap.put(groupPosition + "_" + childPosition, spi);
               id_ll_view_layout.addView(spi);
           }


         //  convertView.setTag(childViewHolder);
       //}else
       // {
            //Log.i("XXX", "convertView==" + type);
        //  childViewHolder= (ChildViewHolder) convertView.getTag();

      // }

   /*   String showNameStr=showname+"_"+groupPosition+"_"+childPosition;
       View viewTemp= (View) childViewMap.get(groupPosition + "_" + childPosition);

    if (viewTemp instanceof TextView){
        viewTemp.setTag(columsName_key);
        ((TextView) viewTemp).setText(showNameStr);

    }else if (viewTemp instanceof EditText){
        viewTemp.setTag(columsName_key);
        ((EditText) viewTemp).setText(showNameStr);
    }else if (viewTemp instanceof RadioGroup){
        viewTemp.setTag(columsName_key);
       // ((RadioGroup) viewTemp).setText(showNameStr);
    }else if (viewTemp instanceof LinearLayout){
        viewTemp.setTag(columsName_key);
        //((LinearLayout) viewTemp).setText(showNameStr);
    }else if (viewTemp instanceof Spinner){
        viewTemp.setTag(columsName_key);
        //((Spinner) viewTemp).setText(showNameStr);
    }*/




        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class GroupViewHolder{
        TextView id_tv_g;
    }
 /*   class ChildViewHolder{
        LinearLayout id_ll_view_layout;
        Map<String,Object> childViewMap=new HashMap<>();
    }*/

  /*  public LinearLayout getId_ll_view_layout() {
        return childViewHolder.id_ll_view_layout;
    }*/
    public Map<String,Object> getChildViewMap(){
        return childViewMap;
    }

    public List<String> getGroupKeyStringList() {
        return groupKeyStringList;
    }
}



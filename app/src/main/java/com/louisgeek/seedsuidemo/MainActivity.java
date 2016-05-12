package com.louisgeek.seedsuidemo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.yyydjk.library.DropDownMenu;

import org.json.JSONArray;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.id_btn)
    Button idBtn;
    private static final String TAG = "MainActivity";
    Agronet agronet = null;
    @Bind(R.id.id_elv)
    ExpandableListView idElv;
    MyBaseExpandableListAdapter myBaseExpandableListAdapter;

    String JsonString = "";
    JSONArray jsonArray;
    @Bind(R.id.id_dropDownMenu)
    DropDownMenu idDropDownMenu;

    private String tab_head_titles[] = {"城市", "年龄", "性别", "星座"};

    private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};
    private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};
    private String constellations[] = {"不限", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};

    private List<View> dropDownViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        // idBtn.setVisibility(View.GONE);
        readXml();


        List<Period> periodList = agronet.getPeriods();
        myBaseExpandableListAdapter = new MyBaseExpandableListAdapter(periodList, this);
        idElv.setAdapter(myBaseExpandableListAdapter);

        for (int i = 0; i < myBaseExpandableListAdapter.getGroupCount(); i++) {
            idElv.expandGroup(i);
        }
        initFABMenu();

        initDropDownMenu();
    }

    private void initDropDownMenu() {

        for (int i = 0; i < tab_head_titles.length; i++) {
            TextView dropDownView = new TextView(this);
            dropDownView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dropDownView.setText("tab_head_view" + i);
            dropDownView.setGravity(Gravity.CENTER);
            dropDownView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            dropDownViews.add(dropDownView);
        }



        //init context view
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        //tabs 所有标题，popupViews  所有菜单，contentView 内容
        idDropDownMenu.setDropDownMenu(Arrays.asList(tab_head_titles), dropDownViews, contentView);
    }

    private void initFABMenu() {
        //1.Create a button to attach the menu: in Activity Context
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_action_new_light));

        FloatingActionButton floatingActionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        //2.Create menu items:
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_action_camera_light));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_action_picture_light));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_action_place_light));
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();


        //3. Create the menu with the items:
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                        // ...
                .attachTo(floatingActionButton)
                .build();
    }

    private void readXml() {
        InputStream inputStream = getResources().openRawResource(R.raw.agronet);

        //  Agronet agronet = getAgronetByPull(inputStream);
        try {
            agronet = getAgronetByDom(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getAgronet: agronet:" + agronet);


    }

    @OnClick(R.id.id_btn)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn:
                //readXml();
                submitJson();
                break;
            default:
                break;
        }


    }

    private void submitJson() {
        List<String> groupKeyStringList = myBaseExpandableListAdapter.getGroupKeyStringList();
        Map<String, Object> childViewMap = myBaseExpandableListAdapter.getChildViewMap();

        Map<String, Object> group4List_Map = new HashMap<>();

        for (int i = 0; i < groupKeyStringList.size(); i++) {
            String groupKeyString = groupKeyStringList.get(i);//0_   1_

            List<Map<String, String>> attrsMapList = new ArrayList<>();
            for (String key : childViewMap.keySet()) {
                Map<String, String> attrsMap = null;
                try {
                    attrsMap = dealChildViews((View) childViewMap.get(key));
                    System.out.println("key= " + key + " and attrsMap= " + attrsMap);
                    if (key.contains(groupKeyString)) {
                        attrsMapList.add(attrsMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            group4List_Map.put(groupKeyString, attrsMapList);

        }


        JsonString = JSON.toJSONString(group4List_Map).toString();
        Log.d(TAG, "JsonString:" + JsonString);



      /*  // 总json对象
        JSONObject jsonObject_agronet = new JSONObject();
        JSONObject jsonObject_table_per = new JSONObject();
        JSONObject jsonObject_periods = new JSONObject();
        JSONArray jsonArray_period = new JSONArray();
        for (int i = 0; i < groupKeyStringList.size(); i++) {
            String  groupKeyString= groupKeyStringList.get(i);
            JSONObject jsonObject_c = new JSONObject();
            jsonObject_c.put("groupKeyString",groupKeyString);
            jsonObject_c.put("groupKeyString",groupKeyString)
            jsonArray_period.put()
        }
        jsonObject_table_per.put("tableid","Sys_Menu");
        jsonObject_periods.put("periods", jsonArray_period);
        jsonObject_table_per.put("periods",jsonObject_periods);
        jsonObject_agronet.put("agronet", jsonObject_table_per);*/


    }

    public Map<String, String> dealChildViews(View view) throws Exception {
        Map<String, String> attrsMap = new HashMap<>();
        if (view instanceof EditText) {
            EditText et = (EditText) view;
            String et_t = et.getTag().toString();
            String et_txt = et.getText().toString();
            Log.d(TAG, "getChildViews: EditText:" + et_t + "====" + et_txt);
            attrsMap.put(et_t, et_txt);
            //mapKeyValue.put("value", et_txt);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            String et_t = tv.getTag().toString();
            String et_txt = tv.getText().toString();
            Log.d(TAG, "getChildViews: TextView:" + et_t + "====" + et_txt);
            attrsMap.put(et_t, et_txt);
            //mapKeyValue.put("value", et_txt);
        } else if (view instanceof RadioGroup) {
            RadioGroup rg = (RadioGroup) view;
            String view_t = rg.getTag().toString();
            RadioButton rb = ButterKnife.findById(this, rg.getCheckedRadioButtonId());
            String view_txt = "";
            if (rb != null) {
                view_txt = rb.getText().toString();
            }
            Log.d(TAG, "getChildViews: RadioGroup:" + view_t + "====" + view_txt);
            attrsMap.put(view_t, view_txt);
            //mapKeyValue.put("value", et_txt);
        } else if (view instanceof CheckBox) {
            CheckBox cb = (CheckBox) view;
            String view_t = cb.getTag().toString();
            String view_txt = cb.getText().toString();
            Log.d(TAG, "getChildViews: CheckBox:" + view_t + "====" + view_txt);
            attrsMap.put(view_t, view_txt);
            //mapKeyValue.put("value", et_txt);
        } else if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            String view_t = spinner.getTag().toString();
            String view_txt = spinner.getSelectedItem().toString();
            Log.d(TAG, "getChildViews: Spinner:" + view_t + "====" + view_txt);
            attrsMap.put(view_t, view_txt);
            //mapKeyValue.put("value", et_txt);
        }
        return attrsMap;
    }

    public void getChildViews(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof EditText) {
                EditText et = (EditText) view;
                String et_t = et.getTag().toString();
                String et_txt = et.getText().toString();
                Log.d(TAG, "getChildViews: EditText" + et_t + "====" + et_txt);
            } else if (view instanceof TextView) {
                TextView tv = (TextView) view;
                String et_t = tv.getTag().toString();
                String et_txt = tv.getText().toString();
                Log.d(TAG, "getChildViews: TextView" + et_t + "====" + et_txt);
            } else if (view instanceof RadioGroup) {
                RadioGroup rg = (RadioGroup) view;
                String et_t = rg.getTag().toString();
                RadioButton rb = ButterKnife.findById(this, rg.getCheckedRadioButtonId());
                String et_txt = rb.getText().toString();
                Log.d(TAG, "getChildViews: RadioGroup" + et_t + "====" + et_txt);
            } else if (view instanceof CheckBox) {
                CheckBox cb = (CheckBox) view;
                String et_t = cb.getTag().toString();
                String et_txt = cb.getText().toString();
                Log.d(TAG, "getChildViews: CheckBox" + et_t + "====" + et_txt);
            } else if (view instanceof Spinner) {
                Spinner spinner = (Spinner) view;
                String et_t = spinner.getTag().toString();
                String et_txt = spinner.getSelectedItem().toString();
                Log.d(TAG, "getChildViews: Spinner" + et_t + "====" + et_txt);
            } else if (view instanceof ViewGroup) {
                // 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
                this.getChildViews((ViewGroup) view);
            }
        }
    }

    public Agronet getAgronetByDom(InputStream input) throws Exception {
        Agronet agronet = new Agronet();
        List<Period> periodList = null;
        Period period = null;
        List<Map<String, Object>> columsMapList = null;
        Map<String, Object> columsMap = null;
        List<Property> propertyList = null;
        Property property = null;


        //获得DOM解析器的工厂示例:
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //从Dom工厂中获得dom解析器
        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        //把要解析的xml文件读入Dom解析器
        Document document = dbBuilder.parse(input);
        // System.out.println("处理该文档的DomImplemention对象=" + doc.getImplementation());
        //得到 "根节点"
        Element rootElement = document.getDocumentElement();//agronet
        //获取根节点的所有items的节点
        NodeList nodeList_table = rootElement.getElementsByTagName("table");//table集合
           /* //或者直接得到文档中名称为person的元素的结点列表
            NodeList nList = document.getElementsByTagName("person");*/
        Element tableElement = (Element) nodeList_table.item(0);//table
        agronet.setTable_id(tableElement.getAttribute("id"));

        NodeList nodeList_periods = tableElement.getElementsByTagName("periods");//periods集合
        Element periodsElement = (Element) nodeList_periods.item(0);//periods

        NodeList nodeList_period = periodsElement.getElementsByTagName("period");//period集合

        periodList = new ArrayList<>();
        //遍历该集合,显示集合中的元素以及子元素的名字
        for (int i = 0; i < nodeList_period.getLength(); i++) {
            Element periodElement = (Element) nodeList_period.item(i);//period
            period = new Period();

            period.setPeriod_name(periodElement.getAttribute("name"));
            period.setPeriod_value(periodElement.getAttribute("value"));

            //获取不固定的Node集合
            NodeList childNodeList = periodElement.getChildNodes();//【不固定】xxx的Node集合

            columsMapList = new ArrayList<>();
            for (int j = 0; j < childNodeList.getLength(); j++) {
                Node childNode = childNodeList.item(j);//【不固定】xxx

                columsMap = new HashMap<>();

                columsMap.put("columsName_key", childNode.getNodeName());
                //判断子node类型是否为元素Node
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Log.d(TAG, "getAgronetByDom: ELEMENT_NODE");
                    Element childElement = (Element) childNode;
                    //循环attr
                    for (int k = 0; k < childElement.getAttributes().getLength(); k++) {
                        Attr attr = (Attr) childElement.getAttributes().item(k);//attr
                        Log.d(TAG, "getAgronetByDom: attr=getName:" + attr.getName());
                        Log.d(TAG, "getAgronetByDom: attr=getValue:" + attr.getValue());
                        columsMap.put(attr.getName(), attr.getValue());

                        //获取子Node集合
                        NodeList childElementNodeList = childElement.getChildNodes();//properties集合

                        propertyList = new ArrayList<>();

                        if (childElementNodeList != null && childElementNodeList.getLength() > 0) {

                            for (int l = 0; l < childElementNodeList.getLength(); l++) {
                                Node childElementNode = childElementNodeList.item(l);//properties
                                if (childElementNode.getNodeType() == Node.ELEMENT_NODE) {

                                    Element childElementNodeElement = (Element) childElementNode;
                                    Log.d(TAG, "getAgronetByDom: xx_childElementNodeElement。getNodeName" + childElementNodeElement.getNodeName());

                                    // NodeList childElementNode_child = childElementNodeElement.getChildNodes();
                                    NodeList nodeList_property = childElementNodeElement.getElementsByTagName("property");//property集合

                                    if (nodeList_property != null && nodeList_property.getLength() > 0) {
                                        for (int m = 0; m < nodeList_property.getLength(); m++) {
                                            Element nodeList_propertyElement = (Element) nodeList_property.item(m);//property
                                            property = new Property();
                                            property.setShowname(nodeList_propertyElement.getAttribute("showname"));
                                            property.setValue(nodeList_propertyElement.getAttribute("value"));
                                            propertyList.add(property);
                                        }
                                    }

                                } else if (childElementNode.getNodeType() == Node.TEXT_NODE) {
                                    Log.d(TAG, "getAgronetByDom: TEXT_NODE");
                                } else {
                                    Log.d(TAG, "getAgronetByDom: getNodeType" + childElementNode.getNodeType());
                                }
                            }
                        }


                    }
                    if (propertyList != null && propertyList.size() > 0) {
                        columsMap.put("columsPropertyList", propertyList);
                    }
                    columsMapList.add(columsMap);
                        /*childElement.
                        if("name".equals(childElement.getNodeName()))
                            p.setName(childElement.getFirstChild().getNodeValue());
                        else if("age".equals(childElement.getNodeName()))
                            p.setAge(Integer.valueOf(childElement.getFirstChild().getNodeValue()));*/
                } else if (childNode.getNodeType() == Node.TEXT_NODE) {
                    Log.d(TAG, "getAgronetByDom: TEXT_NODE");
                } else {
                    Log.d(TAG, "getAgronetByDom: getNodeType" + childNode.getNodeType());
                }
            }
            period.setColumsMap(columsMapList);
            periodList.add(period);
        }
        agronet.setPeriods(periodList);

        return agronet;
    }

    @Deprecated
    public Agronet getAgronetByPull(InputStream input) throws Exception {
        Agronet agronet = null;
        List<Period> periodList = null;
        Period period = null;
        List<Map<String, Object>> columsMapList = null;
        Map<String, Object> columsMap = null;
        List<Property> propertyList = null;
        Property property = null;
        boolean is_Pro_Reading = true;
        XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
        fac.setNamespaceAware(true);
        XmlPullParser parser = fac.newPullParser();
        parser.setInput(input, "UTF-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    Log.d(TAG, "getAgronet:START_DOCUMENT ");
                    break;
                case XmlPullParser.START_TAG:
                    Log.d(TAG, "getAgronet:getName <" + parser.getName());
                    if ("agronet".equals(parser.getName())) {
                        agronet = new Agronet();
                    }
                    if ("table".equals(parser.getName())) {
                        String tableid = parser.getAttributeValue(0);
                        agronet.setTable_id(tableid);
                    }
                    if ("periods".equals(parser.getName())) {
                        periodList = new ArrayList<>();
                    }
                    if ("period".equals(parser.getName())) {
                        period = new Period();
                        String name = parser.getAttributeValue(0);
                        String value = parser.getAttributeValue(1);
                        period.setPeriod_name(name);
                        period.setPeriod_value(value);

                        columsMapList = new ArrayList<>();//!!!!!!
                    } else if (period != null) {
                        if (!"properties".equals(parser.getName()) && !"property".equals(parser.getName())) {
                            columsMap = new HashMap<>();
                            String tempName = parser.getName();
                            columsMap.put("columsName", tempName);//可以不用
                            for (int i = 0; i < parser.getAttributeCount(); i++) {
                                String tempAttributeName = parser.getAttributeName(i);
                                columsMap.put(tempAttributeName, parser.getAttributeValue(i));
                            }
                        }
                        int eventTypeTemp = parser.next();
                        Log.d(TAG, "getAgronet:nextText:" + eventTypeTemp);
                        Log.d(TAG, "getAgronet:nextText:" + eventTypeTemp);
                        if (!"properties".equals(parser.getName())) {
                            columsMapList.add(columsMap);
                        }
                        if ("properties".equals(parser.getName())) {
                            propertyList = new ArrayList<>();
                        }
                        if ("property".equals(parser.getName())) {
                            property = new Property();
                            String showname = parser.getAttributeValue(0);
                            String value = parser.getAttributeValue(1);
                            property.setShowname(showname);
                            property.setValue(value);
                            propertyList.add(property);
                        }


                    }

                    break;
                case XmlPullParser.END_TAG:
                    Log.d(TAG, "getAgronet:getName " + parser.getName() + ">");
                   /* if("property".equals(parser.getName())) {
                        if (columsMap != null && period != null) {
                            propertyList.add(property);
                        }
                    }*/
                    if ("properties".equals(parser.getName())) {
                        if (columsMap != null && period != null) {
                            columsMap.put("columsPropertyList", propertyList);
                            columsMapList.add(columsMap);
                        }
                    }
                    if ("period".equals(parser.getName())) {
                        if (periodList != null && period != null) {
                            period.setColumsMap(columsMapList);
                            periodList.add(period);
                        }
                    }
                    if ("agronet".equals(parser.getName())) {
                        if (agronet != null) {
                            agronet.setPeriods(periodList);
                        }
                    }
                    break;
                default:
                    break;
            }

            eventType = parser.next();// 注意：此处勿要写成parser.next();不要理解成指针
        }

        return agronet;
    }


}

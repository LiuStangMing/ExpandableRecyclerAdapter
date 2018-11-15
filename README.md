[![](https://jitpack.io/v/LiuStangMing/ExpandableRecyclerAdapter.svg)](https://jitpack.io/#LiuStangMing/ExpandableRecyclerAdapter)
# ExpandableRecyclerAdapter
基于RecyclerView的二级列表.

## 引入 
 ### gradle
    project.build:
```java
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
    model.build:
```java
implementation 'com.github.LiuStangMing:ExpandableRecyclerAdapter:1.1.0'
```

## 使用
    
    step1:
    在一级列表数据bean中实现ItemBean接口，实现对应的方法
    
```java
public interface ItemBean {
//返回子集合
List getSubItems();
//是否展开
boolean isExpand();
}
```
    
    step2:
    自定义RecyclerView.Adapter继承ExpandableRecyclerAdapter<K,V>
    (K: 一级列表数据bean, V: 二级列表数据bean)
    
```java 
class ExpandableRecyclerAdapter<T extends ItemBean, K> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
```

    step3:
    在自定义的Adapter中创建ViewHolder并返回.
    
```java
public class GroupViewHolder extends RecyclerView.ViewHolder{

        private TextView title;

        public GroupViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.id_system_section_title_tv);
        }
    }

    public class SubViewHolder extends RecyclerView.ViewHolder{

        private TextView name;

        public SubViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.id_system_item_tv);
        }
    }
    
    @Override
    protected RecyclerView.ViewHolder createGroupViewHolder(ViewGroup parent, LayoutInflater inflater) {
        return new GroupViewHolder(inflater.inflate(R.layout.system_item_title_section, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder createSubViewHolder(ViewGroup parent, LayoutInflater inflater) {
        return new SubViewHolder(inflater.inflate(R.layout.system_item_section, parent, false));
    }
```

    step4 在convert方法中绑定数据
    (通过getGroupItem()方法和getSubItem()方法获得对应的数据bean,通过ItemStatus判断类型为一级列表或是二级列表)

```java
@Override
    protected void convert(RecyclerView.ViewHolder holder, ItemStatus status, int position) {
        if (status.getViewType() == TYPE_GROUPITEM){
            GroupViewHolder viewHolder = (GroupViewHolder) holder;
            //获得一级列表数据bean
            RecyclerBean item = getGroupItem(status);
            String name = item.getName();
            viewHolder.tv.setText(name);
        } else if (status.getViewType() == TYPE_SUBITEM){
            SubViewHolder viewHolder = (SubViewHolder) holder;
            ////获得二级列表数据bean
            RecyclerBean.DataBean bean = getSubItem(status);
            viewHolder.tv.setText(bean.getName());
        }
    }
```

    step5:
    调用setItemBeans()方法传入数据
    
#### 点击事件接口
```java
public interface OnItemClickListener{
      //点击一级列表
      void GroupItemClick(View itemView, int position);
      //点击二级列表
      void SubItemClick(View itemView, int position);
}
```

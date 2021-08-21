package com.example.mylenovo.testapp2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter_Product extends BaseAdapter implements Filterable {

    Activity context;
    List<ProductInfo> productInfos;
    List<ProductInfo> filterList;
    DatabaseReference dRef;

    public CustomAdapter_Product(Activity context, List<ProductInfo> objects) {
        this.context=context;
        this.productInfos=objects;
        this.filterList = objects;
    }

    @Override
    public int getCount() {
        return productInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return productInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.product_layout, null, false);

        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_type = view.findViewById(R.id.product_type);
        TextView price_unit = view.findViewById(R.id.price_unit);
        Button delete = view.findViewById(R.id.product_delete);

        final ProductInfo productInfo = productInfos.get(position);

        product_name.setText(productInfo.getP_name());
        product_type.setText("Type:  "+productInfo.getP_type());
        price_unit.setText("Price/Unit:  "+productInfo.getPrice_unit());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dRef = FirebaseDatabase.getInstance().getReference("Product");
                String name = productInfo.getP_name();
                dRef.child(name).removeValue();
            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return new CustomFilter();
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<ProductInfo> filter = new ArrayList<>();

            if(constraint!=null && constraint.length()>0){
                String cons = constraint.toString().toUpperCase();
                for(int i=0; i<filterList.size(); i++){
                    if(filterList.get(i).getP_name().toUpperCase().contains(cons)){
                        filter.add(filterList.get(i));
                    }
                    filterResults.values=filter;
                    filterResults.count=filter.size();
                }
            }
            else{
                filterResults.values=filterList;
                filterResults.count = filterList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productInfos = (ArrayList<ProductInfo>)results.values;
            notifyDataSetChanged();
        }
    }
}

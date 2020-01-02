package com.mezyapps.vgreenbasket.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.api_common.BaseApi;
import com.mezyapps.vgreenbasket.db.AppDatabase;
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.model.LocationModel;
import com.mezyapps.vgreenbasket.model.ProductListModel;
import com.mezyapps.vgreenbasket.model.ProductUnitModel;
import com.mezyapps.vgreenbasket.utils.NetworkUtils;
import com.mezyapps.vgreenbasket.view.activity.SignUpActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<ProductListModel> productListDashboardModelArrayList;
    private ArrayList<ProductListModel> arrayListFiltered;
    private ArrayList<ProductUnitModel> productUnitModelArrayList = new ArrayList<>();
    private String folder;
    int SpinnerPosition;
    final AppDatabase appDatabase;

    public ProductListAdapter(Context mContext, ArrayList<ProductListModel> productListDashboardModelArrayList, String folder) {
        this.mContext = mContext;
        this.productListDashboardModelArrayList = productListDashboardModelArrayList;
        this.arrayListFiltered = productListDashboardModelArrayList;
        this.folder = folder;
        appDatabase= Room.databaseBuilder(mContext,AppDatabase.class,"VgreenDB").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dashboard_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ProductListModel productListModel = productListDashboardModelArrayList.get(position);
        String imagePath = BaseApi.BASE_URL + folder + productListModel.getProd_image();
        Picasso.with(mContext).load(imagePath).into(holder.iv_product_image);
        holder.textProductName.setText(productListModel.getProd_name());


        productUnitModelArrayList = productListModel.getProductUnitModelArrayList();

        if (productUnitModelArrayList.size() == 1) {
            holder.ll_unit_one.setVisibility(View.VISIBLE);
            holder.ll_spinner.setVisibility(View.GONE);
            final ProductUnitModel productUnitModel = productListModel.getProductUnitModelArrayList().get(0);
            String unit = productUnitModel.getProd_unit() + " " + productUnitModel.getProd_weight();
            String rate = "Rs " + productUnitModel.getProd_rate();
            String mrp = "MRP " + productUnitModel.getProd_mrp();
            holder.textUnitName.setText(unit);
            holder.textPrice.setText(rate);
            holder.textMrp.setText(mrp);
            holder.textMrp.setPaintFlags(holder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.ll_unit_one.setVisibility(View.GONE);
            holder.ll_spinner.setVisibility(View.VISIBLE);
            ArrayList<String> stringArrayList = new ArrayList<>();
            stringArrayList.clear();
            for (ProductUnitModel productUnitModel : productUnitModelArrayList) {
                String unit = productUnitModel.getProd_unit() + " " + productUnitModel.getProd_weight();
                stringArrayList.add(unit);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, stringArrayList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinnerUnit.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

        }

        holder.spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    SpinnerPosition=position;
                    final ProductUnitModel productUnitmodel=productListModel.getProductUnitModelArrayList().get(position);
                    int product_id = Integer.parseInt(productUnitmodel.getId());
                    String unit = productUnitmodel.getProd_unit() + " " + productUnitmodel.getProd_weight();
                    String rate = "Rs " + productUnitmodel.getProd_rate();
                    String mrp = "MRP " + productUnitmodel.getProd_mrp();
                    holder.textUnitName.setText(unit);
                    holder.textPrice.setText(rate);
                    holder.textMrp.setText(mrp);
                    holder.textMrp.setPaintFlags(holder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


      /*  holder.textUnitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_item_list);
                TextView textProductName=dialog.findViewById(R.id.textProductName);
                RecyclerView recyclerView_Item=dialog.findViewById(R.id.recyclerView_Item);

                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
                recyclerView_Item.setLayoutManager(linearLayoutManager);

                textProductName.setText(productListModel.getProd_name());
                dialog.show();

                ProductItemAdapter productItemAdapter=new ProductItemAdapter(mContext,productUnitModelArrayList);
                recyclerView_Item.setAdapter(productItemAdapter);
                productItemAdapter.notifyDataSetChanged();

                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                dialog.setCancelable(true);
            }
        });*/
        holder.ll_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AddItem(position,productListModel);
            }
        });
    }

    private void AddItem(int position, ProductListModel productListModel) {
        String productName=productListDashboardModelArrayList.get(position).getProd_name();
        long product_id=Long.parseLong(productListDashboardModelArrayList.get(position).getProd_id());
        if (productUnitModelArrayList.size() == 1) {
            final ProductUnitModel productUnitModel =productListModel.getProductUnitModelArrayList().get(0);
            long  id=Long.parseLong(productUnitModel.getId());
            long unit =Long.valueOf(productUnitModel.getProd_unit());
            String weight=productUnitModel.getProd_weight();
            long rate =Long.parseLong(productUnitModel.getProd_rate());
            long mrp =Long.parseLong( productUnitModel.getProd_mrp());
            long  qty =Long.parseLong( "1");
            long idVal = appDatabase.getProductDAO().addProduct(new CardProductModel(0,product_id,productName,id,unit,weight,mrp,rate,mrp,rate,qty));
            Toast.makeText(mContext, String.valueOf(idVal), Toast.LENGTH_SHORT).show();
        }
        else
        {
            final ProductUnitModel productUnitModel =productListModel.getProductUnitModelArrayList().get(SpinnerPosition);
            long  id=Long.parseLong(productUnitModel.getId());
            long unit =Long.valueOf(productUnitModel.getProd_unit());
            String weight=productUnitModel.getProd_weight();
            long rate =Long.parseLong(productUnitModel.getProd_rate());
            long mrp =Long.parseLong( productUnitModel.getProd_mrp());
            long  qty =Long.parseLong( "1");
            long idVal = appDatabase.getProductDAO().addProduct(new CardProductModel(0,product_id,productName,id,unit,weight,mrp,rate,mrp,rate,qty));
            Toast.makeText(mContext, String.valueOf(idVal), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return productListDashboardModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_product_image;
        private TextView textProductName, textUnitName, textPrice, textMrp;
        private LinearLayout ll_unit_one, ll_spinner, ll_add_product;
        private Spinner spinnerUnit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            textProductName = itemView.findViewById(R.id.textProductName);
            textUnitName = itemView.findViewById(R.id.textUnitName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textMrp = itemView.findViewById(R.id.textMrp);
            ll_unit_one = itemView.findViewById(R.id.ll_unit_one);
            ll_spinner = itemView.findViewById(R.id.ll_spinner);
            spinnerUnit = itemView.findViewById(R.id.spinnerUnit);
            ll_add_product = itemView.findViewById(R.id.ll_add_product);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().replaceAll("\\s", "").toLowerCase().trim();
                if (charString.isEmpty() || charSequence.equals("")) {
                    productListDashboardModelArrayList = arrayListFiltered;
                } else {
                    ArrayList<ProductListModel> filteredList = new ArrayList<>();
                    for (int i = 0; i < productListDashboardModelArrayList.size(); i++) {
                        String name = productListDashboardModelArrayList.get(i).getProd_name().replaceAll("\\s", "").toLowerCase().trim();
                        if ((name.contains(charString))) {
                            filteredList.add(productListDashboardModelArrayList.get(i));
                        }
                    }
                    if (filteredList.size() > 0) {
                        productListDashboardModelArrayList = filteredList;
                    } else {
                        productListDashboardModelArrayList = arrayListFiltered;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListDashboardModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListDashboardModelArrayList = (ArrayList<ProductListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

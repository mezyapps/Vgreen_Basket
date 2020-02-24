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
import com.mezyapps.vgreenbasket.utils.ReferenceCardUiInterface;
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
    private ReferenceCardUiInterface referenceCardUiInterface;
    private CardProductModel cardProductModel;

    public ProductListAdapter(Context mContext, ArrayList<ProductListModel> productListDashboardModelArrayList, String folder, ReferenceCardUiInterface referenceCardUiInterface) {
        this.mContext = mContext;
        this.productListDashboardModelArrayList = productListDashboardModelArrayList;
        this.arrayListFiltered = productListDashboardModelArrayList;
        this.folder = folder;
        appDatabase = AppDatabase.getInStatce(mContext);
        this.referenceCardUiInterface = referenceCardUiInterface;
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
        final String product_id = productListModel.getProd_id();


        final String imagePath = BaseApi.BASE_URL + folder + productListModel.getProd_image();
        Picasso.with(mContext).load(imagePath).into(holder.iv_product_image);
        holder.textProductName.setText(productListModel.getProd_name());

        productUnitModelArrayList = productListModel.getProductUnitModelArrayList();

        if (productUnitModelArrayList.size() == 1) {
            holder.ll_unit_one.setVisibility(View.VISIBLE);
            holder.ll_spinner.setVisibility(View.GONE);
            final ProductUnitModel productUnitModel = productListModel.getProductUnitModelArrayList().get(0);
            String unit = productUnitModel.getProd_unit() + " " + productUnitModel.getProd_weight();
            double rated=Double.parseDouble(productUnitModel.getProd_rate());
            double mrpd=Double.parseDouble(productUnitModel.getProd_mrp());
            String rate = "Rs " + String.format("%.2f", rated);
            String mrp = "MRP " + String.format("%.2f", mrpd);
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

        callQtyAdd(holder, product_id);

        holder.spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    SpinnerPosition = position;
                    final ProductUnitModel productUnitmodel = productListModel.getProductUnitModelArrayList().get(position);
                    long product_id = Long.parseLong(productUnitmodel.getProd_id());
                    long unit_id = Long.parseLong(productUnitmodel.getId());
                    cardProductModel = appDatabase.getProductDAO().getProductUnitID(product_id, unit_id);
                    if (cardProductModel != null) {
                        String unit = productUnitmodel.getProd_unit() + " " + productUnitmodel.getProd_weight();
                        holder.textUnitName.setText(unit);
                        holder.ll_product_qty.setVisibility(View.VISIBLE);
                        holder.ll_add_product.setVisibility(View.GONE);
                        callQtyAdd(holder, String.valueOf(product_id));
                    } else {
                        String unit = productUnitmodel.getProd_unit() + " " + productUnitmodel.getProd_weight();
                        double rated=Double.parseDouble(productUnitmodel.getProd_rate());
                        double mrpd=Double.parseDouble(productUnitmodel.getProd_mrp());
                        String rate = "Rs " + String.format("%.2f", rated);
                        String mrp = "MRP " + String.format("%.2f", mrpd);
                        holder.textUnitName.setText(unit);
                        holder.textPrice.setText(rate);
                        holder.textMrp.setText(mrp);
                        holder.textMrp.setPaintFlags(holder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.ll_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem(position, productListModel,holder);
            }
        });
        holder.ll_add_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardProductModel = appDatabase.getProductDAO().getProduct(Long.parseLong(product_id));
                String qty = String.valueOf(holder.textQty.getText());
                long qtyVal = cardProductModel.getQty() + 1;
                long card_prod_id = cardProductModel.getProduct_id();
                double mrpTotal = qtyVal * Double.parseDouble(String.format("%.2f", cardProductModel.getMrp()));
                double totalRate = qtyVal * Double.parseDouble(String.format("%.2f", cardProductModel.getPrice()));
                long idVal = appDatabase.getProductDAO().getProductDataUpdate(qtyVal, card_prod_id, totalRate, mrpTotal);
                callQtyAdd(holder, product_id);
                referenceCardUiInterface.reference();
            }
        });
        holder.ll_remove_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardProductModel = appDatabase.getProductDAO().getProduct(Long.parseLong(product_id));
                long qty = cardProductModel.getQty() - 1;
                if (qty == 0) {
                    appDatabase.getProductDAO().deleteAllProductID(Long.parseLong(product_id));
                    callQtyAdd(holder, product_id);
                    notifyDataSetChanged();
                } else {
                    long card_produt_id = cardProductModel.getProduct_id();
                    double mrpTotal = qty * Double.parseDouble(String.format("%.2f", cardProductModel.getMrp()));
                    double totalRate = qty * Double.parseDouble(String.format("%.2f", cardProductModel.getPrice()));
                    long idVal = appDatabase.getProductDAO().getProductDataUpdate(qty, card_produt_id, totalRate, mrpTotal);
                    callQtyAdd(holder, product_id);
                }
                referenceCardUiInterface.reference();


            }
        });
    }

    private void callQtyAdd(MyViewHolder holder, String product_id) {
        cardProductModel = appDatabase.getProductDAO().getProduct(Long.parseLong(product_id));
        final long qty;
        if (cardProductModel != null) {
            qty = cardProductModel.getQty();
            holder.ll_product_qty.setVisibility(View.VISIBLE);
            holder.ll_add_product.setVisibility(View.GONE);
            holder.textQty.setText(String.valueOf(qty));

            double mrpTotal = qty *  cardProductModel.getMrp();
            double totalRate =qty *cardProductModel.getPrice();

            String mrp =" MRP " + String.format("%.2f",mrpTotal);
            String rate = "RS " + String.format("%.2f",totalRate);
            holder.textPrice.setText(rate);
            holder.textMrp.setText(mrp);
            holder.textMrp.setPaintFlags(holder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.ll_product_qty.setVisibility(View.GONE);
            holder.ll_add_product.setVisibility(View.VISIBLE);
        }
    }

    private void AddItem(int position, ProductListModel productListModel, MyViewHolder holder) {
        String productName = productListDashboardModelArrayList.get(position).getProd_name();
        long product_id = Long.parseLong(productListDashboardModelArrayList.get(position).getProd_id());
        if (productUnitModelArrayList.size() == 1) {
            final ProductUnitModel productUnitModel = productListModel.getProductUnitModelArrayList().get(0);
            long id = Long.parseLong(productUnitModel.getId());
            long unit = Long.valueOf(productUnitModel.getProd_unit());
            String weight = productUnitModel.getProd_weight();
            String weight_id = productUnitModel.getProd_weight_id();

            double rated =Double.parseDouble(productUnitModel.getProd_rate());
            double mrpd = Double.parseDouble(productUnitModel.getProd_mrp());
            double rate =Double.parseDouble(String.format("%.2f", rated));
            double mrp = Double.parseDouble(String.format("%.2f", mrpd));

            long qty = Long.parseLong("1");
            long idVal = appDatabase.getProductDAO().addProduct(new CardProductModel(0, product_id, productName, id, unit, weight, weight_id, mrp, mrp, rate, rate, qty, folder + productListModel.getProd_image()));
            if (idVal != 0) {
                Toast.makeText(mContext, productName + "Added  Into Card Successfully", Toast.LENGTH_SHORT).show();
                holder.ll_product_qty.setVisibility(View.VISIBLE);
                holder.ll_add_product.setVisibility(View.GONE);
                holder.textQty.setText(String.valueOf(qty));
            }
            else
                Toast.makeText(mContext, productName + "Not Added", Toast.LENGTH_SHORT).show();
        } else {
            final ProductUnitModel productUnitModel = productListModel.getProductUnitModelArrayList().get(SpinnerPosition);
            long id = Long.parseLong(productUnitModel.getId());
            long unit = Long.valueOf(productUnitModel.getProd_unit());
            String weight = productUnitModel.getProd_weight();
            String weight_id = productUnitModel.getProd_weight_id();

            double rated =Double.parseDouble(productUnitModel.getProd_rate());
            double mrpd = Double.parseDouble(productUnitModel.getProd_mrp());
            double rate =Double.parseDouble(String.format("%.2f", rated));
            double mrp = Double.parseDouble(String.format("%.2f", mrpd));

            long qty = Long.parseLong("1");
            long idVal = appDatabase.getProductDAO().addProduct(new CardProductModel(0, product_id, productName, id, unit, weight, weight_id, mrp, mrp, rate, rate, qty, folder + productListModel.getProd_image()));
            if (idVal != 0) {
                Toast.makeText(mContext, productName + "  Added  Into Card", Toast.LENGTH_SHORT).show();
                holder.ll_product_qty.setVisibility(View.VISIBLE);
                holder.ll_add_product.setVisibility(View.GONE);
                holder.textQty.setText(String.valueOf(qty));
            }
            else
            Toast.makeText(mContext, productName + "  Not Added", Toast.LENGTH_SHORT).show();
        }
        referenceCardUiInterface.reference();
    }

    @Override
    public int getItemCount() {
        return productListDashboardModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_product_image;
        private TextView textProductName, textUnitName, textPrice, textMrp, textQty;
        private LinearLayout ll_unit_one, ll_spinner, ll_add_product, ll_product_qty, ll_remove_qty, ll_add_qty;
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
            ll_product_qty = itemView.findViewById(R.id.ll_product_qty);
            textQty = itemView.findViewById(R.id.textQty);
            ll_remove_qty = itemView.findViewById(R.id.ll_remove_qty);
            ll_add_qty = itemView.findViewById(R.id.ll_add_qty);
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

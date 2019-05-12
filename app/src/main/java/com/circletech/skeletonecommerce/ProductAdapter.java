package com.circletech.skeletonecommerce;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Product> productList;

    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fragment_listing_productlist, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        Glide.with(mCtx)
                .load(product.getProductImage())
                .into(holder.imageViewProductImage);

        holder.textViewProductName.setText(product.getProductName());
        holder.textViewProductDescription.setText(product.getProductDescription());
        holder.textViewProductQuantity.setText(mCtx.getResources().getString(R.string.fragment_listing_productlist_quantityleft, product.getProductQuantity()));
        holder.textViewProductPrice.setText(mCtx.getResources().getString(R.string.fragment_listing_productlist_price, product.getProductPrice()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProductName, textViewProductDescription, textViewProductQuantity, textViewProductPrice;
        ImageView imageViewProductImage;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.textView_ProductName);
            textViewProductDescription = itemView.findViewById(R.id.textView_ProductDescription);
            textViewProductQuantity = itemView.findViewById(R.id.textView_ProductQuantity);
            textViewProductPrice = itemView.findViewById(R.id.textView_ProductPrice);
            imageViewProductImage = itemView.findViewById(R.id.imageView_ProductImage);
        }
    }
}

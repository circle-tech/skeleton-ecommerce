package com.circletech.skeletonecommerce;

import android.content.Context;
import android.content.Intent;
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

        holder.textViewProductId.setText(String.valueOf(product.getProductId()));
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

        TextView textViewProductId, textViewProductName, textViewProductDescription, textViewProductQuantity, textViewProductPrice;
        ImageView imageViewProductImage;

        public ProductViewHolder(final View itemView) {
            super(itemView);

            textViewProductId = itemView.findViewById(R.id.fragment_listing_productlist_textViewProductId);
            textViewProductName = itemView.findViewById(R.id.fragment_listing_productlist_textViewProductName);
            textViewProductDescription = itemView.findViewById(R.id.fragment_listing_productlist_textViewProductDescription);
            textViewProductQuantity = itemView.findViewById(R.id.fragment_listing_productlist_textViewProductQuantity);
            textViewProductPrice = itemView.findViewById(R.id.fragment_listing_productlist_textViewProductPrice);
            imageViewProductImage = itemView.findViewById(R.id.fragment_listing_productlist_imageViewProductImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx.getApplicationContext(), ProductInfoActivity.class);
                    intent.putExtra("PRODUCT_ID", textViewProductId.getText().toString());

                    mCtx.startActivity(intent);
                }
            });
        }
    }
}

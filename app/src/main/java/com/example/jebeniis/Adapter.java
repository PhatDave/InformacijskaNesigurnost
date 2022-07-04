package com.example.jebeniis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
	LayoutInflater inflater;
	List<String> items;
	HashMap<String, String> questions;

	public Adapter(Context context, List<String> items, HashMap<String, String> questions) {
		this.inflater = LayoutInflater.from(context);
		this.items = items;
		this.questions = questions;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		String item = items.get(position);
		holder.itemName.setText(item);
		holder.itemView.setOnClickListener(v -> {
			Toast.makeText(v.getContext(), questions.get(item), Toast.LENGTH_LONG).show();
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView itemName;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);

			itemName = itemView.findViewById(R.id.itemName);
		}
	}
}
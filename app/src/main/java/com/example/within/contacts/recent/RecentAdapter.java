package com.example.within.contacts.recent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.within.Call;
import com.example.within.R;

import java.util.List;

/** Object would handle the recent caller model and implement all methods to
 * display the recent call on the home activity list view*/
public class RecentAdapter extends ArrayAdapter<RecentModel> implements Call {

    // Fields from the recent call xml to be inflated
    private static class ViewHolder {
        TextView name, recent_phone;
        TextView durationTime;
        ImageButton dialRecent;
        ImageView recentImage;
    }


    /* Call the constructor and its super method */
    public RecentAdapter(@NonNull Context context, List<RecentModel> recent_contacts){
        super(context, 0, recent_contacts);
    }

    /* Method inflates the recent view xml and inflate its views*/
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        // Check if the view is null before inflating
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recent_call, parent, false);

            // Initialize the view holder
            holder = new ViewHolder();
            // Get the views from the recent call xml
            holder.name = convertView.findViewById(R.id.recent_name);
            holder.durationTime = convertView.findViewById(R.id.duration_time);
            holder.dialRecent = convertView.findViewById(R.id.dail);

            // Set the holder as a tag for the view
            convertView.setTag(holder);
        } else {
            // View is not null, retrieve the ViewHolder from the tag
            holder = (ViewHolder) convertView.getTag();
        }

        // Call the recent model object
        RecentModel model = getItem(position);
        // Set the values to the views based on the data from RecentModel
        if (model != null) {
            holder.name.setText(model.getName());
            holder.durationTime.setText(String.valueOf(model.getDuration()));
        }

        /* Listener listens to make a call when clicked */
        holder.dialRecent.setOnClickListener(v -> {
            RecentModel model1 = getItem(position);
            // Check if the recent view is null
            if (model1 != null) {
                String phone = model1.getPhone();
                /* Check if the phone number null or empty then make a call */
                if (!phone.isEmpty()) {
                    makeCall();
                } else {
                    Toast.makeText(getContext(), "The number is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView; // Return the inflated view
    }


    @Override
    public void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        getContext().startActivity(intent);
    }
}

package org.icspl.icsconnect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import org.icspl.icsconnect.R;
import org.icspl.icsconnect.models.DtGetViewTstatus;

import java.util.List;

public class ViewTimesheetStatusAdaptor extends RecyclerView.Adapter<ViewTimesheetStatusAdaptor.Holder> {
    Context  context;
    List<DtGetViewTstatus> array;

    public ViewTimesheetStatusAdaptor(Context context, List<DtGetViewTstatus> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public ViewTimesheetStatusAdaptor.Holder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_timesheet_status,parent,false);
        Holder h = new Holder(view);
        return h;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewTimesheetStatusAdaptor.Holder holder, int position) {
      DtGetViewTstatus tstatus=array.get(position);

     holder.month.setText(tstatus.getMonth());

        holder.status.setText(tstatus.getStatus());
        if(tstatus.getSentDate()==null)
        {
            holder.senddate.setText("-");
        }
        else
        {
            holder.senddate.setText(tstatus.getSentDate());
        }
     if (tstatus.getStatus().equals("Sent"))
     {
         holder.status.setBackgroundColor(Color.parseColor("#1C7009"));

     }
    if(array.get(position).getStatus().equals("Pending")){

        holder.status.setBackgroundColor(Color.parseColor("#F63006"));

     }

   /*   if (array.size()>0)
      {

          holder.month.setText(array.get(position).getMonth());
          if(array.get(position).getSentDate()!=null)
          {
              holder.senddate.setText( array.get(position).getSentDate());
          }
          if (array.get(position).getStatus()!=null)
          {

              if((array.get(position).getStatus())=="Sent")
              {


              }
              else if(array.get(position).getStatus().equals("Pending")){
                  holder.status.setBackgroundColor(R.color.red_app);
                  holder.status.setText(array.get(position).getStatus());
              }
          }

          else
          {
              holder.status.setText("-");
          }

      }
      else
      {
          Toast.makeText(context,"Some thing went wrong",Toast.LENGTH_LONG).show();
      }*/

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        AppCompatTextView month,senddate,status;
        public Holder(@NonNull View itemView) {
            super(itemView);
            month= itemView.findViewById(R.id.month);
            senddate= itemView.findViewById(R.id.sendate);
            status= itemView.findViewById(R.id.status);

        }
    }
}

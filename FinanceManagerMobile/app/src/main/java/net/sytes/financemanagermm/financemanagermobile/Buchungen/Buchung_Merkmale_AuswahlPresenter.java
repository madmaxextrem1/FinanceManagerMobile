package net.sytes.financemanagermm.financemanagermobile.Buchungen;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import net.sytes.financemanagermm.financemanagermobile.Datenmanagement.FinanzbuchungToken;
import net.sytes.financemanagermm.financemanagermobile.Globales_Sonstiges.GlobaleVariablen;
import net.sytes.financemanagermm.financemanagermobile.R;

import java.util.ArrayList;
import java.util.List;

public class Buchung_Merkmale_AuswahlPresenter extends RecyclerViewPresenter<FinanzbuchungToken> {

    protected Adapter adapter;

    public Buchung_Merkmale_AuswahlPresenter(Context context) {
        super(context);
    }

    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<FinanzbuchungToken> all = GlobaleVariablen.getInstance().getTokenListe();
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<FinanzbuchungToken> list = new ArrayList<>();
            for (FinanzbuchungToken u : all) {
                if (u.getBeschreibung().toLowerCase().contains(query) ||
                        u.getBeschreibung().toLowerCase().contains(query)) {
                    list.add(u);
                }
            }

            adapter.setData(list);
            Log.e("UserPresenter", "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }



    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        private List<FinanzbuchungToken> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private ImageView imgTyp;
            private TextView lblTitel;
            private TextView lblInhaber;

            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                imgTyp =((ImageView) itemView.findViewById(R.id.imgTyp));
                lblTitel = ((TextView) itemView.findViewById(R.id.lblTitel));
                lblInhaber = ((TextView) itemView.findViewById(R.id.lblInhaber));
            }

        }

        public void setData(List<FinanzbuchungToken> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }



        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.buchung_hinzufuegen_autocomplete_merkmal_item, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (isEmpty()) {
                holder.lblTitel.setText("Kein Merkmalname");
                holder.lblInhaber.setText("Kein Inhaber");
                holder.imgTyp.setImageResource(0);
                holder.root.setOnClickListener(null);
                return;
            }

            final FinanzbuchungToken Merkmal = data.get(position);
            holder.lblTitel.setText(Merkmal.getBeschreibung());
            holder.lblInhaber.setText("@" + GlobaleVariablen.getInstance().getEmail());

            switch (Merkmal.getTyp()) {
                case PERSOENLICH:
                    holder.imgTyp.setImageResource(R.drawable.ic_person_white_24dp);
                    break;
                case GRUPPE:
                    holder.imgTyp.setImageResource(R.drawable.ic_group_white_24dp);
                    break;
            }
            holder.root.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dispatchClick(Merkmal);
                }
            });

        }

    }
}

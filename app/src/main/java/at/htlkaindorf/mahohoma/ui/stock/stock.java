package at.htlkaindorf.mahohoma.ui.stock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import at.htlkaindorf.mahohoma.R;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link stock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class stock extends Fragment{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String CompanyName, Image;
    private TextView name;
    private ImageView ivCompany;
    private String mParam1;
    private String mParam2;

    public stock() {
        // Required empty public constructor
    }

    public stock(String CompanyName, String Image){
        this.CompanyName = CompanyName;
        this.Image = Image;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment stock.
     */
    public static stock newInstance(String param1, String param2) {
        stock fragment = new stock();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_stock, container, false);
        name= root.findViewById(R.id.tvCompanyName);
        name.setText(CompanyName);
        ivCompany = root.findViewById(R.id.IVCompanyImage);
        Picasso.get().load(Image).transform(new RoundedCornersTransformation(40,0)).fit().centerInside().into(ivCompany);
        return root;
    }
}

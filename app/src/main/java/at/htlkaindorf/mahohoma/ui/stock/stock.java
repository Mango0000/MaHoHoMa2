package at.htlkaindorf.mahohoma.ui.stock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyInformations;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyResolver;
import at.htlkaindorf.mahohoma.ui.StockItem.StockItem;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link stock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class stock extends Fragment
{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String CompanyName = "", Image= "", Symbol= "", CompanyCEO= "", WebsiteLink= "", price= "", changes= "", description= "", sector= "", industry= "", exchange= "", exchangeShortName= "";
    private TextView name, tvDescription, tvInformations;
    private ImageView ivCompany;
    private String mParam1;
    private String mParam2;
    private GraphView graph;

    public stock() {
        // Required empty public constructor
    }

    public stock(String CompanyName, String Image, String Symbol)
    {
        this.CompanyName = CompanyName;
        this.Image = Image;
        this.Symbol = Symbol;
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
        getCompanyInformations();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_stock, container, false);
        name= root.findViewById(R.id.tvCompanyName);
        tvDescription = root.findViewById(R.id.tvDescription);
        tvInformations = root.findViewById(R.id.tvInformations);

        name.setText(CompanyName+" ("+Symbol+")");
        tvInformations.setText("CEO: " +CompanyCEO +"\n"
                + WebsiteLink +"\n"
                + "Price: " +price+"\n"
                + "Change: " +changes+"\n"
                + "Sector: " +sector+"\n"
                + "Industry: " + industry+"\n"
                + "Exchange: " +exchange+" ("+exchangeShortName+")\n");
        tvDescription.setText("\n"+description);

        ivCompany = root.findViewById(R.id.IVCompanyImage);
        Picasso.get().load(Image).transform(new RoundedCornersTransformation(40,0)).fit().centerInside().into(ivCompany);
        graph = root.findViewById(R.id.graph);
        LineGraphSeries <DataPoint> series = new LineGraphSeries< >(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(Integer.valueOf(1), Integer.valueOf(2)),
                new DataPoint(Integer.valueOf(2), Integer.valueOf(3)),
                new DataPoint(Integer.valueOf(3), Integer.valueOf(4)),
                new DataPoint(Integer.valueOf(5), Integer.valueOf(2)),
                new DataPoint(Integer.valueOf(6), Integer.valueOf(3)),
                new DataPoint(Integer.valueOf(7), Integer.valueOf(8)),
        });
        graph.addSeries(series);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Sales");
        graph.getViewport().setScrollable(true);
        return root;
    }

    public void getCompanyInformations()
    {
        try
        {
            List<String> res = new CompanyInformations().execute(Symbol).get();
            if(res != null)
            {
                changes = res.get(1);
                price = res.get(0);
                CompanyCEO = res.get(2);
                sector = res.get(3);
                WebsiteLink = res.get(4);
                industry = res.get(5);
                exchange = res.get(6);
                exchangeShortName = res.get(7);
                description = res.get(8);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

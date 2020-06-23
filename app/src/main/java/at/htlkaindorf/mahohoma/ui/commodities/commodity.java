package at.htlkaindorf.mahohoma.ui.commodities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.CommodityChart;
import at.htlkaindorf.mahohoma.backgroundTasks.CommodityInformations;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyInformations;
import at.htlkaindorf.mahohoma.backgroundTasks.IncomeStatement;
import at.htlkaindorf.mahohoma.favourite.favourite;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link commodity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class commodity extends Fragment implements View.OnClickListener
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String Name = "", changePerc= "", Symbol= "", yearHigh= "", yearLow= "", change= "", exchange= "", dayLow= "", dayHigh= "", open= "", previousClose= "", price = "";
    private TextView name, tvDescription, tvInformations;
    private ImageView ivCommodity, ivFavourite;
    private favourite favourites;
    private GraphView graph;


    private String mParam1;
    private String mParam2;

    public commodity() {
        // Required empty public constructor
    }

    public commodity(String symbol)
    {
        this.Symbol = symbol;
        this.favourites = favourite.getTheInstance();
    }

    public static commodity newInstance(String param1, String param2) {
        commodity fragment = new commodity();
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
        getCommodityInformations();
        View root = inflater.inflate(R.layout.commodity_fragment, container, false);
        name = root.findViewById(R.id.tvCommodityName);
        //tvDescription = root.findViewById(R.id.tvDescription);
        tvInformations = root.findViewById(R.id.tvInformationsComm);
        ivFavourite = root.findViewById(R.id.FavouriteComm);
        if(favourites.isFavourite(Symbol)){
            ivFavourite.setImageResource(R.drawable.ic_heart_svg);
        }
        ivFavourite.setOnClickListener(this);

        name.setMovementMethod(LinkMovementMethod.getInstance());
        name.setText(Name+" ("+Symbol+") ");

        tvInformations.setText("Price: " +price+"\n"
                + "Change: " +change+"\n"
                + "Change (%): " +changePerc+"\n"
                + "DayLow: " +dayLow+"\n"
                + "DayHigh: " + dayHigh+"\n"
                + "YearLow: " +yearLow+"\n"
                + "YearHigh: " +yearHigh+"\n"
                + "Open: " +open+"\n"
                + "Previous Close: " +previousClose+"\n"
                + "Exchange: " +exchange+"\n");
        //tvDescription.setText("\n"+description);
        //ivCommodity = root.findViewById(R.id.IVCommodityImage);
        //Picasso.get().load(Image).transform(new RoundedCornersTransformation(40,0)).fit().centerInside().into(ivCompany);

        graph = root.findViewById(R.id.graph);
        try {
            DataPoint revenue[] = new CommodityChart().execute(Symbol).get();
            if(revenue!=null){
                Log.w("test",revenue.toString());
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(revenue);
                graph.addSeries(series);
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                graph.getGridLabelRenderer().setVerticalAxisTitle("Price");
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this.getContext()));
                graph.getGridLabelRenderer().setNumHorizontalLabels(2);
                //graph.getViewport().setScrollable(true);
                double xmin=revenue[0].getX(), xmax=revenue[0].getX(), ymin=revenue[0].getY(), ymax=revenue[0].getY();
                for(DataPoint dp: revenue){
                    if(dp.getX()>xmax){
                        xmax=dp.getX();
                    }
                    if(dp.getX()<xmin){
                        xmin=dp.getX();
                    }
                    if(dp.getY()>ymax){
                        ymax=dp.getY();
                    }
                    if(dp.getY()<ymin){
                        ymin=dp.getY();
                    }
                }

                // graph.getViewport().setMinX(xmin);
                //graph.getViewport().setMaxX(xmax);
                //graph.getViewport().setMinY(ymin);
                // graph.getViewport().setMaxY(ymax);
                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);

                //graph.getViewport().setYAxisBoundsManual(true);
                //graph.getViewport().setXAxisBoundsManual(true);
            }
        }catch (Exception e){
            TextView tv = new TextView(this.getContext());
            tv.setText("No Price");
            replaceView(graph, tv);
        }
        return root;
    }

    private void replaceView(View oldV,View newV){
        ViewGroup par = (ViewGroup)oldV.getParent();
        if(par == null){return;}
        int i1 = par.indexOfChild(oldV);
        par.removeViewAt(i1);
        par.addView(newV,i1);
    }

    public void getCommodityInformations()
    {
        try
        {
            List<String> res = new CommodityInformations().execute(Symbol).get();
            if(res != null)
            {
                Name = res.get(0);
                price = res.get(1);
                changePerc = res.get(2);
                change = res.get(3);
                dayLow = res.get(4);
                dayHigh = res.get(5);
                yearHigh =  res.get(6);
                yearLow = res.get(7);
                exchange = res.get(8);
                open = res.get(9);
                previousClose = res.get(10);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(favourites.isFavourite(Symbol))
        {
            removeFavourite();
        }
        else {
            addFavourite();
        }
    }

    private void addFavourite(){
        new AlertDialog.Builder(getContext())
                .setTitle("Add to Favourite")
                .setMessage("Do you want to add this Item to your favourites?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        favourites.addFavourite(Symbol);
                        ivFavourite.setImageResource(R.drawable.ic_heart_svg);
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
                .setIcon(R.drawable.ic_heart_svg)
                .show();
    }

    private void removeFavourite(){
        new AlertDialog.Builder(getContext())
                .setTitle("Remove Favourite")
                .setMessage("Do you want to remove this Item from your favourites?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        favourites.removeFavourite(Symbol);
                        ivFavourite.setImageResource(R.drawable.ic_heart_disabled_svg);
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setIcon(R.drawable.ic_heart_svg_remove)
                .show();
    }
}
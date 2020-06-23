package at.htlkaindorf.mahohoma.ui.stock;

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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.Chart;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyInformations;
import at.htlkaindorf.mahohoma.favourite.favourite;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link stock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class stock extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String CompanyName = "", Image= "", Symbol= "", CompanyCEO= "", WebsiteLink= "", price= "", changes= "", description= "", sector= "", industry= "", exchange= "", exchangeShortName= "";
    private TextView name, tvDescription, tvInformations;
    private ImageView ivCompany, ivFavourite;
    private String mParam1;
    private String mParam2;
    private GraphView graph;
    private favourite favourites;

    public stock() {
        // Required empty public constructor
    }

    public stock(String CompanyName, String Image, String Symbol)
    {
        this.CompanyName = CompanyName;
        this.Image = Image;
        this.Symbol = Symbol;
        favourites = favourite.getTheInstance();
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_stock, container, false);
        name= root.findViewById(R.id.tvCompanyName);
        tvDescription = root.findViewById(R.id.tvDescription);
        tvInformations = root.findViewById(R.id.tvInformations);
        ivFavourite = root.findViewById(R.id.Favourite);
        if(favourites.isFavourite(Symbol)){
            ivFavourite.setImageResource(R.drawable.ic_heart_svg);
        }
        ivFavourite.setOnClickListener(this);

        name.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='"+WebsiteLink+"'> "+CompanyName+" ("+Symbol+")"+" </a>";
        name.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));

        tvInformations.setText("CEO: " +CompanyCEO +"\n"
                + "Price: " +price+"\n"
                + "Change: " +changes+"\n"
                + "Sector: " +sector+"\n"
                + "Industry: " + industry+"\n"
                + "Exchange: " +exchange+" ("+exchangeShortName+")\n");
        tvDescription.setText("\n"+description);

        ivCompany = root.findViewById(R.id.IVCompanyImage);
        Picasso.get().load(Image).transform(new RoundedCornersTransformation(40,0)).fit().centerInside().into(ivCompany);
        graph = root.findViewById(R.id.graph);
        try {
            DataPoint revenue[] = new Chart().execute(Symbol).get();
            if(revenue!=null){
                Log.w("test",revenue.length+"");
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(revenue);
                graph.addSeries(series);
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                graph.getGridLabelRenderer().setVerticalAxisTitle("Price");
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this.getContext(), new SimpleDateFormat("YYYY.MM.dd. HH")));
                graph.getGridLabelRenderer().setNumHorizontalLabels(2);
                //graph.getViewport().setScrollable(true);
               /* double xmin=revenue[0].getX(), xmax=revenue[0].getX(), ymin=revenue[0].getY(), ymax=revenue[0].getY();
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
                //graph.getViewport().setXAxisBoundsManual(true);
                graph.getGridLabelRenderer().setHumanRounding(false);
                /*graph.getViewport().setMinX(xmin);
                graph.getViewport().setMaxX(xmax);
                graph.getViewport().setMinY(ymin);
                graph.getViewport().setMaxY(ymax);*/
                /*graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setXAxisBoundsManual(true);*/
               //graph.getViewport().setMaxX(xmax);
                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);

                //graph.getViewport().setYAxisBoundsManual(true);
                //graph.getViewport().setXAxisBoundsManual(true);
            }
        }catch (Exception e){
            TextView tv = new TextView(this.getContext());
            tv.setText("No Revenue");
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

    @Override
    public void onClick(View v) {
        if(favourites.isFavourite(Symbol)){
            removeFavourite();
        }else{
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

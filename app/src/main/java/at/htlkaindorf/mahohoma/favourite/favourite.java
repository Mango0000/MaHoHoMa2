package at.htlkaindorf.mahohoma.favourite;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import at.htlkaindorf.mahohoma.MainActivity;

public class favourite {
    private static favourite theInstance = null;
    public List<String> favourites = new ArrayList<>();
    private final String filename = "favourite";

    private favourite() {
        loadFavourites();
    }

    public boolean isFavourite(String company){
        if(favourites.contains(company)){
            return true;
        }
        return false;
    }

    public List<String> getFavourites(){
        return favourites;
    }

    private void loadFavourites() {
        try {
            ObjectInputStream ois = new ObjectInputStream(MainActivity.getContext().openFileInput(filename));
            favourites = (List<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveFavourites(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(MainActivity.getContext().openFileOutput(filename, Context.MODE_PRIVATE));
            oos.writeObject(favourites);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFavourite(String favourite){
        if(!favourites.contains(favourite)){
            favourites.add(favourite);
            saveFavourites();
        }
    }

    public void removeFavourite(String favourite){
        if(favourites.contains(favourite)) {
            favourites.remove(favourite);
            saveFavourites();
        }
    }

    public static favourite getTheInstance() {
        if(theInstance==null) {
            theInstance = new favourite();
        }
        return theInstance;
    }
}

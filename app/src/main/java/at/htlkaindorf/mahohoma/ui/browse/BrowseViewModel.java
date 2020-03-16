package at.htlkaindorf.mahohoma.ui.browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BrowseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BrowseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is browse fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package at.htlkaindorf.mahohoma.ui.commodities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommodityViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public CommodityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is browse fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

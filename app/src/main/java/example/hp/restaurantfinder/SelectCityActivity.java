package example.hp.restaurantfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import example.hp.restaurantfinder.contract.SelectCityContract;
import example.hp.restaurantfinder.databinding.SelectCityLayoutBinding;
import example.hp.restaurantfinder.di.DaggerSharedPrefComponent;
import example.hp.restaurantfinder.di.SharedPrefModule;
import example.hp.restaurantfinder.presenter.SelectCityPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SelectCityActivity extends MvpActivity<SelectCityContract.View, SelectCityPresenter>
        implements SelectCityContract.View {

    private static final String TAG = SelectCityActivity.class.getName();
    private static final String CITY_ID = "city_id";
    private static final String CITY_NAME = "city_name";
    private static final String SELECTED_CITY = "selected_city";
    private static final String VALID_CITY_NAME = "Enter a valid city name!";
    private static final String ENTER_CITY_NAME = "Enter a city name!";
    private static final String NO_RESULTS_FOUND = "No Results Found!";

    private SelectCityLayoutBinding binding;
    private CompositeDisposable disposable;
    private RequestQueue queue;
    private ArrayAdapter<String> adapter;
    private String cities[] = {"Delhi", "Mumbai", "Hyderabad", "Patna", "Bengaluru", "Chennai", "Dehradun"};

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.select_city_layout,null, false);
        setContentView(binding.getRoot());
        disposable = new CompositeDisposable();
        queue = Volley.newRequestQueue(getApplicationContext());
        DaggerSharedPrefComponent.builder().sharedPrefModule(
                new SharedPrefModule(getApplicationContext())).build().inject(this);
        adapter = new ArrayAdapter<>(this, R.layout.select_city_item, R.id.tv_city, cities);
        binding.lvCities.setAdapter(adapter);
        addTextWatcher();
        setDefaultSelectedCity();
        initListener();
    }

    private void setDefaultSelectedCity() {
        if(sharedPreferences.contains(SELECTED_CITY)) {
            binding.etLocation.setText(sharedPreferences.getString(SELECTED_CITY,""));
        }
    }

    private void initListener() {
        disposable.add(RxView.clicks(binding.btnSearch)
                  .throttleFirst(5, TimeUnit.SECONDS)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(s -> {
                      loadData();
                  }, e -> {
                      Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                  }));
    }

    private void addTextWatcher() {
        binding.etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.btnSearch.setVisibility(View.GONE);
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.btnSearch.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @NonNull
    @Override
    public SelectCityPresenter createPresenter() {
        return new SelectCityPresenter();
    }

    @Override
    public void onDataFetched(int cityId, String cityName) {
        if(cityId == 0) {
            Toast.makeText(this, NO_RESULTS_FOUND, Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CITY_ID, cityId);
            editor.putString(CITY_NAME, cityName);
            editor.apply();
            Intent intent = new Intent(SelectCityActivity.this, LandingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResponseError(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public SelectCityPresenter getPresenter() {
        return super.getPresenter();
    }

    private void loadData() {
        List<String> citiesList = Arrays.asList(cities);
        if(!citiesList.contains(binding.etLocation.getText().toString())) {
            Toast.makeText(getBaseContext(), VALID_CITY_NAME, Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(binding.etLocation.getText().toString())) {
            Toast.makeText(this, ENTER_CITY_NAME, Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SELECTED_CITY, binding.etLocation.getText().toString());
            editor.apply();
            getPresenter().fetchData(queue, binding.etLocation.getText().toString());
        }
    }
}

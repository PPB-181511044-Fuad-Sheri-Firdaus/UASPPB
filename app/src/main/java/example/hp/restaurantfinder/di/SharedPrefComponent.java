package example.hp.restaurantfinder.di;

import example.hp.restaurantfinder.DetailsActivity;
import example.hp.restaurantfinder.LandingActivity;
import example.hp.restaurantfinder.ListingActivity;
import example.hp.restaurantfinder.MainActivity;
import example.hp.restaurantfinder.SelectCityActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SharedPrefModule.class})
public interface SharedPrefComponent {
    void inject(MainActivity mainActivity);

    void inject(SelectCityActivity selectCityActivity);

    void inject(LandingActivity landingActivity);

    void inject(ListingActivity listingActivity);

    void inject(DetailsActivity detailsActivity);
}

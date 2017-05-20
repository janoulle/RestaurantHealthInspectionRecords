package com.janeullah.apps.healthinspectionviewer.dtos;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.models.yelp.Business;

import org.parceler.Parcel;

/**
 * @author Jane Ullah
 * @date 5/6/2017.
 */
@Parcel
public class FlattenedYelpData {
    public int reviewCount;
    public int yelpStarsResourceId;
    public String yelpBusinessId;
    public double rating;

    public FlattenedYelpData(){
        /**
         * Default constructor
         */
    }

    public FlattenedYelpData(Business business){
        reviewCount = business.getReviewCount();
        yelpBusinessId = business.getId();
        rating = business.getRating();
        computeAndSetYelpStarImages();
    }

    public void computeAndSetYelpStarImages(){
        if (Double.compare(rating,0) == 0){
            yelpStarsResourceId = R.drawable.stars_small_0;
        }else if (Double.compare(rating,1) == 0){
            yelpStarsResourceId = R.drawable.stars_small_1;
        }else if (Double.compare(rating,1.5) == 0){
            yelpStarsResourceId = R.drawable.stars_small_1_half;
        }else if (Double.compare(rating,2) == 0){
            yelpStarsResourceId = R.drawable.stars_small_2;
        }else if (Double.compare(rating,2.5) == 0){
            yelpStarsResourceId = R.drawable.stars_small_2_half;
        }else if (Double.compare(rating,3) == 0){
            yelpStarsResourceId = R.drawable.stars_small_3;
        }else if (Double.compare(rating,3.5) == 0){
            yelpStarsResourceId = R.drawable.stars_small_3_half;
        }else if (Double.compare(rating,4) == 0){
            yelpStarsResourceId = R.drawable.stars_small_4;
        }else if (Double.compare(rating,4.5) == 0){
            yelpStarsResourceId = R.drawable.stars_small_4_half;
        }else if (Double.compare(rating,5) == 0){
            yelpStarsResourceId = R.drawable.stars_small_5;
        }
    }
}

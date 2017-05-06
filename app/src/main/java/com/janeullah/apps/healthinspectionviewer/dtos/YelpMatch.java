package com.janeullah.apps.healthinspectionviewer.dtos;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import com.janeullah.apps.healthinspectionviewer.models.yelp.Business;

/**
 * http://stackoverflow.com/questions/21057708/java-fuzzy-string-matching-with-names/21087586#21087586
 * @date 5/5/2017.
 */

public class YelpMatch {
    //Uninformed choice of tolerance. TODO: revisit method of picking a tolerance level for similarity matching
    public static final double TOLERANCE = 0.9;
    private Business candidate;
    private double score; // 0 - definitely not, 1.0 - perfect match

    public YelpMatch(Business yelpBusinessObject, double score) {
        this.candidate = yelpBusinessObject;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public Business getCandidate() {
        return candidate;
    }

    public boolean isAtOrAboveTolerance(){
        return Double.compare(score,TOLERANCE) >= 0;
    }

    //http://stackoverflow.com/questions/11758982/how-to-get-max-element-from-list-in-guava
    public static final Ordering<YelpMatch> SCORE_ORDER =
            new Ordering<YelpMatch>() {
                @Override
                public int compare(YelpMatch left, YelpMatch right) {
                    return Doubles.compare(left.score, right.score);
                }
            };
}
